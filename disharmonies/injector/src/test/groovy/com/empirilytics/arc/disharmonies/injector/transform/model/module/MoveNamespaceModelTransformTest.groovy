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
package com.empirilytics.arc.disharmonies.injector.transform.model.module

import com.empirilytics.arc.datamodel.Module
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class MoveNamespaceModelTransformTest extends ModuleModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test")
        Module other = Module.findFirst("name = ?","testmod2")

        // when
        fixture = new MoveNamespaceModelTransform(mod, ns, other)
        fixture.execute()

        // then
        the(ns.nsKey).shouldEqual("testdata:testproj:1.0:test")
        the(ns.getParentModule()).shouldEqual(other)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute ns is null"() {
        // given
        Namespace ns = null
        Module other = Module.findFirst("name = ?", "testmod2")

        // when
        fixture = new MoveNamespaceModelTransform(mod, ns, other)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute new parent is null"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj-1.0:test")
        Module other = null

        // when
        fixture = new MoveNamespaceModelTransform(mod, ns, other)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute parent is same as mod"() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj-1.0:test")
        Module other = Module.findFirst("name = ?", "testmod")

        // when
        fixture = new MoveNamespaceModelTransform(mod, ns, other)
        fixture.execute()
    }
}
