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

import static org.junit.Assert.assertTrue;

public class CouplingTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Coupling";
    }

    @Test
    public void testBiDirectionalAssociation() {
        Type type = retrieveType("BiDirectionalAssociation", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isAssociatedTo(proj.findTypeByQualifiedName("test.B")));
        assertTrue(type.isAssociatedFrom(proj.findTypeByQualifiedName("test.B")));
        DBManager.getInstance().close();
    }

    @Test
    public void testTypeB() {
        Type type = retrieveType("B", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isAssociatedTo(proj.findTypeByQualifiedName("test.BiDirectionalAssociation")));
        assertTrue(type.isAssociatedFrom(proj.findTypeByQualifiedName("test.BiDirectionalAssociation")));
        DBManager.getInstance().close();
    }

    @Test
    public void testContainedClasses() {
        Type type = retrieveType("ContainedClasses", Accessibility.PUBLIC, Type.CLASS);
        Type cont = retrieveType("ContainedClass", Accessibility.PRIVATE, Type.CLASS, "STATIC");

        DBManager.getInstance().open(credentials);
        assertTrue(type.doesContain(cont));
        DBManager.getInstance().close();
    }

    @Test
    public void testContainedClass_ContainedClass() {
        Type type = retrieveType("ContainedClasses", Accessibility.PUBLIC, Type.CLASS);
        Type cont = retrieveType("ContainedClass", Accessibility.PRIVATE, Type.CLASS, "STATIC");

        DBManager.getInstance().open(credentials);
        assertTrue(type.isContainedBy(cont));
        DBManager.getInstance().close();
    }

    @Test
    public void testUniDirectionalAssociation() {
        Type type = retrieveType("UniDirectionalAssociation", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isAssociatedTo(proj.findTypeByQualifiedName("test.C")));
        DBManager.getInstance().close();
    }

    @Test
    public void testTypeC() {
        Type type = retrieveType("C", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.isAssociatedFrom(proj.findTypeByQualifiedName("test.UniDirectionalAssociation")));
        DBManager.getInstance().close();
    }

    @Test
    public void testUseDependency() {
        Type type = retrieveType("UseDependency", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.hasUseTo(proj.findTypeByQualifiedName("test.D")));
        DBManager.getInstance().close();
    }

    @Test
    public void testTypeD() {
        Type type = retrieveType("D", Accessibility.PUBLIC, Type.CLASS);

        DBManager.getInstance().open(credentials);
        assertTrue(type.hasUseFrom(proj.findTypeByQualifiedName("test.UseDependency")));
        DBManager.getInstance().close();
    }
}
