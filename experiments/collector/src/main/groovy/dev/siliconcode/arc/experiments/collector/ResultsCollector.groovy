/*
 * The MIT License (MIT)
 *
 * Empirilytics Results Collector
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
package dev.siliconcode.arc.experiments.collector

import groovy.util.logging.Log4j2

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class ResultsCollector {

    List<File> files = []
    List<File> results = []
    List<File> failures = []

    ResultsCollector() {}

    void execute(String path, String output, String collDir, String targetValue) {
        findFiles(path)
        collectResults(targetValue)
        writeFailuresList(output)
        copyResultsToCollDir(collDir)
    }

    void findFiles(String path) {
        def fileName = "status.txt"
        def directory = new File(path)
        if (!directory.isDirectory()) {
            log.error "The provided directory name ${path} is NOT a directory."
            return
        }
        log.info "Searching for status files in directory ${path}..."

        directory.eachFileRecurse {
            if (it.name == fileName) {
                files << it
            }
        }
    }

    void collectResults(target) {
        files.each {
            String val = it.readLines()[0]
            File result = findResult(it)
            if (val == target) {
                results << result
            } else {
                failures << result
            }
        }
    }

    void writeFailuresList(String path) {
        File f = new File(path)
        f.getParentFile().mkdirs()
        f.text = ""
        failures.each {
            f.text += it.getName().split(/\./)[0]
            f.text += "\n"
        }
    }

    void copyResultsToCollDir(String collDir) {
        Path dir = Paths.get(collDir)
        dir.toFile().mkdirs()

        results.each {
            Path source = it.toPath()
            Path target = new File(dir.toFile(), it.getName()).toPath()
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    File findResult(File file) {
        File parent = file.getParentFile()
        File result = null
        parent.eachFileMatch(~/results(_\d+)+\.csv/) { f ->
            result = f
        }

        return result
    }
}
