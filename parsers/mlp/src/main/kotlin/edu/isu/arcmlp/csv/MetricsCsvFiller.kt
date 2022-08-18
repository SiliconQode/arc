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

import edu.isu.arcmlp.grammars.Grammar
import edu.isu.arcmlp.metrics.Metric
import krangl.DataFrame

class MetricsCsvFiller(
        private val metrics: List<Metric>
) {
    fun fillMetrics(data: DataFrame, grammars: List<Grammar>, prefix: String? = null): DataFrame {
        require(data.nrow == grammars.size) { "The number of rows of data does not match the number of grammars" }

        return metrics.fold(data) { currentData, metric ->
            val column = columnName(metric, prefix)
            currentData.addColumn(column) { grammars.map(metric::measure) }
        }
    }

    private fun columnName(metric: Metric, prefix: String?): String {
        return if (prefix == null) {
            metric.code
        } else {
            "$prefix-${metric.code}"
        }
    }
}
