/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.experimenter.impl.quality.qmood

import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.MetricCategory
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import dev.siliconcode.arc.quality.metrics.annotations.MetricProperties
import dev.siliconcode.arc.quality.metrics.annotations.MetricScale
import dev.siliconcode.arc.quality.metrics.annotations.MetricScope
import dev.siliconcode.arc.quality.metrics.annotations.MetricType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "QMOOD Reusability",
        primaryHandle = "QMREUSE",
        description = "Reflects the presence of object-oriented design characteristics that allow a design to be reapplied to a new problem without significant effort",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = [
                'Bansiya, Jagdish, and Carl G. Davis. "A hierarchical model for object-oriented design quality assessment." IEEE Transactions on software engineering 28.1 (2002): 4-17.'
        ]
)
class ReusabilityEvaluator extends MetricEvaluator {

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node

            List<Double> vals = Measure.getAllClassValues(proj, QMoodConstants.QMOOD_REPO_KEY, "QMREUSE")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMREUSE").on(node).withValue(value)
        }
        else if (node instanceof Type) {
            double coupling   = Measure.retrieve(node, QMoodConstants.QMOOD_REPO_KEY + ":DCC")?.getValue() ?: 0
            double cohesion   = Measure.retrieve(node, QMoodConstants.QMOOD_REPO_KEY + ":CAM")?.getValue() ?: 0
            double messaging  = Measure.retrieve(node, QMoodConstants.QMOOD_REPO_KEY + ":CIS")?.getValue() ?: 0
            double designSize = Measure.retrieve(node, QMoodConstants.QMOOD_REPO_KEY + ":DSC")?.getValue() ?: 0

            def factors = [
                    [-0.25, coupling],
                    [0.25, cohesion],
                    [0.50, messaging],
                    [0.50, designSize],
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMREUSE").on(node).withValue(value)
        }
    }
}
