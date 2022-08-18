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
package dev.siliconcode.arc.experimenter.app.runner.verification

import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.app.runner.ResultsExtractor
import groovy.util.logging.Log4j2

@Log4j2
class VerificationStudyResultsExtractor extends ResultsExtractor {

    void extractProjectResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            Project base = Project.findFirst("projKey = ?", values.get(id, VerificationStudyConstants.BASE_KEY))
            Project infected = Project.findFirst("projKey = ?", values.get(id, VerificationStudyConstants.INFECTED_KEY))
            Project injected = Project.findFirst("projKey = ?", values.get(id, VerificationStudyConstants.INJECTED_KEY))

            measures.each {measure ->
                log.info "Measure: $measure"

                double baseVal = base.getValueFor(measure)
                double infVal = infected.getValueFor(measure)
                double injVal = injected.getValueFor(measure)

                log.info "BaseVal: $baseVal"
                log.info "infVal: $infVal"
                log.info "injVal: $injVal"

                double diffInfBase = infVal - baseVal
                double diffInjBase = injVal - baseVal

                String name = measure.split(":")[1]
                values.put(id, name + ".Inf", "$diffInfBase")
                values.put(id, name + ".Inj", "$diffInjBase")
            }
        }
    }
}
