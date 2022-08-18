/*
 * The MIT License (MIT)
 *
 * Empirilytics Experiment Generator
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
package dev.siliconcode.arc.experiments.generator

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import groovy.util.logging.Log4j2

/**
 * Generates the experiments used for Grime Injection
 *
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class ExperimentGenerator {

    Config config

    ExperimentGenerator(Config config) {
        this.config = config
    }

    void generate() {
        log.info("Generating an Experiment Configurations for ${config.patternTypes.size() * config.injectionTypes.size() * config.severityLevels} experimental units" +
                " separated into ${config.replications} replication(s)")
        (1..config.replications).each {rep ->
            Table<Integer, String, String> table = HashBasedTable.create()
            int id = 0
            config.patternTypes.each { patternType ->
                config.injectionTypes.each { grimeType ->
                    (0..(config.severityLevels - 1)).each { severity ->
                        table.put(id, Constants.PatternType, patternType)
                        table.put(id, Constants.GrimeType, grimeType)
                        table.put(id, Constants.GrimeSeverity, severity.toString())
                        id++
                    }
                }
            }

            List<Integer> ids = table.rowKeySet().asList()
            ids.sort()
            Collections.shuffle(ids)

            config.configurationOrders[rep] = ids
            config.configurationTables[rep] = table
        }
        log.info("Experiment Configuration Generation Complete")
    }
}
