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


import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.impl.patterns.ArcPatternConstants

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
class PatternsTestPhaseFour extends WorkFlow {

    private static final String STUDY_NAME = "Pattern 4 Test - Phase 4"
    private static final String STUDY_DESC = "Pattern Chaining Test"

    Command chaining

    PatternsTestPhaseFour(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        chaining = context.getRegisteredCommand(ArcPatternConstants.PATTERN_CHAIN_CMD_NAME)
    }

    @Override
    void executeStudy() {
        context.open()
        List<System> systems = System.findAll()

        systems.each { sys ->
            context.system = sys
            runTools()
        }
        context.close()
    }

    void runTools() {
        chaining.execute(context)
    }
}
