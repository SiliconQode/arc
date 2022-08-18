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

import edu.isu.arcmlp.grammars.*

/**
 * Converts the bnf grammar representation to the general grammar form.
 */
fun BnfGrammar.toGrammar(): Grammar {
    val ruleConversion = rules().map { it to nonTerminalSymbol(it.name) }.toMap()

    val defaultProduction = Union()

    val unknownRules = mutableMapOf<BnfRule, UndefinedNonTerminalSymbol>()

    fun converter(prod: BnfProduction?): Rule {
        return when (prod) {
            is BnfRule -> ruleConversion[prod] ?: unknownRules.getOrPut(prod) { unknownRule(prod.name) }
            is BnfOptions -> Union(prod.options.map(::converter).toSet())
            is BnfList -> Concatenate(prod.items.map(::converter))
            is BnfLiteral -> Literal(prod.value)
            null -> defaultProduction
        }
    }

    val productions = rules().map { rule ->
        Production(ruleConversion.getValue(rule), converter(rule.production))
    }

    return Grammar(productions, ruleConversion[start]
            ?: throw IllegalArgumentException("Start rule missing definition"))
}


/**
 * This represents a BNF grammar.
 * @property productions This is a list of the productions. Each element is a pair of the rule name to a list of options.
 * Each option is a list of literals or rules.
 */
class BnfGrammar(val productions: List<Pair<BnfToken.Rule, List<List<BnfToken>>>>) {
    /**
     * The start symbol of this grammar. All sentences can be created by expanding [start] via productions.
     */
    val start: BnfRule
        get() = get(productions.first().first)

    /**
     * The rules/symbols of this grammar
     */
    val rules: Set<BnfRule>
        get() = rules()

    /**
     * A map of each rule to their id in [productions]
     */
    private val productionIndices: Map<BnfToken.Rule, Int> =
            productions.mapIndexed { i, (rule, _) -> rule to i }.toMap()

    /**
     * Returns a representation of this grammar in bnf form.
     */
    fun repr(): String {
        return productions.joinToString("\n") { (rule, options) ->
            val right = options.joinToString(" | ") { option ->
                option.joinToString(" ") { it.repr() }
            }

            "${rule.repr()} ::= $right"
        }
    }

    /**
     * Returns the rule with the name [rule]
     */
    operator fun get(rule: String) = get(BnfToken.Rule(rule))

    /**
     * A cache of rules already returned. This allows us to process the input lazily.
     */
    private val rulesCache = mutableMapOf<BnfToken.Rule, BnfRule>()

    /**
     * Returns the rule with the name [rule]
     */
    operator fun get(rule: BnfToken.Rule) = rulesCache.getOrPut(rule) { BnfRule(this, rule, productionIndices[rule]) }

    /**
     * Returns a list of all the rule names.
     */
    fun ruleNames() = productions.map { (rule, _) -> rule.name }.toSet()

    /**
     * Returns a list of all rules.
     */
    fun rules() = ruleNames().map { this[it] }.toSet()
}

/**
 * The label of a node in the bnf grammar. Two items with the same label are considered basically equivalent.
 */
sealed class BnfNodeLabel {
    /**
     * The label of a [BnfRule].
     */
    object Rule : BnfNodeLabel() {
        override fun toString() = "rule"
    }

    /**
     * The label of a [BnfOptions]
     */
    object Options : BnfNodeLabel() {
        override fun toString() = "options"
    }

    /**
     * The label of a [BnfList]
     */
    object List : BnfNodeLabel() {
        override fun toString() = "list"
    }

    /**
     * The label of a [BnfLiteral]
     *
     * @param value The value inside the [BnfLiteral]
     */
    data class Literal(val value: String) : BnfNodeLabel() {
        override fun toString() = "'$value'"
    }
}

/**
 * Represents a rule in a grammar.
 *
 * @property grammar The grammar that this rule is part of.
 * @property symbol The representation of this rule in the grammar.
 * @property index The id of the production in the parent grammar.
 * Null if the grammar has no production for this rule.
 */
data class BnfRule(val grammar: BnfGrammar, val symbol: BnfToken.Rule, val index: Int?) : BnfItem() {

    /**
     * The label of this [BnfRule]
     */
    fun label() = BnfNodeLabel.Rule

    /**
     * The name of this rule.
     */
    val name: String = symbol.name

    /**
     * The production of this rule in the grammar.
     */
    val production: BnfProduction? by lazy here@{
        if (index == null) return@here null

        grammar.productions[index].second.map { items ->
            items.map { item ->
                when (item) {
                    is BnfToken.Rule -> grammar[item]
                    is BnfToken.Literal -> BnfLiteral(item.value)
                }
            }.toOption()
        }.toProduction()
    }
}

/**
 * Converts a list of bnf options to a production.
 */
private fun List<BnfOption>.toProduction(): BnfProduction {
    require(isNotEmpty()) { "Options must not be empty." }

    return if (size == 1) this[0] else BnfOptions(this)
}

/**
 * Converts a list of bnf items to an option in a production.
 */
private fun List<BnfItem>.toOption(): BnfOption {
    require(isNotEmpty()) { "List must not be empty." }

    return if (size == 1) this[0] else BnfList(this)
}

/**
 * Represents a production for a rule.
 */
sealed class BnfProduction

/**
 * Represents a production with multiple options. E.g. `<a> | "n" | <e> <b> "c"`
 *
 * @param options The different options available.
 */
data class BnfOptions(val options: List<BnfOption>) : BnfProduction() {

    /**
     * The label for this node.
     */
    fun label() = BnfNodeLabel.Options

    /**
     * Vararg constructor. @see [BnfOptions].
     */
    constructor(vararg options: BnfOption) : this(options.toList())
}

/**
 * Represents an option of a production with multiple options.
 */
sealed class BnfOption : BnfProduction()

/**
 * Represents a list of consecutive elements in a production. E.g. `"(" <expr> ")"`
 *
 * @param items The different items in the production.
 */
data class BnfList(val items: List<BnfItem>) : BnfOption() {

    /**
     * The label of this node.
     */
    fun label() = BnfNodeLabel.List

    /**
     * Vararg constructor. @see [BnfList]
     */
    constructor(vararg items: BnfItem) : this(items.toList())
}

/**
 * Represents a single item in a list in a production. Must be a rule or a literal string.
 */
sealed class BnfItem : BnfOption()

/**
 * Represents a literal.
 *
 * @param value the value of this literal.
 */
data class BnfLiteral(val value: String) : BnfItem() {

    /**
     * A string representing the literal itself.
     */
    fun label() = BnfNodeLabel.Literal(value)
}