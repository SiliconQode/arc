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

package edu.isu.arcmlp.grammars

import edu.isu.arcmlp.grammars.repr.NAryOperator
import edu.isu.arcmlp.grammars.repr.UnambiguousPrecedence
import edu.isu.arcmlp.grammars.repr.toStringImpl
import edu.isu.arcmlp.utils.concurrentIntSupplier
import edu.isu.arcmlp.utils.intSupplier
import org.apache.commons.text.StringEscapeUtils

data class Union(val alts: Set<Rule>) :
        RuleOperator by NAryOperator(" | ", alts),
        Simplifiable,
        Nestable {
    override fun unNest(children: List<Rule>): Rule {
        return Union(children.flatMap { child ->
            when (child) {
                is Union -> child.alts
                else -> listOf(child)
            }
        })
    }

    override fun canUnnest(rule: Nestable): Boolean {
        return rule is Union
    }

    override fun simplify(): Rule {
        return when (alts.size) {
            0 -> Empty
            1 -> alts.single()
            else -> this
        }
    }

    override fun mapChildren(newChildren: List<Rule>): Rule {
        val s = newChildren.toSet()
        if (s == alts) return this
        return Union(s)
    }

    constructor(vararg alts: Rule) : this(alts.toSet())
    constructor(alts: Iterable<Rule>) : this(alts.toSet())

    override fun toString() = toStringImpl()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Union

        if (alts != other.alts) return false

        return true
    }


    private val hash by lazy { alts.hashCode() }

    override fun hashCode(): Int = hash
}

data class Concatenate(val terms: List<Rule>) : RuleOperator by NAryOperator(" ", terms), Simplifiable, Nestable {
    override fun unNest(children: List<Rule>): Rule {
        return Concatenate(children.flatMap { child ->
            when (child) {
                is Concatenate -> child.terms
                else -> listOf(child)
            }
        })
    }

    override fun canUnnest(rule: Nestable): Boolean {
        return rule is Concatenate
    }

    constructor(vararg terms: Rule) : this(terms.toList())

    override fun simplify(): Rule {
        val simplifiedTerms = terms.filter { it !is Empty }
        return when (simplifiedTerms.size) {
            0 -> Empty
            1 -> simplifiedTerms.single()
            else -> Concatenate(simplifiedTerms)
        }
    }

    override fun mapChildren(newChildren: List<Rule>): Rule {
        return if (newChildren == terms) {
            // Return this to speed up == checks and allow caching of hash codes to be more effective
            this
        } else {
            Concatenate(newChildren)
        }
    }

    override fun toString() = toStringImpl()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Concatenate

        if (terms != other.terms) return false

        return true
    }

    private val hash: Int by lazy { terms.hashCode() }

    override fun hashCode(): Int = hash


}

data class Literal(val literal: String) : TerminalSymbol, Simplifiable {
    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append('"').append(StringEscapeUtils.escapeJava(literal)).append('"')
    }

    override fun simplify(): Rule = when (literal) {
        "" -> Empty
        else -> this
    }

    override fun toString() = toStringImpl()
}

fun String.toLiteral(): Literal = Literal(this)

object Empty : RuleOperand, UnambiguousPrecedence {
    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append("ϵ")
    }

    override fun toString() = toStringImpl()
}

fun unknownRule(name: String = "???") = UndefinedNonTerminalSymbol.DefaultFactory(name)

data class UndefinedNonTerminalSymbol(override val name: String, override val id: Int) : NonTerminalSymbol, Nameable, HasId<UndefinedNonTerminalSymbol> {
    override fun changeId(newId: Int): UndefinedNonTerminalSymbol = copy(id = newId)

    class Factory(val supplier: () -> Int = intSupplier()) {
        operator fun invoke(name: String = "???") = UndefinedNonTerminalSymbol(name, supplier())
    }

    companion object {
        val DefaultFactory = Factory(concurrentIntSupplier())
    }

    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append(toString())
    }

    override fun rename(name: String) = copy(name = name)
    override fun toString(): String {
        return "<$name id=$id unknown>"
    }
}