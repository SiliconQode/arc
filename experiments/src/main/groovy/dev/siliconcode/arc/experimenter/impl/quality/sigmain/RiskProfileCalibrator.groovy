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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain

import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import org.apache.commons.lang3.tuple.Triple

class RiskProfileCalibrator implements Calibrator {

    def calibrate(String metricHandle) {
        List<Tuple<Double>> values = []

        System.findAll().each {
            Project proj = (it as System).getProjects().first()
            values << Tuple.of(
                    Measure.valueFor(SigCalibrationConstants.SIGCAL_REPO_KEY, "${metricHandle}.LOW", proj),
                    Measure.valueFor(SigCalibrationConstants.SIGCAL_REPO_KEY, "${metricHandle}.MOD", proj),
                    Measure.valueFor(SigCalibrationConstants.SIGCAL_REPO_KEY, "${metricHandle}.HIGH", proj),
                    Measure.valueFor(SigCalibrationConstants.SIGCAL_REPO_KEY, "${metricHandle}.VERY_HIGH", proj)
            )
        }

        values = values.sort(new TupleComparator())

        double size = (double) values.size()
        List<Tuple<Double>> fours = values.subList((int) Math.ceil(size * 0.05), (int) Math.ceil(size * 0.35))
        List<Tuple<Double>> threes = values.subList((int) Math.ceil(size * 0.35), (int) Math.ceil(size * 0.65))
        List<Tuple<Double>> twos = values.subList((int) Math.ceil(size * 0.65), (int) Math.ceil(size * 0.95))
        List<Tuple<Double>> ones = values.subList((int) Math.ceil(size * 0.95), (int) size)

        Map<Integer, Triple<Double, Double, Double>> profile = [:]
        profile[5] = Triple.of(fours.first()[1], fours.first()[2], fours.first()[3])
        profile[4] = Triple.of(threes.first()[1], threes.first()[2], threes.first()[3])
        profile[3] = Triple.of(twos.first()[1], twos.first()[2], twos.first()[3])
        profile[2] = Triple.of(ones.first()[1], ones.first()[2], ones.first()[3])
    }
}

class TupleComparator implements Comparator<Tuple<Double>> {

    @Override
    int compare(Tuple<Double> t1, Tuple<Double> t2) {
        if (Double.compare(t1[0], t2[0]) == 0) {
            if (Double.compare(t1[1], t2[1]) == 0) {
                if (Double.compare(t1[2], t2[2]) == 0) {
                    if (Double.compare(t1[3], t2[3]) == 0) {
                        return 0
                    } else {
                        return Double.compare(t1[3], t2[3])
                    }
                } else {
                    return Double.compare(t1[2], t2[2])
                }
            } else {
                return Double.compare(t1[1], t2[1])
            }
        } else {
            return (Double.compare(t2[0], t1[0]))
        }
    }
}
