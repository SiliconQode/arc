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
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import dev.siliconcode.arc.quality.quamoco.processor.LinearDistribution;
import dev.siliconcode.arc.quality.quamoco.processor.Normalizer;
import dev.siliconcode.arc.quality.quamoco.processor.lineardist.NegativeLinearDistribution;
import dev.siliconcode.arc.quality.quamoco.processor.lineardist.PositiveLinearDistribution;
import dev.siliconcode.arc.quality.quamoco.processor.normalizers.NullNormalizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>WeightedRankedEdgeTest</code> contains tests for the class
 * <code>{@link WeightedRankedEdge}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class WeightedRankedEdgeTest {

    private WeightedRankedEdge fixture;

    /**
     * Run the LinearDistribution getDist() method test.
     */
    @Test
    public void testGetDist_1() {
        final LinearDistribution result = fixture.getDist();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof PositiveLinearDistribution);
    }

    /**
     * Run the BigDecimal getLowerBound() method test.
     */
    @Test
    public void testGetLowerBound_1() {
        final double result = fixture.getLowerBound();

        // add additional test code here
        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the BigDecimal getMaxPoints() method test.
     */
    @Test
    public void testGetMaxPoints_1() {
        final double result = fixture.getMaxPoints();

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the Normalizer getNormalizer() method test.
     */
    @Test
    public void testGetNormalizer_1() {
        final Normalizer result = fixture.getNormalizer();

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals("LOC", result.getMetric());
    }

    /**
     * Run the BigDecimal getUpperBound() method test.
     */
    @Test
    public void testGetUpperBound_1() {
        final double result = fixture.getUpperBound();

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the BigDecimal getWeight() method test.
     */
    @Test
    public void testGetWeight_1() {
        final double result = fixture.getWeight();

        // add additional test code here
        Assert.assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the boolean isUsesLinearDist() method test.
     */
    @Test
    public void testIsUsesLinearDist_1() {
        final boolean result = fixture.isUsesLinearDist();

        // add additional test code here
        Assert.assertTrue(result);
    }

    /**
     * Run the boolean isUsesLinearDist() method test.
     */
    @Test
    public void testIsUsesLinearDist_2() {
        fixture.setUsesLinearDist(false);
        final boolean result = fixture.isUsesLinearDist();

        // add additional test code here
        Assert.assertFalse(result);
    }

    /**
     * Run the void setDist(LinearDistribution) method test.
     */
    @Test
    public void testSetDist_1() {
        final LinearDistribution lindist = new NegativeLinearDistribution();

        fixture.setDist(lindist);

        Assert.assertSame(lindist, fixture.getDist());
    }

    /**
     * Run the void setLowerBound(BigDecimal) method test.
     */
    @Test
    public void testSetLowerBound_1() {
        final double lowerBound = 0.5;

        try
        {
            fixture.setLowerBound(lowerBound);
            Assert.assertEquals(0.5, fixture.getLowerBound(), 0.001);
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the void setLowerBound(BigDecimal) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBound_2() {
        fixture.setUpperBound(0.0);
        final double lowerBound = 0.5;

        fixture.setLowerBound(lowerBound);
    }

    /**
     * Run the void setMaxPoints(BigDecimal) method test.
     */
    @Test
    public void testSetMaxPoints_1() {
        final double maxPoints = 100.0;

        fixture.setMaxPoints(maxPoints);
        Assert.assertEquals(maxPoints, fixture.getMaxPoints(), 0.001);
    }

    /**
     * Run the void setMaxPoints(BigDecimal) method test.
     */
    @Test
    public void testSetMaxPoints_2() {
        final double maxPoints = 0.0;

        fixture.setMaxPoints(maxPoints);
        Assert.assertEquals(maxPoints, fixture.getMaxPoints(), 0.001);
    }

    /**
     * Run the void setMaxPoints(BigDecimal) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxPoints_3() {
        final double maxPoints = -1.0;

        fixture.setMaxPoints(maxPoints);
    }

    /**
     * Run the void setNormalizer(Normalizer) method test.
     */
    @Test
    public void testSetNormalizer_1() {
        final Normalizer normalizer = new NullNormalizer(fixture, "NOC", NormalizationRange.CLASS);

        fixture.setNormalizer(normalizer);

        // add additional test code here
        Assert.assertNotNull(fixture.getNormalizer());
        Assert.assertEquals("NOC", fixture.getNormalizer().getMetric());
        Assert.assertEquals(NormalizationRange.CLASS, fixture.getNormalizer().getRange());
    }

    /**
     * Run the void setUpperBound(BigDecimal) method test.
     */
    @Test
    public void testSetUpperBound_1() {
        final double upperBound = 0.5;

        try
        {
            fixture.setUpperBound(upperBound);
            Assert.assertEquals(upperBound, fixture.getUpperBound(), 0.001);
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the void setUpperBound(BigDecimal) method test.
     */
    @Test
    public void testSetUpperBound_2() {
        fixture.setLowerBound(1.0);
        final double upperBound = 0.5;

        try
        {
            fixture.setUpperBound(upperBound);
            Assert.fail();
        }
        catch (final IllegalArgumentException e)
        {
            Assert.assertEquals(1.0, fixture.getUpperBound(), 0.001);
        }
    }

    /**
     * Run the void setUsesLinearDist(boolean) method test.
     */
    @Test
    public void testSetUsesLinearDist_1() {
        final boolean usesLinearDist = true;

        fixture.setUsesLinearDist(usesLinearDist);

        Assert.assertTrue(fixture.usesLinearDist);
    }

    /**
     * Run the void setWeight(BigDecimal) method test.
     */
    @Test
    public void testSetWeight_1() {
        final double weight = 1.0;

        try
        {
            fixture.setWeight(weight);
            Assert.assertEquals(weight, fixture.getWeight(), 0.001);
        }
        catch (final IllegalArgumentException e)
        {
            Assert.fail();
        }
    }

    /**
     * Run the void setWeight(BigDecimal) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetWeight_2() {
        final double weight = -1.0;

        fixture.setWeight(weight);
    }

    /**
     * Run the void setWeight(BigDecimal) method test.
     */
    @Test
    public void testSetWeight_3() {
        final double weight = 0.0;

        fixture.setWeight(weight);
        Assert.assertEquals(weight, fixture.getWeight(), 0.001);
    }

    /**
     * Run the void setWeight(BigDecimal) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetWeight_4() {
        final double weight = 2.0;

        fixture.setWeight(weight);
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

        final FactorNode src = new FactorNode(graph, "src", "factor");
        final FactorNode dest = new FactorNode(graph, "src", "factor");
        fixture = new MeasureToMeasureFindingsNumberEdge("edge", src, dest);

        fixture.upperBound = 1.0;
        fixture.norm = new NullNormalizer(fixture, "LOC", NormalizationRange.CLASS);
        fixture.dist = new PositiveLinearDistribution();
        fixture.usesLinearDist = true;
        fixture.weight = 1.0;
        fixture.maxPoints = 1.0;
        fixture.lowerBound = 0.0;
    }
}
