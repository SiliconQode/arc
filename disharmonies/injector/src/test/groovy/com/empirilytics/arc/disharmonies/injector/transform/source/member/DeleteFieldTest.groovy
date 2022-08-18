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
import org.junit.Test

class DeleteFieldTest extends BaseSourceTransformSpec {

    @Test
    void "test execute single line field"() {
        // given
        Field field = Field.findFirst("name = ?", "name")
        Type type = Type.findFirst("name = ?", "Test1")
        File file = File.findFirst("name = ?", "Test1.java")
        DeleteField fixture = new DeleteField(file, type, field)
        java.io.File actual = new java.io.File(file.getFullPath())
        setFileContents(file, field, 'String name;', 7, 7)
        type.removeMember(field)
        field.thaw()

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}""")
    }

    @Test
    void "test execute multi line field"() {
        // given
        Field field = Field.findFirst("name = ?", "name")
        Type type = Type.findFirst("name = ?", "Test1")
        File file = File.findFirst("name = ?", "Test1.java")
        DeleteField fixture = new DeleteField(file, type, field)
        java.io.File actual = new java.io.File(file.getFullPath())
        setFileContents(file, field, 'String name =\n        "test";', 7, 8)
        type.removeMember(field)
        field.thaw()

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}""")
    }

    @Test
    void "test execute single line multi field first"() {
        // given
        Field field = Field.findFirst("name = ?", "name9")
        Type type = Type.findFirst("name = ?", "Test9")
        File file = File.findFirst("name = ?", "Test9.java")
        DeleteField fixture = new DeleteField(file, type, field)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(field)
        field.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private String other;

    public void method(Test3 param) {

    }
}""")
    }

    @Test
    void "test execute single line multi field last"() {
        // given
        Field field = Field.findFirst("name = ?", "other")
        Type type = Type.findFirst("name = ?", "Test9")
        File file = File.findFirst("name = ?", "Test9.java")
        DeleteField fixture = new DeleteField(file, type, field)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(field)
        field.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private String name9;

    public void method(Test3 param) {

    }
}""")
    }

    private void setFileContents(File file, Field field, String content, int start, int end) {
        java.io.File f = new java.io.File(file.getFullPath())
        f.text = """\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    $content

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}
"""
        field.setStart(start)
        field.setEnd(end)
        file.setEnd(f.readLines().size())
    }
}
