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
class Connectivity extends MetricEvaluator {

    /**
     *
     */
    Connectivity() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0.0

        if (node instanceof Type) {
            MutableGraph<Method> graph = GraphBuilder.undirected().build() // FIXME

            Set<Method> methods = Sets.newHashSet(node.getMethods())
            Set<Field> fields = Sets.newHashSet(node.getFields())

            methods.each { Method m ->
                graph.addNode(m)
            }

            methods.each { Method m1 ->
                methods.each { Method m2 ->
                    if (m1 != m2) {
                        Set<Field> f1 = Sets.newHashSet(m1.getFieldsUsed()).intersect(fields)
                        Set<Field> f2 = Sets.newHashSet(m2.getFieldsUsed()).intersect(fields)
                        Set<Method> mm1 = Sets.newHashSet(m1.getMethodsCalled())
                        Set<Method> mm2 = Sets.newHashSet(m2.getMethodsCalled())

                        if (!f1.isEmpty() && !f2.isEmpty() && !f1.intersect(f2).isEmpty() ||
                                mm1.contains(m2) || mm2.contains(m1))
                            graph.putEdge(m1, m2)
                    }
                }
            }

            double nodes = graph.nodes().size()
            double edges = graph.edges().size()
            total = 2 * ((edges - (nodes - 1)) / ((nodes - 1) * (nodes - 2)))
        }

        total
    }

}
