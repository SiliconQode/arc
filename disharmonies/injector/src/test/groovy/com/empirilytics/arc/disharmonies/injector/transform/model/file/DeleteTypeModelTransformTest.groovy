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
package com.empirilytics.arc.disharmonies.injector.transform.model.file

import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class DeleteTypeModelTransformTest extends FileModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Type type = Type.findFirst("name = ?", "Test1")

        // when
        fixture = new DeleteTypeModelTransform(file, type)
        fixture.execute()

        // then
        the(file.getAllTypes()).shouldNotContain(type)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type is null"() {
        // given
        Type type = null

        // when
        fixture = new DeleteTypeModelTransform(file, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute file does not contain type"() {
        // given
        Type type = Type.findFirst("name = ?", "Test2")

        // when
        fixture = new DeleteTypeModelTransform(file, type)
        fixture.execute()
    }
}
