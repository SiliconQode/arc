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

class ComplexityMetric : Metric {
    override val code: String = "MCC"
    override val name: String = "McCabe Cyclomatic Complexity"
    override val description: String = """
        Measures the number of linear independent paths through code.
        In the case of grammars, this is interpreted as the number of different options can happen at each production
    """.trimIndent()

    override fun measure(grammar: Grammar): Double {
        return grammar.productions.values.sumBy { measure(it.rule) }.toDouble()
    }
}

private fun measure(rule: Rule): Int = when (rule) {
    is Symbol, is Concatenate, is Not, is Empty -> 0
    is Union -> rule.alts.size - 1
    is Star -> 1
    else -> throw UnsupportedMetricException("Unsupported production type ${rule::class.qualifiedName}. Production: $rule")
} + when (rule) {
    is RuleOperator -> rule.children().sumBy(::measure)
    is RuleOperand -> 0
    else -> error("Unexpected rule node")
}