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

import edu.isu.arcmlp.utils.toSet
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultGraphMapping
import org.jgrapht.graph.builder.GraphBuilder

internal fun <V, E> Graph<V, E>.isEdgeOf(edge: E, vertex: V) =
    getEdgeSource(edge) == vertex || getEdgeTarget(edge) == vertex

inline fun <V, E> Graph<V, E>.builder(crossinline builder: GraphBuilder<V, E, Graph<V, E>>.() -> Unit) {
    GraphBuilder(this).builder()
}

fun <V, E> emptyUndirectedGraph(): Graph<V, E> = createGraph(Undirected)

infix fun <V, E> Graph<V, E>.emptyMappingTo(rhs: Graph<V, E>) = DefaultGraphMapping(emptyMap(), emptyMap(), this, rhs)

fun <V, E> Graph<V, E>.oppositeVertex(vertex: V, edge: E): V {
    val target = getEdgeTarget(edge)
    val source = getEdgeSource(edge)
    return when (vertex) {
        target -> source
        source -> target
        else -> error("Edge $edge isn't touching vertex $vertex. Source: $source. Target: $target.")
    }
}

/**
 * Returns the edges in [this] whose source is [vertex]. This is different from
 * [Graph.outgoingEdgesOf] because that may return edges with a target of [vertex] instead
 * for undirected graphs.
 */
fun <V, E> Graph<V, E>.edgesOriginatingFrom(vertex: V): List<E> {
    return outgoingEdgesOf(vertex).filter { getEdgeSource(it) == vertex }
}

fun <V, E> Graph<V, E>.neighbors(v: V): Set<V> {
    return outgoingEdgesOf(v).map { edge -> oppositeVertex(v, edge) }.toSet()
}

fun <V, E> Graph<V, E>.parents(v: V): Set<V> {
    return incomingEdgesOf(v).map { edge -> oppositeVertex(v, edge) }.toSet()
}

/**
 * Returns a pair of the two vertices that are at each endpoint of [e].
 * The order of the pair is the source of the edge to the target. If the graph is undirected,
 * then this order is meaningless but is the same as the order returned by [Graph.getEdgeSource]
 * and [Graph.getEdgeTarget].
 */
fun <V, E> Graph<V, E>.verticesOf(e: E): Pair<V, V> {
    return getEdgeSource(e) to getEdgeTarget(e)
}

infix fun <V, E> Graph<V, E>.equivalentTo(other: Graph<V, E>): Boolean {
    return vertexSet() == other.vertexSet() && edgeSet() == other.edgeSet() && edgeSet().all { edge ->
        if (type.isDirected) {
            verticesOf(edge) == other.verticesOf(edge)
        } else {
            verticesOf(edge).toSet() == other.verticesOf(edge).toSet()
        }
    }
}

fun <V, E> Graph<V, E>.vertices(vararg vertices: V): Graph<V, E> = vertices(vertices.asIterable())

fun <V, E> Graph<V, E>.vertices(vertices: Iterable<V>): Graph<V, E> {
    vertices.forEach { addVertex(it) }
    return this
}

fun <V, E> Graph<V, E>.edges(vararg edges: Pair<V, V>): Graph<V, E> = edges(edges.asIterable())
fun <V, E> Graph<V, E>.edges(edges: Iterable<Pair<V, V>>): Graph<V, E> {
    edges.forEach { (v1, v2) -> addEdge(v1, v2) }
    return this
}

fun <V, E> Graph<V, E>.edges(vararg edges: Triple<V, V, E>): Graph<V, E> {
    edges.forEach { (v1, v2, e) -> addEdge(v1, v2, e) }
    return this
}