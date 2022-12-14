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
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * The class <code>NumberMedianAggregatorTest</code> contains tests for the
 * class <code>{@link NumberMedianAggregator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class NumberMedianAggregatorTest {

    private NumberMedianAggregator fixture;

    /**
     * Run the NumberMedianAggregator(Node) constructor test.
     */
    @Test
    public void testNumberMedianAggregator_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final Node owner = new MeasureNode(graph, "measure", "owner");

        final NumberMedianAggregator result = new NumberMedianAggregator(owner);

        // add additional test code here
        Assert.assertNotNull(result);
    }

    /**
     * Run the BigDecimal aggregate(Map<Node,BigDecimal>) method test.
     */
    @Test
    public void testAggregate_1() {
        final List<Double> values = Lists.newArrayList();
        values.add(10.0);
        values.add(20.0);
        values.add(3.0);
        values.add(13.0);
        values.add(25.0);

        final double result = fixture.aggregate(values);

        // add additional test code here
        Assert.assertEquals(13.0, result, 0.001);
    }

    /**
     * Run the BigDecimal aggregate(Map<Node,BigDecimal>) method test.
     */
    @Test
    public void testAggregate_2() {
        final List<Double> values = Lists.newArrayList();
        values.add(10.0);
        values.add(20.0);
        values.add(3.0);
        values.add(5.0);

        final double result = fixture.aggregate(values);

        // add additional test code here
        Assert.assertEquals(7.5, result, 0.001);
    }

    /**
     * Run the BigDecimal aggregate(Map<Node,BigDecimal>) method test.
     */
    @Test
    public void testAggregate_3() {
        final List<Double> values = Lists.newArrayList();

        final double result = fixture.aggregate(values);

        // add additional test code here
        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the BigDecimal aggregate(Map<Node,BigDecimal>) method test.
     */
    @Test
    public void testAggregate_4() {
        final List<Double> values = null;

        final double result = fixture.aggregate(values);

        // add additional test code here
        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     */
    @Before
    public void setUp() throws Exception {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode node = new MeasureNode(graph, "measure", "owner");
        fixture = new NumberMedianAggregator(node);
    }
}
