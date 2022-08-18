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
package com.empirilytics.arc.disharmonies.injector.transform.model.namespace

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.FileType
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class DeleteFileModelTransformTest extends NamespaceModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")

        // when
        fixture = new DeleteFileModelTransform(ns, file)
        fixture.execute()

        // then
        the(ns.getFiles()).shouldNotContain(file)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute null file"() {
        // given
        File file = null

        // when
        fixture = new DeleteFileModelTransform(ns, file)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute unknown file"() {
        // given
        File file = File.builder().name("TestX.java").relPath("TestX.java").type(FileType.SOURCE).start(1).end(16).create()

        // when
        fixture = new DeleteFileModelTransform(ns, file)
        fixture.execute()
    }

}
