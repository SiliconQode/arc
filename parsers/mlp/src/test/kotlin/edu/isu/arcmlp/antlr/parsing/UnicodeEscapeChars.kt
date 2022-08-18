/**
 * MIT License
 *
 * Copyright (c) 2018-2019, Idaho State University, Empirical Software Engineering
 * Laboratory and Isaac Griffith
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
package edu.isu.arcmlp.antlr.parsing

import edu.isu.arcmlp.antlr.UnicodeCodepointRange
import edu.isu.arcmlp.antlr.parseAntlr
import edu.isu.arcmlp.grammars.transformations.simplify
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class UnicodeEscapeChars : StringSpec({
    "Unicode escape chars should parse" {
        val g = parseAntlr("""
            grammar test;
            a: A;
            A: '\u{10000}' .. '\u{1FFFD}';
        """.trimIndent()).simplify()
        g.productions.size shouldBe 2
        g.rule("A") shouldBe UnicodeCodepointRange(0x10000, 0x1FFFD)
    }
})