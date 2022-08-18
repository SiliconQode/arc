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

import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.provider.AbstractMetricProvider
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.MetricsRegistrar

class SigRatingMetricProvider extends AbstractMetricProvider {

    MetricsRegistrar registrar

    SigRatingMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        registerMetrics()
    }

    @Override
    void updateDatabase() {
        context.open()
        registrar.getPrimaryEvaluators().each {
            it.toMetric(repository)
        }

        registrar.getSecondaryEvaluators().each {
            it.toMetric(repository)
        }
        context.close()
    }

    @Override
    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", SigMainConstants.SIGMAIN_REPO_KEY)
        registrar = new MetricsRegistrar(repository)
        context.close()
    }

    void registerMetrics() {
        // Base Metrics
        registrar.registerPrimary(new Volume(context))
        registrar.registerPrimary(new Duplication(context))
        registrar.registerPrimary(new UnitComplexity(context))
        registrar.registerPrimary(new UnitSize(context))
        registrar.registerPrimary(new UnitInterfacing(context))
        registrar.registerPrimary(new ModuleCoupling(context))
        registrar.registerPrimary(new ComponentBalance(context))
        registrar.registerPrimary(new ComponentIndependence(context))
        registrar.registerPrimary(new ComponentEntanglement(context))

        // Calibration metrics
        registrar.registerSecondary(new AnalyzabilityEvaluator())
        registrar.registerSecondary(new ModifiabilityEvaluator())
        registrar.registerSecondary(new ModularityEvaluator())
        registrar.registerSecondary(new ReusabilityEvaluator())
        registrar.registerSecondary(new TestabilityEvaluator())
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
