"""An implementation of the MOOD metrics suite

Implemented metrics include: Attribute Hiding Factor (ahf), Method Hiding Factor (mhf), Method Inheritance Factor (mif)
and Coupling Factor (cof).

   Typical usage example:

   metrics = MOODMetrics(project)
   metrics.evaluate(type)
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

from ckpm.metrics import Metrics


class MOODMetrics(Metrics):

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
        super().__init__(project)

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
        # self.ahf(type_or_module)
        # self.mhf(type_or_module)
        # self.mif(type_or_module)
        # self.cof(type_or_module)
        pass

    def ahf(self, type_or_module):
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
        raise NotImplementedError('Operation not yet implemented!')

    def mhf(self, type_or_module):
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
        raise NotImplementedError('Operation not yet implemented!')

    def mif(self, type_or_module):
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
        raise NotImplementedError('Operation not yet implemented!')

    def cof(self, type_or_module):
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
        raise NotImplementedError('Operation not yet implemented!')
