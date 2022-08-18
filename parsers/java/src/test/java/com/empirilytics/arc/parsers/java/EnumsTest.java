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

import static org.junit.Assert.*;

import com.empirilytics.arc.datamodel.util.DBManager;
import org.junit.Test;

import java.util.List;

public class EnumsTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Enums";
    }

    @Test
    public void testEnums() {
        Type type = retrieveType("Enums", Accessibility.PUBLIC, Type.ENUM);

        DBManager.getInstance().open(credentials);
        List<Literal> literals = type.getLiterals();
        assertEquals(3, literals.size());
        DBManager.getInstance().close();
    }

    @Test
    public void testEnums2() {
        Type type = retrieveType("Enums2", Accessibility.PUBLIC, Type.ENUM);

        DBManager.getInstance().open(credentials);
        List<Literal> literals = type.getLiterals();
        List<Method> methods = type.getAllMethods();
        List<Field> fields = type.getFields();

        assertEquals(3, literals.size());
        assertEquals(2, methods.size());
        assertEquals(1, fields.size());
        DBManager.getInstance().close();
    }
}
