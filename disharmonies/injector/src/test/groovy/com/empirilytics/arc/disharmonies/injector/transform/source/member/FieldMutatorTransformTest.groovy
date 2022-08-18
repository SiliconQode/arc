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
import com.empirilytics.arc.disharmonies.injector.transform.source.member.AddFieldSetter
import org.junit.Assert
import org.junit.Test

class FieldMutatorTransformTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        Field field = Field.findFirst("name = ?", "name")
        AddFieldSetter fixture = new AddFieldSetter(file, type, field)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }

    public void setName(String name) {
        this.name = name;
    }
}""")
        the(type.getMethods()).shouldContain(Method.findFirst("name = ?", "setName"))
    }
}
