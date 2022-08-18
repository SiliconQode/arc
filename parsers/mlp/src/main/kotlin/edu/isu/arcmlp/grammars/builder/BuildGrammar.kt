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

package edu.isu.arcmlp.grammars.builder

import edu.isu.arcmlp.grammars.*

inline fun buildGrammar(builder: GrammarBuilderDsl.() -> Unit): Grammar {
    return GrammarBuilderDsl().apply(builder).build()
}

/**
 * A helper class to help with building grammars
 */
class GrammarBuilderDsl(
        private val defaultNonTerminalSymbolFactory: DefaultNonTerminalSymbol.Factory = DefaultNonTerminalSymbol.DefaultFactory,
        private val undefinedNonTerminalSymbolFactory: UndefinedNonTerminalSymbol.Factory = UndefinedNonTerminalSymbol.DefaultFactory
) {
    private val productions: MutableMap<NonTerminalSymbol, Rule> = mutableMapOf()
    private val rules: MutableMap<String, NonTerminalSymbol> = mutableMapOf()
    private val unknownRules: MutableMap<String, UndefinedNonTerminalSymbol> = mutableMapOf()
    private var startRule: NonTerminalSymbol? = null
    private val productionBuilder = ProductionBuilder()

    /**
     * Builds and returns a grammar created by this builder.
     * Note that modifications to the productions of this builder will be reflected
     * in the created grammar.
     */
    fun build(): Grammar {
        val s = startRule
        checkNotNull(s) { "Must specify at least one rule" }
        return grammarFromRules(productions, s)
    }

    /**
     * Used between a rule and its production to describe an individual production
     */
    infix fun String.produces(rule: ProductionBuilder.() -> Rule): NonTerminalSymbol {
        return r(this) produces rule
    }

    /**
     * Used between a rule and its production to describe an individual production
     */
    infix fun NonTerminalSymbol.produces(rule: ProductionBuilder.() -> Rule): NonTerminalSymbol {
        val p = productionBuilder.rule()
        productions[this] = p
        if (startRule == null) {
            startRule = this
        }
        return this
    }

    /**
     * Returns the rule with the specified name.
     */
    fun r(name: String): NonTerminalSymbol = rules.getOrPut(name) { defaultNonTerminalSymbolFactory(name) }

    /**
     * Returns an unknown rule later that isn't expected to be defined.
     * Returns the same object if the same name is passed.
     */
    fun ur(name: String): UndefinedNonTerminalSymbol = unknownRules.getOrPut(name) { undefinedNonTerminalSymbolFactory(name) }
}

/**
 * Used to build a production in grammars.
 */
class ProductionBuilder {
    /**
     * Converts a string to a production. Only really useful when a function only returns a string
     * for the production of a grammar
     */
    operator fun String.unaryPlus(): Rule = Literal(this)

    infix fun Rule.or(p: Rule) = Union(this, p)
    infix fun String.or(p: Rule) = +this or p
    infix fun Rule.or(p: String) = this or +p
    infix fun String.or(p: String) = +this or +p


    operator fun Rule.minus(p: Rule) = Concatenate(this, p)
    operator fun String.minus(p: Rule) = +this - p
    operator fun Rule.minus(p: String) = this - +p
    operator fun String.minus(p: String) = +this - +p

    val empty = Empty
}