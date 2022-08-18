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

import dev.siliconcode.arc.datamodel.File;
import dev.siliconcode.arc.datamodel.FileType;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

import java.util.List;

public class BasicJavaCommand extends ToolCommand {

    ArcContext context;

    @Builder(buildMethodName = "create")
    public BasicJavaCommand() {
        super("JavaC", "", "");
    }

    @Override
    public boolean isRequirementsMet() {
        return true;
    }

    @Override
    public CommandLine buildCommandLine() {
        CommandLine cmdLine = new CommandLine("javac");
        List<File> files = context.getProject().getFilesByType(FileType.SOURCE);
        files.forEach(file -> cmdLine.addArgument(file.getName()));

        return cmdLine;
    }

    @Override
    public void updateCollector() {

    }

    @Override
    public int getExpectedExitValue() {
        return 0;
    }
}
