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
import com.empirilytics.arc.datamodel.Import
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class AddImportTest extends BaseSourceTransformSpec {

    @Test
    void "test execute with other import"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Import imp = Import.builder().name("javax.swing.JFrame").create()
        AddImport fixture = new AddImport(file, imp)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("import javax.swing.JFrame;")
        the(actual.text.indexOf("import javax.swing.JFrame") > actual.text.indexOf("import java.util.*;")).shouldBeTrue()
    }

    @Test
    void "test execute with no other import"() {
        // given
        File file = File.findFirst("name = ?", "Test2.java")
        Import imp = Import.builder().name("javax.swing.JFrame").create()
        AddImport fixture = new AddImport(file, imp)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("import javax.swing.JFrame;")
        the(actual.text.indexOf("import javax.swing.JFrame") > actual.text.indexOf("package test.test;")).shouldBeTrue()
    }

    @Test
    void "test execute with no package and no other import"() {
        // given
        File file = File.findFirst("name = ?", "Test3.java")
        Import imp = Import.builder().name("javax.swing.JFrame").create()
        AddImport fixture = new AddImport(file, imp)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("import javax.swing.JFrame;")
    }

    @Test
    void "test execute with no package and other imports"() {
        // given
        File file = File.findFirst("name = ?", "Test7.java")
        Import imp = Import.builder().name("javax.swing.JFrame").create()
        AddImport fixture = new AddImport(file, imp)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldContain("import javax.swing.JFrame;")
        the(actual.text.indexOf("import javax.swing.JFrame") > actual.text.indexOf("import java.util.*;")).shouldBeTrue()
    }
}
