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
package dev.siliconcode.arc.experimenter.impl.quality.td

import dev.siliconcode.arc.datamodel.Metric
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.experimenter.provider.AbstractMetricProvider
import dev.siliconcode.arc.experimenter.ArcContext

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TechDebtMetricProvider extends AbstractMetricProvider {

    TechDebtMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
    }

    @Override
    void updateDatabase() {
        context.open()
        Metric metric = Metric.findFirst("metricKey = ?", (String) "${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.CAST_MEASURE_NAME}")
        if (!metric) {
            metric = Metric.builder()
                    .name(TechDebtConstants.CAST_MEASURE_NAME)
                    .key("${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.CAST_MEASURE_NAME}")
                    .description("Cast TD Principle")
                    .create()
            repository.addMetric(metric)
        }

        metric = Metric.findFirst("metricKey = ?", (String) "${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_PRINCIPLE_DOLLARS}")
        if (!metric) {
            metric = Metric.builder()
                    .name(TechDebtConstants.NUGROHO_PRINCIPLE_DOLLARS)
                    .key("${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_PRINCIPLE_DOLLARS}")
                    .description("Nugroho TD Principle (Dollars)")
                    .create()
            repository.addMetric(metric)
        }

        metric = Metric.findFirst("metricKey = ?", (String) "${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_PRINCIPLE_MM}")
        if (!metric) {
            metric = Metric.builder()
                    .name(TechDebtConstants.NUGROHO_PRINCIPLE_MM)
                    .key("${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_PRINCIPLE_MM}")
                    .description("Nugroho TD Principle (Man Months)")
                    .create()
            repository.addMetric(metric)
        }

        metric = Metric.findFirst("metricKey = ?", (String) "${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_INTEREST_DOLLARS}")
        if (!metric) {
            metric = Metric.builder()
                    .name(TechDebtConstants.NUGROHO_INTEREST_DOLLARS)
                    .key("${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_INTEREST_DOLLARS}")
                    .description("Nugroho TD Interest (Dollars)")
                    .create()
            repository.addMetric(metric)
        }

        metric = Metric.findFirst("metricKey = ?", (String) "${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_INTEREST_MM}")
        if (!metric) {
            metric = Metric.builder()
                    .name(TechDebtConstants.NUGROHO_INTEREST_MM)
                    .key("${TechDebtConstants.TD_REPO_KEY}:${TechDebtConstants.NUGROHO_INTEREST_MM}")
                    .description("Nugroho TD Interest (Man Months)")
                    .create()
            repository.addMetric(metric)
        }
        context.close()
    }

    @Override
    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", TechDebtConstants.TD_REPO_KEY)
        context.close()
    }
}
