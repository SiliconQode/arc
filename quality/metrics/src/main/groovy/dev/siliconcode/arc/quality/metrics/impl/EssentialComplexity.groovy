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
package dev.siliconcode.arc.quality.metrics.impl

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.cfg.ControlFlowNode
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*
import dev.siliconcode.arc.quality.metrics.cfgredux.CfgReducer

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "",
        primaryHandle = "",
        description = "",
        properties = @MetricProperties(
                range = "",
                aggregation = [],
                scope = MetricScope.METHOD,
                type = MetricType.Derived,
                scale = MetricScale.Interval,
                category = MetricCategory.Coupling
        ),
        references = [
                ''
        ]
)
class EssentialComplexity extends MetricEvaluator {

    /**
     *
     */
    EssentialComplexity() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        int total = 0

        if (node instanceof Method) {
            MutableGraph<Integer> graph = GraphBuilder.directed().build()
            def map = [:]
            def nodes = []
            Graph<ControlFlowNode> cfg = node.getCfg().getGraph()
            nodes += cfg.nodes()
            (1..cfg.nodes().size()) { // FIXME
                graph.addNode(it)
                map[nodes[it - 1]] = it
            }

            nodes.each { pred ->
                cfg.successors().each { succ ->
                    graph.putEdge(map[pred], map[succ]) // FIXME
                }
            }

            new CfgReducer().reduce(graph)
            int n = graph.nodes().size()
            int e = graph.edges().size()

            total = e - n + 2
        }

        total
    }
}
