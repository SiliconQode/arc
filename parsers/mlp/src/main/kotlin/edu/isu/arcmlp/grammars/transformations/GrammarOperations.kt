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

import edu.isu.arcmlp.grammars.*

/**
 * Maps the productions and rules of this grammar.
 * Doesn't allow mapping the start symbol to a non-rule.
 * Called for each term, including rules, in the grammar exactly once.
 */
fun Grammar.map(f: (Rule) -> Rule): Grammar {
    val newRules = nonTerminalSymbols().map { it to f(it) }.toMap()
    val fModified = { rule: Rule ->
        when (rule) {
            is NonTerminalSymbol -> newRules.getOrElse(rule) { f(rule) }
            else -> f(rule)
        }
    }

    val newProductions = mutableMapOf<NonTerminalSymbol, Rule>()

    for ((old, new) in newRules) {
        if (new is NonTerminalSymbol) {
            newProductions[new] = rule(old).map(fModified)
        }
    }

    require(newRules.getValue(startSymbol) is NonTerminalSymbol) { "Can't transform start symbol to something that isn't a terminal" }

    return grammarFromRules(newProductions, newRules.getValue(startSymbol) as NonTerminalSymbol)
}

fun Grammar.map(map: Map<Rule, Rule>) = map { map[it] ?: it }

/**
 * Begins a depth first traversal of the grammar
 * starting with the start symbol. Does not return unused
 * symbols and rules.
 */
fun Grammar.depthFirstTraversalFromStartSymbol(): Sequence<Rule> {
    val scanned = mutableSetOf<Rule>()

    return sequence { depthFirstTraversal(this@depthFirstTraversalFromStartSymbol, startSymbol, scanned) }
}

private suspend fun SequenceScope<Rule>.depthFirstTraversal(
        grammar: Grammar, prod: Rule, scanned: MutableSet<Rule>
) {
    if (!scanned.add(prod)) return

    yield(prod)
    if (prod is NonTerminalSymbol && prod in grammar.productions) {
        depthFirstTraversal(grammar, grammar.rule(prod), scanned)
    }

    if (prod !is RuleOperator) return
    for (child in prod.children()) {
        depthFirstTraversal(grammar, child, scanned)
    }
}

fun Grammar.depthFirstTraversal(): Sequence<Rule> {
    val scanned = mutableSetOf<Rule>()

    return sequence {
        for (rule in nonTerminalSymbols()) {
            depthFirstTraversal(this@depthFirstTraversal, rule, scanned)
        }
    }
}

operator fun Grammar.plus(productions: Map<NonTerminalSymbol, Production>): Grammar {
    return Grammar(this.productions + productions, startSymbol)
}

operator fun Map<NonTerminalSymbol, Production>.plus(grammar: Grammar) = grammar + this