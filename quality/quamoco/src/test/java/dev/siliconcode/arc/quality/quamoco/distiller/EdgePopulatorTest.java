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
/**
 *
 */
package dev.siliconcode.arc.quality.quamoco.distiller;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.QualityModel;
import dev.siliconcode.arc.quality.quamoco.model.eval.Evaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.factor.WeightedSumFactorAggregation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.SingleMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.factor.Factor;
import dev.siliconcode.arc.quality.quamoco.model.factor.ProductFactor;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measurement.FactorRanking;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * The class <code>EdgePopulatorTest</code> contains tests for the class
 * <code>{@link EdgePopulator}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class EdgePopulatorTest {

    /**
     * Run the EdgePopulator() constructor test.
     */
    @Test
    public void testEdgePopulator_1() {

        final EdgePopulator result = new EdgePopulator();

        // TODO: add additional test code here
        assertNotNull(result);
    }

    /**
     * Run the void modifyGraph(DistillerData,DirectedSparseGraph<Node,Edge>)
     * method test.
     */
    @Test
    public void testModifyGraph_1() {
        ModelManager manager = new ModelManager();
        Factor f2 = null;
        Measure m1 = null;
        Factor f1 = null;
        QualityModel model = QualityModel.builder().identifier("java").fileName("java.qm")
                .description("")
                .title("")
                .factor("factor2", f2 = ProductFactor.builder().name("factor2").identifier("factor2").create())
                .factor("factor1", f1 = (Factor) ProductFactor.builder().name("factor1").identifier("factor1")
                        .description("")
                        .refines(f2)
                        .create())
                .measure("measure1", m1 = Measure.builder()
                        .name("measure1")
                        .identifier("measure1")
                        .description("")
                        .title("")
                        .type(MeasureType.FINDINGS)
                        .create())
                .evaluation("eval1", (Evaluation) SingleMeasureEvaluation.builder().identifier("eval1")
                        .basedOn(m1)
                        .evaluates(f1)
                        .create())
                .evaluation("eval2", (Evaluation) WeightedSumFactorAggregation.builder().identifier("eval2")
                        .ranking(FactorRanking.builder()
                                .identifier("rank1")
                                .factor(f1)
                                .rank(1)
                                .weight(0.25)
                                .create())
                        .completeness(1.0)
                        .description("")
                        .maximumPoints(100d)
                        .evaluates(f2)
                        .title("")
                        .create())
                .create();
        manager.addModel(model);

        final NodePopulator nodepop = new NodePopulator();
        final EdgePopulator fixture = new EdgePopulator();
        final DistillerData data = new DistillerData(manager);
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();

        nodepop.modifyGraph(data, graph);
        fixture.modifyGraph(data, graph);

        Node fn1 = data.getFactor(f1);
        assertNotNull(fn1);
        Node fn2 = data.getFactor(f2);
        assertNotNull(fn2);
        assertFalse(graph.edgesConnecting(fn1, fn2).isEmpty());

        Node mn1 = data.getMeasure(m1);
        assertNotNull(mn1);
        assertFalse(graph.edgesConnecting(mn1, fn1).isEmpty());

        Assert.assertEquals(3, graph.nodes().size());
        Assert.assertEquals(2, graph.edges().size());
    }
}
