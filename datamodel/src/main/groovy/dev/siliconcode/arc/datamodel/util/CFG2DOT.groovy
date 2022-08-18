/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc.datamodel.util

import com.google.common.graph.EndpointPair
import com.google.common.graph.Graph
import dev.siliconcode.arc.datamodel.cfg.ControlFlowNode
import lombok.extern.slf4j.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CFG2DOT {

    static String generateDot(Graph<ControlFlowNode> graph, String name = null) {
        StringBuilder builder = new StringBuilder()
        if (name)
            builder.append("digraph \"" + name + "\" {\n")
        else
            builder.append("digraph {\n")

        for (EndpointPair<ControlFlowNode> pair : graph.edges()) {
            builder.append("  " + pair.source().getType() + "_" + pair.source().getLabel() + " -> " + pair.target().getType() + "_" + pair.target().getLabel() + "\n")
        }

        builder.append("}")

        builder.toString()
    }

    static void saveDOTFile(String filename, Graph<ControlFlowNode> graph, String name = "") {
        String dot = generateDot(graph, name)

        Path p = Paths.get(filename)
        PrintWriter pw = new PrintWriter(Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
        try {
            pw.print(dot)
        } catch (IOException e) {
            //log.error(e.getMessage())
        } finally {
            pw.close()
        }
    }
}
