"""This module contains the basic components used to store data collected about a program

Data to be collected includes data about Projects, Modules, Types, and Functions. The data is extracted
during the abstract syntax tree analysis phase and later during the metrics analysis phase.
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'


class Project:

    def __init__(self, name: str):
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
        self.name = name
        self.modules = []
        self.metrics = {}  # sloc, bloc, scloc, mcloc, comment_percent
        self.coupling_map = {}

    def __str__(self):
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
        s = "Project: " + self.name + "\n"
        s += "Metrics:\n"
        for k, v in self.metrics.items():
            s += "  " + k + ": " + str(v) + "\n"
        s += "  Modules:\n"
        for m in self.modules:
            s += str(m)

        return s


class Module:

    def __init__(self, name: str, path):
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
        self.name = name
        self.methods = []
        self.classes = []
        self.metrics = {}  # all + loc, sloc, bloc, cloc, comment_percent
        self.vars = []
        self.uses = {}
        self.path = path

    def __str__(self):
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
        s = "    Module: " + self.name + "\n"
        s += "      Metrics:\n"
        for k, v in self.metrics.items():
            s += "        " + k + ": " + str(v) + "\n"
        s += "      Functions:\n"
        for f in self.methods:
            s += str(f)
        s += "      Classes:\n"
        for c in self.classes:
            s += str(c)

        return s


class Function:

    def __init__(self, name: str):
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
        self.name = name
        self.metrics = {}

    def __str__(self):
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
        s = "        Function: " + self.name + "\n"
        s += "          Metrics:" + "\n"
        for k, v in self.metrics.items():
            s += "            " + k + ": " + str(v) + "\n"
        return s


class Type:

    def __init__(self, name: str):
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
        self.name = name
        self.methods = []
        self.bases = []
        self.fields = []
        self.metrics = {}
        self.uses = {}

    def __str__(self):
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
        s = "      Type: " + self.name + "\n"
        s += "        Metrics:\n"
        for k, v in self.metrics.items():
            s += "          " + k + ": " + str(v) + "\n"
        s += "        Uses:\n"
        for k, v in self.uses.items():
            s += "          " + k + ": " + ', '.join(v) + "\n"
        if self.fields:
            s += "        Fields:\n"
            for f in self.fields:
                s += "          " + str(f) + "\n"
        if self.methods:
            s += "        Methods:\n"
            for m in self.methods:
                s += str(m)
        return s


class Method:

    def __init__(self, name: str):
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
        self.name = name
        self.private = name.startswith('_')
        self.predicates = 0
        self.metrics = {}

    def __str__(self):
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
        s = "          Method: " + self.name + "\n"
        s += "            Metrics:" + "\n"
        for k, v in self.metrics.items():
            s += "              " + k + ": " + str(v) + "\n"
        return s
