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

import edu.isu.arcmlp.grammars.repr.defaultRepr

data class Production(val symbol: NonTerminalSymbol, val rule: Rule)

data class Grammar(val productions: Map<NonTerminalSymbol, Production>, val startSymbol: NonTerminalSymbol) {
    constructor(productions: List<Production>, startSymbol: NonTerminalSymbol) :
            this(productions.map { it.symbol to it }.toMap(), startSymbol)

    init {
        require(productions.all { (symbol, production) -> symbol == production.symbol }) {
            "Dictionary of productions isn't consistent"
        }
    }


    val size get() = productions.size
    private val namesToNonTerminalSymbols by lazy { productions.keys.map { it.name to it }.toMap() }

    fun nonTerminalSymbols() = productions.keys
    fun production(symbol: NonTerminalSymbol) = productions.getValue(symbol)
    fun production(symbolName: String) = productions.getValue(namesToNonTerminalSymbols.getValue(symbolName))
    fun rule(symbol: NonTerminalSymbol) = production(symbol).rule
    fun rule(symbolName: String) = production(symbolName).rule

    override fun toString() = defaultRepr()
    fun nonTerminalSymbolWithName(name: String) = namesToNonTerminalSymbols.getValue(name)


    fun hasProduction(symbol: NonTerminalSymbol): Boolean = symbol in productions
}

fun grammarFromRules(rules: Map<NonTerminalSymbol, Rule>, startSymbol: NonTerminalSymbol): Grammar {
    val productions = rules.mapValues { (symbol, rule) -> Production(symbol, rule) }
    return Grammar(productions, startSymbol)
}

fun Map<NonTerminalSymbol, Rule>.toProductionMap(): Map<NonTerminalSymbol, Production> {
    return mapValues { (symbol, rule) -> Production(symbol, rule) }
}