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

import edu.isu.arcmlp.bnf.BNFPrecedence
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.RuleOperator

fun <T> T.optionallyParenthesize(
        builder: StringBuilder,
        precedence: Precedence<T>,
        child: T,
        childRepr: (StringBuilder, Precedence<T>, child: T) -> StringBuilder
): StringBuilder {
    return if (child !is UnambiguousPrecedence && precedence.compare(this, child) != PrecedenceResult.Lower) {
        builder.append("(")
        val newBuilder = childRepr(builder, precedence, child)
        newBuilder.append(")")
    } else {
        childRepr(builder, precedence, child)
    }
}

fun RuleOperator.appendAllWithSeparators(
        separator: String,
        builder: StringBuilder,
        precedence: Precedence<Rule>,
        childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder
): StringBuilder {
    var first = true
    var currentBuilder = builder

    for (child in children()) {
        if (!first) {
            currentBuilder.append(separator)
        } else {
            first = false
        }

        currentBuilder = optionallyParenthesize(currentBuilder, precedence, child, childrenReprs)
    }

    return currentBuilder
}

/**
 * Added to automatically implement toString for productions. Unfortunately an interface
 * can't provide default methods to methods of the Any/Object class.
 */
fun Rule.toStringImpl(): String {
    fun defaultChildrenRepr(builder: StringBuilder, precedence: Precedence<Rule>, child: Rule): StringBuilder {
        return child.defaultRepr(builder, precedence, ::defaultChildrenRepr)
    }

    return defaultChildrenRepr(StringBuilder(), BNFPrecedence, this).toString()
}