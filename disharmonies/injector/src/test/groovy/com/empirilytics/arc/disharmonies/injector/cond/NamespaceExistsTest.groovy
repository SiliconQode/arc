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

import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.datamodel.Project
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class NamespaceExistsTest extends DBSpec {

    NamespaceExists fixture
    Namespace ns
    Project proj

    @Before
    void setUp() throws Exception {
        proj = Project.builder().name("Test").projKey("Test").relPath("test").create()
        Namespace par = Namespace.builder().name("parent").nsKey("parent").create()
        ns = Namespace.builder().name("ns").nsKey("ns").create()
        par.addNamespace(ns)
        proj.addNamespace(par)
        proj.addNamespace(ns)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void check() {
        // given
        fixture = new NamespaceExists(proj, "parent.ns")

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "check a nonexistant namespace"() {
        // given
        fixture = new NamespaceExists(proj, "parent.ns.test")

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test
    void "check a existant namespace"() {
        // given
        fixture = new NamespaceExists(proj, "parent")

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when name is null"() {
        // given
        String name = null
        fixture = new NamespaceExists(proj, name)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when name is empty"() {
        // given
        String name = ""
        fixture = new NamespaceExists(proj, name)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when context is null"() {
        // given
        proj = null
        String name = "parent.ns"
        fixture = new NamespaceExists(proj, name)

        // when
        fixture.check()
    }

}
