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
package dev.siliconcode.arc.quality.quamoco.io;

import dev.siliconcode.arc.quality.quamoco.distiller.ModelManager;
import dev.siliconcode.arc.quality.quamoco.io.qm.QMXMLReader;
import dev.siliconcode.arc.quality.quamoco.model.*;
import dev.siliconcode.arc.quality.quamoco.model.eval.Evaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.factor.WeightedSumFactorAggregation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.MeasureRanking;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.WeightedSumMultiMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.factor.Factor;
import dev.siliconcode.arc.quality.quamoco.model.func.Function;
import dev.siliconcode.arc.quality.quamoco.model.func.LinearFunction;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measurement.FactorRanking;
import dev.siliconcode.arc.quality.quamoco.model.measurement.MeasurementMethod;
import dev.siliconcode.arc.quality.quamoco.model.measurement.ToolBasedInstrument;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

/**
 * The class <code>QMReaderTest</code> contains tests for the class
 * <code>{@link QMXMLReader}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QMXMLReaderTest {

    /**
     * Run the QMReader() constructor test.
     */
    @Test
    public void testQMXMLReader_1()
    {
        final QMXMLReader result = new QMXMLReader(new ModelManager());

        // TODO: add additional test code here
        assertNotNull(result);
    }

    /**
     * Run the QualityModel getModel() method test.
     */
    @Test
    public void testGetModel_1()
    {
        final QMXMLReader fixture = new QMXMLReader(new ModelManager());
        QualityModel result = fixture.getModel();
        assertNull(result);

        fixture.read("data/test/Test.qm");
        result = fixture.getModel();
        assertNotNull(result);
    }

    /**
     * Run the void read(String) method test.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRead_2()
    {
        final QMXMLReader fixture = new QMXMLReader(new ModelManager());

        fixture.read("");
    }

    /**
     * Run the void read(String) method test.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRead_3()
    {
        final QMXMLReader fixture = new QMXMLReader(new ModelManager());

        fixture.read(null);
    }
}
