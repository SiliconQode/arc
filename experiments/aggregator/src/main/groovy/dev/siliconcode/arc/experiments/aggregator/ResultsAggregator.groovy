/*
 * The MIT License (MIT)
 *
 * Empirilytics Results Aggregator
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
package dev.siliconcode.arc.experiments.aggregator

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import groovy.util.logging.Log4j2
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord

@Log4j2
class ResultsAggregator {

    List<File> files = []
    List<String> data = []

    ResultsAggregator() {
    }

    void execute(String path, String output) {
        findFiles(path)
        combineResults()
        writeCombinedResults(output)
    }

    void findFiles(String path) {
        def filePattern = ~/results_\d+_\d+_\d+\.csv/
        def directory = new File(path)
        if (!directory.isDirectory()) {
            log.error "The provided directory name ${path} is NOT a directory."
            return
        }

        log.info "Searching for results files in directory ${path}..."

        directory.eachFileRecurse {
            if (filePattern.matcher(it.name).find()) {
                files << it
            }
        }
    }

    void combineResults() {
        log.info "Combining results from all files found"
        String header = files.first().readLines().first()
        data << header
        files.each {file ->
            List<String> fileLines = file.readLines()
            data += fileLines.subList(1, fileLines.size())
        }
        log.info "Results combined"
    }

    void writeCombinedResults(String file) {
        log.info "Writing combined results to: $file"
        File output = new File(file)
        if (output.exists())
            output.delete()
        output.createNewFile()
        output.text = data.join("\n")
        log.info "Finished writing combined results file"
    }
}
