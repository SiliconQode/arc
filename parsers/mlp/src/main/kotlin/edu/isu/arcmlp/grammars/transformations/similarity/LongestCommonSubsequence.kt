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

package edu.isu.arcmlp.grammars.transformations.similarity

/**
 * Finds the longest common subsequence between t1 and t2.
 *
 * Returns a pair of that subsequence's length and one such subsequence that matches.
 *
 * https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
 */
class LongestCommonSubsequence<T>(val t1: List<T>, val t2: List<T>) {
    private val c: Array<IntArray> = Array(t1.size + 1) { IntArray(t2.size + 1) }

    init {
        for (i in 1..t1.size) {
            for (j in 1..t2.size) {
                if (t1[i - 1] == t2[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1
                } else {
                    c[i][j] = maxOf(c[i][j - 1], c[i - 1][j])
                }
            }
        }
    }

    /**
     * The length of the longest common subsequence
     */
    val length: Int = c[t1.size][t2.size]

    /**
     * The two subsequences merged
     */
    val sequence: List<LongestCommonSubsequenceElement<T>> by lazy { extractMergedSequence(t1, t2, c) }

    fun asPair() = length to sequence
}

sealed class LongestCommonSubsequenceElement<T> {
    data class CommonElement<T>(val common: T) : LongestCommonSubsequenceElement<T>()
    data class DifferentElements<T>(val seq1: List<T>, val seq2: List<T>) : LongestCommonSubsequenceElement<T>()
}

private fun <T> extractMergedSequence(t1: List<T>, t2: List<T>, c: Array<IntArray>): List<LongestCommonSubsequenceElement<T>> {
    val items = mutableListOf<LongestCommonSubsequenceElement<T>>()
    var i = t1.size
    var j = t2.size
    var t1Only = mutableListOf<T>()
    var t2Only = mutableListOf<T>()
    // Initial value of null to represent it is unknown if in common or different run
    var inDifferentRun = false
    while (i > 0 || j > 0) {
        if (i > 0 && j > 0 && t1[i - 1] == t2[j - 1]) {
            if (inDifferentRun) {
                t1Only.reverse()
                t2Only.reverse()
                items += LongestCommonSubsequenceElement.DifferentElements(t1Only, t2Only)
                t1Only = mutableListOf()
                t2Only = mutableListOf()
                inDifferentRun = false
            }
            items.add(LongestCommonSubsequenceElement.CommonElement(t1[i - 1]))
            i--
            j--
        } else {
            inDifferentRun = true
            if (i == 0 || (j != 0 && c[i][j - 1] > c[i - 1][j])) {
                t2Only.add(t2[j - 1])
                j--
            } else {
                t1Only.add(t1[i - 1])
                i--
            }
        }
    }
    if (inDifferentRun) {
        t1Only.reverse()
        t2Only.reverse()
        items += LongestCommonSubsequenceElement.DifferentElements(t1Only, t2Only)
    }

    items.reverse()

    return items
}


