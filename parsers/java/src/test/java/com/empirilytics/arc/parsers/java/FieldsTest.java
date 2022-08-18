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
import org.junit.Test;

public class FieldsTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Fields";
    }

    @Test
    public void testClassA() {
        retrieveType("A", Accessibility.PUBLIC, Type.CLASS);
    }

    @Test
    public void testClassB() {
        retrieveType("B", Accessibility.PUBLIC, Type.CLASS);
    }

    @Test
    public void testFieldModifiers() {
        Type type = retrieveType("FieldModifiers", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "XXX", "String", Accessibility.PUBLIC, "STATIC", "FINAL");
        retrieveField(type, "yyy", "String", Accessibility.PROTECTED, "TRANSIENT");
        retrieveField(type, "zzz", "String", Accessibility.PRIVATE, "VOLATILE");
        retrieveField(type, "aaa", "String", Accessibility.DEFAULT);
    }

    @Test
    public void testMultipleFieldArrayType() {
        Type type = retrieveType("MultipleFieldArrayType", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "field1", "String[]", Accessibility.PRIVATE);
        retrieveField(type, "field2", "String[][]", Accessibility.PRIVATE);
    }

    @Test
    public void testMultipleFields() {
        Type type = retrieveType("MultipleFields", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "field1", "String", Accessibility.DEFAULT);
        retrieveField(type, "field2", "A", Accessibility.DEFAULT);
        retrieveField(type, "field3", "B", Accessibility.DEFAULT);
        retrieveField(type, "field4", "String", Accessibility.DEFAULT);
    }

    @Test
    public void testSingleFieldArrayType() {
        Type type = retrieveType("SingleFieldArrayType", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "field", "A[]", Accessibility.PRIVATE);
    }

    @Test
    public void testSingleFieldKnownType() {
        Type type = retrieveType("SingleFieldKnownType", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "field", "A", Accessibility.PRIVATE);
    }

    @Test
    public void testSingleFieldUnknownType() {
        Type type = retrieveType("SingleFieldUnknownType", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "field", "String", Accessibility.PRIVATE);
    }

    @Test
    public void testSingleFieldPrimitiveType() {
        Type type = retrieveType("SingleFieldPrimitiveType", Accessibility.PUBLIC, Type.CLASS);

        retrieveField(type, "x", "int", Accessibility.PRIVATE);
    }
}
