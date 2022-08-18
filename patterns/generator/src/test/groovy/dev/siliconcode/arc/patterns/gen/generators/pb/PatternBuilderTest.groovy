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

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.gen.generators.java.JavaLanguageProvider
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class PatternBuilderTest extends DBSpec {

    GeneratorContext ctx

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test create"() {
        // given
        Project project = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        Module mod = Module.builder()
                .name("Test")
                .moduleKey("Test:Test")
                .create()
        Namespace parent = Namespace.builder()
                .name("test")
                .nsKey("test")
                .create()
        project.addModule(mod)
        project.addNamespace(parent)
        mod.addNamespace(parent)
        String pattern = "strategy"

        // when
        ctx.patternBuilder.init(parent: parent, pattern: pattern)
        ctx.patternBuilder.create()

        // then
        !project.getPatternInstances().isEmpty()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create no pattern"() {
        // given
        Namespace parent = Namespace.builder()
                .name("test")
                .nsKey("test")
                .create()
        ctx.patternBuilder.init(parent: parent, pattern: null)

        // when
        ctx.patternBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create empty pattern"() {
        // given
        Namespace parent = Namespace.builder()
                .name("test")
                .nsKey("test")
                .create()
        ctx.patternBuilder.init(parent: parent, pattern: "")

        // when
        ctx.patternBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create no parent"() {
        // given
        ctx.patternBuilder.init(parent: null, pattern: "strategy")

        // when
        ctx.patternBuilder.create()
    }
}
