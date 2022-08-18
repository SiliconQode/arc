/*
 * The MIT License (MIT)
 *
 * Empirilytics Parsers
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.parsers

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableList
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.FileType
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager
import groovy.util.logging.Log4j2
import groovyx.gpars.GParsExecutorsPool
import org.apache.commons.lang3.tuple.Pair

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

@Log4j2
class ParallelJavaArtifactIdentifier implements ArtifactIdentifier {

    final List<String> buildFileTypes = ImmutableList.of("pom.xml", "build.gradle")

    List<Path> directories = []

    Project project

    Map<String, Pair<String, FileType>> fileTable = Maps.newHashMap()
    Set<String> checkedDir = Sets.newHashSet()

    int binCount = 0
    DBCredentials credentials

    ParallelJavaArtifactIdentifier(DBCredentials credentials) {
        this.credentials = credentials
    }

    @Override
    void identify(String root) {
        DBManager.instance.open(credentials)
        boolean alreadyIdentified = !project.getAllTypes().isEmpty()
        DBManager.instance.close()

        if (alreadyIdentified)
            return

        buildCheckedDirectorySet(project)
        walkTree(root)
        createFiles()
    }

    private void walkTree(String root) {
        Path rootPath = Paths.get(root)
        log.info "Root Path: $rootPath"
        log.info "Absolute Path: ${rootPath.toAbsolutePath()}"
        try {
            Files.walkFileTree(rootPath.toAbsolutePath(), new DirectoryVisitor())
            GParsExecutorsPool.withPool(8) {
                directories.eachParallel { Path dir ->
                    log.info "Checking Path: " + dir.toString()
                    Files.walkFileTree(dir.toAbsolutePath(), Sets.newHashSet(), 1, new JavaFileVisitor())
                }
            }
        } catch (IOException e) {
            log.warn "Could not walk the file tree"
        }
        log.info "Binary File Count: $binCount"
    }

    @Override
    void setProj(Project proj) {
        this.project = proj
    }

    private void buildCheckedDirectorySet(Project proj) {
        DBManager.instance.open(credentials)
        proj.getFiles().each { File file ->
            Path p = Paths.get(file.getName())
            checkedDir.add(p.getParent().toAbsolutePath().toString())
        }
        DBManager.instance.close()
    }

    private void createFiles() {
        DBManager.instance.open(credentials)
        DBManager.instance.openTransaction()
        fileTable.each { String name, Pair<String, FileType> pair ->
            String relPath = pair.getLeft()
            FileType type = pair.getRight()

            if (!project.getFileByName(name)) {
                File f = File.builder()
                        .fileKey(project.getProjectKey() + ":" + name)
                        .name(name)
                        .relPath(relPath)
                /*.language("Java")*/
                        .type(type)
                        .create()
                project.addFile(f)
                if (type == FileType.SOURCE) {
                    int end = new java.io.File(name).readLines().size()
                    int start = 1
                    f.setStart(start)
                    f.setEnd(end)
                }
            }
            log.info "Identified $type file: $name"
        }
        DBManager.instance.commitTransaction()
        DBManager.instance.close()
    }

    private class DirectoryVisitor extends SimpleFileVisitor<Path> {

        @Override
        FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE
        }

        @Override
        FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            boolean checked = checkedDir.contains(dir.toAbsolutePath().toString())
            if (!checked)
                directories << dir
            return FileVisitResult.CONTINUE
        }

        @Override
        FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            log.info "Checking Directory: ${dir.toString()}"
            if (dir.getFileName().toString().equals(".gradle") || dir.getFileName().toString().equals(".git") ||
                    dir.getFileName().toString().equals(".m2") || dir.getFileName().toString().equals(".mvn") ||
                    dir.getFileName().toString().equals(".idea") || dir.getFileName().toString().equals("gradle") ||
                    dir.getFileName().toString().equals(".svn"))
                return FileVisitResult.SKIP_SUBTREE

            return FileVisitResult.CONTINUE
        }
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
                String name = file.toAbsolutePath().toString()
                String relPath = file.getFileName().toString()
                fileTable.put(name, Pair.of(relPath, type))
            }

            return FileVisitResult.CONTINUE
        }

        @Override
        FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.TERMINATE
        }

        @Override
        FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE
        }
    }
}
