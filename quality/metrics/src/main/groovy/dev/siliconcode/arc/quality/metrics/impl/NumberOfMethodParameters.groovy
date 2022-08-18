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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Number of Method Parameters",
        primaryHandle = "NOMP",
        description = "A count of the number of parameters to a method, excluding template params.",
        properties = @MetricProperties(
                range = "Positive Integers",
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
class NumberOfMethodParameters extends MetricEvaluator {

    /**
     *
     */
    NumberOfMethodParameters() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    def measureValue(Measurable node) {
        double total = 0

        if (node instanceof Method) {
            total = node.getParams().size()
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        } else if (node instanceof Type) {
            Type type = node as Type
            type.getAllMethods().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }
        else if (node instanceof File) {
            File file = node as File
            file.getAllTypes().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }
        else if (node instanceof PatternInstance) {
            PatternInstance inst = node as PatternInstance
            inst.getTypes().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }
        else if (node instanceof System) {
            System sys = node as System
            sys.getProjects().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }
        else if (node instanceof Namespace) {
            Namespace ns = node as Namespace
            ns.getAllTypes().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }
        else if (node instanceof Project) {
            Project ns = node as Project
            ns.getNamespaces().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOMP")
            }
            Measure.of("${repo.getRepoKey()}:NOMP").on(node).withValue(total)
        }

        total
    }

}
