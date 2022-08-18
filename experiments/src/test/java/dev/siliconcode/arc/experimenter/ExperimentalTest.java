/**
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
package dev.siliconcode.arc.experimenter;

import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.datamodel.System;
import dev.siliconcode.arc.experimenter.app.runner.ToolsLoader;
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow;
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.FindBugsProperties;
import dev.siliconcode.arc.experimenter.impl.pattern4.Pattern4Properties;
import dev.siliconcode.arc.experimenter.impl.issues.pmd.PMDProperties;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Log4j2
public class ExperimentalTest {

    ArcContext context;

    @BeforeEach
    public void setup() {
        context = new ArcContext(log);
        context.setLanguage("java");

        String base = "/home/git/msusel/msusel-patterns-experimenter/data/huston";

        // Load Configuration
        context.addArcProperty(ArcProperties.ARC_HOME_DIR, ".");
        context.addArcProperty(FindBugsProperties.FB_TOOL_HOME, "/home/grifisaa/bin/detectors/spotbugs-4.0.3/");
        context.addArcProperty(Pattern4Properties.P4_TOOL_HOME, "/home/grifisaa/bin/detectors/pattern4/");
        context.addArcProperty(PMDProperties.PMD_TOOL_HOME, "/home/grifisaa/bin/detectors/pmd-bin-6.24.0/");
        context.addArcProperty(ArcProperties.TOOL_OUTPUT_DIR, "/home/git/msusel/msusel-patterns-experimenter/data/tool_output/");
        context.addArcProperty(ArcProperties.BASE_DIRECTORY, base);
        context.addArcProperty("arc.db.driver", "org.sqlite.JDBC");
        context.addArcProperty("arc.db.url", "jdbc:sqlite:data/test.db");
        context.addArcProperty("arc.db.type", "sqlite");
        context.addArcProperty("arc.db.user", "arc");
        context.addArcProperty("arc.db.pass", "arc");

        ToolsLoader toolLoader = new ToolsLoader();
        toolLoader.loadTools(context);

        // construct Project elements
        context.open();
        System sys;
        if (System.findFirst("name = ?", "huston") == null)
            sys = System.builder().name("huston").key("huston").basePath(base).create();
        else
            sys = System.findFirst("name = ?", "huston");

        Project proj;
        if (!sys.hasProjectWithName("huston"))
            proj = Project.builder().name("huston").projKey("huston").relPath("").version("1.0").create();
        else
            proj = sys.getProjectByName("huston");

        sys.addProject(proj);
        proj.updateKeys();
        context.setProject(proj);
        context.close();
    }

    @Test
    public void test() {
        context.open();
        context.getProject().setSrcPath(new String[]{"src/"});
        context.getProject().setBinPath(new String[]{"bin/"});
        context.close();

        WorkFlow empiricalStudy = new TestStudy(context);
//        empiricalStudy.execute();
    }

    @Test
    public void testFindBugs() {
        context.open();
        context.getProject().setSrcPath(new String[]{"src/"});
        context.getProject().setBinPath(new String[]{"bin/"});
        context.close();

        WorkFlow empiricalStudy = new FindBugOnly(context);
//        empiricalStudy.execute();
    }
}
