"""Abstract Syntax Tree visitor to extract information necessary for calculating metrics

Visits the abstract syntax tree collection data

   Typical usage example:

   visitor = Visitor(proj, path)
   tree = ast.parse(read_files(path))
   visitor.visit(tree)
"""

__author__ = 'Isaac Griffith'
__copyright__ = 'Copyright 2022, Empirilytics'
__version__ = '0.0.1'

import ast
import os
import re
from ast import NodeVisitor, ClassDef, FunctionDef, Assign, Name, Call, Attribute, If, IfExp, For, While, With, AsyncFor, AsyncWith, Try
from typing import Any

from ckpm.data_model import *


class Visitor(NodeVisitor):

    def __init__(self, proj, path: str):
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
        self.field = False
        self.method_stack = []
        self.class_stack = []
        self.module = Module(os.path.basename(path), path)
        self.in_classdef = False
        self.in_fndef = False
        self.project = proj
        self.in_call = False

    def visit_Module(self, node: ast.Module) -> Any:
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
        self.generic_visit(node)
        self.project.modules.append(self.module)

    def visit_ClassDef(self, node: ClassDef) -> Any:
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
        cls = Type(node.name)
        cls.start = node.lineno
        cls.start_col = node.col_offset
        cls.end = node.end_lineno
        cls.end_col = node.end_col_offset
        self.class_stack.append(cls)
        self.in_classdef = True
        self.generic_visit(node)
        self.module.classes.append(self.class_stack.pop())
        self.in_classdef = False

    def visit_FunctionDef(self, node: FunctionDef) -> Any:
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
        # node.lineno
        # node.col_offset
        # node.end_lineno
        # node.end_col_offset
        # node.name
        self.in_fndef = True
        if self.class_stack:
            func = Method(node.name)
            self.method_stack.append(func)
            self.generic_visit(node)
            self.class_stack[-1].methods.append(self.method_stack.pop())
        else:
            func = Method(node.name)
            self.method_stack.append(func)
            self.generic_visit(node)
            self.module.methods.append(func)
        self.in_fndef = False

    def visit_Assign(self, node: Assign) -> Any:
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
        self.generic_visit(node)

    def visit_Name(self, node: Name) -> Any:
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
        if not self.method_stack and not self.class_stack:
            self.module.vars.append(node.id)
        if self.method_stack and self.method_stack[-1].name == '__init__':  # process fields
            if node.id == "self":
                self.field = True
        elif self.in_classdef and not self.in_fndef:
            self.class_stack[-1].bases.append(node.id)
            if self.class_stack[-1].name in self.project.coupling_map:
                self.project.coupling_map[self.class_stack[-1].name].append(node.id)
            else:
                self.project.coupling_map[self.class_stack[-1].name] = [node.id]

        if self.class_stack and self.in_call and re.match(r'[A-Z]', node.id):
            if self.class_stack[-1].name in self.project.coupling_map:
                self.project.coupling_map[self.class_stack[-1].name].append(node.id)
            else:
                self.project.coupling_map[self.class_stack[-1].name] = [node.id]
        elif self.in_call and re.match(r'[A-Z]', node.id):
            if self.module.name in self.project.coupling_map:
                self.project.coupling_map[self.module.name].append(node.id)
            else:
                self.project.coupling_map[self.module.name] = [node.id]
        if self.method_stack and not self.class_stack and node.id in self.module.vars:
            if self.method_stack[-1].name in self.module.uses:
                self.module.uses[self.method_stack[-1].name].append(node.id)
            else:
                self.module.uses[self.method_stack[-1].name] = [node.id]
        self.generic_visit(node)

    def visit_Call(self, node: Call) -> Any:
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
        self.in_call = True
        self.generic_visit(node)
        self.in_call = False

    def visit_Attribute(self, node: Attribute) -> Any:
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
        if self.method_stack and self.method_stack[-1].name == '__init__':
            if type(node.value) is Name and node.value.id == 'self':
                self.class_stack[-1].fields.append(node.attr)
        elif (self.class_stack and self.method_stack) and node.attr in self.class_stack[-1].fields and (
                type(node.value) is Name and node.value.id == 'self'):
            if self.method_stack[-1].name in self.class_stack[-1].uses:
                self.class_stack[-1].uses[self.method_stack[-1].name].append(node.attr)
            else:
                self.class_stack[-1].uses[self.method_stack[-1].name] = [node.attr]
        elif (not self.class_stack and self.method_stack) and node.attr in self.module.vars:
            if self.method_stack[-1].name in self.module.uses:
                self.module.uses[self.method_stack[-1].name].append(node.attr)
            else:
                self.module.uses[self.method_stack[-1].name] = [node.attr]

        self.generic_visit(node)

    def visit_If(self, node: If) -> Any:
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
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_IfExp(self, node: IfExp) -> Any:
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
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_For(self, node: For) -> Any:
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
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_With(self, node: With) -> Any:
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_Try(self, node: Try) -> Any:
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_AsyncFor(self, node: AsyncFor) -> Any:
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_AsyncWith(self, node: AsyncWith) -> Any:
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)

    def visit_While(self, node: While) -> Any:
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
        if self.method_stack:
            self.method_stack[-1].predicates += 1
        self.generic_visit(node)
