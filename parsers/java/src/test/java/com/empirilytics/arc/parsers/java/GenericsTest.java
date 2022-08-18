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
import com.empirilytics.arc.datamodel.util.DBManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericsTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Generics";
    }

    @Test
    public void testClassMultiTypeParam() {
        DBManager.getInstance().open(credentials);
        Type type = proj.findTypeByQualifiedName("ClassMultiTypeParam");
        assertNotNull(type);
        assertEquals(Type.CLASS, type.getType());

        java.lang.System.out.println("Number of Template Params: " + type.getTemplateParams().size());
        for (TemplateParam node : type.getTemplateParams())
            java.lang.System.out.println("\tName: " + node.getTypeRefs().get(0).getTypeName());

        TemplateParam typeParam = type.getTemplateParam("X");
        assertNotNull(typeParam);

        typeParam = type.getTemplateParam("Y");
        assertNotNull(typeParam);

        Field field = type.getFieldWithName("field1");
        assertNotNull(field);
        assertEquals("X", field.getType().getTypeName());

        field = type.getFieldWithName("field2");
        assertNotNull(field);
        assertEquals("Y", field.getType().getTypeName());
        DBManager.getInstance().close();
    }

    @Test
    public void testClassSingleTypeParam() {
        DBManager.getInstance().open(credentials);
        Type type = proj.findTypeByQualifiedName("ClassSingleTypeParam");
        assertNotNull(type);
        assertEquals(Type.CLASS, type.getType());

        Field field = type.getFieldWithName("field");
        assertNotNull(field);
        assertEquals("T", field.getType().getTypeName());

        Method method = type.getMethodWithName("method1");
        assertNotNull(method);
        assertEquals("T", method.getType().getTypeName());

        method = type.getMethodWithName("method2");
        assertNotNull(method);
        assertEquals("K", method.getType().getTypeName());
        assertNotNull(method.getTemplateParam("K"));
        assertEquals("K", method.getTemplateParams().get(0).getTypeRefs().get(0).getTypeName());

        method = type.getMethodWithName("method3");
        assertNotNull(method);
        assertEquals("X", method.getType().getTypeName());

        TemplateParam typeParam = method.getTemplateParam("X");
        assertNotNull(typeParam);

        typeParam = method.getTemplateParam("Y");
        assertNotNull(typeParam);

        Parameter param = method.getParameterByName("param");
        assertNotNull(param);
        assertEquals("Y", param.getType().getTypeName());

        method = type.getMethodWithName("method4");
        assertNotNull(method);
        assertEquals("X", method.getType().getTypeName());

        typeParam = method.getTemplateParam("X");
        assertNotNull(typeParam);
        DBManager.getInstance().close();
    }

    @Test
    public void testClassSingleTypeParamMultiBound() {
        DBManager.getInstance().open(credentials);
        Type type = proj.findTypeByQualifiedName("ClassSingleTypeParamMultiBound");
        assertNotNull(type);
        assertEquals(Type.CLASS, type.getType());

        TemplateParam typeParam = type.getTemplateParam("X");
        assertNotNull(typeParam);
        DBManager.getInstance().close();
    }

    @Test
    public void testClassWithCollections() {
        DBManager.getInstance().open(credentials);
        Type type = proj.findTypeByQualifiedName("ClassWithCollections");
        assertNotNull(type);
        assertEquals(Type.CLASS, type.getType());

        Field field = type.getFieldWithName("field1");
        assertNotNull(field);
        assertEquals("List", field.getType().getTypeName());
        Assert.assertEquals("String", field.getType().getTypeArgs().get(0).getTypeName());

        field = type.getFieldWithName("field2");
        assertNotNull(field);
        assertEquals("Map", field.getType().getTypeName());
        assertEquals("String", field.getType().getTypeArgs().get(0).getTypeName());
        assertEquals("Integer", field.getType().getTypeArgs().get(1).getTypeName());

        field = type.getFieldWithName("field3");
        assertNotNull(field);
        assertEquals("List", field.getType().getTypeName());
        TypeRef typeRef = field.getType().getTypeArgs().get(0);
        assertEquals("?", typeRef.getTypeName());
        assertEquals(TypeRefType.WildCard, typeRef.getType());
        List<TypeRef> bounds = typeRef.getBounds();
        assertEquals("Number", bounds.get(0).getTypeName());

        field = type.getFieldWithName("field4");
        assertNotNull(field);
        assertEquals("List", field.getType().getTypeName());
        typeRef = field.getType().getTypeArgs().get(0);
        assertEquals("List", typeRef.getTypeName());
        assertNotNull(typeRef.getTypeArgs());
        java.lang.System.out.println("Typed Size: " + typeRef.getTypeArgs().size());
        assertEquals("String", field.getType().getTypeArgs().get(0).getTypeArgs().get(0).getTypeName());

        field = type.getFieldWithName("field5");
        assertNotNull(field);
        assertEquals("List", field.getType().getTypeName());
        assertEquals("?", field.getType().getTypeArgs().get(0).getTypeName());
        DBManager.getInstance().close();
    }
}
