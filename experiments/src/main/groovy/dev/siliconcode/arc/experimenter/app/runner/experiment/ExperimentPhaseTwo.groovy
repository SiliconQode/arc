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

import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.MeasureTable
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Collector
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.FindBugsConstants
import dev.siliconcode.arc.experimenter.impl.issues.pmd.PMDConstants
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.experimenter.impl.quality.qmood.QMoodConstants
import dev.siliconcode.arc.experimenter.impl.quality.quamoco.QuamocoConstants
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigMainConstants
import dev.siliconcode.arc.experimenter.impl.quality.td.TechDebtConstants

class ExperimentPhaseTwo extends WorkFlow {

    Table<String, String, String> results
//    Command findbugs
//    Command pmd
    Command metrics
//    Command qmood
//    Command quamoco
//    Command castTD
    Command nugrohoTD
    Command sigmain
    Command sigrating
//    Collector fbColl
//    Collector pmdColl

    ExperimentPhaseTwo(ArcContext context) {
        super("Experiment Phase Two", "A Test Empirical Study", context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        getContext().addArcProperty("quamoco.models.dir", "config/quamoco/models")

//        findbugs  = getContext().getRegisteredCommand(FindBugsConstants.FB_CMD_NAME)
//        pmd       = getContext().getRegisteredCommand(PMDConstants.PMD_CMD_NAME)
        metrics   = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
//        castTD    = getContext().getRegisteredCommand(TechDebtConstants.CAST_CMD_NAME)
        nugrohoTD = getContext().getRegisteredCommand(TechDebtConstants.NUGROHO_CMD_NAME)
        sigmain   = getContext().getRegisteredCommand(SigMainConstants.SIGMAIN_CMD_NAME)
        sigrating = getContext().getRegisteredCommand(SigMainConstants.SIGRATE_CMD_NAME)
//        qmood     = getContext().getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME)
//        quamoco   = getContext().getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME)
//        fbColl    = getContext().getRegisteredCollector(FindBugsConstants.FB_COLL_NAME)
//        pmdColl   = getContext().getRegisteredCollector(PMDConstants.PMD_COLL_NAME)
    }

    void executeStudy() {
        results.rowKeySet().each { id ->
            runTools(results.get(id, ExperimentConstants.Key1))
            runTools(results.get(id, ExperimentConstants.Key2))
        }
    }

    void runTools(String projKey) {
        MeasureTable.getInstance().reset()
        getContext().open()
        getContext().setProject(Project.findFirst("projKey = ?", projKey))
        getContext().close()

        // SpotBugs
//        findbugs.execute(getContext())
//        fbColl.execute(getContext())

        // PMD
//        pmd.execute(getContext())
//        pmdColl.execute(getContext())

        // Metrics
        metrics.execute(getContext())

        // Cast TechDebt
//        castTD.execute(getContext())

        // QMood
//        qmood.execute(getContext())

        // Quamoco
//        quamoco.execute(getContext())

        // Sig Maintainability
        sigmain.execute(getContext())
        sigrating.execute(getContext())

        // Nugroho TechDebt
        nugrohoTD.execute(getContext())
    }
}
