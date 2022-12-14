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
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The class <code>NormalizationNodeTest</code> contains tests for the class
 * <code>{@link NormalizationNode}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class NormalizationNodeTest {

    /**
     * Run the NormalizationNode(DirectedSparseGraph<Node,Edge>,String,String)
     * constructor test.
     */
    @Test
    public void testNormalizationNode_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String name = "norm";
        final String owner = "node";

        final NormalizationNode result = new NormalizationNode(graph, name, owner);

        // add additional test code here
        assertNotNull(result);
        assertNull(result.getMethod());
        assertEquals(MeasureType.FINDINGS, result.getType());
        assertEquals("node", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("Node(name=norm)", result.toString());
        assertEquals("norm", result.getName());
    }

    /**
     * Run the NormalizationNode(DirectedSparseGraph
     * <Node,Edge>,String,String,long) constructor test.
     */
    @Test
    public void testNormalizationNode_2() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String name = "norm";
        final String owner = "node";

        final NormalizationNode result = new NormalizationNode(graph, name, owner);

        // add additional test code here
        assertNotNull(result);
        assertNull(result.getMethod());
        assertEquals(MeasureType.FINDINGS, result.getType());
        assertEquals("node", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("Node(name=norm)", result.toString());
        assertEquals("norm", result.getName());
    }

    /**
     * Run the double getValue() method test.
     */
    @Test
    public void testGetValue_1() {
        final NormalizationNode fixture = new NormalizationNode(null, "norm", "norm");

        final double result = fixture.getValue();
        assertEquals(0.0, result, 0.001);
    }
}
