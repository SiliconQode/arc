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

public class FindingSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Finding finding = new Finding();
        a(finding).shouldBe("valid");
//        //a(finding.errors().get("author")).shouldBeEqual("Author must be provided");
        finding.set("findingKey", "finding");
        a(finding).shouldBe("valid");
        finding.save();
        finding = (Finding) Finding.findAll().get(0);
        a(finding.getId()).shouldNotBeNull();
        a(finding.get("findingKey")).shouldBeEqual("finding");
        a(Finding.count()).shouldBeEqual(1);
    }

    @Test
    public void canSetReference() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        finding.add(ref);
        finding.save();

        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void onlyAddsOneReference() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        Reference ref2 = Reference.createIt("refKey", "ref2");

        finding.setReference(ref);
        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
        finding.setReference(ref2);
        a(finding.getAll(Reference.class).size()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Finding finding = Finding.createIt("findingKey", "finding");
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        finding.add(ref);
        finding.save();

        a(Finding.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        finding.delete(true);
        a(Finding.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }

    @Test
    public void canBeAddedToProject() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Finding finding = Finding.createIt("findingKey", "finding");
        finding.save();

        p.addFinding(finding);

        a(Finding.belongsTo(Project.class)).shouldBeTrue();
        a(finding.getParentProject()).shouldBeEqual(p);
    }

    @Test
    public void canBeAddedToRule() {
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        Finding finding = Finding.createIt("findingKey", "finding");
        finding.save();

        rule.addFinding(finding);

        a(Finding.belongsTo(Rule.class)).shouldBeTrue();
        a(finding.getParentRule()).shouldBeEqual(rule);
    }
}
