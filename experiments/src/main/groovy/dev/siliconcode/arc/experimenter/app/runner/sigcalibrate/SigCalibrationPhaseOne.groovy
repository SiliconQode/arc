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
package com.empirilytics.arc.experimenter.app.runner.sigcalibrate

import com.empirilytics.arc.datamodel.Project
import com.empirilytics.arc.datamodel.System
import com.empirilytics.arc.experimenter.ArcContext
import com.empirilytics.arc.experimenter.Command
import com.empirilytics.arc.experimenter.app.runner.WorkFlow
import com.empirilytics.arc.experimenter.impl.ghsearch.GitHubSearchConstants
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class SigCalibrationPhaseOne extends WorkFlow {

//    Command ghSearch

    SigCalibrationPhaseOne(ArcContext context) {
        super("Sig Calibration Phase One", "Sig Maintainability Model Calibration - Phase One", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
//        ghSearch  = context.getRegisteredCommand(GitHubSearchConstants.GHSEARCH_CMD_NAME)
    }

    void executeStudy() {
        context.open()
        results.rowKeySet().each {id ->
            def map = results.row(id)
            String key = map[SigCalibrateConstants.KEY]
            String sysName = key.split(/:/)[0]
            String projName = key.split(/:/)[1]
            String projVersion = projName.split(/-/)[1]
            System sys = System.builder()
                    .name(sysName)
                    .key(sysName)
                    .basePath(normalizePath(map[SigCalibrateConstants.LOCATION]))
                    .create()
            Project proj = Project.builder()
                    .name(projName)
                    .projKey(key)
                    .relPath(projVersion)
                    .version(projVersion)
                    .create()
            sys.addProject(proj)
        }
        context.close()
    }

    void runTools() {

    }
}
