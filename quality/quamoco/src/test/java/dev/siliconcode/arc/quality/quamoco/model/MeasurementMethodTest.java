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

import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measurement.ManualInstrument;
import dev.siliconcode.arc.quality.quamoco.model.measurement.MeasurementMethod;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasurementMethodTest {

    MeasurementMethod element;

    @Before
    public void setUp() throws Exception {
        element = ManualInstrument.builder().identifier("instrument")
                .determines(mock(Measure.class))
                .metric("name")
                .create();
    }

    @Test
    public void getDetermines() {
        assertNotNull(element.getDetermines());
    }

    @Test
    public void setDetermines() {
        element.setDetermines(null);
        assertNull(element.getDetermines());
        element.setDetermines(mock(Measure.class));
        assertNotNull(element.getDetermines());
    }

    @Test
    public void getMetric() {
        assertEquals("name", element.getName());
    }

    @Test
    public void setMetric() {
        element.setName(null);
        assertNull(element.getName());
        element.setName("newMetric");
        assertEquals("newMetric", element.getName());
    }
}
