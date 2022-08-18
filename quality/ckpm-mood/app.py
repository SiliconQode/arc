"""The base application component for the ckpm-mood project

Provides the basic components necessary to start the analysis of a project. This includes the command
line interface, the directory scraping components, and the control and use of the metrics analysis and
results writing components.

   Typical usage example:

   app.main()
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

import argparse
import ast
import logging
import os
import sys

import ckpm.raw as raw
from ckpm.visitor import Visitor
from ckpm.data_model import Project
from ckpm.ck_metrics import CKMetrics
from ckpm.mood_metrics import MOODMetrics
from ckpm.other_metrics import OtherMetrics
from ckpm.metrics_writer import MetricsWriter


def collecting_path(direc, paths=None):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    no_collect = ['__pycache__', '.git', 'venv']
    if paths is None:
        paths = []
    count = 0
    for filename in os.listdir(direc):
        pathway = os.path.join(direc, filename)
        if os.path.isfile(pathway) and pathway.endswith(".py"):
            paths.append(pathway)
        elif os.path.isdir(pathway) and not (filename in no_collect):
            paths = collecting_path(pathway, paths)

    return paths


def read_files(path):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    mdef = ''
    with open(path, encoding="utf8", mode='r') as reader:
        mdef = '\n'.join(reader.readlines())

    return mdef


def setup_logging(log):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    log.setLevel(logging.INFO)
    ch = logging.StreamHandler()
    ch.setLevel(logging.INFO)
    formatter = logging.Formatter('[%(levelname)s] %(asctime)s - %(name)s - %(message)s',
                                  datefmt='%m/%d/%Y %I:%M:%S %p')
    ch.setFormatter(formatter)
    log.addHandler(ch)


def process_files(paths, proj):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    for path in paths:
        logger.info('Parsing path: %s', path)
        visitor = Visitor(proj, path)
        tree = ast.parse(read_files(path))
        visitor.visit(tree)


def evaluate_metrics(proj):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    ck_metrics = CKMetrics(proj)
    mood_metrics = MOODMetrics(proj)
    other_metrics = OtherMetrics(proj)

    for m in proj.modules:
        mdef = ''
        with open(m.path, 'r') as f:
            mdef = '\n'.join(f.readlines())
        for clss in m.classes:
            lines = mdef.split('\n')
            cls_def = '\n'.join(lines[clss.start - 1: clss.end])
            cls_raw = raw.analyze(cls_def)
            set_loc(clss, cls_raw)
            ck_metrics.evaluate(clss)
            mood_metrics.evaluate(clss)
            other_metrics.evaluate(clss)
        loc = raw.analyze(mdef)
        set_loc(m, loc)
        ck_metrics.evaluate(m)
        mood_metrics.evaluate(m)
        other_metrics.evaluate(m)


def set_loc(item, raw_metrics):
    item.metrics['loc'] = raw_metrics.loc
    item.metrics['sloc'] = raw_metrics.sloc
    item.metrics['lloc'] = raw_metrics.lloc
    item.metrics['bloc'] = raw_metrics.blank
    item.metrics['mcloc'] = raw_metrics.multi
    item.metrics['scloc'] = raw_metrics.single_comments
    total_loc = (raw_metrics.loc - raw_metrics.blank - raw_metrics.comments)
    if total_loc == 0:
        item.metrics['cp'] = 0
    else:
        item.metrics['cp'] = raw_metrics.comments / total_loc


def output_results(project, type, cons, path):
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    writer = MetricsWriter()
    match type:
        case 'json':
            writer.write_to_json(project, path)
        case 'xml':
            writer.write_to_xml(project, path)
    if cons:
        writer.write_to_console(project)


def main():
    """[Summary]

    [Description]

    Args:
        [Arg]: [Info]
        [Arg]: [Info]

    Returns:
        [Summary]

    Raises:
        [Exception]: [Summary]
    """
    parser = argparse.ArgumentParser(description='Evaluates a python project using the CK and MOOD metrics suites',
                                     epilog=__copyright__)
    parser.add_argument('-p', '--only-public', help='Only analyze public methods', action='store_true')
    parser.add_argument('-d', '--src-dir', help='Source directory containing files to be analyzed', type=str, nargs='?',
                        action='store', required=True)
    parser.add_argument('-o', '--output', help='Name of the directory to output to', type=str, nargs='?',
                        action='store',
                        required=True)
    parser.add_argument('-n', '--no-console-output', help='Do not use console output (default enabled)',
                        action='store_true')
    parser.add_argument('-v', '--version', help='Print version information and exit', action='version', version='ckpm version ' + __version__)
    output_group = parser.add_mutually_exclusive_group(required=True)
    output_group.add_argument('-x', '--xml-output', help='Use XML output', action='store_true')
    output_group.add_argument('-j', '--json-output', help='Use JSON output', action='store_true')

    args = parser.parse_args()

    ANSI_CYAN = '\u001B[36m'
    ANSI_BLUE = '\u001B[34m'
    ANSI_YELLOW = '\u001B[33m'
    ANSI_RESET = '\u001B[0m'

    print(ANSI_BLUE + '         ' + '   ________ __ ____  __  ___      __  _______  ____  ____ ' + ANSI_RESET)
    print(ANSI_BLUE + '         ' + '  / ____/ //_// __ \\/  |/  /     /  |/  / __ \\/ __ \\/ __ \\' + ANSI_RESET)
    print(ANSI_BLUE + '         ' + ' / /   / ,<  / /_/ / /|_/ /_____/ /|_/ / / / / / / / / / /' + ANSI_RESET)
    print(ANSI_BLUE + '         ' + '/ /___/ /| |/ ____/ /  / /_____/ /  / / /_/ / /_/ / /_/ / ' + ANSI_RESET)
    print(ANSI_BLUE + '         ' + '\\____/_/ |_/_/   /_/  /_/     /_/  /_/\\____/\\____/_____/  ' + ANSI_RESET)
    print('')
    print(ANSI_YELLOW + '                       ' + 'CK and Mood Metrics for Python' + ANSI_RESET)
    print('')
    print(ANSI_YELLOW + '                      ' + 'Copyright (C) 2022, Empirilytics' + ANSI_RESET)
    print('')

    setup_logging(logger)

    console = True
    output_type = ''
    output_path = None
    if args.xml_output:
        output_type = 'xml'
    if args.json_output:
        output_type = 'json'
    if args.no_console_output:
        console = False
    if args.output:
        output_path = os.path.normpath(os.path.abspath(args.output))

    path_list = collecting_path(os.path.normpath(os.path.abspath(args.src_dir)))

    logger.info(f'Found {len(path_list)} files to measure')

    common_prefix = os.path.commonprefix(path_list)
    proj_name = os.path.basename(os.path.dirname(common_prefix))
    project = Project(proj_name)
    process_files(path_list, project)
    evaluate_metrics(project)
    output_results(project, output_type, console, output_path)


logger = logging.getLogger("ck4py")

if __name__ == '__main__':
    main()
