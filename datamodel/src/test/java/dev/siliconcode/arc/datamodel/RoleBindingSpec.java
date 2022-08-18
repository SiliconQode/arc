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

public class RoleBindingSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        RoleBinding binding = new RoleBinding();
        a(binding).shouldBe("valid");
//        //a(binding.errors().get("author")).shouldBeEqual("Author must be provided");
        binding.set();
        a(binding).shouldBe("valid");
        binding.save();
        binding = (RoleBinding) RoleBinding.findAll().get(0);
        a(binding.getId()).shouldNotBeNull();
        a(RoleBinding.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddRoleRefPair() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);

        Reference ref2 = Reference.createIt("refKey", "ref2");
        Role role2 = Role.createIt("roleKey", "role2", "name", "role");
        binding.setRoleRefPair(role2, ref2);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRoleRefPair() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(binding.getAll(Role.class).size()).shouldBeEqual(1);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(1);

        binding = (RoleBinding) RoleBinding.findAll().get(0);
        binding.remove(ref);
        binding.remove(role);
        a(binding.getAll(Role.class).size()).shouldBeEqual(0);
        a(binding.getAll(Reference.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        RoleBinding binding = RoleBinding.createIt();
        Reference ref = Reference.createIt("refKey", "ref");
        Role role = Role.createIt("roleKey", "role", "name", "role");

        binding.setRoleRefPair(role, ref);
        a(RoleBinding.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        a(Role.count()).shouldBeEqual(1);
        binding.delete(true);
        a(RoleBinding.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
        a(Role.count()).shouldBeEqual(0);
    }
}
