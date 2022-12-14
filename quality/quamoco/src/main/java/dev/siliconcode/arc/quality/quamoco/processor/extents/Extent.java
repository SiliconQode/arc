/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
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
package dev.siliconcode.arc.quality.quamoco.processor.extents;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.siliconcode.arc.datamodel.Measure;
import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.quality.quamoco.distiller.QuamocoContext;
import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.graph.node.FindingNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureNode;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to evaluate the extent that a set of findings has across a
 * software system. This is accomplished by evaluating the set based on some
 * normalizing name across some given range.
 * <br>
 * We assume that if a finding is locates on a line within a method then that
 * entire method is affected by that finding. If the finding is located within a
 * type but not within a method of that type, then we assume the entire type is
 * affected by that finding. Finally, we assume that if the finding is within a
 * file, but not located within a method or type defined in the file, then it
 * affects the entire file.
 * <br>
 * Once the location of each finding in the set is identified. We then need to
 * be able to evaluate the effect of this finding set on the entire software
 * system. This is where ranges and metrics come into play. There are four
 * ranges: Method, Class, File, and NA. Basically we need to calculate the total
 * value of the name across all the effected entities and then divide this by
 * the total value of the name across the system. This is done as follows for
 * each of the ranges:
 * <ul>
 * <li>Method: if each finding only affects a method, then summing the name
 * value for each affected method and dividing by the sum of the name for all
 * the methods in the system is the correct proportion. If any finding affects a
 * type or file, then we sum (for that finding) the name for all contained
 * methods.
 * <li>Type: if each finding only affects a type, then summing the name value
 * for each affected type and dividing by the sum of the name for all types in
 * the system will provided the correct proportion. On the other hand if any
 * finding affects only a method, then we need to use the name value for the
 * type containing the method. If the finding affects a file, then we must sum
 * the name values for all contained types.
 * <li>File: if each finding only affects a file, then summing the name value
 * for each affect file and dividing by the sum of the name for all files will
 * provide the correct proportion. On the other hand if any finding affects a
 * method or type, then we must use the name value for the containing file.
 * <li>NA: This one is much simpler. Regardless of what the finding affects, we
 * always select the smallest possible unit (File, Type, or Method). We then sum
 * the name value for each finding and divide by the system value for that
 * name.
 * </ul>
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Extent {

    /**
     * Map of project-wide name extents: key = range, value = (key = name
     * name, value = name) extent value
     */
    private final Map<NormalizationRange, Map<String, Double>> totalMetricExtents;

    /**
     * A private static inner class used to hold the singleton instance of this
     * Extent class.
     *
     * @author Isaac Griffith
     * @version 1.2.0
     */
    private static final class ExtentHelper {

        /**
         * The singleton instance
         */
        private static final Extent INSTANCE = new Extent();
    }

    /**
     * Constructs a new extent object
     */
    private Extent() {
        totalMetricExtents = new HashMap<>();
        for (final NormalizationRange range : NormalizationRange.values()) {
            totalMetricExtents.put(range, new HashMap<>());
        }
    }

    /**
     * @return The single instance of Extent
     */
    public static Extent getInstance() {
        return ExtentHelper.INSTANCE;
    }

    /**
     * Determines the extent for a MeasureNode given the normalization name
     * and normalization range.
     *
     * @param metric
     *            Normalization name
     * @param range
     *            Normalization range
     * @param measure
     *            MeasureNode to normalize
     * @return Normalized value of the findings in the given measure node.
     * @throws IllegalArgumentException
     *             if the measure is null or its type is NUMBER, the name is
     *             null or empty, or the range is null.
     */
    public double findMeasureExtent(final String metric, final NormalizationRange range, final MeasureNode measure) {
        if (measure == null) {
            throw new IllegalArgumentException("Measure cannot be null");
        }
        if (measure.getType() == MeasureType.NUMBER) {
            throw new IllegalArgumentException("Cannot find extent of Numerical Value");
        }
        if (metric == null || metric.isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or the empty string.");
        }
        if (range == null) {
            throw new IllegalArgumentException("Range cannot be null.");
        }

        final List<Finding> findingsSet = Lists.newArrayList();
        findingsSet.addAll(measure.getFindings());

        double total = findFindingsExtent(metric, findingsSet, range);

        return Double.compare(total, 0.0) == 0 ? 0.0 : total;
//        return total;
    }

    /**
     * Finds the extent of a given Finding for the provided normalization name
     * and range
     *
     * @param finding
     *            Finding whose extent is required
     * @param metric
     *            Normalization name
     * @param range
     *            Normalization range
     * @return Extent of the normalization name for the given finding
     */
    public double findExtent(final Finding finding, final String metric, final NormalizationRange range) {
        double value = 0.0;
        AbstractExtentDecorator node = NodeExtentDecoratorFactory.getInstance().getDecorator(finding.getLocation());

        switch (range) {
            case CLASS:
                value = node.findClassExtent(metric);
                break;
            case FILE:
                value = node.findFileExtent(metric);
                break;
            case METHOD:
                value = node.findMethodExtent(metric);
                break;
        }

        return value;
    }

    /**
     * Finds the system-wide extent of the given name for the given range.
     *
     * @param metric
     *            Metric name
     * @param range
     *            Normalization range
     * @return total value of the given name in the system for the given
     *         range.
     * @throws IllegalArgumentException
     *             if the name name is null or empty or the range is null.
     */
    public double findExtent(final String metric, final NormalizationRange range) {
        if (metric == null || metric.isEmpty()) {
            throw new IllegalArgumentException("Metric cannot be null or empty");
        }

        if (range == null) {
            throw new IllegalArgumentException("Range cannot be null.");
        }

        if (totalMetricExtents.containsKey(range)) {
            if (totalMetricExtents.get(range).containsKey(metric)) {
                return totalMetricExtents.get(range).get(metric);
            }
        }

        double total = 0.0;
        List<Double> values = null;
        switch (range) {
            case CLASS:
                values = Measure.getAllClassValues(QuamocoContext.instance().getProject(), QuamocoContext.instance().getMetricRepoKey(), metric);
                break;
            case FILE:
                values = Measure.getAllFileValues(QuamocoContext.instance().getProject(), QuamocoContext.instance().getMetricRepoKey(), metric);
                break;
            case METHOD:
                values = Measure.getAllMethodValues(QuamocoContext.instance().getProject(), QuamocoContext.instance().getMetricRepoKey(), metric);
                break;
            case NA:
                return Measure.getProjectMetric(QuamocoContext.instance().getProject(), QuamocoContext.instance().getMetricRepoKey(), metric);
        }

        for (Double value : values) {
            total = total + value;
        }

        totalMetricExtents.get(range).put(metric, total);

        return Double.compare(total, 0.0) == 0 ? 0.0 : total;
    }

    /**
     * Clears the current set of project-wide extents.
     */
    public void clearExtents() {
        totalMetricExtents.clear();
        for (final NormalizationRange range : NormalizationRange.values()) {
            totalMetricExtents.put(range, new HashMap<>());
        }
    }

    /**
     * Detects the appropriate range of a set of findings when the prior range
     * is NA
     *
     * @param prior
     *            The prior range (assumed to be NormalizationRange.NA)
     * @param findings
     *            The set of findings to search for the proper range over.
     * @return The new normalization range to be used.
     */
    public NormalizationRange findRange(Project project, String metric, NormalizationRange prior, List<Finding> findings) {
        NormalizationRange newRange = prior;
        // First find the minimal range;
        for (final Finding f : findings) {
            AbstractExtentDecorator c = NodeExtentDecoratorFactory.getInstance().getDecorator(f.getLocation());
            NormalizationRange temp = c.findRange(metric);

            if (temp.compareTo(newRange) < 0)
                newRange = temp;
        }

        return newRange;
    }

    /**
     * Calculates the ratio of the affect a FindingNode has on the entire
     * system. Essentially, this is the ratio of the sum of the normalizing
     * name across each affected item divided by the total of that name
     * (within the range of those types of items) across the system.
     *
     * @param metric
     *            Metric used for normalizing
     * @param normalizationRange
     *            Range for normalizing
     * @param n
     *            The Finding Node whose findings are to be evaluated.
     * @return The ratio representing the presence that this FindingNode has
     *         within the system as a whole.
     */
    public double findFindingExtent(Project project, String metric, NormalizationRange normalizationRange,
                                    FindingNode n) {
        double value = 0.0;
        NormalizationRange range = normalizationRange;
        if (!n.getFindings().isEmpty()) {
            if (range == NormalizationRange.NA) {
                range = findRange(project, metric, range, n.getFindings());
            }

            value = findFindingsExtent(metric, n.getFindings(), range);
        }

        value = value / findExtent(metric, range);

        return value;
    }

    /**
     * Find the total extent (for the provided name) that the set of findings
     * has within the system. Basically just a simple total of the name for
     * the items affected by the provided set of findings.
     *
     * @param metric
     *            Metric whose extent is to be calculated
     * @param findings
     *            Findings whose locations are to be summed to provide the
     *            extent
     * @param range
     *            Range of the extent
     * @return The extent of affect this set of findings has within the entire
     *         system (un-normalized)
     */
    private double findFindingsExtent(String metric, List<Finding> findings, NormalizationRange range) {
        double value = 0.0;

        for (Finding f : findings) {
            value = value + findExtent(f, metric, range);
        }

        return value;
    }
}
