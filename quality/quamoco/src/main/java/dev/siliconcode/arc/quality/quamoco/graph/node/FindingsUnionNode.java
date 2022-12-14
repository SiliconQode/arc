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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.MutableNetwork;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.FindingsEdge;

import java.util.List;
import java.util.Set;


/**
 * Node which simply constructs a union of all input findings nodes.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindingsUnionNode extends Node {

    /**
     * The union-ed set of findings.
     */
    private final List<Finding> findings;

    /**
     * Constructs a new Node which is contained in the given graph,
     * identified by the given name, extracted from the quamoco model entity
     * with
     * the given identifier.
     *
     * @param name
     *            Identifier of this node
     * @param owner
     *            Identifier of the quamoco model entity this node came from
     */
    public FindingsUnionNode(final String name, final String owner)
    {
        this(null, name, owner);
    }

    public FindingsUnionNode(MutableNetwork<Node, Edge> graph, final String name, final String owner)
    {
        super(graph, name, owner);
        findings = Lists.newArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerResult()
    {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperResult()
    {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue()
    {
//        calculated = true;
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXMLTag()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Finding> getFindings()
    {
        if (findings.isEmpty())
        {
            for (final Edge e : graph.inEdges(this))
            {
                if (e instanceof FindingsEdge)
                {
                    findings.addAll(((FindingsEdge) e).getFindings());
                }
            }
        }

        return findings;
    }
}
