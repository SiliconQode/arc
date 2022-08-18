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

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import groovy.util.logging.Log4j2
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class StudyConfigReader {

    int index = 0
    List<File> files = []
    String filePattern
    List<String> headers

    StudyConfigReader(String filePattern, List<String> headers) {
        this.filePattern = filePattern
        this.headers = headers
    }

    def findConfigs(String path) {
        def filePattern = ~/${filePattern}/
        def directory = new File(path)
        if (!directory.isDirectory()) {
            log.error "The provided directory name ${path} is not a directory."
            return
        }

        log.info "Searching for experimental configurations in directory ${path}..."

        directory.eachFileRecurse {
            if (filePattern.matcher(it.name).find()) {
                files << it
            }
        }
    }

    def initialize() {
        files = [new File(filePattern)]
    }

    def loadNext() {
        Table<String, String, String> table = HashBasedTable.create()
        File file = files[index++]
        Reader reader = new FileReader(file)
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader)
        records.each {record ->
            if (record.size() < headers.size() + 1)
                log.error "Record is malformed"
            int ndx = 1
            headers.each {header ->
                table.put(record.get(0), header, record.get(ndx))
                ndx++
            }
        }
        reader.close()

        return table
    }
}
