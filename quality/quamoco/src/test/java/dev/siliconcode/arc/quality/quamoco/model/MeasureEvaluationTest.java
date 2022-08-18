/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
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
package dev.siliconcode.arc.quality.quamoco.model;

import dev.siliconcode.arc.quality.quamoco.io.factories.EvaluationType;
import dev.siliconcode.arc.quality.quamoco.model.eval.MeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.SingleMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.func.Function;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measure.NormalizationMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasureEvaluationTest {

    MeasureEvaluation element;

    @Before
    public void setUp() throws Exception {
        element = SingleMeasureEvaluation.builder().identifier("ID")
                .normalization(mock(NormalizationMeasure.class))
                .range(NormalizationRange.CLASS)
                .basedOn(mock(Measure.class))
                .function(mock(Function.class))
                .create();
    }

    @Test
    public void getRange() {
        assertEquals(NormalizationRange.CLASS, element.getRange());
    }

    @Test
    public void setRange() {
        element.setRange(NormalizationRange.FILE);
        assertEquals(NormalizationRange.FILE, element.getRange());
    }

    @Test
    public void getNormalization() {
        assertNotNull(element.getNormalization());
    }

    @Test
    public void setNormalization() {
        element.setNormalization(null);
        assertNull(element.getNormalization());
        element.setNormalization(mock(NormalizationMeasure.class));
        assertNotNull(element.getNormalization());
    }

    @Test
    public void getFunction() {
        assertNotNull(element.getFunction());
    }

    @Test
    public void setFunction() {
        element.setFunction(null);
        assertNull(element.getFunction());
        element.setFunction(mock(Function.class));
        assertNotNull(element.getFunction());
    }

    @Test
    public void getBasedOn() {
        assertNotNull(element.getBasedOn());
    }

    @Test
    public void setBasedOn() {
        element.setBasedOn(null);
        assertNull(element.getBasedOn());
        element.setBasedOn(mock(Measure.class));
        assertNotNull(element.getBasedOn());
    }

    @Test
    public void generateXMLTag() {
        String value = "<evaluations xmi:id=\"ID\" xsi:type=\"qm:SingleMeasureEvaluation\" range=\"CLASS\">\n" +
                "<normlizationMeasure href=\"null\" />\n" +
                "\nnull\n</evaluations>\n";

        assertEquals(value, element.generateXMLTag(EvaluationType.SINGLE_MEASURE_EVALUATION.type()));
    }

}
