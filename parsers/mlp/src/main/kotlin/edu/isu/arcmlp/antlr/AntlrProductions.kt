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

package edu.isu.arcmlp.antlr

import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.RuleOperator
import edu.isu.arcmlp.grammars.TerminalSymbol
import edu.isu.arcmlp.grammars.repr.Precedence
import edu.isu.arcmlp.grammars.repr.UnambiguousPrecedence
import edu.isu.arcmlp.grammars.repr.optionallyParenthesize
import org.apache.commons.text.StringEscapeUtils

object Dot : TerminalSymbol {
    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append(".")
    }
}

data class Not(val child: Rule) : RuleOperator {

    override fun mapChildren(newChildren: List<Rule>): Rule {
        require(newChildren.size == 1) { "Must have exactly one child" }
        return Not(newChildren.single())
    }

    override fun children(): Iterable<Rule> = listOf(child)

    override fun defaultRepr(builder: StringBuilder, precedence: Precedence<Rule>, childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder): StringBuilder {
        return optionallyParenthesize(builder.append('~'), precedence, child, childrenReprs)
    }
}

data class UnicodeCodepointRange(internal val start: Int, internal val end: Int) : TerminalSymbol {
    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        val s1 = Character.toChars(start)!!.joinToString("")
        val s2 = Character.toChars(end)!!.joinToString("")
        val e1 = StringEscapeUtils.escapeJava(s1)
        val e2 = StringEscapeUtils.escapeJava(s2)
        return builder.append("'$e1' .. '$e2'")
    }
}

data class CharClass(val inside: String) : TerminalSymbol {
    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append("\\p{$inside}")
    }
}

data class Star(val child: Rule) : RuleOperator, UnambiguousPrecedence {
    override fun mapChildren(newChildren: List<Rule>): Rule {
        require(newChildren.size == 1) { "Must contain exactly one child" }
        return Star(newChildren.single())
    }

    override fun children(): Iterable<Rule> {
        return listOf(child)
    }

    override fun defaultRepr(builder: StringBuilder, precedence: Precedence<Rule>, childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder): StringBuilder {
        return optionallyParenthesize(builder, precedence, child, childrenReprs).append('*')
    }
}