/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.patterns.gen.generators.pb

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.FileType
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class FileBuilderTest extends DBSpec {

    Namespace data
    GeneratorContext ctx

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.getInstance()
        ctx.resetPatternBuilderComponents()
        ctx.srcExt = ".java"
        ctx.srcPath = "src/main/java"

        Project project = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        data = Namespace.builder()
                .name("ns")
                .nsKey("ns")
                .create()
        project.addNamespace(data)
        project.setSrcPath("src/main/java")
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test createFile"() {
        // given
        data
        String typeName = "Type"

        // when
        ctx.fileBuilder.init(parent: data, typeName: typeName)
        ctx.fileBuilder.create()
        List<File> files = File.findAll()

        // then
        the(files.size()).shouldEqual(1)
        the(data.files.size()).shouldEqual(1)
        the(files[0].name).shouldEqual("/src/main/java/ns/Type.java")
        the(files[0].fileKey).shouldEqual("Test:/src/main/java/ns/Type.java")
        the(files[0].type).shouldEqual(FileType.SOURCE)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createFile null parent"() {
        // given
        data = null
        String typeName = "Type"

        // when
        ctx.fileBuilder.init(parent: data, typeName: typeName)
        ctx.fileBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createFile null typeName"() {
        // given
        data
        String typeName = null

        // when
        ctx.fileBuilder.init(parent: data, typeName: typeName)
        ctx.fileBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createFile empty typeName"() {
        // given
        data
        String typeName = ""

        // when
        ctx.fileBuilder.init(parent: data, typeName: typeName)
        ctx.fileBuilder.create()
    }
}
