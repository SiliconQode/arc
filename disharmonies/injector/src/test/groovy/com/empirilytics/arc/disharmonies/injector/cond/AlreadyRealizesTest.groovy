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

import com.empirilytics.arc.datamodel.Type
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlreadyRealizesTest extends DBSpec {

    AlreadyRealizes fixture
    Type node
    Type real
    String strReal = "Inter"

    @Before
    void setUp() throws Exception {
        node = Type.builder().type(Type.CLASS).name("Test").compKey("Test").start(1).create()
        real = Type.builder().type(Type.CLASS).name("Inter").compKey("Inter").create()

        fixture = new AlreadyRealizes(node, strReal)

    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test check when type has interface"() {
        // given
        node.realizes(real)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when type does not have interface"() {
        // given
        fixture

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when node is null"() {
        // given
        fixture = new AlreadyRealizes(null, strReal)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when real is null"() {
        // given
        fixture = new AlreadyRealizes(node, null)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when real is empty"() {
        // given
        fixture = new AlreadyRealizes(node, "")

        // when
        fixture.check()
    }
}
