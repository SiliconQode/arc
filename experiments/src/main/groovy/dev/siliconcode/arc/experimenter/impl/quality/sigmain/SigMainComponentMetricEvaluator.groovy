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
import dev.siliconcode.arc.datamodel.Metric
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition

abstract class SigMainComponentMetricEvaluator extends MetricEvaluator implements Rateable {

    protected ArcContext context

    SigMainComponentMetricEvaluator(ArcContext context) {
        this.context = context
    }

    def measure(Measurable node) {
        measureValue(node)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            context.open()
            boolean hasVal = node.hasValueFor(repo.getRepoKey() + ":" + getMetricName() + ".RAW")
            context.close()
            if (hasVal)
                return

            Project proj = node as Project

            double value = evaluate(proj)

            context.open()
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.RAW").on(proj).withValue(value)
            context.close()
        }
    }

    protected abstract double evaluate(Project proj)

    protected abstract String getMetricName()

    MetricRater getMetricRater() {
        return new SingleValueRater(this.getMetricName())
    }

    @Override
    Metric toMetric(MetricRepository repository) {
        repo = repository
        MetricDefinition mdef = this.getClass().getAnnotation(MetricDefinition.class)
        String primaryHandle = mdef.primaryHandle()
        String metricName = mdef.name()
        String metricDescription = mdef.description()

        Metric metricRaw = Metric.findFirst("metricKey = ?", (String) "${repository.getRepoKey()}:${primaryHandle}.RAW")
        Metric metricRating = Metric.findFirst("metricKey = ?", (String) "${repository.getRepoKey()}:${primaryHandle}.RATING")
        if (!metricRaw) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "RAW")
        }
        if (!metricRating) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "RATING")
        }
    }

    private Metric createMetric(MetricRepository repository, String primaryHandle, String metricName, String metricDescription, String postName) {
        Metric metric = Metric.builder()
                .key("${repository.getRepoKey()}:${primaryHandle}.${postName}")
                .handle("${primaryHandle}.${postName}")
                .name("${metricName}.${postName}")
                .description(metricDescription)
                .evaluator(this.class.getCanonicalName())
                .create()
        repository.addMetric(metric)
    }
}
