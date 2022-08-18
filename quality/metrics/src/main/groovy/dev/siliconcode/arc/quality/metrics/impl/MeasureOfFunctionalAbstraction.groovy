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
import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Modifier
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Measure of Functional Abstraction",
        primaryHandle = "MFA",
        description = "Ratio of the nubmer of methods inherited by a class to the total number of methods accessible by member methods of the class.",
        properties = @MetricProperties(
                range = "0.0..1.0",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Ratio,
                category = MetricCategory.Inheritance
        ),
        references = [
                'Bansiya, Jagdish, and Carl G. Davis. "A hierarchical model for object-oriented design quality assessment." IEEE Transactions on software engineering 28.1 (2002): 4-17.'
        ]
)
class MeasureOfFunctionalAbstraction extends MetricEvaluator {

    /**
     *
     */
    MeasureOfFunctionalAbstraction() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0.0

        if (node instanceof Type) {
            compute((Type) node)
        } else if (node instanceof Project) {
            List<Type> types = node.getAllTypes()
            types.each {
                total += measure(it)
            }

            if (types)
                total /= types.size()
        }

        Measure.of("${repo.getRepoKey()}:MFA").on(node).withValue(total)

        total
    }

    def compute(Type type) {
        double nmi = 0

        Queue<Type> q = Queues.newArrayDeque()
        q.offer(type)

        while (!q.isEmpty()) {
            Type t = q.poll()
            t.getGeneralizedBy().each { Type x ->
                q.offer(x)
                nmi += x.getMethods().count { Method m -> m.getAccessibility() != Accessibility.PRIVATE && !m.isAbstract() && !m.hasModifier("STATIC") }
            }
        }

        double nma = nmi + type.getMethods().count { Method m -> !m.isAbstract() && !m.hasModifier("STATIC") }

        return nmi / nma
    }
}
