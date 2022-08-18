/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
package dev.siliconcode.arc.quality.metrics.annotations.aggregation

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Aggregation {

    static def average(List vals) {
        sum(vals)/count(vals)
    }

    static def count(List vals) {
        vals.size()
    }

    static def max(List vals) {
        vals.max()
    }

    static def min(List vals) {
        vals.min()
    }

    static def median(List vals) {
        def x = vals.sort()
        if (x % 2 == 0)
            (vals[vals.size() / 2] + vals[vals.size() / 2 + 1]) / 2
        else
            vals[vals.size() / 2]
    }

    static def mode(List vals) {
        def counts = [:]
        def mode = []

        vals.each {
            if (counts[it])
                counts[it] = counts[it] + 1
            else
                counts[it] = 0
        }

        int max = 0
        counts.each { key, value ->
            if (value > max)
                max = value
        }

        mode = counts.findAll { key, value -> value == max }
    }

    static def range(List vals) {
        max(vals) - min(vals)
    }

    static def sum(List vals) {
        vals.sum()
    }

    static def randselect(List vals) {
        vals = Collections.shuffle(vals)
        vals.first()
    }

    static def variance(List vals) {
        def mean = average(vals)
        def sqrs = []
        vals.each {
            sqrs << Math.pow(vals - mean, 2)
        }

        sqrs.sum() / (vals.size() - 1)
    }

    static def stddev(List vals) {
        Math.sqrt(variance(vals))
    }
}
