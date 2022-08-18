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

public class FileSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        File file = new File();
        a(file).shouldBe("valid");
//        //a(file.errors().get("author")).shouldBeEqual("Author must be provided");
        file.set("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        a(file).shouldBe("valid");
        file.save();
        file = (File) File.findAll().get(0);
        a(file.getId()).shouldNotBeNull();
        a(file.get("type")).shouldBeEqual(FileType.SOURCE.value());
        a(file.getType()).shouldBeEqual(FileType.SOURCE);
        a(file.get("name")).shouldBeEqual("file");
        a(file.get("fileKey")).shouldBeEqual("fileKey");
        a(File.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddImport() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        file.addImport(imp);
        a(file.getAll(Import.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveImport() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        file.addImport(imp);
        file = (File) File.findAll().get(0);
        file.removeImport(imp);

        a(file.getAll(Import.class).isEmpty()).shouldBeTrue();
    }

    @Test
    public void canAddType() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        a(file.getType()).shouldNotBeNull();
    }

    @Test
    public void canRemoveType() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        file.setType(null);

        a(file.getType()).shouldBeNull();
    }

    @Test
    public void deleteHandlesCorrectly() {
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        Import imp = Import.createIt("name", "imp");

        Type type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass", "type", Type.CLASS);
        type.setAccessibility(Accessibility.PUBLIC);
        type.save();

        file.add(imp);
        file.addType(type);

        file.delete(true);

        a(File.count()).shouldBeEqual(0);
        a(Type.count()).shouldBeEqual(1);
        a(Import.count()).shouldBeEqual(0);
    }
}
