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


import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.provider.AbstractMetricProvider
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.MetricsRegistrar
import dev.siliconcode.arc.quality.metrics.impl.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QMoodMetricProvider extends AbstractMetricProvider {

    MetricsRegistrar registrar

    QMoodMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        registerMetrics()
    }

    @Override
    void updateDatabase() {
//        GParsPool.withPool(8) {
        registrar.getPrimaryEvaluators().each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
        registrar.getSecondaryEvaluators().each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
//        }
    }

    @Override
    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", QMoodConstants.QMOOD_REPO_KEY)
        registrar = new MetricsRegistrar(repository)
        context.close()
    }

    void registerMetrics() {
        // basic metrics
        registrar.registerPrimary(new DesignSize())
        registrar.registerPrimary(new NumberOfHierarchies())
        registrar.registerPrimary(new AverageNumberOfAncestors())
        registrar.registerPrimary(new DataAccessMetric())
        registrar.registerPrimary(new DirectClassCoupling())
        registrar.registerPrimary(new CohesionAmongMethodsOfClass()) // need to finish
        registrar.registerPrimary(new MeasureOfAggregation()) // need to finish
        registrar.registerPrimary(new MeasureOfFunctionalAbstraction()) // need to finish
        registrar.registerPrimary(new NumberOfPolymorphicMethods())
        registrar.registerPrimary(new ClassInterfaceSize())
        registrar.registerPrimary(new NumberOfMethods())

        // quality metrics
        registrar.registerSecondary(new ReusabilityEvaluator())
        registrar.registerSecondary(new FlexibilityEvaluator())
        registrar.registerSecondary(new UnderstandabilityEvaluator())
        registrar.registerSecondary(new FunctionalityEvaluator())
        registrar.registerSecondary(new ExtendibilityEvaluator())
        registrar.registerSecondary(new EffectivenessEvaluator())
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
