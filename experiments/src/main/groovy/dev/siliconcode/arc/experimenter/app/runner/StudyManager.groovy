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
package dev.siliconcode.arc.experimenter.app.runner

import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.app.runner.casestudy.CaseStudyRunner
import dev.siliconcode.arc.experimenter.app.runner.experiment.ExperimentRunner
import dev.siliconcode.arc.experimenter.app.runner.pattern4test.PatternsTestRunner
import dev.siliconcode.arc.experimenter.app.runner.sigcalibrate.SigCalibrationRunner
import dev.siliconcode.arc.experimenter.app.runner.sigrating.SigRatingRunner
import dev.siliconcode.arc.experimenter.app.runner.test.TestExperimentRunner
import dev.siliconcode.arc.experimenter.app.runner.verification.VerificationStudyRunner

class StudyManager {

    final Map<String, EmpiricalStudy> studies

    StudyManager(ArcContext context) {
        studies = [
                //"test" : new TestEmpiricalStudy(context),
                "experiment" : new ExperimentRunner(context),
                "case-study" : new CaseStudyRunner(context),
                "calibration" : new SigCalibrationRunner(context),
                "rating-test" : new SigRatingRunner(context),
                "verification" : new VerificationStudyRunner(context),
                "test" : new TestExperimentRunner(context),
                "p4test" : new PatternsTestRunner(context)
        ]
    }
}
