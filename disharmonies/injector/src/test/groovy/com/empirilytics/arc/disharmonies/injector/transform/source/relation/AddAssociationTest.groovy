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
package com.empirilytics.arc.disharmonies.injector.transform.source.relation

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class AddAssociationTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File fromFile = File.findFirst("name = ?", "Test1.java")
        File toFile = File.findFirst("name = ?", "Test9.java")
        Type fromType = Type.findFirst("name = ?", "Test1")
        Type toType = Type.findFirst("name = ?", "Test9")
        String toName = "test1"
        String fromName = "test9"
        boolean bidirect = true
        AddAssociation fixture = new AddAssociation(fromFile, fromType, toFile, toType, toName, fromName, bidirect)
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())
        java.io.File toActual = new java.io.File(toFile.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private Test9 test9;
    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }

    public Test9 getTest9() {
        return test9;
    }

    public void setTest9(Test9 test9) {
        this.test9 = test9;
    }
}""")

        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private Test1 test1;
    private String name9, other;

    public void method(Test3 param) {

    }

    public Test1 getTest1() {
        return test1;
    }

    public void setTest1(Test1 test1) {
        this.test1 = test1;
    }
}""")
        the(fromType.getAssociatedTo()).shouldContain(toType)
        the(toType.getAssociatedTo()).shouldContain(fromType)
    }
}
