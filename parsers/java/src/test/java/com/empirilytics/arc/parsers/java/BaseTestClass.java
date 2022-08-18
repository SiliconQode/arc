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

import java.util.Arrays;
import java.util.List;

import com.empirilytics.arc.datamodel.*;
import com.empirilytics.arc.datamodel.Module;
import com.empirilytics.arc.datamodel.System;
import com.empirilytics.arc.datamodel.util.DBCredentials;
import com.empirilytics.arc.datamodel.util.DBManager;
import com.empirilytics.arc.parsers.*;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public abstract class BaseTestClass {

    BaseModelBuilder tree;
    DBCredentials credentials;
    BaseDirector builder;
    Project proj;

    public abstract String getBasePath();

    @Before
    public void setUp() throws Exception {
        credentials = DBCredentials.builder().type("sqlite").user("dev1").pass("passwd").url("jdbc:sqlite:data/test.db").driver("org.sqlite.JDBC").create();
        DBManager.instance.createDatabase(credentials);

        DBManager.instance.open(credentials);
        System sys   = System.builder().name("Test").key("test" ).basePath(getBasePath()).create();
        proj  = Project.builder().name("test").projKey("test").relPath("").create();
        Module mod   = Module.builder().name("default").moduleKey("default").relPath("").create();
        proj.addModule(mod);
        sys.addProject(proj);
        DBManager.instance.close();

        builder = new JavaDirector(proj, credentials, true, true);
        builder.build(getBasePath());
        this.tree = builder.getBuilder();
    }

    @After
    public void tearDown() throws Exception {
        if (DBManager.instance.isOpen())
            DBManager.instance.close();
        DBManager.instance.open(credentials);
        DBManager.instance.rollback();
        DBManager.instance.close();
    }

    public Type retrieveType(String typeName, Accessibility access, int typeType, String... mods) {
        Type type = tree.findType(typeName);
        DBManager.getInstance().open(credentials);
        assertNotNull(type);
        assertEquals(typeType, type.getType());
        assertEquals(access, type.getAccessibility());
        List<String> modList = Arrays.asList(mods);
        modList.forEach(mod -> assertTrue(type.hasModifier(mod)));
        DBManager.getInstance().close();

        return type;
    }

    public Field retrieveField(Type type, String fieldName, String typeName, Accessibility access, String... mods) {
        DBManager.getInstance().open(credentials);
        Field field = type.getFieldWithName(fieldName);
        assertNotNull(field);
        assertEquals(typeName, field.getType().getTypeName());
        assertEquals(access, field.getAccessibility());
        List<String> modList = Arrays.asList(mods);
        modList.forEach(mod -> assertTrue(field.hasModifier(mod)));
        DBManager.getInstance().close();

        return field;
    }

    public Method retrieveMethod(Type type, String methodSig, String returnType, Accessibility access, String... mods) {
        DBManager.getInstance().open(credentials);
        type.getAllMethods().forEach(x -> {java.lang.System.out.println("Method Sig: " + x.signature()); });
        Method method = type.findMethodBySignature(methodSig);
        if (!returnType.isEmpty())
            assertEquals(returnType, method.getType().getTypeName());
        assertEquals(access, method.getAccessibility());
        List<String> modList = Arrays.asList(mods);
        modList.forEach(mod -> assertTrue(method.hasModifier(mod)));
        DBManager.getInstance().close();

        return method;
    }

    public Parameter retrieveMethodParameter(Method method, String paramName, String paramType, String... mods) {
        DBManager.getInstance().open(credentials);
        java.lang.System.out.println("Method: " + method.signature());
        method.getParams().forEach(x -> {java.lang.System.out.println("Parameter: " + x.getName()); });
        Parameter param = method.getParameterByName(paramName);
        assertEquals(paramName, param.getName());
        assertEquals(paramType, param.getType().getTypeName());
        List<String> modList = Arrays.asList(mods);
        modList.forEach(mod -> assertTrue(param.hasModifier(mod)));
        DBManager.getInstance().close();

        return param;
    }

    public MethodException retrieveMethodException(Method method, String exception) {
        DBManager.getInstance().open(credentials);
        MethodException ref = method.getExceptionByName(exception);
        assertNotNull(ref);
        assertEquals(exception, ref.getTypeRef().getTypeName());
        DBManager.getInstance().close();

        return ref;
    }

    protected Initializer retrieveStaticInitializer(Type type, final int i) {
        DBManager.getInstance().open(credentials);
        Initializer init = type.getStaticInitializer(i);
        assertFalse(init.isInstance());
        assertEquals("<init-" + i + ">", init.getName());
        DBManager.getInstance().close();

        return init;
    }

    protected Initializer retrieveInstanceInitializer(Type type, final int i) {
        DBManager.getInstance().open(credentials);
        Initializer init = type.getInstanceInitializer(i);
        assertTrue(init.isInstance());
        assertEquals("<init-" + i + ">", init.getName());
        DBManager.getInstance().close();

        return init;
    }
}
