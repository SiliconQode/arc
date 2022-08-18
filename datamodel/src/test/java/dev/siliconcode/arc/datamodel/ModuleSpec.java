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

public class ModuleSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Module module = new Module();
        a(module).shouldBe("valid");
//        //a(module.errors().get("author")).shouldBeEqual("Author must be provided");
        module.set("moduleKey", "module", "name", "module");
        a(module).shouldBe("valid");
        module.save();
        module = (Module) Module.findAll().get(0);
        a(module.getId()).shouldNotBeNull();
        a(module.get("name")).shouldBeEqual("module");
        a(module.get("moduleKey")).shouldBeEqual("module");
        a(Module.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddNamespace() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        a(module.getNamespaces().size()).shouldBeEqual(0);
        module.addNamespace(ns);
        a(module.getNamespaces().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveNamespace() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        module.addNamespace(ns);

        module = Module.findById(module.getId());
        module.removeNamespace(ns);
        module.refresh();

        a(module.getNamespaces().isEmpty()).shouldBeTrue();
    }

    @Test
    public void deleteHandlesCorrectly() {
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");

        module.addNamespace(ns);
        module.delete(true);

        a(Module.count()).shouldBeEqual(0);
        a(Namespace.count()).shouldBeEqual(1);
    }
}
