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

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.MeasureToFactorNumberEdge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.ValueToMeasureEdge;
import dev.siliconcode.arc.quality.quamoco.graph.node.FactorNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.graph.node.ValueNode;
import dev.siliconcode.arc.quality.quamoco.model.InfluenceEffect;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMeanAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.MeanEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.normalizers.NullNormalizer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>EvaluatorTest</code> contains tests for the class
 * <code>{@link Evaluator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class EvaluatorTest {

    private Evaluator fixture;

    /**
     * Run the double process() method test.
     */
    @Test
    public void testProcess_1()
    {
        final double result = fixture.process();

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the double process() method test.
     */
    @Test
    public void testProcess_2() {
        final ValueNode vn = new ValueNode(fixture.owner.getGraph(), "key", "owner", "tool");
        final MeasureNode mn = new MeasureNode(fixture.owner.getGraph(), "measure", "owner");
        mn.setProcessor(new NumberMeanAggregator(mn));
        mn.setType(MeasureType.NUMBER);
        mn.getGraph().addEdge(vn, mn, new ValueToMeasureEdge("v2m", vn, mn));
        final MeasureToFactorNumberEdge m2f = new MeasureToFactorNumberEdge(
                "m2f", mn, fixture.owner, InfluenceEffect.POSITIVE);
        m2f.setUsesLinearDist(false);
        m2f.setNormalizer(new NullNormalizer(m2f, "LOC", NormalizationRange.CLASS));
        vn.addValue(10.0);
        mn.getGraph().addEdge(mn, fixture.owner, m2f);

        final double result = fixture.process();

        // add additional test code here
        Assert.assertEquals(0.10, result, 0.001);
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
        final FactorNode node = new FactorNode(graph, "factor", "owner");
        fixture = new MeanEvaluator(node);
        node.setProcessor(fixture);
        graph.addNode(node);
    }
}
