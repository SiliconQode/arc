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

public class LiteralSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Member member = new Literal();
        a(member).shouldBe("valid");
//        //a(member.errors().get("author")).shouldBeEqual("Author must be provided");
        member.set("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        a(member).shouldBe("valid");
        member.save();
        member = (Literal) Literal.findAll().get(0);
        a(member.getId()).shouldNotBeNull();
        a(member.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(member.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(member.get("name")).shouldBeEqual("TestClass");
        a(member.get("compKey")).shouldBeEqual("TestClass");
        a(member.get("start")).shouldBeEqual(1);
        a(member.get("end")).shouldBeEqual(100);
        a(Literal.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddModifier() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        member.save();

        a(Literal.findAll().get(0).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        member.save();

        member = (Literal) Literal.findAll().get(0);
        member.removeModifier(Modifier.Values.STATIC.name());
        a(member.getAll(Modifier.class).size()).shouldBeEqual(0);
        a(LiteralsModifiers.count()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        member.delete();

        a(Literal.count()).shouldBeEqual(0);
    }
}
