/*
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
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
package dev.siliconcode.arc.parsers.java

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.FileType
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager
import groovy.util.logging.Log4j2
import org.apache.logging.log4j.Logger
import dev.siliconcode.arc.parsers.*

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaArtifactIdentifier implements ArtifactIdentifier {

    final List<String> buildFileTypes = ImmutableList.of("pom.xml", "build.gradle")
    Project project

    List<File> files = Lists.newArrayList()
    int binCount = 0
    DBCredentials credentials

    JavaArtifactIdentifier(DBCredentials credentials) {
        this.credentials = credentials
    }

    @Override
    void identify(String root) {
        Path rootPath = Paths.get(root)
        log.info "Root Path: $rootPath"
        log.info "Absolute Path: ${rootPath.toAbsolutePath()}"
        try {
            Files.walkFileTree(rootPath.toAbsolutePath(), new JavaFileVisitor())
        } catch (IOException e) {
            log.warn "Could not walk the file tree"
        }
        log.info "Binary File Count: $binCount"
        log.info "File Found: ${files.size()}"
    }

    @Override
    void setProj(Project proj) {
        this.project = proj
    }

    private class JavaFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            FileType type = null
            if (file.toString().endsWith(".java")) {
                type = FileType.SOURCE
                if (file.getParent().toString().toLowerCase().contains("test"))
                    type = FileType.TEST
            } else if (file.getFileName().toString().endsWith(".class")) {
                type = FileType.BINARY
                binCount++
            } else if (buildFileTypes.contains(file.getFileName().toString())) {
                type = FileType.BUILD
            } else if (file.endsWith(".jar") || file.endsWith(".war") || file.endsWith(".ear")) {
                type = FileType.MODULE
            } else if (file.getFileName().toString().equalsIgnoreCase("readme.md") ||
                    file.getFileName().toString().startsWith("LICENSE") ||
                    file.getFileName().toString().startsWith("CHANGELOG")) {
                type = FileType.DOC
            }
            if (type != null) {
                DBManager.instance.open(credentials)
                if (!project.getFileByName(file.toString())) {
                    File f = File.builder()
                            .fileKey(project.getProjectKey() + ":" + file.toString())
                            .name(file.toString())
                            .relPath(file.getFileName().toString())
                            /*.language("Java")*/
                            .type(type)
                            .create()
                    files.add(f)
                    project.addFile(f)
                    if (type == FileType.SOURCE) {
                        int end = file.toFile().readLines().size()
                        int start = 1
                        f.setStart(start)
                        f.setEnd(end)
                    }
                } else {
                    File f = project.getFileByName(file.toString())
                    files.add(f)
                }
                log.atInfo().log("Identified " + type + " file: " + file.getFileName())
                DBManager.instance.close()
            }

            return FileVisitResult.CONTINUE
        }

        @Override
        FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            log.atInfo().log("Checking Path: " + dir.toString())

            return FileVisitResult.CONTINUE
        }

        @Override
        FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (dir.getFileName().toString().equals(".gradle") || dir.getFileName().toString().equals(".git") ||
                    dir.getFileName().toString().equals(".m2") || dir.getFileName().toString().equals(".mvn") ||
                    dir.getFileName().toString().equals(".idea") || dir.getFileName().toString().equals("gradle"))
                return FileVisitResult.SKIP_SUBTREE

            return FileVisitResult.CONTINUE
        }
    }
}
