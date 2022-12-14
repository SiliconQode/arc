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
package dev.siliconcode.arc.quality.quamoco.processor.aggregators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.MutableNetwork;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.processor.FindingsAggregator;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * A findings based aggregator for measures with type FINDINGS. Simply this
 * collects the incoming findings sets and returns the intersection of all the
 * sets.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindingsIntersectAggregator extends FindingsAggregator {

    /**
     * Constructs a new FindingsUnionAggregator for the given node.
     *
     * @param owner
     *            The node which contains and uses this aggregator
     */
    public FindingsIntersectAggregator(final Node owner)
    {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Finding> aggregate()
    {
        final MutableNetwork<Node, Edge> graph = owner.getGraph();
        List<Finding> retVal = Lists.newArrayList();
        boolean first = true;

        for (final Edge edge : graph.inEdges(owner))
        {
            Node n = owner.getOpposite(edge);
            if (retVal.isEmpty() && first)
            {
                retVal.addAll(n.getFindings());
                first = false;
            }
            else
            {
                List<Finding> temp = Lists.newArrayList(n.getFindings());
                Iterator<Finding> iter = temp.iterator();
                while (iter.hasNext()) {
                    Finding f = iter.next();
                    if (retVal.contains(f)) {
                        retVal.add(f);
                    }
                }
                iter = retVal.iterator();
                while (iter.hasNext()) {
                    Finding f = iter.next();
                    if (!temp.contains(f)) {
                        iter.remove();
                    }
                }
            }
        }

        return retVal;
    }
}
