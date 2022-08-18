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

import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.Nameable
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.utils.partition

fun Grammar.dedupeNames(): Grammar {
    val namedItems = depthFirstTraversalFromStartSymbol().filterIsInstance<Nameable>()
    val (duplicates, nonDuplicates) = namedItems.groupBy { it.name }.partition { key, value -> value.size > 1 || key == "" }

    val names = nonDuplicates.keys.toMutableSet()
    val changedNames = mutableMapOf<Rule, Rule>()

    for ((name, duplicateItems) in duplicates) {
        var currentPostfix = 0

        for (item in duplicateItems) {
            while (name(name, currentPostfix) in names) {
                currentPostfix++
            }
            val newName = name(name, currentPostfix)

            changedNames[item] = item.rename(newName)
            names += newName
        }
    }

    return map(changedNames)
}

private fun name(name: String, currentPostfix: Int) = if (name == "") "$currentPostfix" else "$name-$currentPostfix"