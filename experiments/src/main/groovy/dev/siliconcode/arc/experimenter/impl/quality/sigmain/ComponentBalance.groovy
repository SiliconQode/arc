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

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.quality.metrics.annotations.*
import groovyx.gpars.GParsPool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Component Balance",
        primaryHandle = "sigComponentBalance",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
class ComponentBalance extends SigMainComponentMetricEvaluator {

    ComponentBalance(ArcContext context) {
        super(context)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            Project proj = node as Project

            double value = evaluate(proj)

            context.open()
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.RAW").on(proj).withValue(value)
            context.close()
        }
    }

    @Override
    protected double evaluate(Project proj) {
        List<Double> sizes = createSizesList(proj)
        Collections.sort(sizes)

        double giniCoefficient
        if (sizes.size() - 1 == 1) {
            giniCoefficient = 0.0
        } else {
            giniCoefficient = detemineGiniCoefficient(sizes)
        }

        return giniCoefficient
    }

    @Override
    protected String getMetricName() {
        "sigComponentBalance"
    }

    double detemineGiniCoefficient(List<Double> sizes) {
        int numPartitions
        int partitionSize
        (numPartitions, partitionSize) = createPartitions(sizes)
        Double total
        if (!sizes)
            total = 0
        else
            total = sizes.sum() as Double
        List<Double> totals = calculatePartitionTotals(numPartitions, sizes, partitionSize)

        List<Double> frequencies = []
        List<Double> cumulative = []
        buildFrequencyLists(totals, frequencies, total, cumulative)

        calculateGiniCoef(cumulative, frequencies)
    }

    List<Double> createSizesList(Project proj) {
        List<Double> sizes = Lists.newCopyOnWriteArrayList()
        context.open()
        List<Namespace> namespaces = Lists.newArrayList(proj.getNamespaces())
        context.close()

//        GParsPool.withPool(8) {
//            namespaces.eachParallel { Namespace ns ->
            namespaces.each { Namespace ns ->
                context.open()
                def value = ns.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:SLOC")
                if (value)
                    sizes << ns.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:SLOC")
                context.close()
            }
//        }
//        sizes.removeIf { it == null }
        sizes
    }

    List createPartitions(List<Double> sizes) {
        int partitionSize, numPartitions
        if (sizes.size() <= 5) {
            numPartitions = sizes.size()
            partitionSize = 1
        } else {
            numPartitions = 5
            partitionSize = (int) Math.ceil((double) sizes.size() / 5.0)
        }
        [numPartitions, partitionSize]
    }

    List<Double> calculatePartitionTotals(int numPartitions, List<Double> sizes, int partitionSize) {
        int currentPartition = 0
        int subIndex = 0
        List<Double> totals = []

        int fullSize = numPartitions * partitionSize
        int oneLess = fullSize - (sizes.size() % fullSize)
        int fullPartitions = numPartitions - oneLess

        numPartitions.times {
            totals << 0.0
        }
        sizes.each {
            if (it != null) {
                if (totals[currentPartition] == null)
                    totals[currentPartition] = 0.0d
                totals[currentPartition] = totals[currentPartition] + it
                subIndex++
                if (subIndex == partitionSize) {
                    currentPartition++
                    subIndex = 0
                }
                if (currentPartition == fullPartitions) {
                    partitionSize -= 1
                }
            }
        }
        totals
    }

    void buildFrequencyLists(List<Double> totals, List<Double> frequencies, double total, List<Double> cummulative) {
        double runningTotal = 0

        totals.sort()

        totals.each {
            frequencies << it / total
            runningTotal += it
            cummulative << runningTotal / total
        }
    }

    double calculateGiniCoef(List<Double> cummulative, List<Double> frequencies) {
        double rects = 0
        double tris = 0

        for (int i = 0; i < cummulative.size() - 1; i++) {
            rects += (1.0 / cummulative.size()) * cummulative[i]
        }

        for (int i = 0; i < frequencies.size(); i++) {
            tris += (1.0 / frequencies.size()) * frequencies[i] * 0.5
        }

        double areaB = rects + tris

        double areaA = 0.5 - areaB

        return areaA / (areaA + areaB)
    }
}
