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

interface Precedence<T> {
    fun compare(first: T, second: T): PrecedenceResult {
        val p0 = precedenceOf(first) ?: return PrecedenceResult.Unknown
        val p1 = precedenceOf(second) ?: return PrecedenceResult.Unknown

        return when {
            p0 < p1 -> PrecedenceResult.Lower
            p0 > p1 -> PrecedenceResult.Higher
            p0 == p1 -> PrecedenceResult.Same
            else -> error("Should never happen")
        }
    }

    fun precedenceOf(rule: T): Int?
}

enum class PrecedenceResult {
    Higher {
        // Used a getter so a direct circular dependency isn't introduced.
        override val opposite get() = Lower
    },
    Lower {
        override val opposite = Higher
    },
    Same {
        override val opposite = Same
    },
    Unknown {
        override val opposite = Unknown
    };

    abstract val opposite: PrecedenceResult
}

/**
 * If something has this interface, its representation is unambiguous.
 * These rules have the highest possible precedence.
 */
interface UnambiguousPrecedence

