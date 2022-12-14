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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.siliconcode.arc.quality.quamoco.io.factories.MeasuresType;
import dev.siliconcode.arc.quality.quamoco.model.entity.Entity;
import dev.siliconcode.arc.quality.quamoco.model.factor.Factor;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasureTest {

    Measure element;

    @Before
    public void setup() {
        element = Measure.builder()
                .name("measure")
                .identifier("id")
                .title("title")
                .type(MeasureType.FINDINGS)
                .description("description")
                .characterizes(mock(Entity.class))
                .refines(mock(Measure.class))
                .tag(mock(Tag.class))
                .originatesFrom(mock(Source.class))
                .annotation(mock(Annotation.class))
                .create();
    }

    @Test
    public void getCharacterizes() {
        assertNotNull(element.getCharacterizes());
    }

    @Test
    public void setCharacterizes() {
        element.setCharacterizes(null);
        assertNull(element.getCharacterizes());
        element.setCharacterizes(mock(Entity.class));
        assertNotNull(element.getCharacterizes());
    }

    @Test
    public void getRefines() {
        assertNotNull(element.getRefines());
    }

    @Test
    public void setRefines() {
        element.setRefines(null);
        assertNull(element.getRefines());
        element.setRefines(mock(Measure.class));
        assertNotNull(element.getRefines());
    }

    @Test
    public void addMeasures() {
        assertNotNull(element.getMeasures());
        assertTrue(element.getMeasures().isEmpty());
        element.addMeasures(mock(Factor.class));
        assertFalse(element.getMeasures().isEmpty());
    }

    @Test
    public void removeMeasures() {
        Factor fac = mock(Factor.class);
        element.addMeasures(fac);
        assertFalse(element.getMeasures().isEmpty());
        element.removeMeasures(fac);
        assertTrue(element.getMeasures().isEmpty());
    }

    @Test
    public void getMeasures() {
        Factor fac = mock(Factor.class);
        element.addMeasures(fac);
        assertTrue(element.getMeasures().contains(fac));
    }

    @Test
    public void getTitle() {
        assertEquals("title", element.getTitle());
    }

    @Test
    public void setTitle() {
        element.setTitle("new_title");
        assertEquals("new_title", element.getTitle());
    }

    @Test
    public void getType() {
        assertEquals(MeasureType.FINDINGS, element.getType());
    }

    @Test
    public void setType() {
        element.setType(MeasureType.NUMBER);
        assertEquals(MeasureType.NUMBER, element.getType());
    }

    @Test
    public void isNormalizer() {
        assertFalse(element.isNormalizer());
    }

    @Test
    public void getName() {
        assertEquals("measure", element.getName());
    }

    @Test
    public void setName() {
        element.setName("NewName");
        assertEquals("NewName", element.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("description", element.getDescription());
    }

    @Test
    public void setDescription() {
        element.setDescription("newDescription");
        assertEquals("newDescription", element.getDescription());
    }

    @Test
    public void generateXMLTag() {
        // String exp = "<measures xmi:id=\"id\" xsi:type=\"qm:FindingMeasure\" />\n";
        String exp = "<measures xmi:id=\"id\" xsi:type=\"FINDINGS\" />\n";
        element = Measure.builder()
                .name("measure")
                .identifier("id")
                .title("title")
                .type(MeasureType.FINDINGS)
                .description("description")
                .create();
        String value = element.generateXMLTag("measures", MeasuresType.FINDING_MEASURE.type(), Maps.newHashMap(), Lists.newArrayList());

        assertEquals(exp, value);
    }

    @Test
    public void xmlTag() throws Exception {
        element = Measure.builder()
                .name("measure")
                .identifier("id")
                .title("title")
                .type(MeasureType.FINDINGS)
                .description("description")
                .create();

//        String value = "<measures xmi:id=\"id\" xsi:type=\"qm:FindingMeasure\" name=\"measure\" description=\"description\" title=\"title\" />\n";
        String value = "<measures xmi:id=\"id\" xsi:type=\"FINDINGS\" name=\"measure\" description=\"description\" title=\"title\" />\n";
        assertEquals(value, element.xmlTag());
    }

}
