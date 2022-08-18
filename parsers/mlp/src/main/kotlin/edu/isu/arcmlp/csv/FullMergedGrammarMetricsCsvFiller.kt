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

package edu.isu.arcmlp.csv

import edu.isu.arcmlp.grammars.GrammarSaver
import edu.isu.arcmlp.grammars.transformations.GrammarFullMerger
import krangl.DataFrame
import krangl.asDoubles
import krangl.asStrings

class FullMergedGrammarMetricsCsvFiller(
        private val fileNamesFirstColumn: String,
        private val fileNamesSecondColumn: String,
        private val similarityScoresColumn: String,
        private val outputFileColumn: String,
        private val grammarCsvExtractor: GrammarCsvExtractor,
        private val merger: GrammarFullMerger,
        private val metricsCsvFiller: MetricsCsvFiller,
        private val grammarSaver: GrammarSaver,
        private val metricsPrefix: String
) {
    fun fillCsv(input: DataFrame): DataFrame {
        val firstToMerge = grammarCsvExtractor.extractGrammars(input, fileNamesFirstColumn)
        val secondToMerge = grammarCsvExtractor.extractGrammars(input, fileNamesSecondColumn)
        val simScores = input[similarityScoresColumn].asDoubles().requireNoNulls().toList()
        val files = mutableListOf<String>()
        val total = firstToMerge.size
        val names1 = input[fileNamesFirstColumn].asStrings().requireNoNulls().toList()
        val names2 = input[fileNamesSecondColumn].asStrings().requireNoNulls().toList()

        var i = 0
        val merged = firstToMerge.zip(secondToMerge) { first, second ->
            println("Merging $i/$total")
            val g = merger.merge(first, second, simScores[i])
            println("Saving $i/$total")
            files += grammarSaver.saveGrammar("${names1[i]}+${names2[i]}", g).toString()
            i++
            g
        }

        val withFileNames = input.addColumn(outputFileColumn) { files }

        return metricsCsvFiller.fillMetrics(withFileNames, merged, metricsPrefix)
    }
}