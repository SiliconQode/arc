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
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Node representing a factor in the Quamoco processing graph. A
 * Factor can be a quality characteristic, sub-characteristic or intermediary,
 * but it is not a direct measure related to the product.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FactorNode extends Node {

    /**
     * The method associated with aggregation.
     */
    @Getter
    private String method;
    /**
     * The set of all findings connected to this factor (via the graph)
     */
    private List<Finding> findings;

    /**
     * Constructs a new Factor node in the given graph, with the given name, and
     * associated with the owner id in a Quality Model.
     *
     * @param name  Name of the factor
     * @param owner Id of the entity in a quality model this node represents.
     */
    public FactorNode(final String name, final String owner) {
        this(null, name, owner);
    }

    public FactorNode(MutableNetwork<Node, Edge> graph, final String name, final String owner) {
        super(graph, name, owner);
        method = FactorMethod.MEAN;
        findings = Lists.newArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue() {
        if (!calculated)
            return processor.process();
        else
            return value;
    }

    public double thresholdValue(double value) {
        if (Double.compare(0.0, value) > 0)
            return 0.0;
        else if (Double.compare(1.0, value) < 0)
            return 1.0;
        else
            return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXMLTag() {
        return String.format(
                "<nodes name=\"%s\" description=\"%s\" owner=\"%s\" type=\"FACTOR\">%n",
                StringEscapeUtils.escapeXml10(name), StringEscapeUtils.escapeXml10(description), ownedBy) +
                "\t</nodes>";
    }

    /**
     * Sets the method indicating how aggregation is performed.
     *
     * @param method The new method.
     */
    public void setMethod(final String method) {
        final List<String> methods = Lists.newArrayList();
        methods.add(FactorMethod.MANUAL);
        methods.add(FactorMethod.MEAN);
        methods.add(FactorMethod.ONE);
        methods.add(FactorMethod.RANKING);

        if (method == null || method.isEmpty() || !methods.contains(method)) {
            throw new IllegalArgumentException();
        }

        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerResult() {
        final List<Double> values = Lists.newArrayList();
        for (final Edge e : graph.inEdges(this)) {
            final Node n = getOpposite(e);
            values.add(n.getValue());
        }

        return values.isEmpty() ? 0.0 : Collections.min(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperResult() {
        final List<Double> values = Lists.newArrayList();
        for (final Edge e : graph.inEdges(this)) {
            final Node n = getOpposite(e);
            values.add(n.getValue());
        }

        return values.isEmpty() ? 1.0 : Collections.max(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Finding> getFindings() {
        if (findings == null || findings.isEmpty()) {
            for (Edge edge : graph.inEdges(this)) {
                Node n = getOpposite(edge);
                findings.addAll(n.getFindings());
            }
        }

        return findings;
    }
}
