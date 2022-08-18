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

package edu.isu.arcmlp.grammars.repr

import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.RuleOperator
import mu.KotlinLogging

val logger = KotlinLogging.logger { }

/**
 * Meant to used by operator classes to delegate a bunch of the brute work to.
 */
class NAryOperator(private val separator: String, private val children: Collection<Rule>) : RuleOperator {
    override fun mapChildren(newChildren: List<Rule>): Rule {
        logger.warn { "Class should probably override this function themselves." }
        return NAryOperator(separator, newChildren)
    }

    override fun children(): Iterable<Rule> {
        return children
    }

    override fun defaultRepr(
            builder: StringBuilder,
            precedence: Precedence<Rule>,
            childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder
    ): StringBuilder {
        when (children.count()) {
            0 -> builder.append("($separator)")
            1 -> {
                builder.append("(")
                appendAllWithSeparators(separator, builder, precedence, childrenReprs)
                builder.append("$separator)")
            }
            else -> appendAllWithSeparators(separator, builder, precedence, childrenReprs)
        }
        return builder
    }
}