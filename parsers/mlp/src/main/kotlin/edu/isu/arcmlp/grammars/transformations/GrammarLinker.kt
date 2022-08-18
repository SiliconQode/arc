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
 * Links the rules together so that all rules with the same name are the same.
 * Undefined rules are replaced with unknown rules. An error is throw if a rule
 * is defined multiple times.
 */
fun Grammar.link(): Grammar {
    val definedRules = productions.keys
    val namesToRules = definedRules.groupBy { it.name }
    val duplicateDefinitions = namesToRules.values.filter { it.size > 1 }
    if (duplicateDefinitions.isNotEmpty()) {
        throw IllegalArgumentException("Grammar has multiple definitions for rules ${duplicateDefinitions.joinToString(" ") { it[0].name }}.")
    }
    val nameToRule = namesToRules.mapValues { it.value.single() }
    val allNonTerminalSymbols = depthFirstTraversal().filterIsInstance<NonTerminalSymbol>().toList()
    val rulesToMap: Map<Rule, Rule> = allNonTerminalSymbols
            .filter { it.name in nameToRule }
            .map { it to nameToRule.getValue(it.name) }
            .toMap()

    val unknownRules = allNonTerminalSymbols.filter { it.name !in nameToRule }.groupBy { it.name }
    val unknownRulesToMap: Map<Rule, Rule> = unknownRules
            .values
            .flatMap { rules ->
                val unknown = unknownRule(rules.first().name)
                rules.map { it to unknown }
            }
            .toMap()

    return map(rulesToMap + unknownRulesToMap)
}