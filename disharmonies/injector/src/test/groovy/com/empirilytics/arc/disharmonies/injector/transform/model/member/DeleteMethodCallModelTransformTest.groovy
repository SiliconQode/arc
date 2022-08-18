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
package com.empirilytics.arc.disharmonies.injector.transform.model.member

import com.empirilytics.arc.datamodel.Method
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class DeleteMethodCallModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Method called = Method.findFirst("name = ?", "methodC2")

        // when
        fixture = new DeleteMethodCallModelTransform(method, called)
        fixture.execute()

        // then
        the(method.getMethodsCalled()).shouldNotContain(called)
        the(called.getMethodsCalling()).shouldNotContain(method)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute called method is null"() {
        // given
        Method called = null

        // when
        fixture = new DeleteMethodCallModelTransform(method, called)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is not a method"() {
        // given
        Method called = Method.findFirst("name = ?", "methodC2")

        // when
        fixture = new DeleteMethodCallModelTransform(field, called)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute method is not called by member"() {
        // given
        Method called = Method.findFirst("name = ?", "methodC3")

        // when
        fixture = new DeleteMethodCallModelTransform(method, called)
        fixture.execute()
    }
}
