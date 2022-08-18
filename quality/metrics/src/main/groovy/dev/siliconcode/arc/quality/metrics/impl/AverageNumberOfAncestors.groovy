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

import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Structure
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Average Number of Ancestors",
        primaryHandle = "ANA",
        description = "The average number of classes from which a class inherits .It is computed by determining the number of classes along all paths from the 'root' class(es) to all classes in an inheritance structure",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.STRUCTURAL,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Inheritance
        ),
        references = [
                ''
        ]
)
class AverageNumberOfAncestors extends MetricEvaluator {

    /**
     *
     */
    AverageNumberOfAncestors() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0.0

        if (node instanceof Project) {
            Project struct = (Project) node
            Map<Type, Integer> map = new HashMap<>()
            def classes = struct.getAllTypes()
            classes.each { Type t ->
                total += Measure.valueFor("arc_metrics", "NOA", t)
            }
            if (classes)
                total /= classes.size()
        }

        Measure.of("${repo.getRepoKey()}:ANA").on(node).withValue(total)

        total
    }

    private int recursiveSearch(Type type, Map<Type, Integer> map) {
        if (type.getRealizedBy().isEmpty() && type.getGeneralizedBy().isEmpty()) {
            map[type] = 0
        } else if (!map[type]) {
            int real = 0
            int gen = 0
            type.getRealizedBy().each {
                real += recursiveSearch(it, map)
            }
            type.getGeneralizedBy().each {
                gen += recursiveSearch(it, map)
            }
            map[type] = 1 + real + gen
        }
        map[type]
    }
}
