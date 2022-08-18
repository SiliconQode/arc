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

import dev.siliconcode.arc.datamodel.ComponentContainer
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Number of Ancestor Classes",
        primaryHandle = "NOA",
        description = "A count of the number of ancestor classes for a given classes",
        properties = @MetricProperties(
                range = "",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Derived,
                scale = MetricScale.Interval,
                category = MetricCategory.Inheritance
        ),
        references = [
                ''
        ]
)
class NumberOfAncestorClasses extends MetricEvaluator {

    static Map<String, Integer> typeAncs = [:]

    /**
     *
     */
    NumberOfAncestorClasses() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        int total = 0

        if (node instanceof Type) {
            Type t = (node as Type)
            if (typeAncs.containsKey(t.getCompKey())) {
                total = typeAncs[t.getCompKey()]
            } else {
                Queue<Type> q = new ArrayDeque<>()
                q.offer(node)

                boolean start = true
                while (!q.isEmpty()) {
                    Type type = q.poll()

                    if (typeAncs.containsKey(t.getCompKey()))
                        total += typeAncs[t.getCompKey()]
                    else {
                        type.getRealizes().each { Type real ->
                            q.offer(real)
                        }

                        type.getGeneralizedBy().each {Type gen ->
                            q.offer(gen)
                        }

                        if (!start)
                            total += 1
                        else
                            start = false
                    }
                }
                typeAncs[t.getCompKey()] = total
            }
            Measure.of("${repo.getRepoKey()}:NOA").on(node).withValue(total)
        } else if (node instanceof ComponentContainer && !(node instanceof Type)) {
            node.getAllTypes().each { Type type ->
                total += type.getValueFor("${repo.getRepoKey()}:NOA")
            }
            if (!node.getAllTypes().isEmpty())
                total /= node.getAllTypes().size()
            else
                total = 0
            Measure.of("${repo.getRepoKey()}:NOA").on(node).withValue(total)
        }

        total
    }
}
