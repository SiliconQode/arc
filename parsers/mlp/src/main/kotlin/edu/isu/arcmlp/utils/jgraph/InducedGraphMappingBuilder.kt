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

package edu.isu.arcmlp.utils.jgraph

import edu.isu.arcmlp.utils.toReversedMap
import org.jgrapht.Graph
import org.jgrapht.GraphMapping
import org.jgrapht.graph.DefaultGraphMapping

/**
 * DSL function for creating a graph mapping. Use the [InducedGraphBuilder.correspondsTo] function
 * to convey that a vertex maps to another.
 */
inline fun <V, E> inducedGraphMapping(
    lhs: Graph<V, E>, rhs: Graph<V, E>, builder: InducedGraphBuilder<V, E>.() -> Unit
): GraphMapping<V, E> {
    return InducedGraphBuilder(lhs, rhs).apply(builder).build()
}

class InducedGraphBuilder<V, E>(private val lhs: Graph<V, E>, private val rhs: Graph<V, E>) {
    private val mappings = mutableListOf<Pair<V, V>>()
    
    infix fun V.correspondsTo(vertex: V) {
        mappings += if (this in lhs.vertexSet() && vertex in rhs.vertexSet()) {
            this to vertex
        } else {
            error("Vertices aren't in opposite graphs.")
        }
    }
    
    fun build(): GraphMapping<V, E> {
        return DefaultGraphMapping(
            mappings.toMap(), mappings.toReversedMap(), lhs, rhs
        )
    }
}