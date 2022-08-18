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

public class NamespaceSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Namespace ns = new Namespace();
        a(ns).shouldBe("valid");
//        //a(ns.errors().get("author")).shouldBeEqual("Author must be provided");
        ns.set("nsKey", "ns", "name", "ns");
        a(ns).shouldBe("valid");
        ns.save();
        ns = (Namespace) Namespace.findAll().get(0);
        a(ns.getId()).shouldNotBeNull();
        a(ns.get("name")).shouldBeEqual("ns");
        a(ns.get("nsKey")).shouldBeEqual("ns");
        a(Namespace.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddChildNamespace() {
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns2", "name", "ns2");

        ns.addNamespace(ns2);
        a(ns.getNamespaces().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveChildNamespace() {
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns2", "name", "ns2");

        ns.addNamespace(ns2);
        ns = Namespace.findById(ns.getId());
        ns.removeNamespace(ns2);
        a(ns.getNamespaces().size()).shouldBeEqual(0);
    }

    @Test
    public void parentsHandleCorrectly() {
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns2", "name", "ns2");
        Module module = Module.createIt("moduleKey", "module", "name", "module");

        ns.addNamespace(ns2);
        module.addNamespace(ns);
        module.addNamespace(ns2);

        a(module.getNamespaces().size()).shouldBeEqual(2);
        a(ns.getNamespaces().size()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns2", "name", "ns2");
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        file.setType(FileType.SOURCE);
        file.save();

        ns.addNamespace(ns2);
        ns.addFile(file);

        ns.delete(true);

        a(Namespace.count()).shouldBeEqual(1);
        a(File.count()).shouldBeEqual(1);
    }
}
