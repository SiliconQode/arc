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
package dev.siliconcode.arc.experimenter.impl.issues.findbugs;

import dev.siliconcode.arc.experimenter.command.ToolCommand;
import dev.siliconcode.arc.experimenter.command.CommandUtils;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindBugsCommand extends ToolCommand {

    FindBugsTool owner;

    @Builder(buildMethodName = "create")
    public FindBugsCommand(FindBugsTool owner, String toolHome, String reportFile) {
        super(FindBugsConstants.FB_CMD_NAME, toolHome, reportFile);
        this.owner = owner;
    }

    @Override
    public boolean isRequirementsMet() {
        return CommandUtils.verifyFileExists(toolHome, "lib/spotbugs.jar");
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine("/usr/bin/java")
            .addArgument("-jar")
            .addArgument(CommandUtils.normalizePathString(toolHome) + "lib/spotbugs.jar")
            .addArgument("-projectName")
            .addArgument(projectName)
            .addArgument("-effort:default")
            .addArgument("-xml")
            .addArgument("-output")
            .addArgument(Paths.get(reportFile).toAbsolutePath().toString())
            .addArgument(Paths.get(binaryDirectory).toAbsolutePath().toString());
    }

    @Override
    public void updateCollector() {
        owner.collector.setResultsFile(reportFile);
    }

    @Override
    public int getExpectedExitValue() {
        return FindBugsConstants.FB_CMD_EXIT_VALUE;
    }
}
