/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
package dev.siliconcode.arc.quality.metrics.impl

import dev.siliconcode.arc.datamodel.ComponentContainer
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.cfg.ControlFlowGraph
import dev.siliconcode.arc.datamodel.cfg.StatementType
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.annotations.MetricCategory
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition
import dev.siliconcode.arc.quality.metrics.annotations.MetricProperties
import dev.siliconcode.arc.quality.metrics.annotations.MetricScale
import dev.siliconcode.arc.quality.metrics.annotations.MetricScope
import dev.siliconcode.arc.quality.metrics.annotations.MetricType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Number of Local Variables",
        primaryHandle = "NOV",
        otherHandles = ['#LocalVariableDeclarations'],
        description = "Count of all the variables locally defined in a class.",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.ALL,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Size
        ),
        references = [
        ]
)
class NumberOfLocalVariables extends MetricEvaluator {

    @Override
    def measureValue(Measurable node) {
        double total = 0

        if (node instanceof Method) {
//            ControlFlowGraph cfg = node.getCfg()
//            cfg.graph.nodes().each {
//                if (it.getType() == StatementType.VARDECL)
//                    total += 1
//            }
            total = node.getLocalVarCount()
            total += node.getParams().size()
            Measure.of("${repo.getRepoKey()}:NOV").on(node).withValue(total)
        }
        else if (node instanceof Type) {
            node.getMethods().each {
                total += it.getValueFor("${repo.getRepoKey()}:NOV")
            }
            Measure.of("${repo.getRepoKey()}:NOV").on(node).withValue(total)
        } else if (node instanceof ComponentContainer && !(node instanceof Type)) {
            node.getAllTypes().each { Type type ->
                total += type.getValueFor("${repo.getRepoKey()}:NOV")
            }
            Measure.of("${repo.getRepoKey()}:NOV").on(node).withValue(total)
        }

        total
    }
}
