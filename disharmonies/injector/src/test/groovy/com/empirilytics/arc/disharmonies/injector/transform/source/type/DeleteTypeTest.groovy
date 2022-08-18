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
package com.empirilytics.arc.disharmonies.injector.transform.source.type

import com.empirilytics.arc.datamodel.Accessibility
import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class DeleteTypeTest extends BaseSourceTransformSpec {

    @Test
    void "test execute with a single type in file"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        DeleteType fixture = new DeleteType(file, type)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        def matcher = actual.text =~ /public\.+class\s+Test1\.+\{\.*}/
        the(matcher.size()).shouldEqual(0)
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;""")
    }

    @Test
    void "test execute with multiple types in file"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        DeleteType fixture = new DeleteType(file, type)
        java.io.File actual = new java.io.File(file.getFullPath())
        createMultipleTypeFileFirst(actual)
        file.refresh()
        Type type2 = Type.findFirst("name = ?", "Test5")

        // when
        fixture.execute()
        type2.refresh()

        // then
        def matcher = actual.text =~ /public\.+class\s+Test1\.+\{\.*}/
        the(matcher.size()).shouldEqual(0)
        the(file.getEnd()).shouldEqual(7)
        the(type2.getEnd()).shouldEqual(7)
        the(type2.getStart()).shouldEqual(5)
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test5 {

}""")
    }

    @Test
    void "test execute with multiple types in file to remove is second"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Type type = Type.findFirst("name = ?", "Test1")
        java.io.File actual = new java.io.File(file.getFullPath())
        createMultipleTypeFileSecond(actual)
        file.refresh()
        Type type2 = Type.findFirst("name = ?", "Test5")
        type.refresh()
        DeleteType fixture = new DeleteType(file, type)

        // when
        fixture.execute()
        type2.refresh()

        // then
        def matcher = actual.text =~ /public\.+class\s+Test1\.+\{\.*}/
        the(matcher.size()).shouldEqual(0)
        the(file.getEnd()).shouldEqual(8)
        the(type2.getEnd()).shouldEqual(7)
        the(type2.getStart()).shouldEqual(5)
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test5 {

}""")
    }

    private createMultipleTypeFileFirst(java.io.File actual) {
        File file = File.findFirst("name = ?", "Test1.java")
        file.setEnd(file.getEnd() + 4)
        Type type = Type.findFirst("name = ?", "Test1")
        type.setStart(type.getStart() + 4)
        type.setEnd(type.getEnd() + 4)
        Type type2 = Type.builder().name("Test5").compKey("Test5").accessibility(Accessibility.PUBLIC).start(18).end(20).create()
        file.addType(type2)
        type.getParentNamespace().addType(type2)
        actual.text = """\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}

public class Test5 {

}
"""
    }

    private createMultipleTypeFileSecond(java.io.File actual) {
        File file = File.findFirst("name = ?", "Test1.java")
        file.setEnd(21)
        Type type = Type.findFirst("name = ?", "Test1")
        type.setStart(type.getStart() + 4)
        type.setEnd(type.getEnd() + 4)
        Type type2 = Type.builder().name("Test5").compKey("Test5").accessibility(Accessibility.PUBLIC).start(5).end(7).create()
        file.addType(type2)
        actual.text = """\
package test.test;

import java.util.*;

public class Test5 {

}

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}
"""
    }
}
