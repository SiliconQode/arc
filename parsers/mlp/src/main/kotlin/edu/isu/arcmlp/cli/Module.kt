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

import edu.isu.arcmlp.antlr.ANTLRGrammarLanguageLoader
import edu.isu.arcmlp.antlr.ANTLRRepr
import edu.isu.arcmlp.bnf.BNFGrammarLanguageLoader
import edu.isu.arcmlp.csv.*
import edu.isu.arcmlp.grammars.GrammarSaver
import edu.isu.arcmlp.grammars.repr.GrammarRepr
import edu.isu.arcmlp.grammars.transformations.GrammarFullMerger
import edu.isu.arcmlp.grammars.transformations.GrammarNormalizer
import edu.isu.arcmlp.grammars.transformations.GrammarTrivialMerger
import org.koin.core.qualifier.named
import org.koin.dsl.module

class GrammarFileQualifier
class OutputDirectoryQualifier
class InputCsvQualifier
class OutputCsvQualifier
class MetricsQualifier
class FirstFilesColumnQualifier
class SecondFilesColumnQualifier
class GrammarsDirectoryQualifier

val mergeModule = module {
    single {
        BulkMergeExecution(
                get(named<InputCsvQualifier>()),
                get(),
                get(),
                get(named<OutputCsvQualifier>())
        )
    }
    single {
        TrivialGrammarMergeMetricsCsvFiller(
                get(named<FirstFilesColumnQualifier>()),
                get(named<SecondFilesColumnQualifier>()),
                get(),
                get<GrammarTrivialMerger>(),
                get(),
                metricsPrefix = "trivial"
        )
    }
    single {
        FullMergedGrammarMetricsCsvFiller(
                get(named<FirstFilesColumnQualifier>()),
                get(named<SecondFilesColumnQualifier>()),
                "similarity_threshold",
                "merged_file",
                get(),
                get(),
                get(),
                get(),
                "full"
        )
    }
    single<GrammarCsvExtractor> {
        ReferencedGrammarCsvExtractor(
                FilenamesGrammarCsvExtractor(get(), getOrNull(named<GrammarsDirectoryQualifier>())),
                "name",
                "files",
                get(named<GrammarFileQualifier>())
        )
    }
    single { GrammarSaver(get(named<OutputDirectoryQualifier>()), get()) }
    single<GrammarRepr> { ANTLRRepr() }
    single { GrammarTrivialMerger() }
    single { GrammarFullMerger() }
    single { MetricsCsvFiller(get(named<MetricsQualifier>())) }
    single { DefaultGrammarLoader(ANTLRGrammarLanguageLoader(), BNFGrammarLanguageLoader()) }
    single {
        BulkNormalizeExecution(
                get(named<InputCsvQualifier>()),
                get(),
                get(named<OutputCsvQualifier>())
        )
    }
    single {
        NormalizationMetricsFiller(
                get(named<FirstFilesColumnQualifier>()),
                "normalized",
                get(),
                get(),
                get(),
                "pre-",
                "post-",
                get()
        )
    }
    single { GrammarNormalizer() }
}