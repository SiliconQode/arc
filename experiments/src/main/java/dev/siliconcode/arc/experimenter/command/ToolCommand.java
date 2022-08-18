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
package dev.siliconcode.arc.experimenter.command;

import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public abstract class ToolCommand implements Command {

    @Setter @Getter
    protected String toolName;
    @Setter
    protected String toolHome;
    @Setter
    protected String projectName;
    @Setter
    protected String reportFile;
    @Setter
    protected String sourceDirectory;
    @Setter
    protected String binaryDirectory;
    @Setter
    protected String projectBaseDirectory;
    protected ArcContext context;

    public ToolCommand(String toolName, String toolHome, String reportFile) {
        this.toolName = toolName;
        this.toolHome = toolHome;
        this.reportFile = reportFile;
    }

    public void execute(ArcContext context) {
        this.context = context;
        log.info("Executing " + getToolName() + " Analysis");

        this.projectBaseDirectory = context.getProjectDirectory();
        this.sourceDirectory = CommandUtils.normalizePathString(projectBaseDirectory) + context.getProject().getSrcPath();
        this.binaryDirectory = CommandUtils.normalizePathString(projectBaseDirectory) + context.getProject().getBinaryPath();
        this.projectName = context.getProject().getName();

        if (isRequirementsMet()) {
            log.info("Constructing command line");
            CommandLine cmdLine = buildCommandLine();
            log.info("Executing command");
            log.info("Command Line: " + cmdLine.toString());
            executeCmdLine(cmdLine, getExpectedExitValue());
            log.info("Updating collector");
            updateCollector();
        } else {
            log.error(String.format("Requirements not met for %s", toolName));
        }

        log.info("Finished Executing " + getToolName() + " Analysis");
    }

    public abstract boolean isRequirementsMet();

    public abstract CommandLine buildCommandLine();

    public abstract void updateCollector();

    public abstract int getExpectedExitValue();

    private void executeCmdLine(CommandLine cmdLine, int expectedExitValue) {
        log.info("Executing Command");
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(Paths.get(projectBaseDirectory).toFile());
        executor.setExitValue(expectedExitValue);
        try {
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            int exitval = resultHandler.getExitValue();
            log.info("Exit Value: " + exitval);
        } catch (IOException | InterruptedException e) {
            log.atError().withThrowable(e).log(e.getMessage());
        }
    }
}
