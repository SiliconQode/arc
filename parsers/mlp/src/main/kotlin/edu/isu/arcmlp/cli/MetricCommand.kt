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

package edu.isu.arcmlp.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.metrics.Metrics
import krangl.*

class MetricCommand(availableMetrics: Metrics, private val grammarLoader: DefaultGrammarLoader) : CliktCommand(name = "metric", help = """
    Measures the specified metrics for grammars. Expects a csv file as a single argument
    
    The supported metrics are ${availableMetrics.codes.joinToString(" ")}.
""".trimIndent()) {
    private val metrics by option("-m", "--metric", "--metrics")
            .choice(availableMetrics.codesToMetrics)
            .split(",")
            .default(availableMetrics.metrics)

    private val grammars by argument().file(exists = true, folderOkay = false, readable = true)

    private val output by argument().file(folderOkay = false)

    override fun run() {
        val csv = DataFrame.readCSV(grammars)

        val fileNamesToMeasures = csv["files"].asStrings().associateBy({ it }) { fileNames ->
            val grammar: Grammar
            try {
                grammar = grammarLoader.loadAndLinkFileNames(fileNames!!.split(','))
            } catch (e: Throwable) {
                echo("Error while parsing $fileNames")
                e.printStackTrace()
                throw e
            }
            metrics.map { it.measure(grammar) }
        }

        val result = csv.addColumns(*metrics.mapIndexed { i, metric ->
            ColumnFormula(metric.code) {
                it["files"].asStrings().map { fileNames ->
                    fileNamesToMeasures[fileNames]!![i]
                }
            }
        }.toTypedArray())

        result.writeCSV(output)
    }
}