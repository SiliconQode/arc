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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

import java.io.File;

@Log4j2
public class JavaBuildCommand extends ToolCommand {

    ToolCommand command;
    ToolCommand gradleCmd;
    ToolCommand mavenCmd;
    ToolCommand basicJavaCmd;

    @Builder(buildMethodName = "create")
    public JavaBuildCommand(String gradleHome, String mavenHome) {
        super(JavaConstants.JAVA_BUILD_CMD_NAME, null, null);

        if (System.getenv("GRADLE_HOME") != null) {
            File f = new File(System.getenv("GRADLE_HOME"));
            File f2 = new File(f, "gradle");
            gradleHome = f2.getAbsolutePath();
        }

        if (System.getenv("MAVEN_HOME") != null) {
            File f = new File(System.getenv("MAVEN_HOME"));
            File f2 = new File(f, "mvn");
            mavenHome = f2.getAbsolutePath();
        }

        gradleCmd = GradleCommand.builder().toolHome(gradleHome).create();
        mavenCmd = MavenCommand.builder().toolHome(mavenHome).create();
        basicJavaCmd = BasicJavaCommand.builder().create();
    }

    private void selectCommand() {
        String projDir = context.getProjectDirectory();
        log.info("Project Directory: " + projDir);
        File dir = new File(projDir);
        File gradle = new File(dir, "build.gradle");
        File maven = new File(dir, "pom.xml");
        if (gradle.exists()) {
            log.info("Selected Gradle for the Build");
            command = gradleCmd;
        } else if (maven.exists()) {
            log.info("Selected Maven for the Build");
            command = mavenCmd;
        } else {
            log.info("Selected Generic JavaC for the Build");
            command = basicJavaCmd;
        }
    }

    @Override
    public boolean isRequirementsMet() {
        return true;
    }

    @Override
    public CommandLine buildCommandLine() {
        selectCommand();

        command.setSourceDirectory(context.getProject().getSrcPath());
        command.setBinaryDirectory(context.getProject().getBinaryPath());
        command.setProjectBaseDirectory(context.getProjectDirectory());
        command.setProjectName(context.getProject().getName());

        return command.buildCommandLine();
    }

    @Override
    public void updateCollector() {
        command.updateCollector();
    }

    @Override
    public int getExpectedExitValue() {
        return command.getExpectedExitValue();
    }
}
