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
package com.empirilytics.arc.disharmonies.injector.transform.source.structural

import com.empirilytics.arc.datamodel.Module
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class MoveNamespaceTest extends BaseSourceTransformSpec {

    @Test
    void "test execute ns to mod"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test3")
        Namespace from = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test")
        Module to = Module.findFirst("name = ?", "testmod")
        MoveNamespace fixture = new MoveNamespace(ns, from, to)
        File locOld = new File("testdata/testproj/testmod/src/main/java/test/test3/")
        File locNew = new File("testdata/testproj/testmod/src/main/java/test3/")

        // when
        to.addNamespace(ns)
        from.removeNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }

    @Test
    void "test execute ns to ns"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        Namespace from = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test")
        Namespace to = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test4")
        MoveNamespace fixture = new MoveNamespace(ns, from, to)
        File locOld = new File("testdata/testproj/testmod/src/main/java/test/test/")
        File locNew = new File("testdata/testproj/testmod/src/main/java/test4/test/")

        // when
        to.addNamespace(ns)
        from.removeNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }

    @Test
    void "test execute mod to mod"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test4")
        Module from = Module.findFirst("name = ?", "testmod")
        Module to = Module.findFirst("name = ?", "testmod2")
        MoveNamespace fixture = new MoveNamespace(ns, from, to)
        File locOld = new File("testdata/testproj/testmod/src/main/java/test4/")
        File locNew = new File("testdata/testproj/testmod2/src/main/java/test4/")

        // when
        to.addNamespace(ns)
        from.removeNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }

    @Test
    void "test execute mod to ns"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test4")
        Namespace to = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test")
        Module from = Module.findFirst("name = ?", "testmod")
        MoveNamespace fixture = new MoveNamespace(ns, from, to)
        File locOld = new File("testdata/testproj/testmod/src/main/java/test4/")
        File locNew = new File("testdata/testproj/testmod/src/main/java/test/test4")

        // when
        to.addNamespace(ns)
        from.removeNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }
}
