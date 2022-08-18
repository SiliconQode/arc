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

import com.empirilytics.arc.datamodel.Accessibility;
import com.empirilytics.arc.datamodel.Type;
import com.empirilytics.arc.datamodel.util.DBManager;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class UseDepsTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/UseDeps";
    }

    @Test
    public void testTypes() {
        retrieveType("Types", Accessibility.PUBLIC, Type.CLASS);
        retrieveType("A", Accessibility.PUBLIC, Type.CLASS);
        retrieveType("B", Accessibility.PUBLIC, Type.CLASS);
        retrieveType("C", Accessibility.PUBLIC, Type.CLASS);
    }

    @Test
    public void testMethodInvocation_Known() {
        Type type = retrieveType("MethodInvocation", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown(A)", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testMethodInvocation_Unknown() {
        Type type = retrieveType("MethodInvocation", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown(String)", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testMethodReference_Known() {
        Type type = retrieveType("MethodReference", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown()", "void", Accessibility.PUBLIC);

        // TODO Finish this. fail("Not finished");
    }

    @Test
    public void testMethodReference_Unknown() {
        Type type = retrieveType("MethodReference", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown()", "void", Accessibility.PUBLIC);

        // TODO Finish this. fail("Not finished");
    }

    @Test
    public void testMethodParam_Known() {
        Type type = retrieveType("MethodParameter", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown(A)", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testMethodParam_Unknown() {
        Type type = retrieveType("MethodParameter", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown(String)", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testMethodReturn_Known() {
        Type type = retrieveType("MethodReturnType", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "A testMethodKnown()", "A", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testMethodReturn_Unknown() {
        Type type = retrieveType("MethodReturnType", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "String testMethodUnknown()", "String", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testInstanceCreation_Known() {
        Type type = retrieveType("InstanceCreation", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testInstanceCreation_Unknown() {
        Type type = retrieveType("InstanceCreation", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testLocalVariable_Known() {
        Type type = retrieveType("LocalVariable", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testLocalVariable_Unknown() {
        Type type = retrieveType("LocalVariable", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testFieldAccess_Known() {
        Type type = retrieveType("FieldAccess", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodKnown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    @Test
    public void testFieldAccess_Unknown() {
        Type type = retrieveType("FieldAccess", Accessibility.PUBLIC, Type.CLASS);
        retrieveMethod(type, "void testMethodUnknown()", "void", Accessibility.PUBLIC);

        checkUseDeps(type, "A", "String");
    }

    private void checkUseDeps(Type type, String... depNames) {
        List<String> deps = Arrays.asList(depNames);

        DBManager.getInstance().open(credentials);
        Set<Type> uses = type.getUseFrom();
        uses.forEach(key -> { assertTrue(deps.contains(key.getName())); });
        DBManager.getInstance().close();
    }
}
