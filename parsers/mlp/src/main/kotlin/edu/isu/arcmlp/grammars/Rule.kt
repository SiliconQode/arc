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

import edu.isu.arcmlp.grammars.repr.Precedence
import edu.isu.arcmlp.grammars.repr.UnambiguousPrecedence

interface Rule {

    /**
     * Returns the default representation of the production.
     */
    fun defaultRepr(
            builder: StringBuilder,
            precedence: Precedence<Rule>,
            childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder
    ): StringBuilder
}

interface RuleOperator : Rule {
    /**
     * Replaces the children of this object with the specified values.
     * Should return the same object if there are no children.
     */
    fun mapChildren(newChildren: List<Rule>): Rule

    /**
     * Returns the children of a term in a production. A rule doesn't return any children.
     */
    fun children(): Iterable<Rule>
}

fun RuleOperator.mapChildren(mapper: (Rule) -> Rule) = mapChildren(children().map(mapper))

interface RuleOperand : Rule {
    override fun defaultRepr(builder: StringBuilder, precedence: Precedence<Rule>, childrenReprs: (StringBuilder, Precedence<Rule>, child: Rule) -> StringBuilder): StringBuilder {
        return defaultRepr(builder)
    }

    fun defaultRepr(builder: StringBuilder): StringBuilder
}


/**
 * Represents a term of a production with no productions inside of it.
 */
interface Symbol : RuleOperand, UnambiguousPrecedence

interface TerminalSymbol : Symbol
/**
 * All non terminals must have names.
 */
interface NonTerminalSymbol : Symbol, Nameable

/**
 * Signifies that this production is simplifiable
 */
interface Simplifiable : Rule {
    fun simplify(): Rule
}

/**
 * Signifies that the elements of this production are nestable.
 *
 * E.g. let's say the type is called Foo, and its parse tree is represented by
 * Foo(child1, child2, ...). Then implementing this interface means that Foo(c1, c2, Foo(c3, c4, c5), c6)
 * is equivalent to Foo(c1, c2, c3, c4, c5, c6)
 */
interface Nestable : RuleOperator {
    fun unNest(children: List<Rule>): Rule
    fun canUnnest(rule: Nestable): Boolean
}

/**
 * Signifies the given terminal can be named.
 */
interface Nameable : Symbol {
    val name: String

    /**
     * Gives a new name to this nameable.
     * If the name is the same, should return a new object.
     */
    fun rename(name: String): Nameable
}
