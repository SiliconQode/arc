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

import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor type applicable only to MeasureNodes.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Aggregator extends Processor {

    /**
     * Constructs a new Aggregator for the given node.
     *
     * @param owner
     *            The node into which the aggregator will be installed.
     */
    public Aggregator(final Node owner)
    {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double process()
    {
        final List<Double> values = new ArrayList<>();
        for (final Edge n : owner.getGraph().inEdges(owner))
        {
            values.addAll(n.getValues());
        }

        return aggregate(values);
    }

    /**
     * Method which given the list of values returns an aggregate of those
     * values based on a particular function.
     *
     * @param values
     *            Values to aggregate
     * @return The aggregate value
     */
    protected abstract double aggregate(List<Double> values);
}
