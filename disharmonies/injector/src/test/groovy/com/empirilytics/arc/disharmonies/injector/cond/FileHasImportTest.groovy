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
import com.empirilytics.arc.datamodel.Import
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class FileHasImportTest extends DBSpec {

    FileHasImport fixture
    File file
    String imp

    @Before
    void setUp() throws Exception {
        file = File.builder().name("testdir/file.txt").fileKey("testdir/file.txt").create()
        imp = "java.util.*"
        fixture = new FileHasImport(file, imp)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test check when file has import"() {
        // given
        Import i = Import.builder().name(imp).create()
        file.addImport(i)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when file does not have import"() {
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
        file = null
        fixture = new FileHasImport(file, imp)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when import is null"() {
        // given
        imp = null
        fixture = new FileHasImport(file, imp)

        // when
        fixture.check()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when import is empty"() {
        // given
        imp = ""
        fixture = new FileHasImport(file, imp)

        // when
        fixture.check()
    }
}
