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
package dev.siliconcode.arc.quality.quamoco.graph.edge;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.node.FactorNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.model.InfluenceEffect;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.lineardist.PositiveLinearDistribution;
import dev.siliconcode.arc.quality.quamoco.processor.normalizers.NullNormalizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The class <code>InfluenceEdgeTest</code> contains tests for the class
 * <code>{@link FactorToFactorEdge}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FactorToFactorEdgeTest {

    private FactorToFactorEdge fixture;

    /**
     * Run the InfluenceEdge(String) constructor test.
     */
    @Test
    public void testFactorToFactorEdge_1() {
        final String name = "TestEdge";

        final FactorToFactorEdge result = new FactorToFactorEdge(name, null, null, InfluenceEffect.POSITIVE);

        // TODO: add additional test code here
        assertNotNull(result);
        assertEquals(1.0, result.getWeight(), 0.001);
        assertEquals(InfluenceType.POS, result.getInf());
        assertEquals(1.0, result.getUpperBound(), 0.001);
        assertEquals(0.0, result.getLowerBound(), 0.001);
        assertEquals(name, result.getName());
    }

    /**
     * Run the String getInf() method test.
     */
    @Test
    public void testGetInf_1() {
        final String result = fixture.getInf();

        // TODO: add additional test code here
        assertEquals(InfluenceType.POS, result);
    }

    /**
     * Run the double getLowerBound() method test.
     */
    @Test
    public void testGetLowerBound_1() {
        final double result = fixture.getLowerBound();

        // TODO: add additional test code here
        assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the double getUpperBound() method test.
     */
    @Test
    public void testGetUpperBound_1() {
        final double result = fixture.getUpperBound();

        // TODO: add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(0.5);

        fixture.setDist(null);
        fixture.setMaxPoints(100.0);
        fixture.setWeight(0.5);
        fixture.setInf(InfluenceType.POS);
        fixture.setNormalizer(null);

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        // TODO: add additional test code here
        assertEquals(0.25, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_2() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(0.75);

        fixture.setDist(null);
        fixture.setMaxPoints(1.0);
        fixture.setWeight(0.5);
        fixture.setInf(InfluenceType.NEG);
        fixture.setNormalizer(null);

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        // TODO: add additional test code here
        assertEquals(0.125, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_3() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(0.5);

        fixture.setDist(new PositiveLinearDistribution());
        fixture.setMaxPoints(100.0);
        fixture.setInf(null);
        fixture.setNormalizer(null);

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        // TODO: add additional test code here
        assertEquals(0.5, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_4() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(1.5);

        fixture.setDist(new PositiveLinearDistribution());
        fixture.setMaxPoints(1.0);
        fixture.setInf(null);
        fixture.setNormalizer(null);

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        // TODO: add additional test code here
        assertEquals(1.0, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_5() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
//        final ValueNode src = new ValueNode(graph, "source", "source", "");

        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(-1.0);

        fixture.setDist(null);
        fixture.setInf(null);
        fixture.setNormalizer(null);

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        assertEquals(0.0, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getValue(DirectedSparseGraph<Node,Edge>,Node) method test.
     */
    @Test
    public void testGetValue_6() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final FactorNode dest = new FactorNode(graph, "dest", "dest");
        FactorNode src = mock(FactorNode.class);
        when(src.getValue()).thenReturn(0.75);

        fixture.setDist(null);
        fixture.setMaxPoints(1.0);
        fixture.setWeight(0.5);
        fixture.setInf(InfluenceType.POS);
        fixture.setNormalizer(new NullNormalizer(fixture, "NOM", NormalizationRange.CLASS));

        graph.addEdge(src, dest, fixture);
        fixture.source = src;
        fixture.dest = dest;

        // TODO: add additional test code here
        assertEquals(0.375, fixture.getValue(), 0.001);
    }

    /**
     * Run the double getWeight() method test.
     */
    @Test
    public void testGetWeight_1() {
        final double result = fixture.getWeight();

        // TODO: add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInf_1() {
        final String inf = "";

        fixture.setInf(inf);
        fail();
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_2() {
        final String inf = null;

        fixture.setInf(inf);
        assertNull(fixture.getInf());
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_3() {
        final String inf = InfluenceType.POS;

        fixture.setInf(inf);
        assertEquals(InfluenceType.POS, fixture.getInf());
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_4() {
        final String inf = InfluenceType.NEG;

        fixture.setInf(inf);
        assertEquals(InfluenceType.NEG, fixture.getInf());
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInf_5() {
        final String inf = "test";

        fixture.setInf(inf);
    }

    /**
     * Run the void setLowerBound(double) method test.
     */
    @Test
    public void testSetLowerBound_1() {
        final double lowerBound = 0.0;

        fixture.setLowerBound(lowerBound);
        assertEquals(lowerBound, fixture.getLowerBound(), 0.001);
    }

    /**
     * Run the void setLowerBound(double) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBound_2() {
        final double lowerBound = 2.0;

        fixture.setLowerBound(lowerBound);
    }

    /**
     * Run the void setUpperBound(double) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetUpperBound_1() {
        final double upperBound = 0.0;
        fixture.setLowerBound(1.0);
        fixture.setUpperBound(upperBound);
    }

    /**
     * Run the void setUpperBound(double) method test.
     */
    @Test
    public void testSetUpperBound_2() {
        final double upperBound = 2.0;

        fixture.setUpperBound(upperBound);
        assertEquals(upperBound, fixture.getUpperBound(), 0.001);
    }

    /**
     * Run the void setWeight(double) method test.
     */
    @Test
    public void testSetWeight_1() {
        final double weight = 0.0;

        fixture.setWeight(weight);
        assertEquals(weight, fixture.getWeight(), 0.001);
    }

    /**
     * Run the void setWeight(double) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetWeight_2() {
        final double weight = -1.0;

        fixture.setWeight(weight);
        fail();
    }

    /**
     * Run the void setWeight(double) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetWeight_3() {
        final double weight = 1.5;

        fixture.setWeight(weight);
        fail();
    }

    /**
     *
     */
    @Before
    public void setUp() {
        fixture = new FactorToFactorEdge("", null, null, InfluenceEffect.POSITIVE);
        fixture.setInf(InfluenceType.POS);
        fixture.setUpperBound(1.0);
        fixture.setWeight(1.0);
        fixture.setLowerBound(0.0);
    }
}
