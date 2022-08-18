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

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import org.apache.commons.lang3.tuple.Pair

abstract class SigMainMetricEvaluator extends SigAbstractMetricEvaluator implements Rateable {

    protected Map<RiskCategory, Double> profile = [:]
    protected Table<Integer, RiskCategory, Range<Double>> ratingTable = HashBasedTable.create()
    protected Map<RiskCategory, Pair<Double, Double>> riskMap = [:]

    SigMainMetricEvaluator(ArcContext context) {
        super(context)
    }

    def measureValue(Measurable node) {
        if (node instanceof Project) {
            context.open()
            boolean hasVal = node.hasValueFor(repo.getRepoKey() + ":" + getMetricName() + ".LOW")
            context.close()
            if (hasVal)
                return

            profile[RiskCategory.LOW] = 0.0d
            profile[RiskCategory.MODERATE] = 0.0d
            profile[RiskCategory.HIGH] = 0.0d
            profile[RiskCategory.VERY_HIGH] = 0.0d
            Project proj = node as Project

            evaluate(proj)

            double sysSize = profile.values().sum()
            normalize(sysSize)

            context.open()
            Measure.of("${repo.getRepoKey()}:${getMetricName()}.LOW").on(node).withValue(profile[RiskCategory.LOW])
            Measure.of("${repo.getRepoKey()}:${getMetricName()}.MOD").on(node).withValue(profile[RiskCategory.MODERATE])
            Measure.of("${repo.getRepoKey()}:${getMetricName()}.HIGH").on(node).withValue(profile[RiskCategory.HIGH])
            Measure.of("${repo.getRepoKey()}:${getMetricName()}.VHIGH").on(node).withValue(profile[RiskCategory.VERY_HIGH])
            context.close()
        }
    }

    abstract def evaluate(Project proj)

    def normalize(sysSize) {
        profile[RiskCategory.LOW] /= sysSize
        profile[RiskCategory.MODERATE] /= sysSize
        profile[RiskCategory.HIGH] /= sysSize
        profile[RiskCategory.VERY_HIGH] /= sysSize
    }

    void categorize(Component comp, String handle) {
        double size = comp.getValueFor((String) "${MetricsConstants.METRICS_REPO_NAME}:SLOC")
        double value = comp.getValueFor((String) "${MetricsConstants.METRICS_REPO_NAME}:${handle}")

        boolean found = false

        riskMap.each {cat, range ->
            if (value >= range.left && value <= range.right && !found) {
                profile[cat] += size
                found = true
            }
        }

        if (!found)
            profile[RiskCategory.VERY_HIGH] += size
    }

    MetricRater getMetricRater() {
        return new MultiValueRater(this.getMetricName())
    }

    protected abstract String getMetricName()

    Metric toMetric(MetricRepository repository) {
        this.toMetric(repository, ["LOW", "MOD", "HIGH", "VHIGH", "RATING"])

        null
    }
}
