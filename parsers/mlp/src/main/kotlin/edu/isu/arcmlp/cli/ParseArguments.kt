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
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import com.github.ajalt.clikt.parameters.arguments.validate
import edu.isu.arcmlp.grammars.Grammar
import java.io.File

/**
 * Creates a delegate for parsing grammars from the specified files.
 */
fun CliktCommand.grammarFileArguments(grammarLoader: DefaultGrammarLoader) = argument()
        .multiple()
        .transformAll { parseArguments(it, grammarLoader, this::fail) }
        .validate {
            if (it.isEmpty()) fail("Must specify at least one grammar.")
        }

/**
 * Parses the arguments. Will call the immediately fail function with an error message while parsing arguments,
 * but not when the returned functions are called.
 */
fun parseArguments(
        arguments: List<String>,
        grammarLoader: DefaultGrammarLoader,
        immediateFail: (message: String) -> Nothing
): List<() -> Grammar> {
    return bundleFileNames(arguments, immediateFail).map { bundle ->
        val files = validateFileNames(bundle, immediateFail, grammarLoader)
        grammarLoader.loadAndLinkFilesLazily(files)
    }
}

private fun validateFileNames(
        fileNames: List<String>,
        immediateFail: (message: String) -> Nothing,
        grammarLoader: DefaultGrammarLoader): List<File> {
    return fileNames.map { fileName ->
        val file = File(fileName)
        if (!file.exists()) {
            immediateFail("File $fileName doesn't exist.")
        }
        if (!grammarLoader.canLoadFile(file)) {
            immediateFail("Doesn't support loading grammars of type ${file.name}")
        }
        file
    }
}


private fun bundleFileNames(files: List<String>, immediateFail: (message: String) -> Nothing): List<List<String>> {
    val bundles = mutableListOf<MutableList<String>>()
    val currentBundle = mutableListOf<String>()

    for (fileName in files) {
        if (fileName == "-") {
            if (currentBundle.isNotEmpty()) {
                bundles += currentBundle
                currentBundle.clear()
            } else {
                immediateFail("Must specify at least one file in each grammar")
            }
        } else {
            currentBundle += fileName
        }
    }

    if (currentBundle.isEmpty()) {
        immediateFail("Must specify at least one file in each grammar")
    }

    bundles += currentBundle

    return bundles
}
