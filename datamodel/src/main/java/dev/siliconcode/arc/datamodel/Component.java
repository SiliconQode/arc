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

import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Component extends Model implements Measurable {

    public void setCompKey(String key) {
        set("compKey", key);
        save();
    }

    public String getCompKey() {
        return getString("compKey");
    }

    public void setStart(int start) {
        set("start", start);
        save();
    }

    public int getStart() {
        return getInteger("start");
    }

    public void setEnd(int end) {

        set("end", end);
        save();
    }

    public int getEnd() {
        return getInteger("end");
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public void setAccessibility(Accessibility access) {
        set("accessibility", access.value());
        save();
    }

    public Accessibility getAccessibility() {
        return Accessibility.fromValue(getInteger("accessibility"));
    }

    public void addModifier(String mod) {
        add(Modifier.forName(mod));
        save();
    }

    public void addModifier(Modifier mod) {
        add(mod);
        save();
    }

    public void removeModifier(String mod) {
        remove(Modifier.forName(mod));
        save();
    }

    public void removeModifier(Modifier mod) {
        remove(mod);
        save();
    }

    public List<Modifier> getModifiers() {
        return getAll(Modifier.class);
    }

    public boolean hasModifier(String name) {
        return !get(Modifier.class, "name = ?", name.toUpperCase()).isEmpty();
    }

    public boolean hasModifier(Modifier mod) {
        return hasModifier(mod.getName().toUpperCase());
    }

    public boolean hasModifier(Modifier.Values value) {
        return hasModifier(value.toString());
    }

    protected void createRelation(Component toComp, Component fromComp, RefType toType, RefType fromType, RelationType type) {
        Reference to, from;
        toComp.refresh();
        fromComp.refresh();

        from = Reference.builder().refKey(fromComp.getCompKey()).refType(fromType).create();
        to = Reference.builder().refKey(toComp.getCompKey()).refType(toType).create();

        Relation rel = Relation.createIt("relKey", fromComp.getCompKey() + "-" + toComp.getCompKey());
        rel.saveIt();
        rel.setToAndFromRefs(to, from);
        rel.setType(type);
        rel.saveIt();

        List<Project> projects = getParentProjects();
        if (!projects.isEmpty()) {
            Project proj = projects.get(0);
            proj.addRelation(rel);
        }
    }

    abstract public List<Project> getParentProjects();

    protected void deleteRelation(Component toComp, Component fromComp, RefType toType, RefType fromType, RelationType type) {
        toComp.refresh();
        fromComp.refresh();

        List<Project> projects = getParentProjects();
        final Relation[] rel = new Relation[1];
        if (!projects.isEmpty()) {
            Project proj = projects.get(0);

            proj.getRelations().forEach( r -> {
                if (r.getRelKey().equals(fromComp.getCompKey() + "-" + toComp.getCompKey()) && r.getType() == type)
                    rel[0] = r;
            });
        } else {
            List<Relation> rels = Relation.find("relKey = ? AND type = ?", fromComp.getCompKey() + "-" + toComp.getCompKey(), type.value());
            if (!rels.isEmpty())
                rel[0] = rels.get(0);
        }

        if (rel[0] != null)
            rel[0].delete();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Component) {
            Component comp = (Component) o;
            return comp.getCompKey().equals(this.getCompKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompKey());
    }

    public void addMeasure(Measure meas) {
        add(meas);
        save();
    }

    public void removeMeasure(Measure meas) {
        remove(meas);
        save();
    }

    public List<Measure> getMeasures() {
        return getAll(Measure.class);
    }

    public void updateReferences(String oldKey) {
        Reference.find("refKey = ?", oldKey).forEach(model -> {
            Reference ref = (Reference) model;
            ref.setRefKey(this.getCompKey());
        });
    }

    public double getValueFor(String metricKey) {
        try {
            List<Measure> measures = get(Measure.class, "metricKey = ?", metricKey);
            Measure measure = measures.get(measures.size() - 1);
            return measure.getValue();
        } catch (IndexOutOfBoundsException ex) {
            return 0.0;
        }
    }

    public boolean hasValueFor(String metricKey) {
        return get(Measure.class, "metricKey = ?", metricKey).size() > 0;
    }

    public abstract Reference createReference();
}
