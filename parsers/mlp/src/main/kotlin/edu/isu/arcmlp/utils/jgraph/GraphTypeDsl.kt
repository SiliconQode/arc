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

import org.jgrapht.Graph
import org.jgrapht.GraphType
import org.jgrapht.graph.builder.GraphTypeBuilder

sealed class GraphEdgeDirectionType

object Directed : GraphEdgeDirectionType()
object Undirected : GraphEdgeDirectionType()

inline fun <V, E> createGraph(
    proto: Graph<V, E>, crossinline build: GraphTypeBuilder<V, E>.() -> Unit = {}
): Graph<V, E> {
    val builder = GraphTypeBuilder.forGraph<V, E>(proto)
    builder.build()
    return builder.buildGraph()
}

inline fun <V, E> createGraph(type: GraphType, crossinline build: GraphTypeBuilder<V, E>.() -> Unit = {}): Graph<V, E> {
    val builder = GraphTypeBuilder.forGraphType<V, E>(type)
    builder.build()
    return builder.buildGraph()
}

inline fun <V, E> createGraph(
    type: GraphEdgeDirectionType, crossinline build: GraphTypeBuilder<V, E>.() -> Unit = {}
): Graph<V, E> {
    val builder: GraphTypeBuilder<V, E> = when (type) {
        is Directed -> GraphTypeBuilder.directed()
        is Undirected -> GraphTypeBuilder.undirected()
    }
    builder.build()
    return builder.buildGraph()
}

fun <V> GraphTypeBuilder<V, Int>.autoIncrementEdges() {
    var id = 0
    edgeSupplier { id++ }
}

inline fun <V, reified E> GraphTypeBuilder<V, E>.autoEdges() {
    edgeClass(E::class.java)
}

inline fun <reified V, E> GraphTypeBuilder<V, E>.autoVertices() {
    vertexClass(V::class.java)
}