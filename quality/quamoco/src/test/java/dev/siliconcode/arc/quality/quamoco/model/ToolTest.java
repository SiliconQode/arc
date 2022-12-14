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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ToolTest {

    Tool element;

    @Before
    public void setUp() throws Exception {
        element = Tool.builder()
                .name("tool")
                .identifier("ID")
                .description("description")
                .title("title")
                .create();
    }

    @Test
    public void getTitle() {
        assertEquals("title", element.getTitle());
    }

    @Test
    public void setTitle() {
        element.setTitle("NewTitle");
        assertEquals("NewTitle", element.getTitle());
    }

    @Test
    public void getName() {
        assertEquals("tool", element.getName());
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
        element.setDescription("NewDescription");
        assertEquals("NewDescription", element.getDescription());
    }

    @Test
    public void xmlTag() throws Exception {
        String value = "<tools xmi:id=\"ID\" name=\"tool\" description=\"description\" title=\"title\" />\n";
        assertEquals(value, element.xmlTag());
    }

//    @Test
//    public void toScript() {
//        fail();
//    }
}
