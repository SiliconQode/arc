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

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.quality.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Volume",
        primaryHandle = "sigVolume",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
class Volume extends SigAbstractMetricEvaluator implements Rateable {

    Volume(ArcContext context) {
        super(context)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            context.open()
            boolean hasVal = node.hasValueFor(repo.getRepoKey() + ":sigVolume.RAW")
            context.close()

            if (hasVal)
                return

            context.open()
            double systemSize = node.getValueFor((String) "${MetricsConstants.METRICS_REPO_KEY}:SLOC")
            double technologyFactor = 0.00136d

            double rebuildValue = (systemSize * technologyFactor) / 12

            Measure.of("${repo.getRepoKey()}:sigVolume.RAW").on(node).withValue(rebuildValue)
            context.close()
        }
    }

    MetricRater getMetricRater() {
        return new SingleValueRater("sigVolume")
    }

    Metric toMetric(MetricRepository repository) {
        this.toMetric(repository, ["RAW", "RATING"])

        null
    }
}
