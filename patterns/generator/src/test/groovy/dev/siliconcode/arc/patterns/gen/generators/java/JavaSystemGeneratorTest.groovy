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

import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class JavaSystemGeneratorTest extends DBSpec {

    System data
    GeneratorContext ctx
    FileTreeBuilder builder
    final File testDir = new File('testdir')

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        data = System.builder()
                .name("Test")
                .key("Test")
                .create()
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "generate empty system"() {
        ctx.sysGen.init(sys: data, builder: builder)
        ctx.sysGen.generate()

        File sysdir = new File(testDir, "Test")

        the(sysdir.exists()).shouldBeTrue()
        the(sysdir.isDirectory()).shouldBeTrue()
    }

    @Test
    void "generate system with one project"() {
        Project proj1 = Project.builder()
                .name("Test-1")
                .version("1.0")
                .projKey("Test")
                .create()
        data.addProject(proj1)
        data.updateKeys()

        ctx.sysGen.init(sys: data, builder: builder)
        ctx.sysGen.generate()

        File sysdir = new File(testDir, "Test")
        File proj1dir = new File(sysdir, "Test-1")

        the(sysdir.exists()).shouldBeTrue()
        the(sysdir.isDirectory()).shouldBeTrue()
        the(proj1dir.exists()).shouldBeTrue()
        the(proj1dir.isDirectory()).shouldBeTrue()
    }

    @Test
    void "generate system with multiple projects"() {
        Project proj1 = Project.builder()
                .name("Test-1")
                .version("1.0")
                .projKey("Test")
                .create()
        Project proj2 = Project.builder()
                .name("Test-2")
                .version("1.0")
                .projKey("Test2")
                .create()
        data.addProject(proj1)
        data.addProject(proj2)

        ctx.sysGen.init(sys: data, builder: builder)
        ctx.sysGen.generate()

        File sysdir = new File(testDir, "Test")
        File proj1dir = new File(sysdir, "Test-1")
        File proj2dir = new File(sysdir, "Test-2")

        the(sysdir.exists()).shouldBeTrue()
        the(sysdir.isDirectory()).shouldBeTrue()
        the(proj1dir.exists()).shouldBeTrue()
        the(proj1dir.isDirectory()).shouldBeTrue()
        the(proj2dir.exists()).shouldBeTrue()
        the(proj2dir.isDirectory()).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "generate null system"() {
        data = null

        ctx.sysGen.init(sys: data, builder: builder)
        ctx.sysGen.generate()
    }

    @Test(expected = IllegalArgumentException.class)
    void "generate system with null builder"() {
        ctx.sysGen.init(sys: data, builder: null)
        ctx.sysGen.generate()
    }
}
