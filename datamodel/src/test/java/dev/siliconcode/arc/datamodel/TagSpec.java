/**
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
package dev.siliconcode.arc.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class TagSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Tag t = new Tag();
//        a(t).shouldNotBe("valid");
//        a(t.errors().get(""));
        t.set("tag", "tag");
        a(t).shouldBe("valid");
        t.save();
        a(t.getId()).shouldNotBeNull();
        a(t.get("tag")).shouldBeEqual("tag");
        a(Tag.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Tag t = Tag.createIt("tag", "tag");

        a(Tag.count()).shouldBeEqual(1);
        t.delete();
        a(Tag.count()).shouldBeEqual(0);
    }
}
