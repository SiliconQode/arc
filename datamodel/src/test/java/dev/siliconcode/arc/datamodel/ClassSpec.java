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

public class ClassSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Type type = new Type();
        a(type).shouldBe("valid");
//        //a(type.errors().get("author")).shouldBeEqual("Author must be provided");
        type.set("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        a(type).shouldBe("valid");
        type.save();
        type = (Type) Type.findAll().get(0);
        a(type.getId()).shouldNotBeNull();
        a(type.get("accessibility")).shouldBeEqual(Accessibility.PUBLIC.value());
        a(type.getAccessibility()).shouldBeEqual(Accessibility.PUBLIC);
        a(type.get("name")).shouldBeEqual("TestClass");
        a(type.get("compKey")).shouldBeEqual("TestClass");
        a(type.get("start")).shouldBeEqual(1);
        a(type.get("end")).shouldBeEqual(100);
        a(Type.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddMembers() {
        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Member member = Field.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);
        a(member.getId()).shouldNotBeNull();
    }

    @Test
    public void canRemoveMembers() {
        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Member member = Field.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);
        a(type.getAll(Field.class).size()).shouldBeEqual(1);

        type.remove(member);
        a(type.getAll(Field.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddModifier() {
        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.addModifier(Modifier.Values.STATIC.name());
        type.save();

        a(Type.findAll().get(0).getAll(Modifier.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModifier() {
        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.addModifier(Modifier.Values.STATIC.name());
        type.save();

        type = (Type) Type.findAll().get(0);
        type.removeModifier(Modifier.Values.STATIC.name());
        a(type.getAll(Modifier.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();
        type.addModifier(Modifier.Values.STATIC.name());

        Member member = Field.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.save();

        type.add(member);

        type.delete(true);
        a(Field.count()).shouldBeEqual(0);
        a(TypesModifiers.count()).shouldBeEqual(0);
    }

    @Test
    public void parentsWorksCorrectly() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass", "type", Type.CLASS);
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        Type type2 = Type.createIt("name", "TestClass2", "start", 1, "end", 100, "compKey", "TestClass2", "type", Type.CLASS);
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        type.addType(type2);
        file.addType(type);
        file.addType(type2);

        a(file.getClasses().size()).shouldBeEqual(2);
        a(type.getClasses().size()).shouldBeEqual(1);
    }
}
