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
import org.jgrapht.GraphMapping

class EquivalentVertexGraphMapping<V, E>(val left: Graph<V, E>, val right: Graph<V, E>) : GraphMapping<V, E> {
    override fun getEdgeCorrespondence(edge: E, forward: Boolean): E? {
        val source = getSource(forward)
        val target = getTarget(forward)
    
        if (edge !in target.edgeSet()) return null
    
        val targetVertexSource = getVertexCorrespondence(source.getEdgeSource(edge), forward) ?: return null
        val targetVertexTarget = getVertexCorrespondence(source.getEdgeTarget(edge), forward) ?: return null
        val actualVertexSource = target.getEdgeSource(edge)
        val actualVertexTarget = target.getEdgeTarget(edge)
    
        if (targetVertexSource == actualVertexSource && targetVertexTarget == actualVertexTarget) {
            return edge
        }
    
        if (left.type.isUndirected || right.type.isUndirected) {
            if (targetVertexSource == actualVertexTarget && targetVertexTarget == actualVertexSource) {
                return edge
            }
        }
    
        return null
    }
    
    private fun getTarget(forward: Boolean): Graph<V, E> = if (forward) right else left
    private fun getSource(forward: Boolean): Graph<V, E> = getTarget(!forward)
    
    override fun getVertexCorrespondence(vertex: V, forward: Boolean): V? {
        val opposite = getTarget(forward)
        return if (vertex in opposite.vertexSet()) vertex else null
    }
}

fun <V, E> mapSharedVerticesAndEdges(left: Graph<V, E>, right: Graph<V, E>) = EquivalentVertexGraphMapping(left, right)