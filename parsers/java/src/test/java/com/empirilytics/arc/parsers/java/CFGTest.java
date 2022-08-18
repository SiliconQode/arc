/**
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
 * Copyright (c) 2015-2021 Empirilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.empirilytics.arc.parsers.java;

import com.google.common.collect.Lists;
import com.empirilytics.arc.datamodel.*;
import com.empirilytics.arc.datamodel.Module;
import com.empirilytics.arc.datamodel.System;
import com.empirilytics.arc.datamodel.cfg.ControlFlowGraph;
import com.empirilytics.arc.datamodel.util.DBCredentials;
import com.empirilytics.arc.datamodel.util.DBManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CFGTest extends BaseTestClass {

    public String getBasePath() { return "data/java-example-project/CFG"; }

    @Test
    public void testAssert() {
        Type type = retrieveType("Assert", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodAssert1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();
        assertTrue(dot.contains("METHSTRT_0 -> VARDECL_1"));
        assertTrue(dot.contains("VARDECL_1 -> ASSERT_2"));
        assertTrue(dot.contains("ASSERT_2 -> METHEND_0"));
    }

    @Test
    public void testBasic() {
        Type type = retrieveType("Basic", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void method()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();
        assertTrue(dot.contains("METHSTRT_0 -> VARDECL_1"));
        assertTrue(dot.contains("VARDECL_1 -> METHEND_0"));
    }

    @Test
    public void testBreak() {
        Type type = retrieveType("Break", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodBreak1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> FOR_1",
                "  FOR_1 -> END_3",
                "  FOR_1 -> VARDECL_4",
                "  END_3 -> METHEND_0",
                "  VARDECL_4 -> EXPRESSION_5",
                "  END_2 -> FOR_1",
                "  EXPRESSION_5 -> IF_6",
                "  IF_6 -> END_7",
                "  IF_6 -> EXPRESSION_8",
                "  END_7 -> EMPTY_10",
                "  EXPRESSION_8 -> EMPTY_9",
                "  EMPTY_10 -> END_2",
                "  EMPTY_9 -> END_7"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testContinue() {
        Type type = retrieveType("Continue", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodContinue1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> WHILE_2",
                "  WHILE_2 -> EXPRESSION_5",
                "  WHILE_2 -> END_4",
                "  EXPRESSION_5 -> IF_6",
                "  END_4 -> METHEND_0",
                "  END_3 -> WHILE_2",
                "  IF_6 -> END_7",
                "  IF_6 -> EXPRESSION_8",
                "  END_7 -> END_3",
                "  EXPRESSION_8 -> EMPTY_9",
                "  EMPTY_9 -> CONTINUE_10",
                "  EMPTY_9 -> EMPTY_11",
                "  CONTINUE_10 -> WHILE_2",
                "  EMPTY_11 -> END_7"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testDoWhile() {
        Type type = retrieveType("DoWhile", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodDoWhile()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> DO_2",
                "  DO_2 -> EXPRESSION_5",
                "  EXPRESSION_5 -> EMPTY_6",
                "  END_3 -> METHEND_0",
                "  END_3 -> DO_2",
                "  EMPTY_6 -> END_3"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testEmpty() {
        Type type = retrieveType("Empty", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodEmpty1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> EMPTY_1",
                "  EMPTY_1 -> METHEND_0"};
        compareDOT(contents, dot);
    }

    @Test
    public void testExpression() {
        Type type = retrieveType("Expression", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodExpr1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> EMPTY_2",
                "  EMPTY_2 -> METHEND_0"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testFor() {
        Type type = retrieveType("For", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodFor1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> FOR_1",
                "  FOR_1 -> END_3",
                "  FOR_1 -> VARDECL_4",
                "  END_3 -> METHEND_0",
                "  VARDECL_4 -> EXPRESSION_5",
                "  END_2 -> FOR_1",
                "  EXPRESSION_5 -> EMPTY_6",
                "  EMPTY_6 -> END_2"};

        compareDOT(contents, dot);
    }

    @Test
    public void testIfs() {
        Type type = retrieveType("Ifs", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodIf1()", "void", Accessibility.DEFAULT);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> IF_2",
                "  IF_2 -> END_3",
                "  IF_2 -> EXPRESSION_4",
                "  END_3 -> EMPTY_6",
                "  EXPRESSION_4 -> EMPTY_5",
                "  EMPTY_6 -> METHEND_0",
                "  EMPTY_5 -> END_3"};

        compareDOT(contents, dot);
    }

    @Test
    public void testIfs_2() {
        Type type = retrieveType("Ifs", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodIf2()", "void", Accessibility.DEFAULT);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();
        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> IF_2",
                "  IF_2 -> END_3",
                "  IF_2 -> EXPRESSION_4",
                "  END_3 -> METHEND_0",
                "  EXPRESSION_4 -> EMPTY_5",
                "  EMPTY_5 -> EXPRESSION_6",
                "  EXPRESSION_6 -> EMPTY_7",
                "  EMPTY_7 -> END_3"};

        compareDOT(contents, dot);
    }

    @Test
    public void testReturn() {
        Type type = retrieveType("Return", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodReturn1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> IF_2",
                "  IF_2 -> RETURN_4",
                "  IF_2 -> END_3",
                "  END_3 -> METHEND_0",
                "  RETURN_4 -> METHEND_0"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testSwitch() {
        Type type = retrieveType("Switch", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodSwitch1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> VARDECL_2",
                "  VARDECL_2 -> SWITCH_3",
                "  SWITCH_3 -> EMPTY_7",
                "  SWITCH_3 -> EMPTY_5",
                "  EMPTY_7 -> BREAK_8",
                "  EMPTY_5 -> BREAK_6",
                "  END_4 -> EMPTY_9",
                "  EMPTY_9 -> METHEND_0",
                "  BREAK_6 -> END_4",
                "  BREAK_8 -> END_4"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testSynchronized() {
        Type type = retrieveType("Synchronized", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodSync1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> SYNCHRONIZED_2",
                "  SYNCHRONIZED_2 -> EMPTY_4",
                "  EMPTY_4 -> END_3",
                "  END_3 -> METHEND_0"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testThrow() {
        Type type = retrieveType("Throw", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodThrow1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> THROW_1",
                "  THROW_1 -> METHEND_0"};
        compareDOT(contents, dot);
    }

    @Test
    public void testTryCatch_1() {
        Type type = retrieveType("TryCatch", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodTryCatch1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> TRY_1",
                "  TRY_1 -> EMPTY_3",
                "  END_2 -> METHEND_0",
                "  EMPTY_3 -> CATCH_4",
                "  CATCH_4 -> END_5",
                "  END_5 -> END_2"};
        compareDOT(contents, dot);
    }

    @Test
    public void testTryCatch_2() {
        Type type = retrieveType("TryCatch", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodTryCatch2()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> TRY_1",
                "  TRY_1 -> EMPTY_3",
                "  EMPTY_3 -> CATCH_4",
                "  END_2 -> METHEND_0",
                "  CATCH_4 -> EMPTY_6",
                "  EMPTY_6 -> END_5",
                "  END_5 -> END_2"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testTryCatch_3() {
        Type type = retrieveType("TryCatch", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodTryCatch3()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> TRY_1",
                "  TRY_1 -> EMPTY_3",
                "  END_2 -> METHEND_0",
                "  EMPTY_3 -> CATCH_4",
                "  CATCH_4 -> EMPTY_6",
                "  END_5 -> FINALLY_7",
                "  EMPTY_6 -> END_5",
                "  FINALLY_7 -> EMPTY_9",
                "  END_8 -> END_2",
                "  EMPTY_9 -> END_8"};
        compareDOT(contents, dot);
    }

    @Test
    public void testVarDecl() {
        Type type = retrieveType("VarDecl", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodVarDecl1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                    "  VARDECL_1 -> VARDECL_2",
                    "  VARDECL_2 -> METHEND_0"};
        compareDOT(contents, dot);
    }

    @Test
    public void testWhile() {
        Type type = retrieveType("While", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodWhile1()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> WHILE_2",
                "  WHILE_2 -> EXPRESSION_5",
                "  WHILE_2 -> END_4",
                "  EXPRESSION_5 -> EMPTY_6",
                "  END_4 -> METHEND_0",
                "  END_3 -> WHILE_2",
                "  EMPTY_6 -> END_3"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testBlock() {
        Type type = retrieveType("Block", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodBlock()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> EXPRESSION_1",
                "  EXPRESSION_1 -> EMPTY_2",
                "  EMPTY_2 -> METHEND_0"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testLabeled1() {
        Type type = retrieveType("Labeled", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodLabeledBreak()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> VARDECL_1",
                "  VARDECL_1 -> LABELED_2",
                "  LABELED_2 -> EXPRESSION_3",
                "  EXPRESSION_3 -> FOR_4",
                "  FOR_4 -> VARDECL_7",
                "  FOR_4 -> END_6",
                "  VARDECL_7 -> EXPRESSION_8",
                "  END_6 -> METHEND_0",
                "  END_5 -> FOR_4",
                "  EXPRESSION_8 -> FOR_9",
                "  FOR_9 -> EMPTY_17",
                "  FOR_9 -> END_11",
                "  FOR_9 -> VARDECL_12",
                "  EMPTY_17 -> END_5",
                "  END_11 -> END_5",
                "  VARDECL_12 -> EXPRESSION_13",
                "  END_10 -> FOR_9",
                "  EXPRESSION_13 -> IF_14",
                "  IF_14 -> END_15",
                "  IF_14 -> BREAK_16",
                "  END_15 -> END_10",
                "  BREAK_16 -> LABELED_2"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testLabeled2() {
        Type type = retrieveType("Labeled", Accessibility.PUBLIC, Type.CLASS);
        Method method = retrieveMethod(type, "void methodLabeledContinue()", "void", Accessibility.PUBLIC);

        ControlFlowGraph cfg = method.getCfg();

        String dot = cfg.toDOT();

        String[] contents = {"  METHSTRT_0 -> LABELED_1",
                "  LABELED_1 -> EXPRESSION_2",
                "  EXPRESSION_2 -> FOR_3",
                "  FOR_3 -> END_5",
                "  FOR_3 -> VARDECL_6",
                "  END_5 -> METHEND_0",
                "  VARDECL_6 -> EXPRESSION_7",
                "  END_4 -> FOR_3",
                "  EXPRESSION_7 -> EMPTY_8",
                "  EMPTY_8 -> FOR_9",
                "  FOR_9 -> END_11",
                "  FOR_9 -> EMPTY_18",
                "  FOR_9 -> VARDECL_12",
                "  END_11 -> END_4",
                "  EMPTY_18 -> END_4",
                "  VARDECL_12 -> EXPRESSION_13",
                "  END_10 -> FOR_9",
                "  EXPRESSION_13 -> EMPTY_14",
                "  EMPTY_14 -> IF_15",
                "  IF_15 -> CONTINUE_17",
                "  IF_15 -> END_16",
                "  CONTINUE_17 -> FOR_9",
                "  END_16 -> END_10"};

        java.lang.System.out.println(dot);
        compareDOT(contents, dot);
    }

    @Test
    public void testNested() {
        // TODO finish this. fail("Not yet implemented");
    }

    private void compareDOT(String[] expected, String dot) {
        String[] split = dot.split("\n");
        List<String> expList = Lists.newArrayList(expected);
        List<String> dotList = Lists.newArrayList(split);
        dotList.remove(0);
        dotList.remove(dotList.size() - 1);

        dotList.removeAll(expList);
        assertTrue(dotList.isEmpty());
    }
}
