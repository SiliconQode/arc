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
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import edu.isu.arcmlp.metrics.Metrics
import org.koin.Logger.slf4jLogger
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication


class NormalizeCommand(
        availableMetrics: Metrics
) : CliktCommand(name = "bulk-normalize", help = """
    Normalizes several grammars in bulk from a csv file. Expects as first argument the file specifying the grammars
    to merge, the second argument to be a file describing the location of the grammars, and the third argument as where to save the results.
     Measures the metrics for each grammar produced.
    It reads the names of the grammars to merge from the name1 and name2 columns of the csv file.
    
    
    The first file must have the following columns:
    
    name
    
    java 
    
    bach
    
    ...
    
    
    The second file must have the following columns:
    
    name, files
    
    java,"JavaParser.g4,JavaLexer.bnf"
    
    ...
    
    The third file will include all the columns along with two columns added for each metric and a column
    with the file location of where the combined grammar is stored.
""".trimIndent()) {
    private val outDir by option("--out-dir", "-o", "--out", help = """
        The directory to output normalized grammars.
    """.trimIndent()).file(fileOkay = false).required()
    private val grammarsDirectory by option("--grammar-dir", "-g", "-gd", help="""
        The directory that grammars are stored in.
    """.trimIndent()).file(fileOkay = false, exists = true, readable = true)
    private val inFile by argument().file(exists = true, folderOkay = false, readable = true)
    private val grammarFile by argument().file(exists = true, folderOkay = false, readable = true)
    private val outFile by argument().file(folderOkay = false)
    private val metrics by option("-M", "--metric", "--metrics")
            .choice(availableMetrics.codesToMetrics)
            .split(",")
            .default(availableMetrics.metrics)

    override fun run() {
        val koin = koinApplication {
            slf4jLogger()
            modules(mergeModule)
        }.koin

        koin.declare(outDir, named<OutputDirectoryQualifier>())
        koin.declare(inFile, named<InputCsvQualifier>())
        koin.declare(grammarFile, named<GrammarFileQualifier>())
        koin.declare(outFile, named<OutputCsvQualifier>())
        koin.declare(metrics, named<MetricsQualifier>())
        koin.declare("name", named<FirstFilesColumnQualifier>())
        koin.declare(grammarsDirectory, named<GrammarsDirectoryQualifier>())

        koin.get<BulkNormalizeExecution>().run()
    }
}