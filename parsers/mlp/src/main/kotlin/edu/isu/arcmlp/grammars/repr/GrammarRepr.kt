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

package edu.isu.arcmlp.grammars.repr

import edu.isu.arcmlp.bnf.BNFPrecedence
import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.NonTerminalSymbol
import edu.isu.arcmlp.grammars.Rule

interface GrammarRepr {
    val ruleProductionSeparator: String
    val precedence: Precedence<Rule>
    val extension: String
    val ruleEnd: String
        get() = "\n"

    fun repr(builder: StringBuilder, precedence: Precedence<Rule>, rule: Rule): StringBuilder {
        return rule.defaultRepr(builder, precedence, ::repr)
    }
}


fun GrammarRepr.reprOf(g: Grammar): String {
    val builder = StringBuilder()

    fullRepr(g, g.startSymbol, builder)
    for (rule in g.nonTerminalSymbols()) {
        if (rule != g.startSymbol) {
            builder.append(ruleEnd)
            fullRepr(g, rule, builder)
        }
    }

    return builder.toString()
}


private fun GrammarRepr.fullRepr(grammar: Grammar, rule: NonTerminalSymbol, builder: StringBuilder) {
    repr(builder, BNFPrecedence, rule)
    builder.append(ruleProductionSeparator)
    repr(builder, BNFPrecedence, grammar.rule(rule))
}