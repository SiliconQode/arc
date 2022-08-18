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
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "QMOOD Effectiveness",
        primaryHandle = "QMEFFECT",
        description = "This refers to a design's ability to achieve the desired functionality and behavior using object-oriented design concepts and techniques.",
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
class EffectivenessEvaluator extends MetricEvaluator {

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node

            List<Double> vals = Measure.getAllClassValues(proj, QMoodConstants.QMOOD_REPO_KEY, "QMEFFECT")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMEFFECT").on(node).withValue(value)
        } else if (node instanceof Type) {
            double abstraction = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "ANA", node)
            double encapsulation = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DAM", node)
            double composition = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "MOA", node)
            double inheritance = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "MFA", node)
            double polymorphism = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOP", node)

            def factors = [
                    [0.20, abstraction],
                    [0.20, encapsulation],
                    [0.20, composition],
                    [0.20, inheritance],
                    [0.20, polymorphism]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${repo.getRepoKey()}:QMEFFECT").on(node).withValue(value)
        }
    }
}
