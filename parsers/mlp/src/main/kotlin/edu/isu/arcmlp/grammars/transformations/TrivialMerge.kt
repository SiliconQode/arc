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

infix fun Grammar.trivialMerge(g2: Grammar): Grammar {
    val newStart = nonTerminalSymbol()
    val production = newStart to Production(newStart, Union(startSymbol, g2.startSymbol))
    val newProductions = mapOf(production) + productions + g2.productions
    return Grammar(newProductions, newStart)
}

fun trivialMerge(grammars: List<Grammar>): Grammar {
    val newStart = nonTerminalSymbol() as NonTerminalSymbol
    val production = newStart to Production(newStart, Union(grammars.map { it.startSymbol }))
    val newProductions = mutableMapOf(production)
    grammars.forEach { newProductions += it.productions }
    return Grammar(newProductions, newStart)
}