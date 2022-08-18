/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
 */
package dev.siliconcode.arc.quality.metrics.cfgredux

import com.google.common.graph.MutableGraph
import com.google.common.graph.GraphBuilder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CfgReducer {

    /**
     *
     */
    CfgReducer() {
        // TODO Auto-generated constructor stub
    }

    static void main(String[] args) {
        MutableGraph<Integer> graph

        println "A reducible example"
        graph = GraphBuilder.directed().build()
        1..8.each {
            graph.addNode(it)
        }

        graph.putEdge(1,2)
        graph.putEdge(2,3)
        graph.putEdge(2,4)
        graph.putEdge(2,7)
        graph.putEdge(3,5)
        graph.putEdge(4,5)
        graph.putEdge(7,8)
        graph.putEdge(8,5)
        graph.putEdge(5,6)

        new CfgReducer().reduce(graph)
        assert graph.nodes().size() == 1
        println "All Good!"
        println "Complexity ${complexity(graph)}"

        println "\nA irreducible example"
        graph = GraphBuilder.directed().build()
        1..6.each {
            graph.addNode(it)
        }

        graph.putEdge(1,2)
        graph.putEdge(2,3)
        graph.putEdge(3,4)
        graph.putEdge(4,5)
        graph.putEdge(5,6)
        graph.putEdge(3,2)
        graph.putEdge(4,1)
        graph.putEdge(2,6)

        new CfgReducer().reduce(graph)
        assert graph.nodes().size() == 6
        println "All Good!"
        println "Complexity ${complexity(graph)}"
    }

    static int complexity(graph) {
        int nodes = graph.nodes().size()
        int edges = graph.edges().size()

        edges - nodes + 2
    }

    def reduce(graph) {
        while (true) {
            int updates = 0

            def paths = findSimplePaths(graph)
            paths.each {
                updates += reduceSimplePath(it, graph)
            }

            def sgs = findSiblingGroups(graph)
            sgs.each {
                updates += reduceSimpleDecision(it, graph)
            }

            if (updates == 0)
                break
        }
    }

    def findSimplePaths(graph) {
        def pathMembers = []
        def sinks = []
        def sources = []
        def paths = []

        pathMembers = graph.nodes().findAll {
            graph.inDegree(it) <= 1 && graph.outDegree(it) <= 1
        }

        pathMembers.each { src ->
            if (graph.successors(src).isEmpty())
                sinks << src
            else {
                graph.successors(src).each { succ ->
                    if (graph.inDegree(succ) > 1 || graph.outDegree(succ) > 1)
                        sinks << src
                }
            }
        }

        pathMembers.each { dest ->
            if (graph.predecessors(dest).isEmpty())
                sources << dest
            else {
                graph.predecessors(dest).each { pred ->
                    if (graph.inDegree(pred) > 1 || graph.outDegree(pred) > 1)
                        sources << dest
                }
            }
        }

        sources.each {
            Path p = new Path()
            p.add(it)
            paths << p
        }

        paths.each { path ->
            while (!sinks.contains(path.last())) {
                graph.successors(path.last()).each { path.add(it) }
            }
        }

        paths
    }

    def findSiblingGroups(graph) {
        def decisionEndPoints = []
        def sinks = []
        def sources = []
        def groups = []

        sources = graph.nodes().findAll {
            graph.inDegree(it) <= 1 && graph.outDegree(it) > 1 &&
                    !graph.successors(it).any { graph.inDegree(it) > 1 || graph.outDegree(it) > 1 }
        }

        sinks = graph.nodes().findAll {
            graph.inDegree(it) >= 1 && graph.outDegree(it) <= 1 &&
                    !graph.predecessors(it).any { graph.inDegree(it) > 1 || graph.outDegree(it) > 1 }
        }

        sources.each { src ->
            sinks.each { sink ->
                def group = []
                group += graph.successors(src).findAll { graph.successors(it).contains(sink) }
                if (!group.isEmpty())
                    groups.add(group)
            }
        }

        groups
    }

    int reduceSimplePath(path, graph) {
        int updates = 0

        def first = path.first()
        def lastSucc = []
        lastSucc += graph.successors(path.last())
        def last = null
        if (lastSucc)
            last = lastSucc.first()

        if (path.length() > 1) {
            path.remove(first)

            if (last)
                graph.putEdge(first, last)
            path.contents().each { graph.removeNode(it) }

            updates = path.contents().size()
        }

        updates
    }

    int reduceSimpleDecision(sibgroup, graph) {
        int updates = 0
        def first = sibgroup.first()
        sibgroup -= first

        sibgroup.each {
            graph.removeNode(it)
            updates += 1
        }

        updates
    }
}
