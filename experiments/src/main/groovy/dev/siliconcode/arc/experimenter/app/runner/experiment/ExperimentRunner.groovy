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

import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.app.runner.EmpiricalStudy
import dev.siliconcode.arc.experimenter.app.runner.StudyConfigReader
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ExperimentRunner extends EmpiricalStudy {

    private static final String STUDY_NAME = "Grime Experiments"
    private static final String STUDY_DESC = ""

    ExperimentRunner(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context, new StudyConfigReader(getConfigFileName(), getConfigHeaders()))

        this.phases = [
                new PatternGeneratorExecutor(context),
                new ExperimentPhaseOne(context),
                new SourceInjectorExecutor(context),
                new ExperimentPhaseOneInjected(context),
                new ExperimentPhaseTwo(context)
        ]

        this.headers = ExperimentConstants.HEADERS
        this.keyHeaders = [ExperimentConstants.Key1, ExperimentConstants.Key2]
        this.identifier = ExperimentConstants.ID
    }

    def static getConfigHeaders() {
        return [
                ExperimentConstants.PatternType,
                ExperimentConstants.GrimeType,
                ExperimentConstants.GrimeSeverity
        ]
    }

    def static getConfigFileName() {
        "experiment.conf"
    }
}
