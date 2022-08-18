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

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Measure Of Aggregation",
        primaryHandle = "MOA",
        description = "The extent of the part-whole relationship, realized by using attributes. The metric is a count of the number of data declarations whose types are user defined classes.",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Reuse
        ),
        references = [
                'Bansiya, Jagdish, and Carl G. Davis. "A hierarchical model for object-oriented design quality assessment." IEEE Transactions on software engineering 28.1 (2002): 4-17.'
        ]
)
class MeasureOfAggregation extends MetricEvaluator {

    /**
     *
     */
    MeasureOfAggregation() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0

        if (node instanceof Type) {
            node.getFields().each {
                if (shouldCount(it, node)) {
                    total += 1
                }
            }
        } else if (node instanceof Project) {
            List<Type> types = node.getAllTypes()
            types.each {
                total += measure(it)
            }

            if (types)
                total /= types.size()
        }

        Measure.of("${repo.getRepoKey()}:MOA").on(node).withValue(total)

        total
    }

    private boolean shouldCount(Field field, Measurable node) {
        field.getType().getType() == TypeRefType.Type &&
                field.getType().getReference() &&
                !(field.getType().getReference().getReferencedComponent(node.getParentProject()) instanceof Type &&
                        ((Type) field.getType().getReference().getReferencedComponent(node.getParentProject())).getType() == Type.UNKNOWN)
    }
}
