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

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.FileType
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class AddFileTest extends BaseSourceTransformSpec {

    @Test
    void "test file created by execute"() {
        // given
        java.io.File toCreate = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test5.java")
        File test5 = File.builder().name("Test5.java").relPath("Test5.java").type(FileType.SOURCE).create()
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        AddFile fixture = new AddFile(test5, ns)

        // when
        ns.addFile(test5)
        test5.updateKey()
        fixture.execute()

        // then
        the(toCreate.exists()).shouldBeTrue()
    }

    @Test
    void "test file contents are correct when created by execute"() {
        // given
        java.io.File toCreate = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test5.java")
        File test5 = File.builder().name("Test5.java").relPath("Test5.java").type(FileType.SOURCE).create()
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")

        AddFile fixture = new AddFile(test5, ns)

        // when
        ns.addFile(test5)
        test5.updateKey()
        fixture.execute()

        // then
        the(toCreate.text).shouldBeEqual(
                """\
                /**
                 * The MIT License (MIT)
                 *
                 * MSUSEL Software Injector
                 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
                 * Software Engineering Laboratory
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

                package test.test;
                """.stripIndent()
        )
    }
}
