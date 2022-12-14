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
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.graph.node.ValueNode;
import dev.siliconcode.arc.quality.quamoco.model.InfluenceEffect;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.WeightedSumEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.lineardist.PositiveLinearDistribution;
import dev.siliconcode.arc.quality.quamoco.processor.normalizers.NullNormalizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The class <code>MeasureToFactorNumberEdgeTest</code> contains tests for the
 * class <code>{@link MeasureToFactorNumberEdge}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasureToFactorNumberEdgeTest {

    private MeasureToFactorNumberEdge fixture;

    /**
     * Run the MeasureToFactorNumberEdge(String,InfluenceEffect) constructor
     * test.
     */
    @Test
    public void testMeasureToFactorNumberEdge_1() {
        final String name = "";
        final InfluenceEffect effect = InfluenceEffect.NEGATIVE;

        final MeasureToFactorNumberEdge result = new MeasureToFactorNumberEdge(name, null, null, effect);

        // add additional test code here
        assertNotNull(result);
        assertEquals("NEGATIVE", result.getInf());
        assertNull(result.getDist());
        assertEquals(100.0, result.getMaxPoints(), 0.001);
        assertFalse(result.isUsesLinearDist());
        assertNull(result.getNormalizer());
        assertEquals(1.0, result.getWeight(), 0.001);
        assertEquals(1.0, result.getUpperBound(), 0.001);
        assertEquals(0.0, result.getLowerBound(), 0.001);
        assertEquals("", result.getName());
    }

    /**
     * Run the MeasureToFactorNumberEdge(String,InfluenceEffect) constructor
     * test.
     */
    @Test
    public void testMeasureToFactorNumberEdge_2() {
        final String name = "";
        final InfluenceEffect effect = null;

        final MeasureToFactorNumberEdge result = new MeasureToFactorNumberEdge(name, null, null, effect);

        // add additional test code here
        assertNotNull(result);
        assertEquals("POSITIVE", result.getInf());
        assertNull(result.getDist());
        assertEquals(100.0, result.getMaxPoints(), 0.001);
        assertFalse(result.isUsesLinearDist());
        assertNull(result.getNormalizer());
        assertEquals(1.0, result.getWeight(), 0.001);
        assertEquals(1.0, result.getUpperBound(), 0.001);
        assertEquals(0.0, result.getLowerBound(), 0.001);
        assertEquals("", result.getName());
    }

    /**
     * Run the String getInf() method test.
     */
    @Test
    public void testGetInf_1() {
        final String result = fixture.getInf();

        // add additional test code here
        assertEquals(InfluenceType.NEG, result);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_1() {
        fixture.setRank(1);
        fixture.weight = 1.0;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = true;
        fixture.dist = new PositiveLinearDistribution();
        fixture.norm = new NullNormalizer(fixture, "norm", NormalizationRange.CLASS);

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_2() {
        fixture.weight = 0.5;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = false;
        fixture.maxPoints = 100.0;
        fixture.setInf(InfluenceType.POS);
        fixture.setRank(1);
        fixture.dist = new PositiveLinearDistribution();
        fixture.norm = new NullNormalizer(fixture, "norm", NormalizationRange.CLASS);

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(0.55, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_3() {
        fixture.weight = 1.0;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = false;
        fixture.maxPoints = 100.0;
        fixture.setInf(InfluenceType.POS);
        fixture.dist = null;
        fixture.norm = new NullNormalizer(fixture, "norm", NormalizationRange.CLASS);

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_4() {
        fixture.weight = 0.5;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = true;
        fixture.setRank(1);
        fixture.maxPoints = 100.0;
        fixture.setInf(InfluenceType.POS);
        fixture.dist = new PositiveLinearDistribution();
        fixture.norm = null;

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(0.55, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_5() {
        fixture.weight = 1.0;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = false;
        fixture.setInf(InfluenceType.NEG);
        fixture.dist = null;
        fixture.norm = null;

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_6() {
        fixture.weight = 1.0;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = false;
        fixture.setInf(InfluenceType.POS);
        fixture.dist = null;
        fixture.norm = null;

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_7() {
        fixture.weight = 1.0;
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = false;
        fixture.setInf(null);
        fixture.dist = null;
        fixture.norm = null;

        final double result = fixture.getValue();

        // add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_1() {
        final String inf = "";

        try
        {
            fixture.setInf(inf);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            assertEquals(InfluenceType.NEG, fixture.getInf());
        }
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
        final String inf = "Test";

        try
        {
            fixture.setInf(inf);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            assertEquals(InfluenceType.NEG, fixture.getInf());
        }
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_4() {
        final String inf = InfluenceType.POS;

        try
        {
            fixture.setInf(inf);
            assertEquals(InfluenceType.POS, fixture.getInf());
        }
        catch (final IllegalArgumentException e)
        {
            fail();
        }
    }

    /**
     * Run the void setInf(String) method test.
     */
    @Test
    public void testSetInf_5() {
        final String inf = InfluenceType.NEG;

        try
        {
            fixture.setInf(inf);
            assertEquals(InfluenceType.NEG, fixture.getInf());
        }
        catch (final IllegalArgumentException e)
        {
            fail();
        }
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
        final MeasureNode src = new MeasureNode(graph, "measure", "owner");
        src.setType(MeasureType.NUMBER);
        src.setProcessor(new WeightedSumEvaluator(src));
        final FactorNode dest = new FactorNode(graph, "factor", "owner");
        final ValueNode srcsrc = new ValueNode(graph, "key", "owner", "tool");
        srcsrc.addValue(10.0);
        srcsrc.addValue(100.0);

        fixture = new MeasureToFactorNumberEdge("", src, dest, InfluenceEffect.NEGATIVE);
        graph.addEdge(srcsrc, src, new ValueToMeasureEdge("v2m", srcsrc, src));
        graph.addEdge(src, dest, fixture);

        fixture.weight = 1.0;
        fixture.setRank(1);
        fixture.lowerBound = 0.0;
        fixture.upperBound = 1.0;
        fixture.usesLinearDist = true;
        fixture.maxPoints = 100.0;
        fixture.dist = new PositiveLinearDistribution();
        fixture.norm = new NullNormalizer(fixture, "norm", NormalizationRange.CLASS);
    }
}
