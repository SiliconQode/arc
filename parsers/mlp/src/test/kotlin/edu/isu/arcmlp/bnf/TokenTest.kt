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

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class TokenTest : StringSpec({
    "rule tokens should be surrounded with <> in representation" {
        BnfToken.Rule("apple").repr() shouldBe "<apple>"
    }
    
    "rule tokens should not include <> in name" {
        BnfToken.Rule.fromToken("<apple>").name shouldBe "apple"
    }
    
    "when parsing rule tokens, they should not be allowed to have missing <>" {
        listOf("noAngleBrackets", "<leadingAngleBracket", "closingAngleBracket>").forEach { string ->
            shouldThrow<IllegalArgumentException> {
                BnfToken.Rule.fromToken(string)
            }
        }
    }
    
    "bnf literals should be quoted in representation" {
        BnfToken.Literal("""apple""").repr() shouldBe """"apple""""
    }
    
    "double quotes in bnf literals should be escaped in representation" {
        BnfToken.Literal("""app"le""").repr() shouldBe """"app\"le""""
    }
    
    "backslashes in bnf literals should be escaped in representation" {
        BnfToken.Literal("""ap\ple""").repr() shouldBe """"ap\\ple""""
    }
    
    "other characters should not be escaped in literals" {
        BnfToken.Literal("\n").repr() shouldBe """"
""""
    }
    
    "when parsing literal tokens, quotes must be there" {
        shouldThrow<IllegalArgumentException> {
            BnfToken.Literal.fromToken("noQuotes")
        }
    }
    
    "when parsing literal tokens, quotes should be removed" {
        BnfToken.Literal.fromToken(""""quoted"""").value shouldBe "quoted"
    }
    
    "when parsing literal tokens, escaped quotes should be unescaped" {
        BnfToken.Literal.fromToken(""""once upon\" a time"""").value shouldBe """once upon" a time"""
    }
    
    "when parsing literal tokens, unescaped quotes should cause an error" {
        shouldThrow<IllegalArgumentException> {
            BnfToken.Literal.fromToken(""""once upon" a time"""")
        }
    }
    
    "when parsing literal tokens, escaped backslashes should be unescaped" {
        BnfToken.Literal.fromToken(""""C:\\\\files\\topSecretStuff"""").value shouldBe """C:\\files\topSecretStuff"""
    }
    
    "when parsing literal tokens, unescaped backslashes should cause an error" {
        shouldThrow<IllegalArgumentException> {
            BnfToken.Literal.fromToken(""""back\slashed"""")
        }
    }
    
    "if stuff other than backslashes or double quotes are escaped, it should cause an error" {
        shouldThrow<IllegalArgumentException> {
            BnfToken.Literal.fromToken(""""\n"""")
        }
    }
})
