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

package edu.isu.arcmlp.grammars.transformations

import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.transformations.similarity.SimilarAlternativesDetector
import edu.isu.arcmlp.grammars.transformations.similarity.SimilarProductionsMerger
import edu.isu.arcmlp.grammars.transformations.similarity.SimilarTermsDetector
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun combineGrammars(g1: Grammar, g2: Grammar, minSimilarityScore: Double = .5): Grammar {
    logger.info { "Merging grammars\n$g1\nand\n$g2" }
    val trivial = g1 trivialMerge g2
    logger.info { "Trivial merge\n$trivial" }
    val similarityMerger = SimilarProductionsMerger(
            SimilarAlternativesDetector(),
            SimilarTermsDetector(),
            minScore = minSimilarityScore
    )
    return trivial.repeatedlyTransform(
            Grammar::normalized,
            similarityMerger::mergeMostSimilarProductions
    ).dedupeNames()
}


fun combineGrammars(grammars: List<Grammar>, minSimilarityScore: Double = .5): Grammar {
    logger.info { "Merging grammars" }
    val trivial = trivialMerge(grammars)
    logger.info { "Trivial merge\n$trivial" }
    val similarityMerger = SimilarProductionsMerger(
            SimilarAlternativesDetector(),
            SimilarTermsDetector(),
            minScore = minSimilarityScore
    )
    return trivial.repeatedlyTransform(
            Grammar::normalized,
            similarityMerger::mergeMostSimilarProductions
    ).dedupeNames()
}