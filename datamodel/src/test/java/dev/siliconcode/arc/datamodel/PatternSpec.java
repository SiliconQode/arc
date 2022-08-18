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

public class PatternSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Pattern pattern = new Pattern();
        a(pattern).shouldBe("valid");
//        //a(pattern.errors().get("author")).shouldBeEqual("Author must be provided");
        pattern.set("patternKey", "pattern", "name", "pattern");
        a(pattern).shouldBe("valid");
        pattern.save();
        pattern = Pattern.findById(pattern.getId());
        a(pattern.getId()).shouldNotBeNull();
        a(pattern.get("name")).shouldBeEqual("pattern");
        a(pattern.get("patternKey")).shouldBeEqual("pattern");
        a(Pattern.count()).shouldBeEqual(25);
    }

    @Test
    public void canAddPatternInstance() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(pattern.getInstances().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePatternInstance() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(pattern.getInstances().size()).shouldBeEqual(1);
        pattern = Pattern.findById(24);
        pattern.remove(inst);
        a(pattern.getInstances().size()).shouldBeEqual(0);
    }

    @Test
    public void canAddRole() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        pattern.save();

        a(pattern.getRoles().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRole() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        pattern.save();

        a(pattern.getRoles().size()).shouldBeEqual(1);
        pattern = Pattern.findById(24);
        pattern.removeRole(role);
        a(pattern.getRoles().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        Role role = Role.createIt("roleKey", "role", "name", "role");
        pattern.addRole(role);
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        pattern.addInstance(inst);
        pattern.save();

        a(Pattern.count()).shouldBeEqual(25);
        a(Role.count()).shouldBeEqual(1);
        a(PatternInstance.count()).shouldBeEqual(1);
        pattern.delete(true);
        a(Pattern.count()).shouldBeEqual(24);
        a(Role.count()).shouldBeEqual(0);
        a(PatternInstance.count()).shouldBeEqual(0);
    }
}
