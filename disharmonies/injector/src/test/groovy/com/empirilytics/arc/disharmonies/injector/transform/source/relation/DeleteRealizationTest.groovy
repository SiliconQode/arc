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

class DeleteRealizationTest extends BaseSourceTransformSpec {

    @Test
    void "test execute with only a single realization"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        Type real = Type.findFirst("name = ?", "Test2")
        DeleteRealization fixture = new DeleteRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("public class Test1")
        the(actual.text).shouldNotContain("implements Test2")
    }

    @Test
    void "test execute with multiple realizations and no generalization"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        Type real = Type.findFirst("name = ?", "Test2")
        DeleteRealization fixture = new DeleteRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())
        setFileText(actual, "", "Test3, Test2, Test4")

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("public class Test1 implements Test3, Test4")
    }

    @Test
    void "test execute with only a single realization and generalization"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        Type real = Type.findFirst("name = ?", "Test2")
        DeleteRealization fixture = new DeleteRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())
        setFileText(actual, "Test3", "")

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("public class Test1 extends Test3")
    }

    @Test
    void "test execute with multiple realizations and generalization"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        Type real = Type.findFirst("name = ?", "Test2")
        DeleteRealization fixture = new DeleteRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())
        setFileText(actual, "Test3", "Test3, Test2, Test4")

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("public class Test1 extends Test3 implements Test3, Test4")
    }

    private void setFileText(java.io.File file, String ext, String real) {
        if (ext)
            ext = "extends $ext "
        if (real)
            real = "implements $real "

        file.text = """\
package test.test;

import java.util.*;

public class Test1 ${ext}${real}{

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}
"""
    }
}
