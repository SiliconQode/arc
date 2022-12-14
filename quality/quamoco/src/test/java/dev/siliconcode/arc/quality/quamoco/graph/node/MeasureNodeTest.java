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
import dev.siliconcode.arc.quality.quamoco.graph.edge.ValueToMeasureEdge;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.processor.NullProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The class <code>MeasureNodeTest</code> contains tests for the class
 * <code>{@link MeasureNode}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasureNodeTest {

    /**
     * Run the MeasureNode(DirectedSparseGraph<Node,Edge>,String,String)
     * constructor test.
     */
    @Test
    public void testMeasureNode_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String name = "name";
        final String owner = "owner";

        final MeasureNode result = new MeasureNode(graph, name, owner);

        // TODO: add additional test code here
        assertNotNull(result);
        assertNull(result.getMethod());
        assertEquals(MeasureType.FINDINGS, result.getType());
        assertEquals("owner", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("Node(name=name)", result.toString());
        assertEquals("name", result.getName());
    }

    /**
     * Run the MeasureNode(DirectedSparseGraph<Node,Edge>,String,String,long)
     * constructor test.
     */
    @Test
    public void testMeasureNode_2() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final String name = "name";
        final String owner = "owner";

        final MeasureNode result = new MeasureNode(graph, name, owner);

        // TODO: add additional test code here
        assertNotNull(result);
        assertNull(result.getMethod());
        assertEquals(MeasureType.FINDINGS, result.getType());
        assertEquals("owner", result.getOwnedBy());
        assertEquals("", result.getDescription());
        assertEquals("Node(name=name)", result.toString());
        assertEquals("name", result.getName());
    }

    /**
     * Run the String getMethod() method test.
     */
    @Test
    public void testGetMethod_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "name", "owner");
        fixture.setType(MeasureType.NONE);
        fixture.setMethod("");
        fixture.name = "";
        fixture.value = 1.0;
        fixture.graph = graph;
        fixture.ownedBy = "";
        fixture.description = "";

        final String result = fixture.getMethod();

        // TODO: add additional test code here
        assertEquals("", result);
    }

    /**
     * Run the String getType() method test.
     */
    @Test
    public void testGetType_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "name", "owner");
        fixture.setType(MeasureType.NONE);
        fixture.setMethod("");
        fixture.name = "";
        fixture.value = 1.0;
        fixture.graph = graph;
        fixture.ownedBy = "";
        fixture.description = "";

        final MeasureType result = fixture.getType();

        // TODO: add additional test code here
        assertEquals(MeasureType.NONE, result);
    }

    /**
     * Run the BigDecimal getValue() method test.
     */
    @Test
    public void testGetValue_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "fixture", "fixture");
        final ValueNode value = new ValueNode(graph, "value", "value", "");
        final ValueToMeasureEdge edge = new ValueToMeasureEdge("edge", fixture, value);
        fixture.setProcessor(new NullProcessor(fixture));
        fixture.setType(MeasureType.NUMBER);
        fixture.value = 1.0;

        graph.addEdge(value, fixture, edge);

        final double result = fixture.getValue();

        assertEquals(1.0, result, 0.001);
        fixture.setValue(1.0);
        assertEquals(1.0, fixture.value, 0.001);
    }

    /**
     * Run the BigDecimal getValue() method test.
     */
    @Test
    public void testGetValue_2() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "fixture", "fixture");
        final ValueNode value = new ValueNode(graph, "value", "value", "");
        final ValueToMeasureEdge edge = new ValueToMeasureEdge("edge", fixture, value);

        fixture.setProcessor(new NullProcessor(fixture));
        fixture.setType(MeasureType.FINDINGS);

        graph.addEdge(value, fixture, edge);

        assertEquals(0.0, fixture.getValue(), 0.001);
    }

    /**
     * Run the BigDecimal getValue() method test.
     */
    @Test
    public void testGetValue_3() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
//        final MeasureNode fixture = new MeasureNode(graph, "fixture", "fixture");
//        final ValueNode value = new ValueNode(graph, "value", "value", "");
//        final ValueToMeasureEdge edge = new ValueToMeasureEdge("edge", fixture, value);
//        fixture.setProcessor(new NullProcessor(fixture));
//        fixture.setType(MeasureType.NUMBER);
//
//        graph.addEdge(value, fixture, edge);
//
//        final double result = fixture.getValue();
//
//        assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the String getXMLTag() method test.
     */
    @Test
    public void testGetXMLTag_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "name", "owner");
        fixture.setType(MeasureType.NUMBER);
        fixture.setMethod("");
        fixture.name = "";
        fixture.value = 1.0;
        fixture.graph = graph;
        fixture.ownedBy = "";
        fixture.description = "";

        final String result = fixture.getXMLTag();

        // TODO: add additional test code here
        assertEquals("<nodes name=\"\" description=\"\" owner=\"\" type=\"MEASURE\">\n" +
                "\t</nodes>", result);
    }

    /**
     * Run the void setMethod(String) method test.
     */
    @Test
    public void testSetMethod_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "name", "owner");
        fixture.setType(MeasureType.NONE);
        fixture.setMethod("");
        fixture.name = "";
        fixture.value = 1.0;
        fixture.graph = graph;
        fixture.ownedBy = "";
        fixture.description = "";
        final String method = "";

        fixture.setMethod(method);

        // TODO: add additional test code here
        assertEquals(method, fixture.getMethod());
    }

    /**
     * Run the void setType(String) method test.
     */
    @Test
    public void testSetType_1() {
        final MutableNetwork<Node, Edge> graph = NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(false)
                .expectedNodeCount(10000)
                .expectedEdgeCount(10000)
                .build();
        final MeasureNode fixture = new MeasureNode(graph, "name", "owner");
        fixture.setType(MeasureType.NONE);
        fixture.setMethod("");
        fixture.name = "";
        fixture.value = 1.0;
        fixture.graph = graph;
        fixture.ownedBy = "";
        fixture.description = "";
        final MeasureType type = MeasureType.NUMBER;

        fixture.setType(type);

        assertEquals(type, fixture.getType());
    }
}
