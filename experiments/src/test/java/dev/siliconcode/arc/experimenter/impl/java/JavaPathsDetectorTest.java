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
package dev.siliconcode.arc.experimenter.impl.java;

import dev.siliconcode.arc.datamodel.System;
import dev.siliconcode.arc.datamodel.*;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.ArcProperties;
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.FindBugsProperties;
import dev.siliconcode.arc.experimenter.impl.pattern4.Pattern4Properties;
import dev.siliconcode.arc.experimenter.impl.issues.pmd.PMDProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class JavaPathsDetectorTest extends DBSpec {

    ArcContext context;
    Project proj;
    JavaPathsDetector fixture;

    @BeforeEach
    public void setup() {
        context = new ArcContext(log);
        fixture = new JavaPathsDetector(context);

        String base = "/home/git/msusel/msusel-patterns-experimenter/data/";

        // Load Configuration
        context.addArcProperty(ArcProperties.ARC_HOME_DIR, ".");
        context.addArcProperty(FindBugsProperties.FB_TOOL_HOME, "/home/grifisaa/bin/detectors/spotbugs-4.0.3/");
        context.addArcProperty(Pattern4Properties.P4_TOOL_HOME, "/home/grifisaa/bin/detectors/pattern4/");
        context.addArcProperty(PMDProperties.PMD_TOOL_HOME, "/home/grifisaa/bin/detectors/pmd-bin-6.24.0/");
        context.addArcProperty(ArcProperties.TOOL_OUTPUT_DIR, "data/tool_output/");
        context.addArcProperty(ArcProperties.BASE_DIRECTORY, base);

        // construct Project elements
        System sys = System.builder().name("test_proj").key("test_proj").basePath(base).create();
        proj = Project.builder().name("test_proj").projKey("test_proj").relPath("test_proj").version("1.0").create();

        sys.addProject(proj);
        proj.updateKeys();

        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/main/java/refactoring_guru/adapter/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/main/java/refactoring_guru/bridge/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/main/java/refactoring_guru/iterator/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());

        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/test/java/refactoring_guru/adapter/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/test/java/refactoring_guru/bridge/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/src/test/java/refactoring_guru/iterator/example/Demo.java").fileKey("").relPath("").type(FileType.SOURCE).start(1).end(2).create());

        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/build/classes/java/main/refactoring_guru/adapter/example/Demo.java").fileKey("").relPath("").type(FileType.BINARY).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/build/classes/java/main/refactoring_guru/bridge/example/Demo.java").fileKey("").relPath("").type(FileType.BINARY).start(1).end(2).create());
        proj.addFile(File.builder().name("/home/git/msusel/msusel-patterns-experimenter/data/test_proj/build/classes/java/main/refactoring_guru/iterator/example/Demo.java").fileKey("").relPath("").type(FileType.BINARY).start(1).end(2).create());

        proj.addNamespace(Namespace.builder().name("refactoring_guru").nsKey("refactoring_guru").relPath("refactoring_guru").create());

        context.setProject(proj);
        proj.refresh();
    }

    @AfterEach
    public void teardown() {}

    @Test
    public void testBuildGraph_Source() {
        //given

        // when
        Pair<List<String>, List<String>> pair = fixture.buildGraph(FileType.SOURCE);

        // then
        assertEquals(1, pair.getLeft().size());
        assertTrue(pair.getLeft().contains("src/main/java"));

        assertEquals(1, pair.getRight().size());
        assertTrue(pair.getRight().contains("src/test/java"));

        assertEquals(3, proj.getFilesByType(FileType.TEST).size());
    }

    @Test
    public void testBuildGraph_Binary() {
        //given

        // when
        Pair<List<String>, List<String>> pair = fixture.buildGraph(FileType.BINARY);

        // then
        assertEquals(1, pair.getLeft().size());
        assertTrue(pair.getLeft().contains("build/classes/java/main"));

        assertTrue(pair.getRight().isEmpty());
    }

}
