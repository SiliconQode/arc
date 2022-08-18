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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain

import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Analyzability",
        primaryHandle = "sigAnalyzability",
        description = "The degree of effectiveness and efficiency with which it is possible to assess the impact on a product or system of an intended change to one or more of its parts, or to diagnose a product for deficiencies or causes of failures, or to identify parts to be modified.",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = ["ISO/IEC 25010"]
)
class AnalyzabilityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        measureValue(node)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            double volume      = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigVolume.RATING", node)
            double duplication = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigDuplication.RATING", node)
            double unitSize    = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigUnitSize.RATING", node)
            double compBalance = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigComponentBalance.RATING", node)

            def factors = [
                    [0.25, volume],
                    [0.25, duplication],
                    [0.25, unitSize],
                    [0.25, compBalance]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:sigAnalyzability").on(node).withValue(value)
        }
    }
}
