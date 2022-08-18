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
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.MeasureTable
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigMainConstants
import dev.siliconcode.arc.experimenter.impl.quality.td.TechDebtConstants
import groovy.util.logging.Log4j2

@Log4j2
class VerificationStudyPhaseFour extends WorkFlow {

    private static final String STUDY_NAME = "Verification Study Phase Four"
    private static final String STUDY_DESC = "Phase Four"

    Table<String, String, String> results
    Command metrics
    Command nugrohoTD
    Command sigmain
    Command sigrating

    VerificationStudyPhaseFour(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        metrics   = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
        nugrohoTD = getContext().getRegisteredCommand(TechDebtConstants.NUGROHO_CMD_NAME)
        sigmain   = getContext().getRegisteredCommand(SigMainConstants.SIGMAIN_CMD_NAME)
        sigrating = getContext().getRegisteredCommand(SigMainConstants.SIGRATE_CMD_NAME)
    }

    @Override
    void executeStudy() {
        MeasureTable.getInstance().reset()
        results.rowKeySet().each {id ->
            runTools(results.row(id).get(VerificationStudyConstants.BASE_KEY))
            runTools(results.row(id).get(VerificationStudyConstants.INFECTED_KEY))
            runTools(results.row(id).get(VerificationStudyConstants.INJECTED_KEY))
        }
    }

    void runTools(String projKey) {
        log.info "Project Key: $projKey"
        context.open()
        context.setProject(Project.findFirst("projKey = ?", projKey))
        context.close()

        metrics.execute(context)
        sigmain.execute(context)
        sigrating.execute(context)
        nugrohoTD.execute(context)
    }
}
