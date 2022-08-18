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

import com.empirilytics.arc.datamodel.*;
import com.empirilytics.arc.parsers.*;
import org.junit.Test;

public class MethodsTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Methods";
    }

    @Test
    public void testAbstractClassMethods() {
        Type type = retrieveType("AbstractClassMethods", Accessibility.PUBLIC, Type.CLASS, "ABSTRACT");

        retrieveMethod(type, "void testMethod1()", "void", Accessibility.PUBLIC, "ABSTRACT");
        retrieveMethod(type, "void testMethod2()", "void", Accessibility.PUBLIC, "ABSTRACT");
    }

    @Test
    public void testInterfaceMethods() {
        Type type = retrieveType("InterfaceMethods", Accessibility.PUBLIC, Type.INTERFACE);

        retrieveMethod(type, "void testMethod1()", "void", Accessibility.PUBLIC, "ABSTRACT");
        retrieveMethod(type, "void testMethod2()", "void", Accessibility.PUBLIC, "ABSTRACT");
    }

    @Test
    public void testMethodExceptions() {
        Type type = retrieveType("MethodExceptions", Accessibility.PUBLIC, Type.CLASS);

        Method method = retrieveMethod(type, "void exceptions()", "void", Accessibility.PUBLIC);
//        ((List<?>)method.getExceptions()).forEach(Project.out::println);
        retrieveMethodException(method, "RuntimeException");

        method = retrieveMethod(type, "void exceptions2()", "void", Accessibility.PUBLIC);
//        java.lang.System.out.println(method.getExceptions());
        retrieveMethodException(method, "RuntimeException");
        retrieveMethodException(method, "NullPointerException");
    }

    @Test
    public void testMethodMultipleParams() {
        Type type = retrieveType("MethodMultipleParams", Accessibility.PUBLIC, Type.CLASS);

        Method method = retrieveMethod(type, "void method(String, String)", "void", Accessibility.PUBLIC);
        retrieveMethodParameter(method, "param1", "String");
        retrieveMethodParameter(method, "param2", "String");

        method = retrieveMethod(type, "void vargs(String, String)", "void", Accessibility.PUBLIC);
        retrieveMethodParameter(method, "param1", "String");
        retrieveMethodParameter(method, "param2", "String");
    }

    @Test
    public void testMethodParamMods() {
        Type type = retrieveType("MethodParamMods", Accessibility.PUBLIC, Type.CLASS);

        Method method = retrieveMethod(type, "void method(String)", "void", Accessibility.PUBLIC);
        retrieveMethodParameter(method, "param", "String", "FINAL");
    }

    @Test
    public void testMethodParams() {
        Type type = retrieveType("MethodParams", Accessibility.PUBLIC, Type.CLASS);

        Method method = retrieveMethod(type, "void knownType(B)", "void", Accessibility.PUBLIC);
        retrieveMethodParameter(method, "param", "B");
        method = retrieveMethod(type, "void unknownType(String)", "void", Accessibility.PUBLIC);
        retrieveMethodParameter(method, "param", "String");
    }

    @Test
    public void testMethodsReturnType() {
        Type type = retrieveType("MethodsReturnType", Accessibility.PUBLIC, Type.CLASS);

        retrieveMethod(type, "String unknownType()", "String", Accessibility.PUBLIC);
        retrieveMethod(type, "A knownType()", "A", Accessibility.PUBLIC);
    }

    @Test
    public void testMethodsTypeParam() {
        Type type = retrieveType("MethodsTypeParam", Accessibility.PUBLIC, Type.CLASS);

        retrieveMethod(type, "T testMethod()", "T", Accessibility.PUBLIC);
    }

    @Test
    public void testVoidMethods() {
        Type type = retrieveType("VoidMethods", Accessibility.PUBLIC, Type.CLASS);

        retrieveMethod(type, "void testMethod1()", "void", Accessibility.PUBLIC);
        retrieveMethod(type, "void testMethod2()", "void", Accessibility.PUBLIC);
    }

    @Test
    public void testConstructors() {
        Type type = retrieveType("Constructors", Accessibility.PUBLIC, Type.CLASS);

        retrieveMethod(type, "Constructors()", "", Accessibility.PUBLIC);
        Method method = retrieveMethod(type, "Constructors(int)", "", Accessibility.PRIVATE);
        retrieveMethodParameter(method, "x", "int");
    }

    @Test
    public void testStaticInit() {
        Type type = retrieveType("StaticInit", Accessibility.PUBLIC, Type.CLASS);
        retrieveStaticInitializer(type, 1);
    }

    @Test
    public void testInitializer() {
        Type type = retrieveType("Initializer", Accessibility.PUBLIC, Type.CLASS);
        retrieveInstanceInitializer(type, 1);
    }
}
