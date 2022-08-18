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

import com.empirilytics.arc.datamodel.FileType
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class RenameNamespaceTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        RenameNamespace fixture = new RenameNamespace(ns, "testdata/testproj/testmod/src/main/java/test/test/", FileType.SOURCE)
        File locOld = new File("testdata/testproj/testmod/src/main/java/test/test/")
        File locNew = new File("testdata/testproj/testmod/src/main/java/test/other/")

        // when
        ns.setName("other")
        ns.updateKey()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }
}