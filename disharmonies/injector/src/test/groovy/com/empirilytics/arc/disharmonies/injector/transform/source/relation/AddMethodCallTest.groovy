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

import com.empirilytics.arc.datamodel.Accessibility
import com.empirilytics.arc.datamodel.Field
import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Method
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.datamodel.TypeRef
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddMethodCallTest extends BaseSourceTransformSpec {

    @Test()
    void "does nothing"() {
    }

    @Test
    void "test execute with static method"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Type.findFirst("name = ?", "Test16")
        Method caller = Method.findFirst("name = ?", "testXY")
        Type type6 = Type.findFirst("name = ?", "Test9")
        Method callee = Method.builder().name("callee").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        callee.addModifier("static")
        type6.add(callee)
        type6.save()
        callee.save()
        callee.refresh()
        AddMethodCall fixture = new AddMethodCall(file, caller, callee)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        Test9.callee();


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with method from same type"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Type.findFirst("name = ?", "Test16")
        Method caller = Method.findFirst("name = ?", "testXY")
        Type type6 = Type.findFirst("name = ?", "Test9")
        Method callee = Method.findFirst("name = ?", "testXX")
        AddMethodCall fixture = new AddMethodCall(file, caller, callee)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        this.testXX();


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with non static method from other type"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Type.findFirst("name = ?", "Test16")
        Method caller = Method.findFirst("name = ?", "testXY")
        Type type6 = Type.findFirst("name = ?", "Test9")
        Method callee = Method.builder().name("callee").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        type6.add(callee)
        type6.save()
        callee.save()
        callee.refresh()
        AddMethodCall fixture = new AddMethodCall(file, caller, callee)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        Test9 test9 = new Test9();
        test9.callee();


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with method that declares param"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Type.findFirst("name = ?", "Test16")
        Method caller = Method.findFirst("name = ?", "testXY")
        Type type6 = Type.findFirst("name = ?", "Test9")
        Method callee = Method.findFirst("name = ?", "testYY")
        AddMethodCall fixture = new AddMethodCall(file, caller, callee)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        this.testYY(null);


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }
}
