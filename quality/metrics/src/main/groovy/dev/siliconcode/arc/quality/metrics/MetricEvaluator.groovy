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
package dev.siliconcode.arc.quality.metrics

import dev.siliconcode.arc.datamodel.Component
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Metric
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import org.jetbrains.annotations.NotNull

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class MetricEvaluator implements Comparable<MetricEvaluator> {

    MetricRepository repo

    def measure(Measurable node) {
        MetricDefinition mdef = this.getClass().getAnnotation(MetricDefinition.class)
        if (node.hasValueFor((String) "${repo.getRepoKey()}:${mdef.primaryHandle()}"))
            return node.getValueFor((String) "${repo.getRepoKey()}:${mdef.primaryHandle()}")
        else
            measureValue(node)
    }

    abstract def measureValue(Measurable node)

    Metric toMetric(MetricRepository repository) {
        repo = repository
        MetricDefinition mdef = this.getClass().getAnnotation(MetricDefinition.class)
        Metric metric = Metric.findFirst("metricKey = ?", (String) "${repository.getRepoKey()}:${mdef.primaryHandle()}")
        if (!metric) {
            metric = Metric.builder()
                    .key("${repository.getRepoKey()}:${mdef.primaryHandle()}")
                    .handle(mdef.primaryHandle())
                    .name(mdef.name())
                    .description(mdef.description())
                    .evaluator(this.class.getCanonicalName())
                    .create()
            repository.addMetric(metric)
        }

        return metric
    }

    List<String> getDependencies() {
        []
    }

    @Override
    int compareTo(MetricEvaluator metricEvaluator) {
        getDependencies().size() <=> metricEvaluator.getDependencies().size()
    }

    double getMeasure(Component comp, String repo, String handle) {
        if (comp == null)
            return 0.0

        Project proj = comp.getParentProject()

        proj.getMeasuredValue(comp, repo, handle)
    }

    void resetState() {}
}
