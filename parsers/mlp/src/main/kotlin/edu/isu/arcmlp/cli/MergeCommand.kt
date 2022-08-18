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

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.file
import edu.isu.arcmlp.antlr.ANTLRPrecedence
import edu.isu.arcmlp.bnf.toBnfRepr
import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.repr.defaultReprWithoutIds
import edu.isu.arcmlp.grammars.transformations.combineGrammars
import edu.isu.arcmlp.grammars.transformations.reorderProductions
import mu.KotlinLogging
import java.io.BufferedWriter
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.OutputStreamWriter

private val logger = KotlinLogging.logger {}

class MergeCommand(grammarLoader: DefaultGrammarLoader) : CliktCommand(
        name = "merge",
        help = """
            Merges together grammars from multiple files.
            
            Must specify at least one grammar. Should specify at least two grammars.
            Separate the files in each grammar with a single dash -
        
            E.g. merge java-parser.g4 java-lexer.g4 - c#-parser.g4 c#-lexer.g4
        """.trimIndent()
) {
    private val output by option("-o", help = "File to output grammars to. Extension is used to determine output format.")
            .file(folderOkay = false)

    private val fileType by option("-e", "--ext", "--file-type", help = "Grammar format to output")
            .convert { outputFormatter(it) }

    private val inputGrammars by grammarFileArguments(grammarLoader)

    private val minSimilarityScore by option("-m", "--min-similarity", help = "Minimum similarity of two rules two merge")
            .double().default(.5)

    private fun outputStream() = BufferedWriter(OutputStreamWriter(
            output?.outputStream()
                    ?: FileOutputStream(FileDescriptor.out)))

    private fun outputFormatter(): (Grammar) -> String {
        return fileType ?: outputFormatter(output?.extension ?: "default")
    }

    private fun outputFormatter(name: String): (Grammar) -> String {
        return when (name) {
            "bnf" -> { g -> g.toBnfRepr() }
            "default" -> { g -> g.defaultReprWithoutIds(ANTLRPrecedence) }
            else -> error("Unsupported output format: $name")
        }
    }

    override fun run() {
        try {
            if (inputGrammars.size == 1) {
                echo("Warning: Merging only one grammar", err = true)
            }

            val grammars = inputGrammars.map { it() }
            val combined = combineGrammars(grammars, minSimilarityScore).reorderProductions()
            val output = outputFormatter()(combined)
            val out = outputStream()
            out.write(output)
            out.flush()
            out.close()
        } catch (t: Throwable) {
            logger.error(t) { "Unhandled error" }
            throw Abort(true)
        }
    }
}