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

public class RuleRepositorySpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        RuleRepository repo = new RuleRepository();
        a(repo).shouldBe("valid");
        //a(repo.errors().get("author")).shouldBeEqual("Author must be provided");
        repo.set("repoKey", "key", "name", "name");
        a(repo).shouldBe("valid");
        repo.save();
        repo = (RuleRepository) RuleRepository.findAll().get(0);
        a(repo.getId()).shouldNotBeNull();
        a(repo.get("repoKey")).shouldBeEqual("key");
        a(repo.get("name")).shouldBeEqual("name");
        a(RuleRepository.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddRule() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(repo.getAll(Rule.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRule() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(repo.getAll(Rule.class).size()).shouldBeEqual(1);
        repo = (RuleRepository) RuleRepository.findAll().get(0);
        repo.remove(rule);
        a(repo.getAll(Rule.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        RuleRepository repo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        rule.save();
        repo.add(rule);

        a(RuleRepository.count()).shouldBeEqual(1);
        a(Rule.count()).shouldBeEqual(1);
        repo.delete(true);
        a(RuleRepository.count()).shouldBeEqual(0);
        a(Rule.count()).shouldBeEqual(0);
    }
}
