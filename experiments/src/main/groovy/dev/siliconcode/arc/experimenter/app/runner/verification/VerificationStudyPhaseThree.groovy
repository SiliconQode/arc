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
package dev.siliconcode.arc.experimenter.app.runner.verification

import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.impl.java.JavaConstants
import groovy.util.logging.Log4j2

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class VerificationStudyPhaseThree  extends WorkFlow {

    Table<String, String, String> results
    Command java
    Command jdi
    Command parser

    VerificationStudyPhaseThree(ArcContext context) {
        super("Verification Study Phase Three", "Phase Three", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
        java     = getContext().getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        parser   = getContext().getRegisteredCommand(JavaConstants.JAVA_PARSE_CMD_NAME)
        jdi      = getContext().getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
    }

    void executeStudy() {
        ConfigSlurper slurper = new ConfigSlurper()

        results.rowKeySet().each {id ->
            ConfigObject config = createConfig(slurper, results.row(id))

            context.open()
            def vals = VerificationInjectorDirector.instance.inject(config)
            context.close()

            vals.each { col, value ->
                results.put(id, col, value)
            }

            context.open()
            context.project = Project.findFirst("projKey = ?", results.row(id).get(VerificationStudyConstants.INJECTED_KEY))
            context.close()
            runTools()
        }
    }

    void runTools() {
        java.execute(context)
        parser.execute(context)
        jdi.execute(context)
    }

    private ConfigObject createConfig(ConfigSlurper slurper, Map<String, String> map) {
        context.open()
        log.info("Looking up project with key: ${map[VerificationStudyConstants.BASE_KEY]}")
        Project proj = Project.findFirst("projKey = ?", map[VerificationStudyConstants.BASE_KEY])
        PatternInstance inst

        if (proj) inst = proj.getPatternInstances().first()

        if (proj && inst) {
            String confText = """
            where {
                systemKey = '${proj.getParentSystem().getKey()}'
                baseKey = '${proj.getProjectKey()}'
                injectedKey = '${map[VerificationStudyConstants.INJECTED_KEY]}'
                injectedLoc = '${map[VerificationStudyConstants.INJECTED_LOCATION]}'
                patternInst = '${inst.getInstKey()}'
            }
            control {
                fileName = '${map[VerificationStudyConstants.CONTROL_FILE]}'
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
}
