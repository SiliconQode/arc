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

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.datamodel.File;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.FindingToMeasureEdge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.MeasureToMeasureFindingsEdge;
import dev.siliconcode.arc.quality.quamoco.graph.node.*;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * The class <code>FindingsUnionAggregatorTest</code> contains tests for the
 * class <code>{@link FindingsUnionAggregator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindingsUnionAggregatorTest extends DBSpec {

    private FindingsUnionAggregator fixture;

    /**
     * Run the FindingsUnionAggregator(Node) constructor test.
     */
    @Test
    public void testFindingsUnionAggregator_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final Node owner = new MeasureNode(graph, "measure", "owner");

        final FindingsUnionAggregator result = new FindingsUnionAggregator(owner);

        // add additional test code here
        Assert.assertNotNull(result);
    }

    /**
     * Run the Set<Finding> aggregate() method test.
     */
    @Test
    public void testAggregate_1() {
        final List<Finding> result = fixture.aggregate();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
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
        final FindingNode fn1 = new FindingNode(graph, "key1", "owner", "rule", "tool");
        final FindingNode fn2 = new FindingNode(graph, "key2", "owner", "rule", "tool");

        fn1.addFinding(new FileFinding(File.builder().fileKey("path1").create(), "issue1", "issue"));
        fn1.addFinding(new FileFinding(File.builder().fileKey("path2").create(), "issue1", "issue"));
        fn2.addFinding(new FileFinding(File.findFirst("fileKey = ?", "path1"), "issue2", "issue"));
        fn2.addFinding(new FileFinding(File.findFirst("fileKey = ?", "path2"), "issue2", "issue"));

        final MeasureNode owner = new MeasureNode(graph, "measure", "owner");
        owner.setType(MeasureType.FINDINGS);

        final MeasureNode mn = new MeasureNode(graph, "measure2", "owner");
        mn.setType(MeasureType.FINDINGS);
        mn.setProcessor(new FindingsUnionAggregator(mn));

        graph.addEdge(fn1, owner, new FindingToMeasureEdge("edge", fn1, owner));
        graph.addEdge(fn2, owner, new FindingToMeasureEdge("edge2", fn2, mn));
        graph.addEdge(mn, owner, new MeasureToMeasureFindingsEdge("edge3", mn, owner));

        fixture = new FindingsUnionAggregator(owner);
        owner.setProcessor(fixture);
    }
}
