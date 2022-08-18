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
import edu.isu.arcmlp.grammars.HasId
import edu.isu.arcmlp.grammars.Rule


/**
 * Returns a new grammar equivalent to [g] but makes sure all elements
 * with ids have different ids than in this.
 */
fun Grammar.ensureDifferentIds(g: Grammar): Grammar {
    val thisIds = depthFirstTraversal().filterIsInstance<HasId<*>>().map { it.id }.toSet()
    var i = 1
    val idGenerator = {
        while (i in thisIds) {
            i++
        }
        i++
    }

    val gsIds = depthFirstTraversal()
            .filterIsInstance<HasId<*>>()
            .map { it as Rule to it.changeId(idGenerator()) as Rule }
            .toMap()

    return g.map(gsIds)
}