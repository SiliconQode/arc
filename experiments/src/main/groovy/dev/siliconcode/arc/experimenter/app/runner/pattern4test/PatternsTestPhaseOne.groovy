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
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.app.runner.verification.VerificationStudyConstants

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
class PatternsTestPhaseOne extends WorkFlow {

    PatternsTestPhaseOne(ArcContext context) {
        super("Pattern 4 Test Phase One", "Phase One", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {

    }

    void executeStudy() {
        context.open()
        results.rowKeySet().each { id ->
            def map = results.row(id)
            String key = map[VerificationStudyConstants.KEY]
            String sysName = key.split(/:/)[0]
            String projName = key.split(/:/)[1]
            String projVersion = projName.split(/-/)[1]
            System sys = System.findFirst("sysKey = ?", sysName)
            if (!sys) {
                sys = System.builder()
                        .name(sysName)
                        .key(sysName)
                        .basePath(normalizePath(map[VerificationStudyConstants.LOCATION]))
                        .create()
            }
            Project proj = Project.builder()
                    .name(projName)
                    .projKey(key)
                    .relPath("")
                    .version(projVersion)
                    .create()
            sys.addProject(proj)
        }
    }
}
