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
package dev.siliconcode.arc.experimenter.impl.reporting

import com.google.common.collect.Table
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CSVReportWriter implements ReportWriter {

    @Override
    void write(String reportFileName, List<String> columns, String measures, String findings, Table<String, String, String> data) {
        String[] headers = columns
        headers += measures.split(", ")
        headers += findings.split(", ")
        columns

        FileWriter out = new FileWriter(reportFileName)
        try {
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))
            Map<String, Map<String, String>> rows = data.rowMap()
            rows.each { String key, Map<String, String> cols ->
                String[] row = []
                headers.each {
                    row << cols[it]
                }
                printer.printRecord(row)
            }
        } catch (Exception e) {

        }
    }
}
