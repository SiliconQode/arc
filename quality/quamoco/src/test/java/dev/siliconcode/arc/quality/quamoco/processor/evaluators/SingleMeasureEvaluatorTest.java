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
package dev.siliconcode.arc.quality.quamoco.processor.evaluators;

import com.google.common.collect.Lists;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.FactorNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * The class <code>SingleMeasureEvaluatorTest</code> contains tests for the
 * class <code>{@link SingleMeasureEvaluator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class SingleMeasureEvaluatorTest {

    private SingleMeasureEvaluator fixture;

    /**
     * Run the SingleMeasureEvaluator(Node) constructor test.
     */
    @Test
    public void testSingleMeasureEvaluator_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final Node owner = new FactorNode(graph, "factor", "owners");

        final SingleMeasureEvaluator result = new SingleMeasureEvaluator(owner);

//         add additional test code here
        Assert.assertNotNull(result);
    }

    /**
     * Run the BigDecimal evaluate(List<Double>) method test.
     */
    @Test
    public void testEvaluate_1() {
        final List<Double> values = Lists.newArrayList();

        values.add(100.0);
        values.add(20.0);
        values.add(-5.0);

        final double result = fixture.evaluate(values);

        // add additional test code here
        Assert.assertEquals(115.0, result, 0.001);
    }

    /**
     * Run the BigDecimal evaluate(List<Double>) method test.
     */
    @Test
    public void testEvaluate_2() {
        final List<Double> values = Lists.newArrayList();

        final double result = fixture.evaluate(values);

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the BigDecimal evaluate(List<Double>) method test.
     */
    @Test
    public void testEvaluate_3() {
        final List<Double> values = null;

        final double result = fixture.evaluate(values);

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     */
    @Before
    public void setUp() throws Exception
    {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        fixture = new SingleMeasureEvaluator(new FactorNode(graph, "factor", "owner"));
    }
}
