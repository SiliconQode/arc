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

package edu.isu.arcmlp.cli;

import edu.isu.arcmlp.csv.NormalizationMetricsFiller;
import krangl.DataFrame;
import org.apache.commons.csv.CSVFormat;

import java.io.File;

import static krangl.TableIOKt.readCSV;
import static krangl.TableIOKt.writeCSV;

public class BulkNormalizeExecution implements Runnable {
    private final File inputFile;
    private final NormalizationMetricsFiller normalizationMetricsFiller;
    private final File outputFile;

    public BulkNormalizeExecution(File inputFile, NormalizationMetricsFiller normalizationMetricsFiller, File outputFile) {
        this.inputFile = inputFile;
        this.normalizationMetricsFiller = normalizationMetricsFiller;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        var input = readCSV(DataFrame.Companion, inputFile);
        var processed = normalizationMetricsFiller.fillCsv(input);
        var format = CSVFormat.DEFAULT.withHeader(processed.getNames().toArray(new String[0]));
        writeCSV(processed, outputFile, format);
    }
}
