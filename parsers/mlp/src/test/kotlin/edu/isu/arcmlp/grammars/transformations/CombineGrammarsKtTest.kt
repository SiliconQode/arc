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

package edu.isu.arcmlp.grammars.transformations

import edu.isu.arcmlp.bnf.parseBnf
import edu.isu.arcmlp.bnf.toBnfRepr
import edu.isu.arcmlp.bnf.toGrammar
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class CombineGrammarsKtTest : StringSpec() {

    init {
        // Change this test if the new result makes sense.
        "Combining these two grammars should always produce the same result" {
            val g1 = parseBnf("""
                <exp> ::= <exp> "+" <exp> | <exp> "*" <exp> | "(" <exp> ")" | "a" | "b" | "c"
                """.trimIndent()
            ).toGrammar()

            val g2 = parseBnf("""
                <exp> ::= <exp> "+" <exp> | <mulexpr>
                <mulexpr> ::= <mulexpr> "*" <mulexpr> | "(" <exp> ")" | "a" | "b" | "c"
                """.trimIndent()
            ).toGrammar()

            val expectedResult = """
                <exp+exp+mulexpr> ::= <0> | <2> | "a" | "b" | "c"
                <0> ::= <exp+exp+mulexpr> <1> <exp+exp+mulexpr>
                <1> ::= "+" | "*"
                <2> ::= "(" <exp+exp+mulexpr> ")"
            """.trimIndent()

            val combined = combineGrammars(g1, g2).reorderProductions().toBnfRepr()

            combined shouldBe expectedResult
        }
    }

}