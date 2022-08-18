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

import dev.siliconcode.arc.patterns.gen.GeneratorContext
import spock.lang.Specification

class JavaGitIgnoreGeneratorTest extends Specification {

    GeneratorContext ctx
    FileTreeBuilder builder
    File testDir = new File('testdir')

    def setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
    }

    def cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    def "test generate"() {
        given:
        builder

        when:
        ctx.ignoreGen.init(tree: builder)
        ctx.ignoreGen.generate()
        File f = new File(testDir, '.gitignore')

        then:
        f.exists()
    }

    def "test generate with null builder"() {
        given:
        builder = null

        when:
        ctx.ignoreGen.init(tree: builder)
        ctx.ignoreGen.generate()

        then:
        thrown IllegalArgumentException
    }
}
