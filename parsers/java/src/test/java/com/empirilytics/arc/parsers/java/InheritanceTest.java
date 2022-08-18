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
import org.testng.collections.Lists;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class InheritanceTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Inheritance";
    }

    @Test
    public void testTypeA() {
        retrieveType("A", Accessibility.PUBLIC, Type.CLASS);
    }

    @Test
    public void testTypeB() {
        retrieveType("B", Accessibility.PUBLIC, Type.CLASS);
    }

    @Test
    public void testTypeC() {
        retrieveType("C", Accessibility.PUBLIC, Type.INTERFACE);
    }

    @Test
    public void testTypeD() {
        retrieveType("D", Accessibility.PUBLIC, Type.INTERFACE);
    }

    @Test
    public void testClassGenMultiReal() {
        Type type = retrieveType("ClassGenMultiReal", Accessibility.PUBLIC, Type.CLASS);
        Type gen = retrieveType("A", Accessibility.PUBLIC, Type.CLASS);
        Type real1 = retrieveType("D", Accessibility.PUBLIC, Type.INTERFACE);
        Type real2 = retrieveType("C", Accessibility.PUBLIC, Type.INTERFACE);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isGeneralizedBy(gen));
        assertTrue(type.isRealizing(real1));
        assertTrue(type.isRealizing(real2));
        DBManager.getInstance().close();
    }

    @Test
    public void testClassGenReal() {
        Type type = retrieveType("ClassGenReal", Accessibility.PUBLIC, Type.CLASS);
        Type gen = retrieveType("B", Accessibility.PUBLIC, Type.CLASS);
        Type real = retrieveType("C", Accessibility.PUBLIC, Type.INTERFACE);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isGeneralizedBy(gen));
        assertTrue(type.isRealizing(real));
        DBManager.getInstance().close();
    }

    @Test
    public void testClassInheritance() {
        Type type = retrieveType("ClassInheritance", Accessibility.PUBLIC, Type.CLASS);
        Type gen = retrieveType("A", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isGeneralizedBy(gen));
        DBManager.getInstance().close();
    }

    @Test
    public void testClassRealization() {
        Type type = retrieveType("ClassRealization", Accessibility.PUBLIC, Type.CLASS);
        Type real = retrieveType("C", Accessibility.PUBLIC, Type.INTERFACE);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isRealizing(real));
        DBManager.getInstance().close();
    }

    @Test
    public void testEnumMultipleInterface() {
        Type type = retrieveType("EnumMultipleInterface", Accessibility.PUBLIC, Type.ENUM);

        DBManager.getInstance().open(credentials);
        Set<Type> reals = type.getRealizes();
        assertFalse(reals.isEmpty());
        for (Type t : reals) {
            if (!t.getName().equals("Serializable") && !t.getName().equals("Comparable"))
                fail("Incorrect type for realization: " + t.getName());
        }
        DBManager.getInstance().close();
    }

    @Test
    public void testEnumSingleInterface() {
        Type type = retrieveType("EnumSingleInterface", Accessibility.PUBLIC, Type.ENUM);

        DBManager.getInstance().open(credentials);
        List<Type> reals = Lists.newArrayList(type.getRealizes());
        assertFalse(reals.isEmpty());
        if (!reals.get(0).getName().equals("Comparable"))
            fail("Incorrect type for realization");
        DBManager.getInstance().close();
    }

    @Test
    public void testInteraceGen() {
        Type type = retrieveType("InterfaceGen", Accessibility.PUBLIC, Type.INTERFACE);
        Type real = retrieveType("C", Accessibility.PUBLIC, Type.INTERFACE);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isGeneralizedBy(real));
        DBManager.getInstance().close();
    }
}
