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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.siliconcode.arc.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RuleRepository extends Model {

    public RuleRepository() {

    }

    @Builder(buildMethodName = "create")
    public RuleRepository(String key, String name) {
        set("repoKey", key);
        setName(name);
    }

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addRule(Rule rule) { add(rule); save(); }

    public void removeRule(Rule rule) { remove(rule); save(); }

    public List<Rule> getRules() { return getAll(Rule.class); }

    public List<Finding> getFindings() {
        Set<Finding> findings = Sets.newHashSet();
        getRules().forEach(rule -> {
            findings.addAll(rule.getFindings());
        });
        return Lists.newArrayList(findings);
    }

    public List<Tag> getTags() {
        Set<Tag> tags = Sets.newHashSet();
        getRules().forEach(rule -> {
            tags.addAll(rule.getTags());
        });
        return Lists.newArrayList(tags);
    }
}
