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

import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.NonTerminalSymbol
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.toProductionMap
import edu.isu.arcmlp.grammars.transformations.map
import edu.isu.arcmlp.grammars.transformations.mergeNonTerminals
import edu.isu.arcmlp.grammars.transformations.plus
import mu.KotlinLogging

val logger = KotlinLogging.logger { }

class SimilarProductionsMerger(
        private val similarityDetectors: List<SimilarityDetector>,
        private val minScore: Double = .5,
        private val nonTerminalSymbolMerger: (List<NonTerminalSymbol>) -> NonTerminalSymbol = ::mergeNonTerminals
) {
    constructor(
            vararg detectors: SimilarityDetector,
            minScore: Double = .5,
            nonTerminalSymbolMerger: (List<NonTerminalSymbol>) -> NonTerminalSymbol = ::mergeNonTerminals) :
            this(detectors.toList(), minScore, nonTerminalSymbolMerger)

    init {
        require(similarityDetectors.size > 1) { "Must be using at least one similarity detector." }
    }


    /**
     * Finds the two most similar productions in [g] and merges them
     * if their similarity score is greater than or equal to [minScore].
     * Returns the result.
     */
    fun mergeMostSimilarProductions(g: Grammar): Grammar {
        var bestDetectorName: String? = null
        var bestSimilarity: SimilarityResult? = null
        var bestR1: NonTerminalSymbol? = null
        var bestR2: NonTerminalSymbol? = null
        var bestScore = Double.NEGATIVE_INFINITY

        // Faster to iterate over an array than to iterate over a map
        val rulesToProductions = g.productions.map { it.value }.toTypedArray()

        for (i in rulesToProductions.indices) {
            val (rule1, p1) = rulesToProductions[i]
            for (j in i + 1..rulesToProductions.lastIndex) {
                val (rule2, p2) = rulesToProductions[j]

                for (detector in similarityDetectors) {
                    val similarity = detector.calculateSimilarity(p1, p2)

                    if (similarity.similarity > bestScore) {
                        bestScore = similarity.similarity
                        bestSimilarity = similarity
                        bestDetectorName = detector.name
                        bestR1 = rule1
                        bestR2 = rule2
                    }
                }
            }
        }

        if (bestScore < minScore) return g
        bestR1!!
        bestR2!!

        val merged = bestSimilarity!!.merged

        logger.info {
            """
            Merging (similarity score $bestScore) via $bestDetectorName: 
            $bestR1 = ${g.production(bestR1)}
            $bestR2 = ${g.production(bestR2)}
            to
            $merged
            """.trimIndent()
        }

        val newRule = nonTerminalSymbolMerger(listOf(bestR1, bestR2))

        return (g + mapOf(newRule to merged).toProductionMap()).map(mapOf<Rule, Rule>(bestR1 to newRule, bestR2 to newRule))
    }
}

