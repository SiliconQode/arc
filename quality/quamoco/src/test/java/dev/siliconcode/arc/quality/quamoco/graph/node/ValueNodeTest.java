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
package dev.siliconcode.arc.quality.quamoco.graph.node;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The class <code>ValueNodeTest</code> contains tests for the class
 * <code>{@link ValueNode}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ValueNodeTest {

    private ValueNode fixture;

    /**
     * Run the ValueNode(DirectedSparseGraph<Node,Edge>,String,String,String)
     * constructor test.
     */
    @Test
    public void testValueNode_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String key = "node";
        final String owner = "owner";
        final String tool = "tool";

        final ValueNode result = new ValueNode(graph, key, owner, tool);

        // TODO: add additional test code here
        assertNotNull(result);
        assertEquals("tool", result.getTool());
        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals("node", result.getKey());
        assertEquals("owner", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("node", result.getName());
    }

    /**
     * Run the ValueNode(DirectedSparseGraph
     * <Node,Edge>,String,String,String,long) constructor test.
     */
    @Test
    public void testValueNode_2() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String key = "node";
        final String owner = "owner";
        final String tool = "tool";

        final ValueNode result = new ValueNode(graph, key, owner, tool);

        // TODO: add additional test code here
        assertNotNull(result);
        assertEquals("tool", result.getTool());
        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals("node", result.getKey());
        assertEquals("owner", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("node", result.getName());
    }

    /**
     * Run the String getKey() method test.
     */
    @Test
    public void testGetKey_1() {
        final String result = fixture.getKey();

        // TODO: add additional test code here
        assertEquals("node", result);
    }

    /**
     * Run the String getTool() method test.
     */
    @Test
    public void testGetTool_1() {
        final String result = fixture.getTool();

        // TODO: add additional test code here
        assertEquals("tool", result);
    }

    /**
     * Run the BigDecimal getValue() method test.
     */
    @Test
    public void testGetValue_1() {
        fixture.addValue(1.0);
        fixture.addValue(1.0);
        fixture.addValue(1.0);
        fixture.addValue(-2.0);

        final double result = fixture.getValue();

        // TODO: add additional test code here
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Run the void setKey(String) method test.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_1() {
        String key = "";

        fixture.setKey(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_2() {
        String key = null;

        fixture.setKey(key);
    }

    @Test
    public void testSetKey() {
        String key = "other";
        fixture.setKey(key);

        // TODO: add additional test code here
        assertEquals(key, fixture.getKey());
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
        fixture = new ValueNode(graph, "node", "owner", "tool");
        fixture.graph = graph;
    }
}
