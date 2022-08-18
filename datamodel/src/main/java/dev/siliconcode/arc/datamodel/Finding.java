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
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Manies;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Manies({
        @Many2Many(other = Project.class, join = "projects_findings", sourceFKName = "project_id", targetFKName = "finding_id"),
        @Many2Many(other = Rule.class, join = "rules_findings", sourceFKName = "rule_id", targetFKName = "finding_id")
})
public class Finding extends Model {

    public String getFindingKey() { return getString("findingKey"); }

    public void setReference(Reference ref) {
        List<Reference> refs = Lists.newArrayList(getReferences());
        for (Reference r : refs) {
            removeReference(r);
        }
        if (ref != null)
            add(ref);
        save();
    }

    public void removeReference(Reference ref) { remove(ref); save(); }

    public List<Reference> getReferences() { return getAll(Reference.class); }

    public List<System> getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newArrayList();
        Project parent = getParentProject();
        if (parent != null)
            projects.add(parent);
        return projects;
    }

    public RuleRepository getParentRuleRepository() {
        Rule parent = getParentRule();
        if (parent != null)
            return parent.getParentRuleRepository();
        return null;
    }

    public Rule getParentRule() {
        if (getAll(Rule.class).isEmpty())
            return null;
        return getAll(Rule.class).get(0);
    }

    public Project getParentProject() {
        if (getAll(Project.class).isEmpty())
            return null;
        return getAll(Project.class).get(0);
    }

    public boolean isInjected() {
        return getBoolean("injected");
    }

    public void setInjected(boolean injected) {
        setBoolean("injected", injected);
        save();
    }

    public static Finding of(String ruleKey) {
        Finding f = Finding.create("findingKey", ruleKey);
        f.save();
        Rule rule = Rule.findFirst("ruleKey = ?", ruleKey);
        rule.addFinding(f);
        return f;
    }

    public Finding on(Reference ref) {
        add(ref);
        save();
        return this;
    }

    public Finding on(Component comp) {
        add(Reference.to(comp));
        save();
        comp.getParentProject().addFinding(this);
        return this;
    }

    public Finding on(PatternInstance inst) {
        add(Reference.to(inst));
        save();
        inst.getParentProject().addFinding(this);
        return this;
    }

    public Finding on(Namespace ns) {
        add(Reference.to(ns));
        save();
        ns.getParentProject().addFinding(this);
        return this;
    }

    public Finding and(Reference ref) {
        add(ref);
        save();
        return this;
    }

    public Finding and(Component comp) {
        add(Reference.to(comp));
        save();
        comp.getParentProject().addFinding(this);
        return this;
    }

    public Finding and(PatternInstance inst) {
        add(Reference.to(inst));
        save();
        inst.getParentProject().addFinding(this);
        return this;
    }

    public Finding and(Namespace ns) {
        add(Reference.to(ns));
        save();
        ns.getParentProject().addFinding(this);
        return this;
    }

    public Finding injected() {
        setInjected(true);
        save();
        return this;
    }

    public void setStart(int start) {
        setInteger("start", start);
        save();
    }

    public int getStart() {
        return getInteger("start");
    }

    public void setEnd(int end) {
        setInteger("end", end);
        save();
    }

    public int getEnd() {
        return getInteger("end");
    }

    public Finding copy(String oldPrefix, String newPrefix) {
        Finding f = Finding.of(this.getFindingKey());
        for (Reference ref : getReferences()) {
            f.on(ref.copy(oldPrefix, newPrefix));
        }
        return f;
    }

    public static List<Finding> getFindingsFor(String refKey) {
        return Finding.findBySQL("SELECT findings.* FROM findings INNER JOIN refs ON findings.id = refs.parent_id AND refs.parent_type = ? AND refs.refKey = ?;", Finding.class.getCanonicalName(), refKey);
    }
}
