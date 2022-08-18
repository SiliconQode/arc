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

import com.empirilytics.arc.datamodel.Field
import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.Test

class MoveFieldTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File fromFile = File.findFirst("name = ?", "Test10.java")
        File toFile = File.findFirst("name = ?", "Test11.java")
        Type fromType = Type.findFirst("name = ?", "Test10")
        Type toType = Type.findFirst("name = ?", "Test11")
        Field field = Field.findFirst("name = ?", "COUNT")
        MoveField fixture = new MoveField(fromFile, fromType, toFile, toType, field)
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())
        java.io.File toActual = new java.io.File(toFile.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    public static void method4(Test3 param) {

    }
}""")
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(toActual.text, """\
package test.test;

import java.util.*;

public class Test11 implements Test2 {
    private volatile static final int COUNT;

    public void paramsTest(int param1, int param2, int param3) {

    }
}""")
        the(fromType.getFields()).shouldNotContain(field)
        the(toType.getFields()).shouldContain(field)
    }
}
