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
import org.junit.Assert
import org.junit.Test

class CreateEncapsulatedFieldTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File file = File.findFirst("name = ?", "Test11.java")
        Type type = Type.findFirst("name = ?", "Test11")
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")
        String name = "test"
        Modifier[] mods = [Modifier.forName("static"), Modifier.forName("transient")]
        CreateEncapsulatedField fixture = new CreateEncapsulatedField(file, type, fieldType, name, Accessibility.PRIVATE, mods)
        java.io.File actual = new java.io.File(file.getFullPath())

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(10)
        the(type.getStart()).shouldBeEqual(5)
        the(type.getEnd()).shouldBeEqual(10)

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test11 implements Test2 {

    private static transient int test;

    public void paramsTest(int param1, int param2, int param3) {

    }

    public static int getTest() {
        return test;
    }

    public static void setTest(int test) {
        this.test = test;
    }
}""")
    }
}
