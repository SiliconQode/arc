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
package dev.siliconcode.arc.experimenter.impl.metrics

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
class MetricsToolMetricsProvider extends AbstractMetricProvider {

    static MetricsRegistrar registrar

    private static final String NOT_METHODS = "not-methods"
    private static final String METHODS_ONLY = "methods-only"
    private static final String TYPE_CONT = "type-containers"
    private static final String TYPES_ONLY = "types-only"
    private static final String ALL = "all"

    MetricsToolMetricsProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        registerMetrics()
    }

    @Override
    void updateDatabase() {
        registrar.getCategoryEvaluators(ALL).each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
        registrar.getCategoryEvaluators(METHODS_ONLY).each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
        registrar.getCategoryEvaluators(NOT_METHODS).each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
        registrar.getSecondaryEvaluators().each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
    }

    @Override
    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", MetricsConstants.METRICS_REPO_KEY)
        registrar = new MetricsRegistrar(repository)
        context.close()
    }

    void registerMetrics() {
//        registrar.register(new NumberOfFields(), NOT_METHODS) // for quamoco
//        registrar.register(new NumberOfMethods(), NOT_METHODS) // for quamoco
//        registrar.register(new NumberOfPrivateAttributes(), NOT_METHODS) // for quamoco
//        registrar.register(new NumberOfProtectedAttributes(), NOT_METHODS) // for quamoco
//        registrar.register(new NumberOfPublicMethods(), NOT_METHODS) // Needed for Quamoco
//        registrar.register(new NumberOfInstanceVariables(), NOT_METHODS) // Needed for Quamoco
//        registrar.register(new NumberOfClassVariables(), NOT_METHODS) // Needed for Quamoco
        registrar.register(new AfferentCoupling(), NOT_METHODS) // Needed for Sig Maintainability
//        registrar.register(new NumberOfAncestorClasses(), NOT_METHODS) // Needed for QMOOD
//        registrar.register(new NumberOfTypes(), NOT_METHODS) // Needed for Quamoco
//        registrar.register(new NumberOfClasses(), NOT_METHODS) // Needed for Quamoco
//        registrar.register(new NumberOfAncestorClasses(), NOT_METHODS)

//        registrar.register(new NumberOfStatements(), ALL) // for quamoco
        registrar.register(new NumberOfMethodParameters(), ALL) // for sig maintainability
        registrar.register(new SourceLinesOfCode(), ALL) // for quamoco and sig maintainability
        registrar.register(new LinesOfCode(), ALL)
//        registrar.register(new NumberOfLocalVariables(), ALL) // Needed for Quamoco

        registrar.register(new CyclomaticComplexity(), METHODS_ONLY) // for sig maintainability

//        registrar.registerSecondary(new LinesOfCodePerClass()) // depends on LOC -> Needed for QMOOD
//        registrar.registerSecondary(new TotalNumberOfAttributes()) // depends on NOA -> Needed for QMOOD
//        registrar.registerSecondary(new TotalNumberOfClasses()) // depends on NC -> Needed for QMOOD
//        registrar.registerSecondary(new TotalNumberOfMethods()) // depends on NOM -> Needed for QMOOD
//        registrar.registerSecondary(new PolymorphicMethods()) // depends on NOC -> Needed for QMOOD
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
