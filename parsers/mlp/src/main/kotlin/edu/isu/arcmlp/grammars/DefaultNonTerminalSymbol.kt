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

import edu.isu.arcmlp.utils.concurrentIntSupplier
import edu.isu.arcmlp.utils.intSupplier

fun nonTerminalSymbol(name: String = "") = DefaultNonTerminalSymbol.DefaultFactory(name)

data class DefaultNonTerminalSymbol(override val name: String, override val id: Int) : NonTerminalSymbol, Nameable, HasId<DefaultNonTerminalSymbol> {
    override fun changeId(newId: Int): DefaultNonTerminalSymbol = copy(id = newId)

    /**
     * Creates a rule factory generating ids starting at one.
     */
    class Factory(val supplier: () -> Int = intSupplier()) {
        operator fun invoke(name: String = "") = DefaultNonTerminalSymbol(name, supplier())
    }

    companion object {
        /**
         * A threadsafe default factory
         */
        val DefaultFactory = Factory(concurrentIntSupplier())
    }

    override fun defaultRepr(builder: StringBuilder): StringBuilder {
        return builder.append(toString())
    }

    override fun rename(name: String) = DefaultNonTerminalSymbol(name, id)

    override fun toString(): String {
        return "<$name id=$id>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DefaultNonTerminalSymbol

        // Order switched from default because comparing ids is faster
        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    /**
     * Cache hashcode to increase performance
     */
    private val hashCode: Int by lazy {
        var result = name.hashCode()
        result = 31 * result + id
        result
    }

    override fun hashCode(): Int = hashCode
}