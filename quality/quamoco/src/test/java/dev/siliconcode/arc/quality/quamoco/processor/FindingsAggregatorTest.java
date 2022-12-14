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

import com.google.common.collect.Lists;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.datamodel.File;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.FindingToMeasureEdge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.MeasureToMeasureFindingsEdge;
import dev.siliconcode.arc.quality.quamoco.graph.node.*;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.FindingsUnionAggregator;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * The class <code>FindingsAggregatorTest</code> contains tests for the class
 * <code>{@link FindingsAggregator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindingsAggregatorTest extends DBSpec {

    private FindingsAggregator fixture;

    /**
     * Run the double aggregate(Map<Node,Double>) method test.
     */
    @Test
    public void testAggregate_1() {
        final List<Double> values = Lists.newArrayList();
        values.add(10.0);
        values.add(20.0);
        values.add(0.0);
        final double result = fixture.aggregate(values);

        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the Set<Finding> processFindings() method test.
     */
    @Test
    public void testProcessFindings_1() {
        final List<Finding> result = fixture.processFindings();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
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

        final FindingNode fn = new FindingNode(graph, "fn1", "owner", "rule1", "tool");
        fn.addFinding(new FileFinding(File.builder().fileKey("path2").create(), "issue1", "issue"));
        final MeasureNode mn = new MeasureNode(graph, "measure1", "owner");
        mn.setType(MeasureType.FINDINGS);
        final MeasureNode mn2 = new MeasureNode(graph, "measure2", "owner");
        mn2.setType(MeasureType.FINDINGS);
        final FindingNode fn2 = new FindingNode(graph, "fn2", "owner", "rule1", "tool");
        fn2.addFinding(new FileFinding(File.builder().fileKey("path").create(), "issue1", "issue"));

        graph.addEdge(fn, mn, new FindingToMeasureEdge("edge1", fn, mn));
        graph.addEdge(mn2, mn, new MeasureToMeasureFindingsEdge("edge2", mn2, mn));
        graph.addEdge(fn2, mn2, new FindingToMeasureEdge("edge3", fn2, mn2));

        fixture = new FindingsUnionAggregator(mn);
        mn.setProcessor(fixture);
        mn2.setProcessor(new FindingsUnionAggregator(mn2));
    }
}
