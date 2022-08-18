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

import edu.isu.arcmlp.cli.DefaultGrammarLoader
import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.transformations.GrammarMerger
import krangl.DataFrame
import krangl.asStrings
import krangl.readCSV
import java.io.File

class TrivialGrammarMergeMetricsCsvFiller(
        private val fileNamesFirstColumn: String,
        private val fileNamesSecondColumn: String,
        private val grammarCsvExtractor: GrammarCsvExtractor,
        private val merger: GrammarMerger,
        private val metricsCsvFiller: MetricsCsvFiller,
        private val metricsPrefix: String
) {
    fun fillCsv(input: DataFrame): DataFrame {
        val firstToMerge = grammarCsvExtractor.extractGrammars(input, fileNamesFirstColumn)
        val secondToMerge = grammarCsvExtractor.extractGrammars(input, fileNamesSecondColumn)

        val total = firstToMerge.size
        var current = 0
        val merged = firstToMerge.zip(secondToMerge) { first, second ->
            println("Merging $current/$total")
            current++
            merger.merge(first, second)
        }

        return metricsCsvFiller.fillMetrics(input, merged, metricsPrefix)
    }
}

class ReferencedGrammarCsvExtractor(
        extractor: GrammarCsvExtractor,
        nameColumn: String,
        filesColumn: String,
        grammarCsvFile: File
) : GrammarCsvExtractor {
    private val namesToGrammars: Map<String, Grammar>

    init {
        val csv = DataFrame.readCSV(grammarCsvFile)
        if (nameColumn !in csv.names) {
            error("missing column")
        }
        val names = csv[nameColumn].asStrings().toList().requireNoNulls()
        val grammars = extractor.extractGrammars(csv, filesColumn)
        namesToGrammars = names.zip(grammars).toMap()
    }

    override fun extractGrammars(data: DataFrame, col: String): List<Grammar> {
        return data[col].asStrings().requireNoNulls().map { namesToGrammars[it] ?: error("Unknown grammar $it") }
    }

}

interface GrammarCsvExtractor {
    fun extractGrammars(data: DataFrame, col: String): List<Grammar>
}

class FilenamesGrammarCsvExtractor(
        private val grammarLoader: DefaultGrammarLoader,
        private val parentDirectory: File? = null
) : GrammarCsvExtractor {
    override fun extractGrammars(data: DataFrame, col: String): List<Grammar> {
        return data[col].asStrings().map { fileNames ->
            val split = fileNames?.split(",") ?: error("Missing files for grammar")
            val files = if (parentDirectory != null) {
                split.map { parentDirectory.resolve(it) }
            } else {
                split.map { File(it) }
            }
            val grammar = grammarLoader.loadAndLinkFiles(files)
            grammar
        }
    }
}