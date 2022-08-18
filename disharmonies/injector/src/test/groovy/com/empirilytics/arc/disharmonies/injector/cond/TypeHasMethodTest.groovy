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
package com.empirilytics.arc.disharmonies.injector.cond

import com.empirilytics.arc.datamodel.Method
import com.empirilytics.arc.datamodel.Type
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class TypeHasMethodTest extends DBSpec {

    TypeHasMethod fixture
    Method method
    Type type
    String name

    @Before
    void setUp() throws Exception {
        type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()
        method = Method.builder().name("Method").compKey("Test:Method").create()
        name = method.name
        fixture = new TypeHasMethod(type, method)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test check when method has been added"() {
        // given
        type.addMember(method)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when method has not been added"() {
        // given
        fixture

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test
    void "test check with name when method has been added"() {
        // given
        fixture = new TypeHasMethod(type, name)
        type.addMember(method)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check with name when method has not been added"() {
        // given
        fixture = new TypeHasMethod(type, name)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when method is null"() {
        // given
        fixture = new TypeHasMethod(type, null)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when type is null"() {
        // given
        fixture = new TypeHasMethod(null, method)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when name is empty"() {
        // given
        fixture = new TypeHasMethod(type, "")

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when name is null"() {
        // given
        fixture = new TypeHasMethod(type, (String) null)

        // when
        fixture.check()
    }
}
