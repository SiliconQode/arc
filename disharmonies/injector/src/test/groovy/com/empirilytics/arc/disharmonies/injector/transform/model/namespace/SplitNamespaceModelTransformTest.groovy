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
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class SplitNamespaceModelTransformTest extends NamespaceModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        List<File> left
        List<File> right
        (left, right) = splitNamespaceFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()

        // then
        the(ns.getFiles().isEmpty()).shouldBeFalse()
    }

    @Test
    void "test execute happy path module parent"() {
        // given
        List<File> left
        List<File> right
        ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test4")
        (left, right) = splitNamespaceFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()

        // then
        the(ns.getFiles().isEmpty()).shouldBeFalse()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute ns is null"() {
        // given
        Namespace ns = null
        List<File> left = []
        List<File> right = []
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute left is null"() {
        // given
        List<File> left = null
        List<File> right = ns.getFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute left is empty"() {
        // given
        List<File> left = []
        List<File> right = ns.getFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute right is null"() {
        // given
        List<File> left = ns.getFiles()
        List<File> right = null
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute right is empty"() {
        // given
        List<File> left = ns.getFiles()
        List<File> right = []
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute no parent"() {
        // given
        List<File> left
        List<File> right
        (left, right) = splitNamespaceFiles()
        ns = Namespace.builder().name("test").nsKey("test7").create()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    def splitNamespaceFiles() {
        List<File> left = []
        List<File> right = []

        List<File> files = ns.getFiles()
        left = files[0..(files.size()/2)]
        right = files[(files.size()/2 + 1)..(files.size() - 1)]
        [left,right]
    }
}
