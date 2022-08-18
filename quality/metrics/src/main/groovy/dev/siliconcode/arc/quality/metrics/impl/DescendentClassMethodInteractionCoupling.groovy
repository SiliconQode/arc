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
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Descendent Class Method Interaction Efferent Coupling",
        primaryHandle = "DCMEC",
        description = "Count of all class method interactions from descendent class metheods to the measured class.",
        properties = @MetricProperties(
                range = "Positive Integer",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Coupling
        ),
        references = [
                'Briand, Lionel, Prem Devanbu, and Walcelio Melo. "An investigation into coupling measures for C++." Proceedings of the 19th international conference on Software engineering. ACM, 1997.',
                'Briand, Lionel C., John W. Daly, and Jurgen K. Wust. "A unified framework for coupling measurement in object-oriented systems." IEEE Transactions on software Engineering 25.1 (1999): 91-121.'
        ]
)
class DescendentClassMethodInteractionCoupling extends MetricEvaluator {

    /**
     *
     */
    DescendentClassMethodInteractionCoupling() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        int total = 0

        if (node instanceof Type) {
            Set desc = []
            desc += node.getDescendentTypes()

            desc.each { Type d ->
                d.getMethods().each { Method m ->
                    if (!m.isOverriding(d, mediator)) { // FIXME
                        def p = m.getParams().collect { it.getType() }
                        total += p.findAll { it.reference.refKey == node.getRefKey() }.size()
                    }
                }
            }
        }

        total
    }
}
