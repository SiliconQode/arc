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

import com.empirilytics.arc.datamodel.Module
import com.empirilytics.arc.datamodel.Project
import com.empirilytics.arc.patterns.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class ModuleBuilderTest extends DBSpec {

    GeneratorContext ctx
    Project data

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.getInstance()
        ctx.resetPatternBuilderComponents()
        data = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void createModule() {
        // given:
        data
        String name = "Module"
        String pattern = "strategy"

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
        List<Module> mods = Module.findAll()

        // then:
        the(mods.size()).shouldEqual(1)
        the(mods[0].name).shouldEqual(name)
        the("${mods[0].moduleKey}").shouldEqual("${data.projectKey}:${name}")
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create module null project"() {
        // given:
        data = null
        String name = "Module"
        String pattern = "strategy"

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create module null name"() {
        // given:
        data
        String name = null
        String pattern = "strategy"

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create module empty name"() {
        // given:
        data
        String name = ""
        String pattern = "strategy"

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create module null pattern"() {
        // given:
        data
        String name = "Module"
        String pattern = null

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create module emptyh pattern"() {
        // given:
        data
        String name = "Module"
        String pattern = ""

        // when:
        ctx.modBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.modBuilder.create()
    }
}
