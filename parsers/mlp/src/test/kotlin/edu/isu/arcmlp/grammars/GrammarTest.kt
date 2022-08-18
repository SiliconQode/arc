/*
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
 *
 */

package edu.isu.arcmlp.grammars

import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class GrammarTest : StringSpec() {

    init {
        "Grammar should not allow inconsistent symbol mappings" {
            val productions: Map<NonTerminalSymbol, Production> = mapOf(
                    nonTerminalSymbol("a") to Production(
                            nonTerminalSymbol("b"),
                            Literal("c")
                    )
            )
            val first = productions.keys.single()

            shouldThrow<IllegalArgumentException> {
                Grammar(productions, first)
            }
        }
    }

}