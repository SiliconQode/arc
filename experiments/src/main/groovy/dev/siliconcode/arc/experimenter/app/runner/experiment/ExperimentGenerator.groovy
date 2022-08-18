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
package dev.siliconcode.arc.experimenter.app.runner.experiment

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import groovy.util.logging.Log4j2

@Log4j2
class ExperimentGenerator {

    void initialize() {
    }

    Table<String, String, String> generate(List<String> patternTypes, List<String> grimeTypes, int severityLevels) {
        Table<String, String, String> table = HashBasedTable.create()

        log.info("Generating an Experiment for ${patternTypes.size() * grimeTypes.size() * 7} experimental units")
        int id = 0
        patternTypes.each { patternType ->
            grimeTypes.each {grimeType ->
                (0..(severityLevels - 1)).each {severity ->
                    table.put("$id", ExperimentConstants.PatternType, patternType)
                    table.put("$id", ExperimentConstants.GrimeType, grimeType)
                    table.put("$id", ExperimentConstants.GrimeSeverity, "$severity")
                    id++
                }
            }
        }

        return table
    }
}
