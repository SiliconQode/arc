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

import spock.lang.Specification

class ComponentBalanceTest extends Specification {

    ComponentBalance fixture

    def setup() {
        fixture = new ComponentBalance()
    }

    def "CreatePartitions"() {
        int np, ps

        when:
        (np, ps) = fixture.createPartitions(sizes)

        then:
        np == numPartitions
        ps == partitionSize

        where:
        sizes                                          || numPartitions | partitionSize
        [125, 100, 50, 75, 100]                        || 5             | 1
        [125, 100, 50, 75, 100, 125, 50, 75, 100, 125] || 5             | 2
        [125, 100, 50, 75, 100, 125, 50, 75, 100]      || 5             | 2
        [125, 100, 50, 75, 100, 125]                   || 5             | 2
        [125, 100, 50, 75]                             || 4             | 1
    }

    def "CalculatePartitionTotals"() {
        given:
        expected = (expected as List<Double>)
        sizes = (sizes as List<Double>)

        when:
        List<Double> result = fixture.calculatePartitionTotals(numPartitions, sizes, partitionSize)

        then:
        result == expected

        where:
        numPartitions | partitionSize | sizes                                          || expected
        5             | 1             | [125, 100, 50, 75, 100]                        || [125, 100, 50, 75, 100]
        5             | 2             | [125, 100, 50, 75, 100, 125, 50, 75, 100, 125] || [225, 125, 225, 125, 225]
        5             | 2             | [125, 100, 50, 75, 100, 125, 50, 75, 100]      || [225, 125, 225, 125, 100]
        5             | 2             | [125, 100, 50, 75, 100, 125]                   || [225, 50, 75, 100, 125]
        5             | 1             | [125, 100, 50, 75]                             || [125, 100, 50, 75, 0]
    }

    def "BuildFrequencyLists"() {
        given:
        List<Double> totals = [125, 100, 50, 75, 150]
        List<Double> frequencies = []
        double total = totals.sum() as double
        List<Double> cummulative = []
        List<Double> expectedFreqs = [0.1, 0.15, 0.2, 0.25, 0.3]
        List<Double> expectedCumm = [0.1, 0.25, 0.45, 0.7, 1.0]

        when:
        fixture.buildFrequencyLists(totals, frequencies, total, cummulative)

        then:
        frequencies.sort() == expectedFreqs
        cummulative.sort() == expectedCumm
    }

    def "CalculateGiniCoef"() {
        given:
        List<Double> frequencies = [0.1, 0.15, 0.2, 0.25, 0.3]
        List<Double> cummulative = [0.1, 0.25, 0.45, 0.7, 1.0]
        double expected = 0.2

        when:
        double result = fixture.calculateGiniCoef(cummulative, frequencies)

        then:
        Math.abs(result - expected) < 0.001
    }
}
