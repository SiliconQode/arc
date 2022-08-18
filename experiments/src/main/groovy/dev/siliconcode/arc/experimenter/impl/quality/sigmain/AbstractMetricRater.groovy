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

abstract class AbstractMetricRater implements MetricRater {

    String metricHandle

    AbstractMetricRater(String metricHandle) {
        this.metricHandle = metricHandle
    }

    double calcRating(double value, Map<Integer, Double> table) {
        double rating = 0.0

        for (int i = 1; i < 5; i++) {
            if (value <= table[i] && value > table[i + 1]) {
                rating = findPoint(value, table, i)
            }
        }

        if (rating == 0.0) {
            if (value < table[1])
                rating = 5.0
            else
                rating = 1.0
        }

        return rating
    }

    double findPoint(double value, Map<Integer, Double> table, int index) {
        double min = table[index]
        double max = table[index + 1]

        return index + ((value - min) / (max - min))
    }
}
