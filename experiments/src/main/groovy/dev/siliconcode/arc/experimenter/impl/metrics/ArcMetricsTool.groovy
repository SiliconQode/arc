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

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.MetricsRegistrar
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ArcMetricsTool {

    MetricsRegistrar registrar
    ArcContext context
    List<MetricEvaluator> evaluatorList
    List<MetricEvaluator> secondaryList

    ArcMetricsTool(ArcContext context) {
        this.context = context
        registrar = MetricsToolMetricsProvider.getRegistrar()
    }

    void withDb(Closure cl) {
//        log.info "Opened at $method"
//        if (!DBManager.getInstance().isOpen())
        context.open()
        cl.call()
        context.close()
//        log.info "Closed at $method"
    }

    void init() {
        evaluatorList = Lists.newArrayList()
        evaluatorList.addAll(registrar.getCategoryEvaluators("all"))
        evaluatorList.addAll(registrar.getCategoryEvaluators("not-methods"))
        secondaryList = registrar.getSecondaryEvaluators()
        Collections.sort(evaluatorList)
    }

    void exec() {
        Project proj = context.getProject()
        log.info "Resetting Evaluator State"
        evaluatorList*.resetState()
        secondaryList*.resetState()
        log.info "Measuring Primary Metrics"
        streamAndMeasureProject(proj, evaluatorList, true)
        log.info "Measuring Secondary Metrics"
        streamAndMeasureProject(proj, secondaryList, false)
    }

    private void streamAndMeasureMethods(Type type, List<MetricEvaluator> evaluatorList) {
        List<Method> methods = []
        withDb {
            methods = Lists.newArrayList(type.getAllMethods())
        }

//        GParsPool.withPool(8) {
//            methods.eachParallel { method ->
        methods.each { method ->
            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Method using ${mdef.primaryHandle()}"
                withDb {
                    metricEvaluator.measure(method as Method)
                }
            }
//            }
        }
    }

    private void streamAndMeasureTypes(Namespace ns, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        List<Type> types = []
        withDb {
            types = Lists.newArrayList(ns.getAllTypes())
        }

//        GParsPool.withPool(8) {
//            types.eachParallel { type ->
        types.each { type ->
            if (measureMethods) {
                List<MetricEvaluator> methodEvals = Lists.newArrayList()
                methodEvals.addAll(registrar.getCategoryEvaluators("all"))
                methodEvals.addAll(registrar.getCategoryEvaluators("methods-only"))
                streamAndMeasureMethods(type as Type, methodEvals)
            }

            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Types using ${mdef.primaryHandle()}"
                withDb {
                    metricEvaluator.measure(type as Type)
                }
            }
        }
//        }
    }

    private void streamAndMeasureFiles(Project proj, List<MetricEvaluator> evaluatorList) {
        List<File> files = []
        withDb {
            files = Lists.newArrayList(proj.getFilesByType(FileType.SOURCE))
        }

//        GParsPool.withPool(8) {
//            files.eachParallel { file ->
        files.each { file ->
            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Files using ${mdef.primaryHandle()}"
                withDb {
                    metricEvaluator.measure(file as File)
                }
            }
        }
//        }
    }

    private void streamAndMeasureNamespaces(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        List<Namespace> namespaces = []
        withDb {
            namespaces = Lists.newArrayList(proj.getNamespaces())
        }

//        GParsPool.withPool(8) {
//            namespaces.eachParallel { ns ->
        namespaces.each { ns ->
            streamAndMeasureTypes(ns as Namespace, evaluatorList, measureMethods)
            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Namespaces using ${mdef.primaryHandle()}"
                withDb {
                    metricEvaluator.measure(ns as Namespace)
                }
            }
        }
//        }
    }

    private void streamAndMeasureModules(Project project, List<MetricEvaluator> evaluatorList) {
        List<Module> modules = []
        withDb {
            modules = Lists.newArrayList(project.getModules())
        }

//        GParsPool.withPool(8) {
//            modules.eachParallel { mod ->
        modules.each { mod ->
            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Modules using ${mdef.primaryHandle()}"
                withDb {
                    metricEvaluator.measure(mod as Module)
                }
            }
        }
//        }
    }

    private void streamAndMeasureProject(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        boolean hasAll = true
        registrar.getCategoryEvaluators("all").each {metricEvaluator ->
            MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
            withDb {
                hasAll = hasAll && proj.hasValueFor(metricEvaluator.getRepo().getRepoKey() + ":" + mdef.primaryHandle())
            }
        }

        if (!hasAll) {
            streamAndMeasureNamespaces(proj, evaluatorList, measureMethods)
            streamAndMeasureFiles(proj, evaluatorList)
            streamAndMeasureModules(proj, evaluatorList)

//        GParsPool.withPool(8) {
//            evaluatorList.eachParallel { metricEvaluator ->
            evaluatorList.each { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Projects using ${mdef.primaryHandle()}"
                withDb {
                    (metricEvaluator as MetricEvaluator).measure(proj)
                }
            }
        }
//        }
    }
}
