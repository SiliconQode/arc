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

import dev.siliconcode.arc.quality.quamoco.io.factories.MeasurementMethodType;
import dev.siliconcode.arc.quality.quamoco.model.measurement.RuleBasedInstrument;
import dev.siliconcode.arc.quality.quamoco.model.measurement.ToolBasedInstrument;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ToolBasedInstrumentTest {

    ToolBasedInstrument element;

    @Before
    public void setUp() throws Exception {
        element = RuleBasedInstrument.builder()
                .metric("instrument")
                .tool(mock(Tool.class))
                .identifier("ID").create();
    }

    @Test
    public void getTool() throws Exception {
        assertNotNull(element.getTool());
    }

    @Test
    public void setTool() throws Exception {
        element.setTool(null);
        assertNull(element.getTool());
        Tool tool = mock(Tool.class);
        element.setTool(tool);
        assertEquals(tool, element.getTool());
    }

//    @Test
//    public void generateXMLTag() throws Exception {
//        System.out.println(element.generateXMLTag(MeasurementMethodType.RULE_BASED_INSTRUMENT.type()));
//    }

}
