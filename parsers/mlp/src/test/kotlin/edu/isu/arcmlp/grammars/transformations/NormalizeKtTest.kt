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

import edu.isu.arcmlp.antlr.Star
import edu.isu.arcmlp.grammars.*
import edu.isu.arcmlp.grammars.builder.buildGrammar
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.types.shouldNotBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class NormalizeKtTest : StringSpec() {

    init {
        "Terms inside a production should be moved to a new rule" {
            val g = buildGrammar {
                "start" produces { "a" or "b" or "c" - "d" }
            }
            val simplified = g.normalized()
            simplified.productions.size shouldBe 2

            val generatedRule = simplified.nonTerminalSymbolWithName("")

            val firstProduction = (simplified.rule("start") as Union).alts
            firstProduction shouldContainExactlyInAnyOrder listOf("a".toLiteral(), "b".toLiteral(), generatedRule)

            val secondProduction = (simplified.rule(generatedRule) as Concatenate).terms
            secondProduction shouldContainExactlyInAnyOrder listOf("c".toLiteral(), "d".toLiteral())
        }

        "Stuff that is nested should be un-nested" {
            val g = buildGrammar {
                "start" produces { "a" or r("nested") }
                "nested" produces { "b" or "c" }
            }
            val simplified = g.normalized()
            simplified.productions.size shouldBe 1

            val production = (simplified.rule("start") as Union).alts
            production shouldContainExactlyInAnyOrder listOf("a", "b", "c").map { it.toLiteral() }
        }

        "Complicated nesting in alternatives should be uncomplicated" {
            val g = buildGrammar {
                "start" produces {
                    ("a" or "a" or empty) or
                            (empty or "b" or "a") or
                            ("c" - empty) or
                            Union(Union(Union(+"d"))) or
                            Concatenate(Concatenate(Concatenate(+"e"), empty))
                }
            }
            val simplified = g.normalized()
            simplified.productions.size shouldBe 1

            val start = simplified.nonTerminalSymbolWithName("start")
            val production = (simplified.rule(start) as Union).alts
            production shouldContainExactlyInAnyOrder listOf(
                    "a".toLiteral(),
                    "b".toLiteral(),
                    "c".toLiteral(),
                    "d".toLiteral(),
                    "e".toLiteral(),
                    Empty
            )
        }

        "Should not be able to unnest items when including itself" {
            val g = buildGrammar {
                "start" produces { "a" or r("start") }
            }
            shouldThrow<HasDirectNestingDependency> { g.normalized() }
        }

        "Should remove useless rules" {
            val g = buildGrammar {
                "start" produces { r("useless") }
                "useless" produces { +"a" }
            }
            val simplified = g.normalized()
            simplified.productions.size shouldBe 1
            simplified.rule("start") shouldBe "a".toLiteral()
        }

        "Should combine equivalent stuff" {
            val g = buildGrammar {
                "start" produces { r("same1") or r("same2") }
                "same1" produces { "a" - r("x") - "c" }
                "same2" produces { "a" - r("y") - "c" }
                "x" produces { +"b" }
                "y" produces { +"b" }
            }
            val simplified = g.normalized()
            simplified.productions.size shouldBe 1
            val production = (simplified.rule("same1+same2") as Concatenate).terms
            production shouldContainExactly listOf("a", "b", "c").map { it.toLiteral() }
        }



        "Simplify should simplify union with one element in it" {
            val g = buildGrammar {
                "start" produces { Union(Literal("test")) }
            }

            val simplified = g.simplify()

            simplified.rule("start") shouldBe Literal("test")
        }

        "Simplify should simplify concatenate nested within union" {
            val g = buildGrammar {
                "start" produces { Union(Concatenate(Literal("test"))) }
            }

            val simplified = g.simplify()

            simplified.rule("start") shouldBe Literal("test")
        }

        // Added because there was a bug
        "Simplify should simplify union nested within concatenate nested within union" {
            val g = buildGrammar {
                "start" produces { Concatenate(Union(Concatenate(Literal("test")))) }
            }

            val simplified = g.simplify()

            simplified.rule("start") shouldBe Literal("test")
        }


        "Unnest productions should ignore nested non-defined rules" {
            val unknown = unknownRule("unknown")
            val g = buildGrammar {
                "start" produces { unknown or empty }
            }

            val unnested = g.unnestProduction(g.rule("start"))

            unnested shouldBe g.rule("start")
        }

        "Star productions should be removed" {
            val g = buildGrammar {
                "start" produces { Star(Literal("a")) }
            }
            val unStarred = g.normalized()

            unStarred.depthFirstTraversal().forEach { it.shouldNotBeInstanceOf<Star>() }
        }
    }
}