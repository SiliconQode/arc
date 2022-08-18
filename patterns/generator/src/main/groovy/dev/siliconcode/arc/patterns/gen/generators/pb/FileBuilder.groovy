/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen.generators.pb

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.FileType
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project

import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FileBuilder extends AbstractBuilder {

    def create() {
        if (!params.parent)
            throw new IllegalArgumentException("createFile: parent cannot be null")
        if (!params.typeName)
            throw new IllegalArgumentException("createFile: typeName cannot be empty or null")

        String srcPath = ctx.srcPath
        String srcExt = ctx.srcExt
        String path = "${((Namespace) params.parent).getFullPath(FileType.SOURCE, 0)}${java.io.File.separator}${params.typeName}${srcExt}"

        File file = File.builder()
                .name(Paths.get(path).toAbsolutePath().toString())
                .fileKey(((Namespace) params.parent).getParentProject().projectKey + ":" + Paths.get(path).toAbsolutePath().toString())
                .relPath("${params.typeName}${srcExt}")
                .type(FileType.SOURCE)
                .create()
        file.save()

        ((Namespace) params.parent)?.getParentProject()?.addFile(file)
        ((Namespace) params.parent)?.addFile(file)
        file
    }
}
