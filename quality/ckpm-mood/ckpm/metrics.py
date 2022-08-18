"""This module contains the abstract base class from which all metrics analysis classes derive
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

from abc import ABC, abstractmethod


class Metrics(ABC):

    def __init__(self, project):
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
        self.project = project

    @abstractmethod
    def evaluate(self, type_or_module):
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
        pass
