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
package dev.siliconcode.arc.quality.quamoco.graph.node;

import com.google.common.graph.MutableNetwork;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import lombok.Getter;
import lombok.Setter;

/**
 * Node originally meant to provide the means by which normalization was to
 * occur, currently this node is no longer needed and should be removed
 * FIXME: Remove this class
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class NormalizationNode extends MeasureNode {

    @Getter
    @Setter
    private NormalizationRange range;

    /**
     * @param owner
     */
    public NormalizationNode(final String name, final String owner)
    {
        this(null, name, owner);
    }

    public NormalizationNode(MutableNetwork<Node, Edge> graph, final String name, final String owner)
    {
        super(graph, name, owner);
        range = NormalizationRange.NA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue()
    {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXMLTag()
    {
        return String.format("<nodes name=\"%s\" owner=\"%s\" type=\"NORMALIZATION\" />", name, ownedBy);
    }

}
