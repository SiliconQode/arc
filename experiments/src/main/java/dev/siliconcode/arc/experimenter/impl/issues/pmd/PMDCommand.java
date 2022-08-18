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
package dev.siliconcode.arc.experimenter.impl.issues.pmd;

import dev.siliconcode.arc.experimenter.command.CommandUtils;
import dev.siliconcode.arc.experimenter.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PMDCommand extends ToolCommand {

    PMDTool owner;

    @Builder(buildMethodName = "create")
    public PMDCommand(PMDTool owner, String toolHome, String reportFile) {
        super(PMDConstants.PMD_CMD_NAME, toolHome, reportFile);
        this.owner = owner;
    }

    @Override
    public boolean isRequirementsMet() {
        return CommandUtils.verifyFileExists(toolHome, "bin/run.sh");
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine(CommandUtils.normalizePathString(toolHome) + "bin/run.sh")
            .addArgument("pmd")
            .addArgument("-d")
            .addArgument(Paths.get(sourceDirectory).toAbsolutePath().toString())
            .addArgument("-f")
            .addArgument("xml")
            .addArgument("-r")
            .addArgument(Paths.get(reportFile).toAbsolutePath().toString())
            .addArgument("-R")
            .addArgument("category/java/bestpractices.xml,category/java/codestyle.xml,category/java/design.xml,category/java/documentation.xml,category/java/errorprone.xml,category/java/multithreading.xml,category/java/performance.xml")
            .addArgument("-version")
            .addArgument("1.8")
            .addArgument("-language")
            .addArgument("java");
    }

    @Override
    public void updateCollector() {
        owner.collector.setResultsFile(reportFile);
    }

    @Override
    public int getExpectedExitValue() {
        return PMDConstants.PMD_CMD_EXIT_VALUE;
    }
}
