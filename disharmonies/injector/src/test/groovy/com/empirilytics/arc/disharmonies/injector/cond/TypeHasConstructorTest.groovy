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

import com.empirilytics.arc.datamodel.Constructor
import com.empirilytics.arc.datamodel.Type
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class TypeHasConstructorTest extends DBSpec {

    TypeHasConstructor fixture
    Constructor cons
    Type type

    @Before
    void setUp() throws Exception {
        type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()
        cons = Constructor.creator().name("Test").compKey("Test:Test").create()
        fixture = new TypeHasConstructor(type, cons)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test check when constructor has been added"() {
        // given
        type.addMember(cons)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when constructor has not been added"() {
        // given
        fixture

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when method is null"() {
        // given
        fixture = new TypeHasConstructor(type, null)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when type is null"() {
        // given
        fixture = new TypeHasConstructor(null, cons)

        // when
        fixture.check()
    }
}
