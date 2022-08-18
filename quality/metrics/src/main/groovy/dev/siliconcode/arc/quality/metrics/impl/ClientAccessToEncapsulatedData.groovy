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
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Client Access to Encapsulated Data",
        primaryHandle = "CAED",
        description = "Count of the number of methods that return a reference or pointer to encapsulated data",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Encapsulation
        ),
        references = [
                'Hudli, Raghu V., Curtis L. Hoskins, and Anand V. Hudli. "Software metrics for object-oriented designs." Computer Design: VLSI in Computers and Processors, 1994. ICCD\'94. Proceedings., IEEE International Conference on. IEEE, 1994.'
        ]
)
class ClientAccessToEncapsulatedData extends MetricEvaluator {

    /**
     *
     */
    ClientAccessToEncapsulatedData() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        return null
    }
}