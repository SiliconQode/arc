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

import edu.isu.arcmlp.grammars.Concatenate
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.Union

class SimilarTermsDetector : SimilarityDetector {
    override val name: String = "similar terms detector"

    override fun calculateSimilarity(p1: Rule, p2: Rule): SimilarityResult {
        if (p1 !is Concatenate || p2 !is Concatenate) {
            return NotSimilar
        }
        return SimilarityBetweenTerms(p1, p2)
    }
}

class SimilarityBetweenTerms(t1: Concatenate, t2: Concatenate) : SimilarityResult {
    private val lcs = LongestCommonSubsequence(t1.terms, t2.terms)

    override val similarity: Double
        get() = calculateScore(lcs)
    override val merged: Rule
        get() = toTerms(lcs.sequence)
}

private fun toTerms(commonParts: List<LongestCommonSubsequenceElement<Rule>>): Rule {
    return Concatenate(commonParts.map {
        when (it) {
            is LongestCommonSubsequenceElement.CommonElement -> it.common
            is LongestCommonSubsequenceElement.DifferentElements -> Union(Concatenate(it.seq1), Concatenate(it.seq2))
        }
    }).simplify()
}

private fun calculateScore(lcs: LongestCommonSubsequence<Rule>) = 2.0 * lcs.length / (lcs.t1.size + lcs.t2.size)
