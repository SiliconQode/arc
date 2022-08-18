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

import dev.siliconcode.arc.experimenter.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class GradleCommand extends ToolCommand {

    @Builder(buildMethodName = "create")
    public GradleCommand(String toolHome) {
        super(GradleConstants.GRADLE_CMD_NAME, toolHome, null);
    }

    @Override
    public boolean isRequirementsMet() {
        Path bg = Paths.get(projectBaseDirectory, "build.gradle");
        Path gw = Paths.get(projectBaseDirectory, "gradlew");
        boolean executable = Files.isExecutable(gw);
        return Files.exists(bg) && Files.exists(gw) && executable;
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine(toolHome)
                .addArgument("clean")
                .addArgument("compileJava");
    }

    @Override
    public void updateCollector() {

    }

    @Override
    public int getExpectedExitValue() {
        return GradleConstants.GRADLE_CMD_EXIT_VALUE;
    }

    @Override
    public String getToolName() {
        return GradleConstants.GRADLE_CMD_NAME;
    }
}
