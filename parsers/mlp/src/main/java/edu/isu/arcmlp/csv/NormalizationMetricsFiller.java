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

package edu.isu.arcmlp.csv;

import edu.isu.arcmlp.grammars.GrammarSaver;
import edu.isu.arcmlp.grammars.transformations.GrammarNormalizer;
import krangl.DataFrame;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static krangl.ColumnsKt.asStrings;

public class NormalizationMetricsFiller {
    private final String grammarColumn;
    private final String outputFileColumn;
    private final GrammarCsvExtractor grammarCsvExtractor;
    private final GrammarSaver grammarSaver;
    private final GrammarNormalizer normalizer;
    private final String metricPrefixBefore;
    private final String metricPrefixAfter;
    private final MetricsCsvFiller metricsCsvFiller;

    public NormalizationMetricsFiller(String grammarColumn, String outputFileColumn, GrammarCsvExtractor grammarCsvExtractor, GrammarSaver grammarSaver, GrammarNormalizer normalizer, String metricPrefixBefore, String metricPrefixAfter, MetricsCsvFiller metricsCsvFiller) {
        this.grammarColumn = grammarColumn;
        this.outputFileColumn = outputFileColumn;
        this.grammarCsvExtractor = grammarCsvExtractor;
        this.grammarSaver = grammarSaver;
        this.normalizer = normalizer;
        this.metricPrefixBefore = metricPrefixBefore;
        this.metricPrefixAfter = metricPrefixAfter;
        this.metricsCsvFiller = metricsCsvFiller;
    }

    public DataFrame fillCsv(DataFrame input) {
        var names = asStrings(input.get(grammarColumn));

        var grammars = grammarCsvExtractor.extractGrammars(input, grammarColumn);
        var csv = metricsCsvFiller.fillMetrics(input, grammars, metricPrefixBefore);
        var normalizedGrammars = grammars.stream().map(normalizer::normalize).collect(Collectors.toList());
        csv = metricsCsvFiller.fillMetrics(csv, normalizedGrammars, metricPrefixAfter);

        var locations = new ArrayList<String>();
        for (int i = 0; i < normalizedGrammars.size(); i++) {
            var fileName = String.format("normalized-%s", names[i]);
            var location = grammarSaver.saveGrammar(fileName, normalizedGrammars.get(i));
            locations.add(location.toString());
        }

        csv.addColumn(outputFileColumn, (_1, _2) -> locations);

        return csv;
    }
}
