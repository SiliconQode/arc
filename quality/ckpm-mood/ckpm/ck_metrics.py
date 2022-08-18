"""An implementation of the Chidamber and Kenemer metrics suite

Includes the following metrics: Weighted Methods per Class (wmc), Number of Public Methods (npm), Coupling Between
Objects (cbo), Lack of Cohesion in Object Methods (lcom), Improved Lack of Cohesion of Object Methods (lcom3), Depth
of Inheritance Tree (dit), Number of Children (noc), and Response for a Class (rfc). Additionally, it includes an
implementation of McCabe's Cyclomatic Complexity.

   Typical usage example:

   metrics = CKMetrics(project)
   metrics.evaluate(type)
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

from ckpm.metrics import Metrics


class CKMetrics(Metrics):

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
        self.cc(type_or_module)
        self.wmc(type_or_module)
        self.cbo(type_or_module)
        self.lcom(type_or_module)
        self.npm(type_or_module)

    def cc(self, type_or_module):
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
        cc = 0
        for m in type_or_module.methods:
            cc += m.predicates + 1
            m.metrics['cc'] = cc

    def wmc(self, type_or_module):
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
        wmc = 0
        for m in type_or_module.methods:
            wmc += m.metrics['cc']

        type_or_module.metrics['wmc'] = wmc

    def npm(self, type_or_module):
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
        npm = 0
        npm = len(list(filter(lambda x: not x.private, type_or_module.methods)))

        type_or_module.metrics['npm'] = npm

    def cbo(self, type_or_module):
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
        cbo = 0
        if type_or_module.name in self.project.coupling_map:
            cbo = len(self.project.coupling_map[type_or_module.name])
        type_or_module.metrics['cbo'] = cbo

    def lcom(self, type_or_module):
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
        disjuncts = 0
        conjuncts = 0
        for m in type_or_module.methods:
            for n in type_or_module.methods:
                if m is not n:
                    m_uses = set()
                    n_uses = set()
                    if m.name in type_or_module.uses:
                        m_uses = set(type_or_module.uses[m.name])
                    if n.name in type_or_module.uses:
                        n_uses = set(type_or_module.uses[n.name])
                    if m_uses.intersection(n_uses):
                        conjuncts += 1
                    else:
                        disjuncts += 1
        lcom = 0
        if disjuncts > conjuncts:
            lcom = disjuncts - conjuncts
        type_or_module.metrics['lcom'] = lcom

    def dit(self, type_or_module):
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

    def noc(self, type_or_module):
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

    def rfc(self, type_or_module):
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

    def lcom3(self, type_or_module):
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
