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

import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Attribute Hiding Factor",
        primaryHandle = "AHF",
        description = "Ratio of hidden attributes to total attributes",
        properties = @MetricProperties(
                range = "0.0..1.0",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Ratio,
                category = MetricCategory.Encapsulation
        ),
        references = [
                'Abreu, Fernando Brito, and Rogério Carapuça. "Object-oriented software engineering: Measuring and controlling the development process." Proceedings of the 4th international conference on software quality. Vol. 186. 1994.',
                'e Abreu, F. Brito, and Walcelio Melo. "Evaluating the impact of object-oriented design on software quality." Software Metrics Symposium, 1996., Proceedings of the 3rd International. IEEE, 1996.',
                'Abreu, F. Brito, Miguel Goulão, and Rita Esteves. "Toward the design quality evaluation of object-oriented software systems." Proceedings of the 5th International Conference on Software Quality, Austin, Texas, USA. 1995.'
        ]
)
class AttributeHidingFactor extends MetricEvaluator {

    /**
     *
     */
    AttributeHidingFactor() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0.0

        if (node instanceof Type) {
            def classes = node.getChildTypes()

            def isVisible = { Field m ->
                Type t = m.owner

                Set<Type> dc = []
                dc += t.getGeneralizes()
                dc += t.getRealizes()

                Set<Type> pc = []
                if (t.getParentNamespaces().size() > 0)
                    pc += t.getParentNamespaces().get(0).getAllTypes()

                Set<Type> mc = []
                if (t.getParentModules().size() > 0)
                    mc += t.getParentModules().get(0).getAllTypes()

                Accessibility access = ((Field) m).getAccessibility()

                switch (access) {
                    case Accessibility.PUBLIC:
                        1
                        break
                    case Accessibility.PRIVATE:
                        0
                        break
                    case Accessibility.PROTECTED:
                        (double) dc.size() / (double) (classes.size() - 1)
                        break
                    case Accessibility.PACKAGE:
                        (double) pc.size() / (double) (classes.size() - 1)
                        break
                    case Accessibility.DEFAULT:
                        (double) pc.size() / (double) (classes.size() - 1)
                        break
                    case Accessibility.INTERNAL:
                        (double) mc.size() / (double) (classes.size() - 1)
                        break
                    case Accessibility.PROTECTED_INTERNAL:
                        (double) (dc.size() + mc.size()) / (double) (classes.size() - 1)
                        break
                    default:
                        0
                }
            }

            def visibility = { Field m ->
                double totalVisible = 0.0

                classes.each {
                    totalVisible += isVisible(m, it) // FIXME
                }

                totalVisible / (classes.size() - 1)
            }

            double totalAd = 0.0
            double totalVis = 0.0

            classes.each {
                totalAd += getMetric(it, "NAD")

                it.fields().each { field ->
                    totalVis += (1 - visibility(field)) // FIXME
                }
            }


        }

        total
    }
}
