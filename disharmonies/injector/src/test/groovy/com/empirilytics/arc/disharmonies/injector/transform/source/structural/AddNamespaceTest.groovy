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

class AddNamespaceTest extends BaseSourceTransformSpec {

    @Test
    void "test execute add to namespace"() {
        // given
        File toCreate = new File("testdata/testproj/testmod/src/main/java/test/test2/")
        Namespace parent = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test")
        Namespace ns = Namespace.builder().name("test2").nsKey("test6").relPath("test/test2").create()
        AddNamespace fixture = new AddNamespace(ns, parent)

        // when
        parent.addNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(toCreate.exists()).shouldBeTrue()
    }

    @Test
    void "test execute add to module"() {
        // given
        File toCreate = new File("testdata/testproj/testmod/src/main/java/test2/")
        Module parent = Module.findFirst("name = ?", "testmod")
        Namespace ns = Namespace.builder().name("test2").nsKey("test6").relPath("test2").create()
        AddNamespace fixture = new AddNamespace(ns, parent)

        // when
        parent.addNamespace(ns)
        ns.updateKey()
        fixture.execute()

        // then
        the(toCreate.exists()).shouldBeTrue()
    }
}
