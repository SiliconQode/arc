/*
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
package dev.siliconcode.arc.experimenter.app.runner

import com.google.common.collect.Table
import dev.siliconcode.arc.experimenter.ArcContext
import groovy.util.logging.Log4j2
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ResultsWriter {

    String[] headers = []
    String[] measures = []
    String file
    ArcContext context

    void initialize(List<String> headers, List<String> measures, String file, ArcContext context) {
        this.headers = headers
        this.measures = measures
        this.file = file
        this.context = context
    }

    void writeResults(Table<String, String, String> table) {
        combineHeadersAndMeasures()
        produceResults(table)
    }

    void produceResults(Table<String, String, String> table) {
        FileWriter out = new FileWriter(file)
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
            table.rowKeySet().each {id ->
                Map<String, String> row = table.row(id)
                List<String> rowValues = []
                headers.each {
                    if (row.containsKey(it)) rowValues << row.get(it)
                    else rowValues << ""
                }
                rowValues.remove(0)
                rowValues.add(0, id.toString())
                printer.printRecord(rowValues)
            }
        }
    }

    void combineHeadersAndMeasures() {
//        headers = ExperimentConstants.HEADERS
        measures.each {
            headers += it.split(":")[1]
        }
    }
}
