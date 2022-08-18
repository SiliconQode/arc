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

import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.grammars.transformations.concatProductions
import edu.isu.arcmlp.grammars.transformations.link
import edu.isu.arcmlp.grammars.transformations.simplify
import java.io.File

class LinkException(message: String?, cause: Throwable?) : Exception(message, cause)


class DefaultGrammarLoader(languageLoaders: List<GrammarLanguageLoader>) : GrammarLoader {
    constructor(vararg languageLoaders: GrammarLanguageLoader) : this(languageLoaders.toList())

    private val extensionsToLoaders = languageLoaders.flatMap { loader ->
        loader.supportedExtensions().map { ext -> ext to loader }
    }.toMap()

    /**
     * Parses the file into a grammar. Throws an error if a grammar loader cannot be found
     * for the file type.
     */
    override fun loadFile(file: File): Grammar {
        val loader = extensionsToLoaders[file.extension]
                ?: throw IllegalArgumentException("Doesn't support files with extensions of ${file.extension}: ${file.absolutePath}")
        return loader.loadGrammar(file).simplify()
    }

    override fun canLoadFile(file: File): Boolean {
        return extensionsToLoaders.containsKey(file.extension)
    }

    override fun loadAndLinkFileNames(fileNames: List<String>) = loadAndLinkFiles(fileNames.map { File(it) })


    override fun loadAndLinkFiles(files: List<File>): Grammar {
        val combined = files.map { loadFile(it) }
                .reduce { acc, grammar -> acc.concatProductions(grammar) }

        try {
            return combined
                    .link()
                    .simplify()
        } catch (e: Exception) {
            throw LinkException("problem while loading files: ${files.joinToString { it.absolutePath }}", e)
        }
    }


    override fun loadAndLinkFilesLazily(files: List<File>): () -> Grammar = { loadAndLinkFiles(files) }
}