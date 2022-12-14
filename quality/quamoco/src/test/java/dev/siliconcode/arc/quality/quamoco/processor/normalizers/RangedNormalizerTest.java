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
package dev.siliconcode.arc.quality.quamoco.processor.normalizers;

import com.google.common.collect.Lists;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.datamodel.System;
import dev.siliconcode.arc.datamodel.*;
import dev.siliconcode.arc.quality.quamoco.distiller.QuamocoContext;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.MeasureToMeasureNumberEdge;
import dev.siliconcode.arc.quality.quamoco.graph.edge.ValueToMeasureEdge;
import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.graph.node.*;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMeanAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.extents.Extent;
import org.easymock.EasyMock;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * The class <code>RangedNormalizerTest</code> contains tests for the class
 * <code>{@link RangedNormalizer}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RangedNormalizerTest extends DBSpec {

    private RangedNormalizer fixture;
    private List<Finding> findings;
    private File file;
    private File file2;
    private File file3;

    /**
     * Run the RangedNormalizer(Edge,String,NormalizationRange) constructor
     * test.
     */
    @Test
    public void testRangedNormalizer_1() {
        final Edge owner = EasyMock.createMock(Edge.class);
        final String normMetric = "LOC";
        final NormalizationRange range = NormalizationRange.CLASS;
        // add mock object expectations here

        EasyMock.replay(owner);

        final RangedNormalizer result = new RangedNormalizer(owner, normMetric, range);

        // add additional test code here
        EasyMock.verify(owner);
        Assert.assertNotNull(result);
        Assert.assertEquals("LOC", result.getMetric());
    }

    @Test
    public void testNormalize_Set_Finding_1() {
        findings = Lists.newArrayList();
        Finding f1 = new FileFinding(file, "issue", "issue");
        Finding f2 = new FileFinding(file2, "issue", "issue");
        findings.add(f1);
        findings.add(f2);

        double result = fixture.normalize(findings);
        double exp = 0.66667;
        double res = result;
        Assert.assertEquals(exp, res, 0.00001);
    }

    @Test
    public void testNormalize_Set_Finding_2() {
        double result = fixture.normalize((List<Finding>) null);

        Assert.assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testNormalize_Set_Finding_3() {
        double result = fixture.normalize(Lists.newArrayList());

        Assert.assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testNormalize_Set_Finding_4() {
        findings = Lists.newArrayList();
        Finding f1 = new FileFinding(file, "issue", "issue");
        Finding f2 = new FileFinding(file2, "issue", "issue");
        Finding f3 = new FileFinding(file3, "issue", "issue");
//        findings.add(f1);
//        findings.add(f2);
//        findings.add(f3);

        double result = fixture.normalize(findings);
        Assert.assertEquals(1.0, result, 0.001);
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

        MetricRepository repo = MetricRepository.builder().key("repo").name("repo").create();
        Metric.builder().handle("LOC").key("repo:LOC").name("LOC").create();
        Metric.builder().handle("NOM").key("repo:NOM").name("NOM").create();
        Metric.builder().handle("NIV").key("repo:NIV").name("NIV").create();
        Metric.builder().handle("NOC").key("repo:NOC").name("NOC").create();

        final ValueNode vn = new ValueNode(graph, "LOC", "owner", "repo");
        vn.addValue(100.0);

        final MeasureNode src = new MeasureNode(graph, "src", "owner");
        src.setProcessor(new NumberMeanAggregator(src));

        final MeasureNode dest = new MeasureNode(graph, "dest", "owner");
        dest.setProcessor(new NumberMeanAggregator(dest));

        final Edge e = new MeasureToMeasureNumberEdge("edge", src, dest);
        fixture = new RangedNormalizer(e, "LOC", NormalizationRange.FILE);

        graph.addEdge(src, dest, e);
        graph.addEdge(vn, src, new ValueToMeasureEdge("v2m", vn, src));

        // setup metrics
        System sys = System.builder().key("system").name("System").create();
        file = null;
        file2 = null;
        Project proj = Project.builder().projKey("Test").create();
        sys.addProject(proj);
        Namespace ns = Namespace.builder().nsKey("ns").name("ns").create();
        proj.addNamespace(ns);
        QuamocoContext.instance().setMetricRepoKey("repo");
        QuamocoContext.instance().setProject(proj);
        proj.addMeasure(Measure.of("repo:LOC").on(proj).withValue(1000.0));
        proj.addMeasure(Measure.of("repo:NOM").on(proj).withValue(2.0));
        proj.addMeasure(Measure.of("repo:NIV").on(proj).withValue(10.0));
        proj.addMeasure(Measure.of("repo:NOC").on(proj).withValue(2.0));

        file = File.builder().fileKey("path").create();
        proj.addFile(file);
        proj.addMeasure(Measure.of("repo:LOC").on(file).withValue(200.0));

        Type type = Type.builder().type(Type.CLASS).compKey("namespace.Type").start(1).end(100).create();
        file.addType(type);
        ns.addType(type);
        proj.addMeasure(Measure.of("repo:LOC").on(type).withValue(100.0));

        Method method1 = Method.builder().compKey("namespace.Type#method").start(20).end(100).create();
        type.addMember(method1);
        proj.addMeasure(Measure.of("repo:LOC").on(method1).withValue(80.0));

        file2 = File.builder().fileKey("path2").create();
        proj.addFile(file2);
        proj.addMeasure(Measure.of("repo:LOC").on(file2).withValue(200.0));

        file3 = File.builder().fileKey("path3").create();
        proj.addFile(file3);
        proj.addMeasure(Measure.of("repo:LOC").on(file3).withValue(200.0));

        sys.addProject(proj);

        findings = Lists.newArrayList();
        Finding f1 = new FileFinding(file, "issue", "issue");
        Finding f2 = new FileFinding(file2, "issue", "issue");
        findings.add(f1);
        findings.add(f2);

        Extent.getInstance().clearExtents();
    }
}
