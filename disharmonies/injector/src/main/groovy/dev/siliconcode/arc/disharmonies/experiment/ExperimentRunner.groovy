/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.disharmonies.experiment

import dev.siliconcode.arc.disharmonies.injector.grime.GrimeInjector
import groovy.util.logging.Slf4j

import java.util.concurrent.RecursiveTask
import java.util.logging.Level
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Slf4j
class ExperimentRunner extends RecursiveTask<DataHolder> {

    private DesignNode node
    private GrimeInjector injector

    /**
     * @param node
     */
    ExperimentRunner(DesignNode node)
    {
        this.node = node
        logger.setLevel(Level.INFO)
        injector = initInjector(node.getInjectType())
    }

    /**
     * @param injectType
     * @return
     */
    private GrimeInjector initInjector(String injectType)
    {
        return null
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.RecursiveTask#compute()
     */
    @Override
    protected DataHolder compute()
    {
//        SystemEntity sys = new SystemEntity(node.getSystem(), "", node.getSystem(), node.getInstanceLoc())
//        PatternEntity pattern = new PatternEntity(node.getPatternType(), "pattern: " + node.getPatternType())
//        logger.logp(Level.INFO, "", "", "Anaylyzing System: " + sys.getName() + " Version: " + sys.getVersion())
//        logger.logp(Level.INFO, "", "", "Source Directory: " + sys.getSourceDirectory())
//        logger.logp(Level.INFO, "", "", "Building Model")
//
//        final List<SystemEntity> systems = new LinkedList<>()
//        systems.add(sys)
//        final CodeGraph graph = new CodeGraph()
//        final CodeGraphBuilder cgb = new CodeGraphBuilder(new Java7GraphParser())
//        cgb.process(systems, graph)
//        graph.addPattern(pattern)
//        for (ClassOrInterfaceEntity cie : sys.getClasses())
//        {
//            pattern.addClass(cie)
//        }
//
//        logger.logp(Level.INFO, "", "", "Initiating Metrics Controller")
//        MetricsController controller = new MetricsController(graph)
//        logger.logp(Level.INFO, "", "", "Measuring Classes")
//        controller.measureClasses()
//        logger.logp(Level.INFO, "", "", "Measuring System")
//        controller.measureSystems()
//        logger.logp(Level.INFO, "", "", "Measuring Before Quality Aspects")
//        QualityModel model = new QMOODModel()
//        model.generateModel()
//        Map<String, Double> beforeValues = model.measureAspects(graph, sys)
//
//        logger.logp(Level.INFO, "", "", "Injecting " + node.getInjectType())
//        injector.inject(graph, pattern)
//
//        logger.logp(Level.INFO, "", "", "Measuring After Quality Aspects")
//        controller.measureClasses()
//        controller.measureSystems()
//        Map<String, Double> afterValues = model.measureAspects(graph, sys)
//
//        int rep = node.getRep()
//        return createDataHolder(rep, beforeValues, afterValues)
        return null
    }

    /**
     * @param beforeValues
     * @param afterValues
     * @return
     */
    private DataHolder createDataHolder(int rep, Map<String, Double> beforeValues, Map<String, Double> afterValues)
    {
        DataHolder holder = new DataHolder()
        for (String key : beforeValues.keySet())
        {
            holder.addBefore(rep, node.getPatternType(), node.getInjectType(), key, beforeValues.get(key))
            holder.addAfter(rep, node.getPatternType(), node.getInjectType(), key, afterValues.get(key))
        }

        return holder
    }
}
