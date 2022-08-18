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
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Structure
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@MetricDefinition(
        name = "Number of Fields",
        primaryHandle = "NOF",
        otherHandles = ['#FieldDeclarations'],
        description = "Count of the number of fields or attributes defined but not inherited by a class",
        properties = @MetricProperties(
                range = "Postive Integers",
                aggregation = [],
                scope = MetricScope.TYPE,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.BasicProperty
        ),
        references = [
                ''
        ]
)
class NumberOfFields extends MetricEvaluator {

    /**
     *
     */
    NumberOfFields() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        int total = 0

        if (node instanceof Type) {
            total = node.getFields().size()
            Measure.of("${repo.getRepoKey()}:NOF").on(node).withValue(total)
        } else if (node instanceof File) {
            ((File) node).getAllTypes().each { Type type ->
                total += type.getValueFor("${repo.getRepoKey()}:NOF")
            }
            Measure.of("${repo.getRepoKey()}:NOF").on(node).withValue(total)
        } else if (node instanceof Namespace) {
            ((Namespace) node).getAllTypes().each { Type type ->
                total += type.getValueFor("${repo.getRepoKey()}:NOF")
            }
            Measure.of("${repo.getRepoKey()}:NOF").on(node).withValue(total)
        } else if (node instanceof ComponentContainer) {
            node.getAllTypes().each { Type type ->
                total += type.getValueFor("${repo.getRepoKey()}:NOF")
            }
            Measure.of("${repo.getRepoKey()}:NOF").on(node).withValue(total)
        }
    }

}
