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
package dev.siliconcode.arc.experimenter.app.runner.pattern4test

import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Collector
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.app.runner.verification.VerificationStudyConstants
import dev.siliconcode.arc.experimenter.impl.pattern4.Pattern4Constants

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
class PatternsTestPhaseThree extends WorkFlow {

    Command p4cmd
    Collector p4coll

    PatternsTestPhaseThree(ArcContext context) {
        super("Pattern4 Test Phase Three - Pattern 4 Tool", "Phase Three", context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        p4cmd = context.getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME)
        p4coll = context.getRegisteredCollector(Pattern4Constants.PATTERN4_COLL_NAME)
    }

    void executeStudy() {
        context.open()
        List<Project> projects = []
        results.rowKeySet().each { row ->
            projects.add(Project.findFirst("projKey = ?", results.get(row, PatternsTestConstants.KEY)))
        }
        context.close()

        projects.each { project ->
            context.project = project
            runTools()
        }
    }

    void runTools() {
        p4cmd.execute(context)
        p4coll.execute(context)
    }
}
