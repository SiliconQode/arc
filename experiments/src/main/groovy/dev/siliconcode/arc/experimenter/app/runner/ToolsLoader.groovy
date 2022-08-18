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
import dev.siliconcode.arc.experimenter.Tool
import dev.siliconcode.arc.experimenter.impl.ghsearch.GitHubSearchTool
import dev.siliconcode.arc.experimenter.impl.git.GitTool
import dev.siliconcode.arc.experimenter.impl.injector.SoftwareInjectorTool
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.FindBugsTool
import dev.siliconcode.arc.experimenter.impl.issues.grime.GrimeTool
import dev.siliconcode.arc.experimenter.impl.issues.pmd.PMDTool
import dev.siliconcode.arc.experimenter.impl.java.JavaTool
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsTool
import dev.siliconcode.arc.experimenter.impl.patextract.PatternExtractorTool
import dev.siliconcode.arc.experimenter.impl.pattern4.Pattern4Tool
import dev.siliconcode.arc.experimenter.impl.patterngen.PatternGeneratorTool
import dev.siliconcode.arc.experimenter.impl.patterns.ArcPatternTool
import dev.siliconcode.arc.experimenter.impl.quality.qmood.QMoodTool
import dev.siliconcode.arc.experimenter.impl.quality.quamoco.QuamocoTool
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigCalibrationTool
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigMainTool
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigRatingTool
import dev.siliconcode.arc.experimenter.impl.quality.td.TechDebtTool
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ToolsLoader {

    void loadTools(ArcContext context) {
        log.info("Instantiating tools")

        Tool[] tools = [
                new FindBugsTool(context),
                new GitHubSearchTool(context),
                new GitTool(context),
                new GrimeTool(context),
                new JavaTool(context),
                new MetricsTool(context),
                new Pattern4Tool(context),
                new PMDTool(context),
                new QuamocoTool(context),
                new SoftwareInjectorTool(context),
                new TechDebtTool(context),
                new QMoodTool(context),
                new ArcPatternTool(context),
                new PatternGeneratorTool(context),
                new SigMainTool(context),
                new SigCalibrationTool(context),
                new SigRatingTool(context),
                new PatternExtractorTool(context)
        ]

        log.info("Tools instantiated now loading repos and initializing commands")
        tools.each {
            it.getRepoProvider().load()
            it.getOtherProviders()*.load()
            it.init()
        }
        log.info("Finished loading tools")
    }
}
