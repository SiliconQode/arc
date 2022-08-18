/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
 * Copyright (c) 2015-2021 Empirilytics
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
 */
package dev.siliconcode.arc.datamodel.util

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table

class MeasureTable {

    private MeasureTable() {}

    private static final class Holder {
        private static final MeasureTable INSTANCE = new MeasureTable()
    }

    static MeasureTable getInstance() {
        return Holder.INSTANCE
    }

    Table<String, String, Double> table = HashBasedTable.create()

    void addMeasure(String compKey, String metricKey, Double value) {
        table.put(compKey, metricKey, value)
    }

    double getValue(String compKey, String metricKey) {
        if (table.contains(compKey, metricKey))
            return table.get(compKey, metricKey)
        else
            return 0.0d
    }

    void reset() {
        table.clear()
    }

    boolean contains(String compKey, String metricKey) {
        return table.contains(compKey, metricKey)
    }
}
