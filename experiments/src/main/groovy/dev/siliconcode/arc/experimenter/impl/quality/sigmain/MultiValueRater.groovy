/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

class MultiValueRater extends AbstractMetricRater {

    MultiValueRater(String metricHandle) {
        super(metricHandle)
    }

    @Override
    void rate(Measurable measurable) {
        Table<Integer, RiskCategory, Double> ratingTable = loadRatingTable()
        Map<RiskCategory, Double> profile = loadRiskProfile(measurable)
        double rating = 0.0
        List<Double> ratings = []

        [RiskCategory.MODERATE, RiskCategory.HIGH, RiskCategory.VERY_HIGH].each {
            Map<Integer, Double> table = ratingTable.columnMap()[it]
            ratings << calcRating(profile[it], table)
        }

        rating = ratings.min()

        Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${metricHandle}.RATING").on(measurable).withValue(rating)
    }

    Table<Integer, RiskCategory, Double> loadRatingTable() {
        Table<Integer, RiskCategory, Double> table = HashBasedTable.create()
        InputStream is = this.getClass().getResourceAsStream("/edu/montana/gsoc/msusel/arc/impl/quality/sig/${metricHandle}.csv")
        InputStreamReader isr = new InputStreamReader(is)
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(isr)
        records.each { record ->
            table.put(Integer.parseInt(record.get(0)), RiskCategory.LOW, Double.parseDouble(record.get(1)))
            table.put(Integer.parseInt(record.get(0)), RiskCategory.MODERATE, Double.parseDouble(record.get(2)))
            table.put(Integer.parseInt(record.get(0)), RiskCategory.HIGH, Double.parseDouble(record.get(3)))
            table.put(Integer.parseInt(record.get(0)), RiskCategory.VERY_HIGH, Double.parseDouble(record.get(4)))
        }
        is.close()
        table
    }

    Map<RiskCategory, Double> loadRiskProfile(Measurable measureable) {
        Map<RiskCategory, Double> map = [:]

        map[RiskCategory.LOW] = measureable.getValueFor((String) "${SigMainConstants.SIGMAIN_REPO_KEY}:${metricHandle}.LOW")
        map[RiskCategory.MODERATE] = measureable.getValueFor((String) "${SigMainConstants.SIGMAIN_REPO_KEY}:${metricHandle}.MOD")
        map[RiskCategory.HIGH] = measureable.getValueFor((String) "${SigMainConstants.SIGMAIN_REPO_KEY}:${metricHandle}.HIGH")
        map[RiskCategory.VERY_HIGH] = measureable.getValueFor("${SigMainConstants.SIGMAIN_REPO_KEY}:${metricHandle}.VHIGH")

        map
    }
}
