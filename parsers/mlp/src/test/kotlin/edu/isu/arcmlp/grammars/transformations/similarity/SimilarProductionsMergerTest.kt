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

package edu.isu.arcmlp.grammars.transformations.similarity

import edu.isu.arcmlp.grammars.Concatenate
import edu.isu.arcmlp.grammars.DefaultNonTerminalSymbol
import edu.isu.arcmlp.grammars.Union
import edu.isu.arcmlp.grammars.builder.buildGrammar
import edu.isu.arcmlp.grammars.nonTerminalSymbol
import edu.isu.arcmlp.grammars.transformations.simplify
import io.kotlintest.TestCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SimilarProductionsMergerTest : StringSpec() {
    private lateinit var toTest: SimilarProductionsMerger
    private val mergedRule = nonTerminalSymbol("merged")

    override fun beforeTest(testCase: TestCase) {
        toTest = SimilarProductionsMerger(
                SimilarTermsDetector(),
                SimilarAlternativesDetector()
        ) { mergedRule }
    }

    init {
        "Should merge two most similar alternatives" {
            val start = nonTerminalSymbol("start")
            val a = nonTerminalSymbol("a")
            val b = nonTerminalSymbol("b")
            val g = buildGrammar {
                start produces { a or b }
                a produces { Union(+"a", +"b", +"c", +"d") }
                b produces { Union(+"a", +"b", +"c", +"e") }
            }
            val expected = buildGrammar {
                start produces { mergedRule }
                mergedRule produces { Union(+"a", +"b", +"c", +"d", +"e") }
            }
            val result = toTest.mergeMostSimilarProductions(g).simplify()
            result shouldBe expected
        }

        "Should merge two most similar terms together" {
            val start: DefaultNonTerminalSymbol = nonTerminalSymbol("start")
            val A: DefaultNonTerminalSymbol = nonTerminalSymbol("A")
            val B: DefaultNonTerminalSymbol = nonTerminalSymbol("B")

            val g = buildGrammar {
                start produces { A or B }
                A produces { Concatenate(+"a", +"b", +"c") }
                B produces { Concatenate(+"a", +"b", +"d") }
            }
            val expected = buildGrammar {
                start produces { mergedRule }
                mergedRule produces { Concatenate(+"a", +"b", "c" or "d") }
            }

            val merged = toTest.mergeMostSimilarProductions(g).simplify()
            merged shouldBe expected
        }
    }

}