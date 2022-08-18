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

public class RuleSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Rule rule = new Rule();
        a(rule).shouldBe("valid");
        //a(rule.errors().get("author")).shouldBeEqual("Author must be provided");
        rule.set("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        a(rule).shouldBe("valid");
        rule.save();
        rule = (Rule) Rule.findAll().get(0);
        a(rule.getId()).shouldNotBeNull();
        a(rule.get("priority")).shouldBeEqual(4);
        a(rule.getPriority()).shouldBeEqual(Priority.HIGH);
        a(rule.get("ruleKey")).shouldBeEqual("fake key");
        a(rule.get("name")).shouldBeEqual("fake name");
        a(rule.get("description")).shouldBeEqual("fake content");
        a(rule.get("rule_repository_id")).shouldBeEqual(1);
        a(Rule.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddTag() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(rule.getAll(Tag.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveTag() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(rule.getAll(Tag.class).size()).shouldBeEqual(1);
        rule = (Rule) Rule.findAll().get(0);
        rule.remove(t);
        a(rule.getAll(Tag.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddFinding() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        rule.save();

        a(rule.getAll(Finding.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveFinding() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        rule.save();

        a(rule.getAll(Finding.class).size()).shouldBeEqual(1);
        rule = (Rule) Rule.findAll().get(0);
        rule.remove(finding);
        a(rule.getAll(Finding.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canSetPriority() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();

        a(rule.get("priority")).shouldBeEqual(Priority.HIGH.value());
        a(rule.getPriority()).shouldBeEqual(Priority.HIGH);

        rule.setPriority(Priority.LOW);
        rule.save();

        a(rule.get("priority")).shouldBeEqual(Priority.LOW.value());
        a(rule.getPriority()).shouldBeEqual(Priority.LOW);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Finding finding = Finding.createIt("findingKey", "finding");
        rule.add(finding);
        Tag t = Tag.createIt("tag", "tag");
        rule.add(t);
        rule.save();

        a(Rule.count()).shouldBeEqual(1);
        a(Finding.count()).shouldBeEqual(1);
        a(Tag.count()).shouldBeEqual(1);
        rule.delete(true);
        a(Rule.count()).shouldBeEqual(0);
        a(Finding.count()).shouldBeEqual(0);
        a(Tag.count()).shouldBeEqual(0);
    }
}
