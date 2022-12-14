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
package dev.siliconcode.arc.quality.quamoco.processor;

import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;

import java.util.List;
import java.util.Set;

/**
 * Base class for the aggregation of Findings sets, as produced from
 * FindingNodes or MeasureNodes.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class FindingsAggregator extends Aggregator {

    /**
     * Constructs a new FindingsAggregator for the given node.
     *
     * @param owner
     *            The node into which this FindingsAggregator will be installed.
     */
    public FindingsAggregator(final Node owner)
    {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double aggregate(final List<Double> values)
    {
        // TODO Auto-generated method stub
        return 0.0;
    }

    /**
     * Aggregates the collected findings sets from all input nodes.
     *
     * @return The aggregated set of findings.
     */
    protected abstract List<Finding> aggregate();

    /**
     * Process all findings sets incoming from input nodes, by calling the
     * aggregate method.
     *
     * @return The processed set of findings.
     */
    public List<Finding> processFindings()
    {
        return aggregate();
    }
}
