"""Implementations of other useful metrics beyond the CK and MOOD metrics

Includes an implementation of the following metrics: Data Abstraction Metric (dam), Average Method Complexity (amc),
Cohesion Among Methods (cam), Afferent Coupling (ca), Efferent Coupling (ce), Inheritance Coupling (ic), Coupling
Between Methods (cbm), Essential Cyclometric Complexity (ecc).

   Typical usage example:

   metrics = OtherMetrics(project)
   metrics.evaluate(type)
   metrics.evaluate(type)
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

from ckpm.data_model import Type
from ckpm.metrics import Metrics


class OtherMetrics(Metrics):

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
        self.dam(type_or_module)
        self.amc(type_or_module)

    def dam(self, type_or_module):
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
        dam = 0
        if type(type_or_module) is Type:
            dam = len(list(filter(lambda x: not x.startswith('_'), type_or_module.fields)))
        else:
            dam = len(list(filter(lambda x: not x.startswith('_'), type_or_module.vars)))
        type_or_module.metrics['dam'] = dam

    def amc(self, type_or_module):
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
        loc = type_or_module.metrics['loc']
        wmc = type_or_module.metrics['wmc']
        amc = 0
        if not wmc == 0:
            amc = loc / wmc
        type_or_module.metrics['amc'] = amc

    def cam(self, source):
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

    def ca(self):
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

    def ce(self):
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

    def ic(self):
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

    def cbm(self):
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

    def ecc(self):
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

