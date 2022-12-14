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

import dev.siliconcode.arc.quality.quamoco.graph.node.Node;

import java.util.List;

/**
 * The interface for edges used in the Quamoco processing graph.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface Edge {

    /**
     * @return The identifying number of this edge.
     */
    long getId();

    /**
     * @return The name of this edge.
     */
    String getName();

    /**
     * Retrieves the value associated with this edge from the perspective of the
     * calling node within the given graph.
     *
     * @return The value
     */
    double getValue();

    /**
     * Sets the identifying number for this edge to the given value. Note that
     * the use of this method may invalidate the uniqueness of the identifier.
     *
     * @param id
     *            The new identifying number.
     */
    void setId(long id);

    /**
     * @return The set of values provided by the incoming side of the edge
     */
    List<Double> getValues();

    Node getSource();

    Node getDest();
}
