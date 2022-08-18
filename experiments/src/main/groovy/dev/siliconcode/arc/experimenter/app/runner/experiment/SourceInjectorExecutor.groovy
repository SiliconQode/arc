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
package dev.siliconcode.arc.experimenter.app.runner.experiment


import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.disharmonies.injector.Director
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class SourceInjectorExecutor extends WorkFlow {

    int NUM
    Map<Integer, Tuple2<Integer, Integer>> severityMap = [
            1 : new Tuple2(1, 15),
            2 : new Tuple2(16, 30),
            3 : new Tuple2(31, 45),
            4 : new Tuple2(46, 60),
            5 : new Tuple2(61, 75)
    ]

    SourceInjectorExecutor(ArcContext context) {
        super("Source Injector", "Injects Issues into Source Code", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
    }

    void executeStudy() {
        ConfigSlurper slurper = new ConfigSlurper()
        results.rowKeySet().each {id ->
            ConfigObject config = createConfig(slurper, results.row(id))

            context.open()
            def vals = Director.instance.inject(config)
            context.close()

            vals.each { col, value ->
                results.put(id, col, value)
            }
        }
    }

    private ConfigObject createConfig(ConfigSlurper slurper, Map<String, String> map) {
        context.open()
        log.info("Looking up project with key: ${map[ExperimentConstants.Key1]}")
        Project proj = Project.findFirst("projKey = ?", map[ExperimentConstants.Key1])
        PatternInstance inst
        String pattern = map.get(ExperimentConstants.PatternType)
        String grimeType = map.get(ExperimentConstants.GrimeType)
        int min = 0, max = 0

        if (proj) {
            inst = proj.getPatternInstances().first()
            (min, max) = calculateGrimeSeverity(inst, Integer.parseInt(map.get(ExperimentConstants.GrimeSeverity)))
        }

        if (proj && inst) {
            String confText = """
            where {
                systemKey = '${proj.getParentSystem().getKey()}'
                projectKey = '${proj.getProjectKey()}'
                patternKey = 'gof:$pattern'
                patternInst = '${inst.getInstKey()}'
            }
            what {
                type = 'grime'
                form = '$grimeType'
                min = $min
                max = $max
            }
            """
            context.close()
            return slurper.parse(confText)
        }
        else {
            context.close()
            return null
        }
    }

    private def calculateGrimeSeverity(PatternInstance inst, int severity) {
        if (severity == 0)
            return [0, 0]

        int size = inst.getRoleBindings().size()

        int min = (int) Math.floor((double) (severityMap[severity].v1 / 100) * size) + 1
        int max = (int) Math.ceil((double) (severityMap[severity].v2 / 100) * size) + 1

        return [min, max]
    }
}
