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
package edu.isu.arcmlp.bnf

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ParserTest : StringSpec({
    "hello world" {
        val grammar = parseBnf(
            """
            <start> ::= "hello world"
        """
        )
        
        grammar.productions shouldContain (BnfToken.Rule("start") to listOf(listOf(BnfToken.Literal("hello world"))))
    }
    
    "bf" {
        val grammar = parseBnf(
            """
            <bf> ::= <chars>
            <chars> ::= "" | <char> <chars>
            <char> ::= ">" | "<" | "+" | "-" | "." | "," | "[" | "]"
        """
        )
        
        grammar.productions shouldContainExactly listOf(BnfToken.Rule("bf") to listOf(listOf(BnfToken.Rule("chars"))),
            BnfToken.Rule("chars") to listOf(
                listOf(BnfToken.Literal("")), listOf(BnfToken.Rule("char"), BnfToken.Rule("chars"))
            ),
            BnfToken.Rule("char") to "><+-.,[]".map { listOf(BnfToken.Literal(it.toString())) })
        
        grammar["bf"].production shouldBe grammar["chars"]
        
        grammar["chars"].production shouldBe BnfOptions(
            BnfLiteral(""), BnfList(grammar["char"], grammar["chars"])
        )
        
        grammar["char"].production shouldBe BnfOptions("><+-.,[]".map { BnfLiteral(it.toString()) })
        
        grammar.ruleNames() shouldBe setOf("bf", "chars", "char")
        grammar.rules() shouldBe setOf(grammar["bf"], grammar["chars"], grammar["char"])
    }
    
    "start rule should be first production" {
        val grammar = parseBnf(
            """
            <bf> ::= <chars>
            <chars> ::= "" | <char> <chars>
            <char> ::= ">" | "<" | "+" | "-" | "." | "," | "[" | "]"
        """
        )
        grammar.start.name shouldBe "bf"
    }
})