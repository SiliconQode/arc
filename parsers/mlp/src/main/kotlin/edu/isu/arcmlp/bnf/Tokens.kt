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

/**
 * Represents a rule or literal token for bnf
 */
sealed class BnfToken {
    /**
     * Returns the representation of this token as would be given in source code.
     */
    abstract fun repr(): String
    
    /**
     * A token representing a bnf rule. E.g. `<a>`.
     * In that case, the [name] of it would be `a`.
     * @property name The name of the rule. Does not contain angle brackets.
     */
    data class Rule(val name: String) : BnfToken() {
        companion object {
            /**
             * Parses the rule from the given token (e.g. `<a>`).
             */
            fun fromToken(name: String): Rule = Rule(fromBnfRuleRepr(name))
        }
        
        /**
         * How this would be represented in source code. E.g. `<a>`
         */
        override fun repr() = toBnfRuleRepr(name)
    }
    
    /**
     * A token representing a bnf literal. E.g. "a".
     * In that case, the [value] of it would be `a`.
     * @property value The value of this literal.
     * Does not contain quotes and quotes and backslashes inside it are not escaped.
     */
    data class Literal(val value: String) : BnfToken() {
        companion object {
            /**
             * Parses the Literal from the given token. Strips the outside quotes and then unescapes the content.
             */
            fun fromToken(literal: String) = Literal(fromBnfLiteralRepresentation(literal))
        }
        
        /**
         * Returns how this would be represented in source code. All backslashes and double quotes are escaped.
         * Outside quotes are added. E.g. "\\a\""
         */
        override fun repr() = toBnfLiteralRepresentation(value)
    }
}

private fun toBnfLiteralRepresentation(string: String): String {
    val slashesEscaped = string.replace("\\", "\\\\")
    val quotesEscaped = slashesEscaped.replace("\"", "\\\"")
    return "\"$quotesEscaped\""
}

private fun fromBnfLiteralRepresentation(repr: String): String {
    require(repr[0] == '\"') { "$repr must start with \"" }
    require(repr[repr.lastIndex] == '\"') { "$repr must end with \"" }
    
    val quotesStripped = repr.slice(1 until repr.lastIndex)
    
    return unescape(quotesStripped)
}

private fun toBnfRuleRepr(name: String) = "<$name>"

private fun fromBnfRuleRepr(repr: String): String {
    require(repr[0] == '<') { "$repr must start with <" }
    require(repr[repr.lastIndex] == '>') { "$repr must end with >" }
    return repr.slice(1 until repr.lastIndex)
}

private fun unescape(repr: String): String {
    val stringBuilder = StringBuilder()
    
    var i = 0
    while (i < repr.length) {
        when (val c = repr[i]) {
            '\\' -> {
                i++
                require(i < repr.length) { "$repr has an unmatched escape character at the end." }
                when (repr[i]) {
                    '\\' -> stringBuilder.append('\\')
                    '"' -> stringBuilder.append('"')
                    else -> throw IllegalArgumentException("\\${repr[i]} is an invalid escape sequence in $repr.")
                }
            }
            '"' -> throw IllegalArgumentException("$repr contains an illegal \".")
            else -> stringBuilder.append(c)
        }
        i++
    }
    
    return stringBuilder.toString()
}

