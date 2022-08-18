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

import com.google.common.collect.Sets
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

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
class LackOfCohesionAmongMethods4 extends MetricEvaluator {

    /**
     *
     */
    LackOfCohesionAmongMethods4() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        int total = 0

        if (node instanceof Type) {
            MutableGraph<Method> graph = GraphBuilder.undirected().build()

            Set<Method> methods = node.getMethods()
            Set<Field> fields = node.getFields()

            methods.each {
                graph.addNode(it)
            }

            methods.each { Method m1 ->
                methods.each { Method m2 ->
                    if (m1 != m2) {
                        Set f1 = Sets.newHashSet(m1.getFieldsUsed()).intersect(fields)
                        Set f2 = Sets.newHashSet(m2.getFieldsUsed()).intersect(fields)
                        Set mm1 = m1.getMethodsCalled()
                        Set mm2 = m2.getMethodsCalled()

                        if (!f1.isEmpty() && !f2.isEmpty() && !f1.intersect(f2).isEmpty() ||
                                mm1.contains(m2) || mm2.contains(m1))
                            graph.putEdge(m1, m2)
                    }
                }
            }

            Set identified = []

            graph.nodes().each { mnode ->
                if (!identified.contains(mnode)) {
                    identified.add(mnode)
                    Set adj = []
                    adj += graph.adjacentNodes(mnode)
                    adj.removeAll(adj.intersect(identified)) // FIXME

                    Queue<Method> que = new ArrayDeque<>()
                    que.addAll(adj)
                    while (!que.isEmpty()) {
                        Method current = que.poll()
                        identified.add(current)
                        adj = []
                        adj += graph.adjacentNodes(mnode)
                        adj.removeAll(adj.intersect(identified)) // FIXME
                        que.addAll(adj)
                    }

                    total += 1
                }
            }
        }

        //Measure.of(this).on(node).withValue(total).store())
    }

}
