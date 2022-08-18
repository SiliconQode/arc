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
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun Grammar.normalized(): Grammar {
    val transformed = repeatedlyTransform(
            Grammar::eliminateUnusedRules,
            Grammar::simplify,
            Grammar::mergeEquivalentRules,
            Grammar::squish,
            Grammar::expand,
            Grammar::unnest,
            Grammar::removeStarProductions
    )

    if (logger.isInfoEnabled && this != transformed) {
        logger.info { "Normalized\n${this.dedupeNames()}\nto\n${transformed.dedupeNames()}" }
    }

    return transformed
}

/**
 * Expands a grammar so that all productions only have rules for their children.
 */
private fun Grammar.expand(): Grammar {
    val newProductions = mutableMapOf<DefaultNonTerminalSymbol, Production>()

    val expandedProductions = productions.mapValues { (symbol, production) ->
        val expanded = when (val rule = production.rule) {
            is RuleOperand -> rule
            is RuleOperator -> {
                rule.mapChildren { child ->
                    when (child) {
                        is RuleOperand -> child
                        is RuleOperator -> {
                            val r = nonTerminalSymbol()
                            newProductions += r to Production(r, child)
                            r
                        }
                        else -> error("Unexpected rule type")
                    }
                }
            }
            else -> error("Unexpected rule type")
        }

        Production(symbol, expanded)
    }

    return copy(productions = expandedProductions + newProductions)
}

/**
 * Replaces rules that evaluate to exactly one term with their rhs.
 */
private fun Grammar.squish(): Grammar {
    val uselessTerms = nonTerminalSymbols().filter { rule -> rule(rule) is RuleOperand && rule != startSymbol }

    // Immediate replacements
    val replacements = uselessTerms.map { it to rule(it) }.toMap<Rule, Rule>()

    //Extended replacements in case rules need to be squished consecutively.
    val iteratedReplacements = replacements.mapValues { (_, newValue) ->
        var a = newValue
        while (a is NonTerminalSymbol && a in replacements) {
            a = replacements.getValue(a)
        }
        a
    }

    val newStartSymbol = if (rule(startSymbol) is NonTerminalSymbol && rule(startSymbol) !in iteratedReplacements) {
        rule(startSymbol) as NonTerminalSymbol
    } else {
        startSymbol
    }

    return Grammar(
            productions - uselessTerms,
            newStartSymbol
    ).map(iteratedReplacements)
}

/**
 * Simplifies the grammar.
 */
fun Grammar.simplify(): Grammar {
    return map {
        when (it) {
            is Simplifiable -> it.simplify()
            else -> it
        }
    }
}

/**
 * Unnests all nestable productions.
 */
private fun Grammar.unnest(): Grammar {
    return map { prod ->
        when (prod) {
            is Nestable -> unnestProduction(prod)
            else -> prod
        }
    }
}

class HasDirectNestingDependency : Exception()

fun Grammar.unnestProduction(prod: Rule): Rule {
    if (prod !is Nestable) return prod

    val children = prod.children()
    val nestedReferenced = children.map { child ->
        if (child !is NonTerminalSymbol || !hasProduction(child)) {
            child
        } else {
            val rule = rule(child)
            when {
                rule is Nestable && prod.canUnnest(rule) -> {
                    if (rule == prod) {
                        throw HasDirectNestingDependency()
                    }
                    unnestProduction(rule)
                }
                else -> child
            }
        }

    }

    return prod.unNest(nestedReferenced)
}


private fun Grammar.eliminateUnusedRules(): Grammar {
    val scanned = depthFirstTraversalFromStartSymbol().toSet()

    return copy(productions = productions.filterKeys { it in scanned })
}