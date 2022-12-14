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
package dev.siliconcode.arc.quality.quamoco.graph.edge;

import dev.siliconcode.arc.quality.quamoco.graph.node.Node;

import java.util.List;

/**
 * Edge which connects ValueNodes to MeasureNodes.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ValueToMeasureEdge extends AbstractEdge {

    /**
     * Constructs a new ValueToMeasureEdge with the given name connecting the
     * source and dest nodes.
     *
     * @param name
     *            Name of this edge
     * @param src
     *            Source node
     * @param dest
     *            Dest node
     */
    public ValueToMeasureEdge(final String name, final Node src, final Node dest)
    {
        super(name, src, dest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue()
    {
        return source.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Double> getValues()
    {
        return source.getValues();
    }

}
