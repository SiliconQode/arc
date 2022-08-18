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

import dev.siliconcode.arc.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Manies;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Manies({
        @Many2Many(other = Finding.class, join = "rules_findings", sourceFKName = "rule_id", targetFKName = "finding_id")
})
public class Rule extends Model {

    public Rule() {}

    @Builder(buildMethodName = "create")
    public Rule(String key, String name, Priority priority, String description) {
        setKey(key);
        setName(name);
        setPriority(priority);
        setDescription(description);
    }

    public void setPriority(Priority p) {
        set("priority", p.value());
        save();
    }

    public Priority getPriority() {
        int p = (Integer) get("priority");
        return Priority.fromValue(p);
    }

    public RuleRepository getParentRuleRepository() {
        return parent(RuleRepository.class);
    }

    public void setKey(String key)
    {
        set("ruleKey", key);
        save();
    }

    public String getKey() {
        return getString("ruleKey");
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public void setDescription(String desc) {
        set("description", desc);
        save();
    }

    public String getDescription() {
        return getString("description");
    }

    public void addTag(Tag tag) {
        if (tag != null)
            add(tag);
        save();
    }

    public void removeTag(Tag tag) {
        if (tag != null)
            remove(tag);
        save();
    }

    public List<Tag> getTags() {
        return getAll(Tag.class);
    }

    public void addFinding(Finding finding) {
        if (finding != null)
            add(finding);
        save();
    }

    public void removeFinding(Finding finding) {
        if (finding != null)
            remove(finding);
        save();
    }

    public List<Finding> getFindings() {
        return getAll(Finding.class);
    }

    public boolean hasFindingOn(Component comp) {
        return getFindings().stream().anyMatch(finding -> {
            List<Reference> refs = finding.getReferences();
            if (refs.isEmpty() || comp == null) {
                return false;
            } else {
                if (refs.get(0).getRefKey() == null) {
                    return false;
                }
                return refs.get(0).getRefKey().equals(comp.getRefKey());
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            Rule other = (Rule) obj;
            if (other.getDescription().equals(getDescription())) {
                if (other.getName().equals(getName())) {
                    return other.getKey().equals(getKey());
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getName(), getKey());
    }
}
