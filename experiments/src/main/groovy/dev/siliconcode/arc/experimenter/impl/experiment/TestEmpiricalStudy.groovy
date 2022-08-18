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
package dev.siliconcode.arc.experimenter.impl.experiment

import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.Collector
import dev.siliconcode.arc.experimenter.Command
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.experimenter.impl.ghsearch.GitHubSearchConstants
import dev.siliconcode.arc.experimenter.impl.git.GitConstants
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.FindBugsConstants
import dev.siliconcode.arc.experimenter.impl.issues.grime.GrimeConstants
import dev.siliconcode.arc.experimenter.impl.issues.pmd.PMDConstants
import dev.siliconcode.arc.experimenter.impl.java.JavaConstants
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.experimenter.impl.pattern4.Pattern4Constants
import dev.siliconcode.arc.experimenter.impl.patterns.ArcPatternConstants
import dev.siliconcode.arc.experimenter.impl.quality.qmood.QMoodConstants
import dev.siliconcode.arc.experimenter.impl.quality.quamoco.QuamocoConstants
import dev.siliconcode.arc.experimenter.impl.quality.td.TechDebtConstants
import dev.siliconcode.arc.experimenter.impl.reporting.CSVReportWriter
import dev.siliconcode.arc.experimenter.impl.reporting.Report
import groovy.util.logging.Log4j2

@Log4j2
class TestEmpiricalStudy extends WorkFlow {

    TestEmpiricalStudy(ArcContext context) {
        super("Test", "A Test Empirical Study", context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
//        workflow = Workflow.builder()
//                .context(context)
//                .name("Test Workflow")
//                .phase(Phase.builder()
//                        .name("Project Discovery")
//                        .context(context)
//                        .command(GitHubSearchConstants.GHSEARCH_CMD_NAME)
//                        .command(GitConstants.GIT_CMD_NAME)
//                        .command(JavaConstants.JAVA_TOOL_CMD_NAME)
//                        .command(GradleConstants.GRADLE_CMD_NAME)
//                        .create())
//                .phase(Phase.builder()
//                        .name("Primary Analyses")
//                        .context(context)
//                        .command(FindBugsConstants.FB_CMD_NAME)
//                        .command(PMDConstants.PMD_CMD_NAME)
//                        .command(Pattern4Constants.PATTERN4_CMD_NAME)
//                        .command(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
//                        .command(ArcPatternConstants.PATTERN_SIZE_CMD_NAME)
//                        .command(GrimeConstants.GRIME_DETECT_CMD_NAME)
//                        .command(MetricsConstants.METRICS_CMD_NAME)
//                        .create())
//                .phase(Phase.builder()
//                        .name("Quality Analyses")
//                        .context(context)
//                        .command(TechDebtConstants.TD_CMD_NAME)
//                        .command(QMoodConstants.QMOOD_CMD_NAME)
//                        .command(QuamocoConstants.QUAMOCO_CMD_NAME)
//                        .create())
//                .create()
    }

    void executeStudy() {
        Command ghSearch  = context.getRegisteredCommand(GitHubSearchConstants.GHSEARCH_CMD_NAME)
        Command git       = context.getRegisteredCommand(GitConstants.GIT_CMD_NAME)
        Command java      = context.getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        Command jdi       = context.getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
        Command build     = context.getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME)
        Command findbugs  = context.getRegisteredCommand(FindBugsConstants.FB_CMD_NAME)
        Command pmd       = context.getRegisteredCommand(PMDConstants.PMD_CMD_NAME)
        Command pattern4  = context.getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME)
        Command coalesce  = context.getRegisteredCommand(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
        Command pSize     = context.getRegisteredCommand(ArcPatternConstants.PATTERN_SIZE_CMD_NAME)
        Command grime     = context.getRegisteredCommand(GrimeConstants.GRIME_DETECT_CMD_NAME)
        Command metrics   = context.getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
        Command techdebt  = context.getRegisteredCommand(TechDebtConstants.CAST_CMD_NAME)
        Command qmood     = context.getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME)
        Command quamoco   = context.getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME)

        Collector fbColl  = context.getRegisteredCollector(FindBugsConstants.FB_COLL_NAME)
        Collector pmdColl = context.getRegisteredCollector(PMDConstants.PMD_COLL_NAME)

        ghSearch.execute(context)
        System.findAll().each { sys ->
            context.project = (sys as System).getProjects().first()
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.GIT))) git.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.BUILD))) build.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.JAI))) java.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.JDI))) jdi.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.FB))) findbugs.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.FB))) fbColl.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PMD))) pmd.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PMD))) pmdColl.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.P4))) pattern4.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PC))) coalesce.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PS))) pSize.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.GRIME))) grime.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.METRICS))) metrics.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.TD))) techdebt.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.QMOOD))) qmood.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.QUAMOCO))) quamoco.execute(context)
        }
    }
}
