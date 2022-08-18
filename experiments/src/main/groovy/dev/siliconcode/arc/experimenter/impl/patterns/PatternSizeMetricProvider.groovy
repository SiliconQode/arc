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
package dev.siliconcode.arc.experimenter.impl.patterns

import dev.siliconcode.arc.datamodel.Metric
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.provider.AbstractMetricProvider
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternSizeMetricProvider extends AbstractMetricProvider {

    PatternSizeMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
    }

    @Override
    void updateDatabase() {
        Class<? extends MetricEvaluator> eval = PatternSizeEvaluator.class
        MetricDefinition mdef = eval.getAnnotation(MetricDefinition.class)
        String handle = mdef.primaryHandle()
        String name = mdef.name()
        String desc = mdef.description()
        String key = ArcPatternConstants.PATTERN_SIZE_REPO_KEY + ":" + handle

        context.open()
        if (!Metric.findFirst("metricKey = ?", key)) {
            Metric metric = Metric.builder()
                    .name(name)
                    .handle(handle)
                    .key(key)
                    .description(desc)
                    .evaluator(eval.getName())
                    .create()
            repository.addMetric(metric)
        }
        context.close()
    }

    @Override
    void initRepository() {
        context.open()
        this.repository = MetricRepository.findFirst("repoKey = ?", ArcPatternConstants.PATTERN_SIZE_REPO_KEY)
        context.close()
    }
}
