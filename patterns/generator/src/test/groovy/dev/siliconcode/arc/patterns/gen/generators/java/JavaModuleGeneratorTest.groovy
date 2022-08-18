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
package dev.siliconcode.arc.patterns.gen.generators.java

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class JavaModuleGeneratorTest extends DBSpec {

    GeneratorContext ctx
    FileTreeBuilder builder
    final File testDir = new File('testdir')
    Module data
    Project proj

    @Before
    void setup() {
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        data = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()

        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        ctx.license = [
                "name": "MIT",
                "year": "2020",
                "holder": "Developer",
                "project": "TestProj",
                "url": "https://testproj.com"
        ]
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "module as subproject"() {
        Project p = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        p.addModule(data)
        ctx.modGen.init(project: p, mod: data, builder: builder, subproject: true, num: 10, pattern: "test")
        ctx.modGen.generate()

        File settings = new File(testDir, "settings.gradle")

        a(testDir.exists()).shouldBeTrue()
        a(settings.exists()).shouldBeFalse()
    }

    @Test
    void "module as not a subproject"() {
        Project p = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        ctx.modGen.init(project: p, mod: data, builder: builder, subproject: false, num: 10, pattern: "test")
        ctx.modGen.generate()

        File settings = new File(testDir, "settings.gradle")

        a(testDir.exists()).shouldBeTrue()
        a(settings.exists()).shouldBeTrue()
        a(settings.isDirectory()).shouldBeFalse()
    }
}
