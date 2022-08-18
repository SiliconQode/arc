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
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.ReportingLevel
import dev.siliconcode.arc.experimenter.app.runner.experiment.ExperimentConstants
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ResultsExtractor {

    List<String> measures = []
    ReportingLevel level
    ArcContext context
    List<String> keyHeaders = []

    void initialize(ReportingLevel level, List<String> keyHeaders, List<String> measures, ArcContext context) {
        this.level = level
        this.keyHeaders = keyHeaders
        this.measures = measures
        this.context = context
    }

    void extractResults(Table<String, String, String> table) {
        context.open()
        switch(level) {
            case ReportingLevel.SYSTEM:
                extractSystemResults(table)
                break
            case ReportingLevel.PROJECT:
                extractProjectResults(table)
                break
        }
        context.close()
    }

    void extractSystemResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            List<String> keys = []
            keyHeaders.each {
                keys << values.get(id, it)
            }

            List<System> systems = []
            keys.each {
                systems << (System) System.findFirst("key = ?", it)
            }

            measures.each {measure ->
                log.info "Measure: $measure"
                List<Double> vals = []
                systems.each { sys ->
                    vals << (sys as System).getMeasureValueByName(measure)
                }

                double diff = vals[-1]
                if (vals.size() > 1) {
                    for (int i = vals.size() - 2; i >= 0; i--)
                        diff -= vals[i]
                }

                String name = measure.split(/:/)[1]
                values.put(id, name, "$diff")
            }
        }
    }

    void extractProjectResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            List<String> keys = []
            keyHeaders.each {
                keys << values.get(id, it)
            }

            List<Project> projects = []
            keys.each {
                projects << (Project) Project.findFirst("projKey = ?", it)
            }

            measures.each {measure ->
                log.info "Measure: $measure"
                List<Double> vals = []
                projects.each { proj ->
                    vals << (proj as Project).getValueFor(measure)
                }

                double diff = vals[-1]
                if (vals.size() > 1) {
                    for (int i = vals.size() - 2; i >= 0; i--)
                        diff -= vals[i]
                }

                String name = measure.split(/:/)[1]
                values.put(id, name, "$diff")
            }
        }
    }
}
