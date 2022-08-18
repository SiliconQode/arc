"""Module for writing the results of a metrics analysis

Provides the ability to export the results of a metrics analysis to either a file or the console

   Typical usage example:

   writer = MetricsWriter()
   writer.write_to_json(project, '/output')
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

import os
import sys
import logging


class MetricsWriter:
    """
    Class to hold global metrics data, as well as the class metrics container
    """

    def __init__(self):
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
        self.logger = logging.getLogger("ck4p")

    def _handle_project(self, project, f):
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
        f.write('{\n')
        f.write('  "name": "' + project.name + '",\n')
        for k, v in project.metrics.items():
            f.write('  "' + k + '": ' + str(float(v)) + ',\n')
        f.write('  "modules": [\n')
        self._handle_modules(project.modules, f)
        f.write('  ]\n')
        f.write('}')

    def _handle_modules(self, modules, f):
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
        for m in modules:
            f.write('    {\n')
            f.write('      "name": "' + m.name + '",\n')
            for k, v in m.metrics.items():
                f.write('      "' + k + '": ' + str(float(v)) + ',\n')
            f.write('      "classes": [\n')
            self._handle_classes(m.classes, f)
            f.write('      ]\n')
            if m == modules[-1]:
                f.write('    }\n')
            else:
                f.write('    },\n')

    def _handle_classes(self, classes, f):
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
        for c in classes:
            f.write("        {\n")
            f.write('          "name": "' + c.name + '",\n')
            for k, v in c.metrics.items():
                f.write('          "' + k + '": ' + str(float(v)) + ',\n')
            self._handle_methods(c.methods, f)
            if c == classes[-1]:
                f.write('        }\n')
            else:
                f.write('        },\n')

    def _handle_methods(self, methods, f):
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
        f.write('          "cc": [\n')
        for m in methods:
            f.write('            { "method_name": "' + m.name + '",')
            if m == methods[-1]:
                f.write('"cc": ' + str(float(m.metrics['cc'])) + " }\n")
            else:
                f.write('"cc": ' + str(float(m.metrics['cc'])) + " },\n")

        f.write('          ]\n')

    def write_to_console(self, project):
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
        self._handle_project(project, sys.stdout)

    def write_to_json(self, project, output_path):
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
        try:
            os.makedirs(os.path.dirname(os.path.abspath(output_path)))
        except FileExistsError:
            pass
        file = output_path
        with open(file, 'w') as f:
            self._handle_project(project, f)

        self.logger.info("Output writen to: %s", file)

    def write_to_xml(self, project, output_path):
        pass
