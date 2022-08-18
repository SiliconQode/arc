/*
 * MIT License
 *
 * Copyright (c) 2018-2019, Idaho State University, Empirical Software Engineering
 * Laboratory and Isaac Griffith
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
 *
 */

package edu.isu.arcmlp.metrics

import edu.isu.arcmlp.antlr.Not
import edu.isu.arcmlp.antlr.Star
import edu.isu.arcmlp.grammars.*
import edu.isu.arcmlp.grammars.transformations.depthFirstTraversal
import kotlin.math.log2

class EffortMetric : Metric {
    override val code: String = "HAL"
    override val name: String = "Halstead effort"
    override val description: String = "Estimates the amount of effort needed to understand a grammar"

    override fun measure(grammar: Grammar): Double {
        val (operators, operands) = grammar.depthFirstTraversal().partition(::isHalsteadOperator)

        // We only care about the type of operators and not the entire subtree rotted at the operator.
        val mu1 = operators.map(Any::javaClass).toSet().count().toDouble()
        val mu2 = operands.toSet().count().toDouble()
        val eta1 = operators.sumBy { op ->
            when (op) {
                // Counted multiple times because Union/Concatenate with more than two children is equivalent to multiple applications of them.
                is Union, is Concatenate -> {
                    op as RuleOperator
                    op.children().count() - 1
                }
                is Empty, is Star, is Not -> 1
                else -> throw UnsupportedMetricException("Unsupported operand type: ${op.javaClass.name}. Production: $op")
            }
        }.toDouble()
        val eta2 = operands.count().toDouble()

        return mu1 * eta2 * (eta1 + eta2) * log2(mu1 + mu2) / (2 * mu2)
    }

    private fun isHalsteadOperator(rule: Rule): Boolean {
        return rule is RuleOperator || rule is Empty
    }
}