/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
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
package com.empirilytics.arc.disharmonies.injector.transform.source.member

import com.empirilytics.arc.datamodel.*
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class AddMethodTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File file = File.findFirst("name = ?", "Test11.java")
        Type type = Type.findFirst("name = ?", "Test11")
        Method method = Method.builder().name("test").accessibility(Accessibility.PUBLIC).compKey("test").type(TypeRef.createPrimitiveTypeRef("void")).create()
        method.addParameter(Parameter.builder().name("x").type(TypeRef.createPrimitiveTypeRef("int")).create())
        method.addModifier("static")
        String body = "    System.out.println([param1]);"
        AddMethod fixture = new AddMethod(file, type, method, body)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test11 implements Test2 {

    public void paramsTest(int param1, int param2, int param3) {

    }

    public static void test(int x) {
        System.out.println(x);
    }
}""")
    }
}
