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

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.FileType
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class FileExistsTest extends DBSpec {

    FileExists fixture
    java.io.File file
    java.io.File dir

    @Before
    void setUp() throws Exception {
        fixture = new FileExists(new File("testdir/test.txt", "testdir/test.txt", FileType.SOURCE, "test.txt",  1, 1))
        dir = new java.io.File("testdir")
        dir.mkdirs()
        file = new java.io.File(dir, "test.txt")
    }

    @After
    void tearDown() throws Exception {
        if (file.exists())
            file.delete()
        if (dir.exists())
            dir.deleteDir()
    }

    @Test
    void "test check when file does exist"() {
        // given
        fixture
        file.createNewFile()
        file.write("test")

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when file does not exist"() {
        // given
        fixture

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when file is null"() {
        // given
        fixture = new FileExists(null)

        // when
        fixture.check()
    }
}
