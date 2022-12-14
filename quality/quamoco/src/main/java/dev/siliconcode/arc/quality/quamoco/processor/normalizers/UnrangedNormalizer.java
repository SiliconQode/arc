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
package dev.siliconcode.arc.quality.quamoco.processor.normalizers;

import dev.siliconcode.arc.quality.quamoco.distiller.QuamocoContext;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.Normalizer;
import dev.siliconcode.arc.quality.quamoco.processor.extents.Extent;

import java.util.List;
import java.util.Set;

/**
 * Class used to normalize un-ranged sets of findings.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class UnrangedNormalizer extends Normalizer {

    /**
     * Constructs a new UnrangedNormalizer for the given edge using the provided
     * normalization name.
     *
     * @param owner
     *            The Edge in which this normalizer was installed
     * @param normMetric
     *            the name of the name used for normalization
     */
    public UnrangedNormalizer(final Edge owner, final String normMetric)
    {
        super(owner, normMetric, NormalizationRange.NA);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double normalize(final List<Finding> findings)
    {
        if (findings == null || findings.isEmpty())
            return 0.0;

        double totalAffected = 0.0;
        NormalizationRange newRange = Extent.getInstance()
                .findRange(QuamocoContext.instance().getProject(), metric, range, findings);

        Extent ext = Extent.getInstance();
        for (final Finding f : findings)
        {
            totalAffected += ext.findExtent(f, QuamocoContext.instance().getMetricRepoKey() + ":" + metric, newRange);
        }

        double extent = ext.findExtent(metric, range);
        double value;

        if (Double.compare(0.0, extent) == 0 && Double.compare(extent, totalAffected) == 0)
            return 0.0;
        else
            value = totalAffected / extent;

        return value;
    }

}
