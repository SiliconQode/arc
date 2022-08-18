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

package edu.isu.arcmlp.antlr

import edu.isu.arcmlp.grammars.Literal
import edu.isu.arcmlp.grammars.Nameable
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.repr.GrammarRepr
import edu.isu.arcmlp.grammars.repr.Precedence
import org.apache.commons.text.StringEscapeUtils

class ANTLRRepr : GrammarRepr {
    override val ruleProductionSeparator: String = ": "
    override val precedence: Precedence<Rule> = ANTLRPrecedence
    override val extension: String = "g4"
    override val ruleEnd = ";\n"

    override fun repr(builder: StringBuilder, precedence: Precedence<Rule>, rule: Rule): StringBuilder {
        return when (rule) {
            is Nameable -> {
                val name = if (rule.name.isNotEmpty() && isValidANTLRStartCharacter(rule.name[0])) {
                    rule.name
                } else {
                    "gen_" + rule.name
                }
                val badPartsRemoved = name.map {
                    if (!isValidANTLRNameChar(it)) '_' else it
                }.joinToString("")
                builder.append(badPartsRemoved)
            }
            is Literal -> {
                val escaped = StringEscapeUtils.escapeJava(rule.literal).replace("'", "\\'")
                builder.append('\'').append(escaped).append('\'')
            }
            else -> rule.defaultRepr(builder, precedence, ::repr)
        }
    }

}

private fun isValidANTLRNameChar(c: Char): Boolean {
    if (isValidANTLRStartCharacter(c)) return true
    return when (c) {
        in '0'..'9',
        '_',
        '\u00B7',
        in '\u0300'..'\u036F',
        in '\u203F'..'\u2040' -> true
        else -> false
    }
}


private fun isValidANTLRStartCharacter(c: Char): Boolean {
    return when (c) {
        in 'A'..'Z',
        in 'a'..'z',
        in '\u00C0'..'\u00D6',
        in '\u00D8'..'\u00F6',
        in '\u00F8'..'\u02FF',
        in '\u0370'..'\u037D',
        in '\u037F'..'\u1FFF',
        in '\u200C'..'\u200D',
        in '\u2070'..'\u218F',
        in '\u2C00'..'\u2FEF',
        in '\u3001'..'\uD7FF',
        in '\uF900'..'\uFDCF',
        in '\uFDF0'..'\uFFFD' -> true
        else -> false
    }
}