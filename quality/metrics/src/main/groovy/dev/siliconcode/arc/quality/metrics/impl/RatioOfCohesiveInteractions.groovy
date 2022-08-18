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

import com.google.common.collect.Queues
import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Parameter
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.MetricCategory
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import dev.siliconcode.arc.quality.metrics.annotations.MetricProperties
import dev.siliconcode.arc.quality.metrics.annotations.MetricScale
import dev.siliconcode.arc.quality.metrics.annotations.MetricScope
import dev.siliconcode.arc.quality.metrics.annotations.MetricType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Ratio of Cohesive Interactions",
        primaryHandle = "RCI",
        description = "",
        properties = @MetricProperties(
                range = "0..1",
                aggregation = [],
                scope = MetricScope.METHOD,
                type = MetricType.Derived,
                scale = MetricScale.Interval,
                category = MetricCategory.Cohesion
        ),
        references = [
                ''
        ]
)
class RatioOfCohesiveInteractions extends MetricEvaluator{

    @Override
    def measureValue(Measurable node) {
        double total = 0.0
        // 1. Create a graph of all DD and DM interactions
        //    add type nodes
        //    add attribute nodes
        //    add method nodes
        //    add method return type nodes
        //    add method parameter nodes
        //    add DD edges
        //    add DM edges
        MutableGraph<?> graph = GraphBuilder.directed().build()
        if (node instanceof Type) {
            graph.addNode(node)
            List<Type> scope = []
            Queue<Type> q = Queues.newArrayDeque()
            q.offer((Type) node)

            int numTypes = 0
            int numFields = 0
            int numMethods = 0
            int numParams = 0

            String projKey = ((Type) node).getParentProjects().get(0).getProjectKey()

            while(!q.isEmpty()) {
                Type type = q.poll()
                numTypes += 1
                scope.add(type)
                graph.addNode(type)
                for (Type t : type.getContainedTypes()) {
                    q.offer(t)
                }
                for (Field f : type.getFields()) {
                    graph.addNode(f)
                    numFields += 1
                }
                for (Method m : type.getMethods()) {
                    graph.addNode(m)
                    numMethods += 1
                    for (Parameter p : m.getParams()) {
                        graph.addNode(p)
                        numFields += 1
                    }
                }
            }

            for (Object o : graph.nodes()) {
                for (Object j : graph.nodes()) {
                    if (o == j)
                        continue
                    if (o instanceof Type) {
                        if (j instanceof Field) {
                            if (j.getType().getType(projKey) == o)
                                graph.putEdge(o, j)
                        } else if (j instanceof Method) {
                            if (j.getType().getType(projKey) == o)
                                graph.putEdge(o, j)
                        } else if (j instanceof Parameter) {
                            if (j.getType().getType(projKey) == o)
                                graph.putEdge(o, j)
                        }
                    } else if (o instanceof Field) {
                        if (j instanceof Method) {
                            j.getFieldsUsed().contains(o)
                            graph.putEdge(o, j)
                        }
                    }
                }
            }
        }
        // 2. CI(c) = Count the number of interactions (edges)
        double ci = 0
        // 3. Max(c) = Count of all possible interations
        double max = (numTypes * (numTypes - 1)) + (numMethods * numTypes) +
                (numFields * numTypes) + (numParams * numTypes) +
                (numFields * numMethods) + (numFields * numParams)
        // 4. RCI = CI(c) / Max(c)
        total = ci / max
        // 5. Return RCI
        return total
    }
}
