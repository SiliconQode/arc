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

public class TypesTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Types";
    }

    @Test
    public void testClassModifiers_1() {
        retrieveType("ClassModifiers", Accessibility.PUBLIC, Type.CLASS, "STRICTFP");
    }

    @Test
    public void testClassModifiers_2() {
        retrieveType("ClassModifiers2", Accessibility.PROTECTED, Type.CLASS);
    }

    @Test
    public void testClassModifiers_3() {
        retrieveType("ClassModifiers3", Accessibility.DEFAULT, Type.CLASS);
    }

    @Test
    public void testClassModifiers_4() {
        retrieveType("ClassModifiers4", Accessibility.PUBLIC, Type.CLASS, "FINAL");
    }

    @Test
    public void testClassModifiers_5() {
        retrieveType("ClassModifiers5", Accessibility.PRIVATE, Type.CLASS, "STATIC");
    }

    @Test
    public void testClassModifiers_6() {
        retrieveType("ClassModifiers6", Accessibility.PUBLIC, Type.CLASS, "ABSTRACT");
    }

    @Test
    public void testInterfaceModifiers_1() {
        retrieveType("InterfaceModifiers", Accessibility.PUBLIC, Type.INTERFACE);
    }

    @Test
    public void testInterfaceModifiers_2() {
        retrieveType("InterfaceModifiers2", Accessibility.DEFAULT, Type.INTERFACE);
    }
}
