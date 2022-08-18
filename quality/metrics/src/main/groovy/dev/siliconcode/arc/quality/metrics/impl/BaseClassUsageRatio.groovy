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
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Modifier
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
class BaseClassUsageRatio extends MetricEvaluator {

    /**
     *
     */
    BaseClassUsageRatio() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0.0

        if (node instanceof Type) {
            Set inheritedAttrs = findInheritedAttributes(node)

            Set usedAttrs = []
            ((Type) node).getMethods().each { Method m ->
                usedAttrs += m.getFieldsUsed()
            }

            double usage = inheritedAttrs.intersect(usedAttrs).size() // FIXME
            double iaSize = inheritedAttrs.size()

            total = usage / iaSize
        }

        total
    }

    private Set findInheritedAttributes(Type t) {
        def parents = t.getParentTypes()

        def attrs = []
        parents.each {
            attrs += it.getFields().findAll {
                it.accessibility != Accessibility.PRIVATE && !it.modifiers.contains(Modifier.STATIC) // FIXME
            }
        }
    }
}
