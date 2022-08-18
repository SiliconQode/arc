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
import dev.siliconcode.arc.datamodel.util.MeasureTable;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Manies;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Manies({
        @Many2Many(other = Project.class,         join = "projects_measures",              sourceFKName = "measure_id", targetFKName = "project_id"),
        @Many2Many(other = Metric.class,          join = "metrics_measures",               sourceFKName = "measure_id", targetFKName = "metric_id"),
        @Many2Many(other = Namespace.class,       join = "namespaces_measures",            sourceFKName = "measure_id", targetFKName = "namespace_id"),
        @Many2Many(other = PatternInstance.class, join = "pattern_instances_measures",     sourceFKName = "measure_id", targetFKName = "pattern_instance_id"),
        @Many2Many(other = File.class,            join = "files_measures",                 sourceFKName = "measure_id", targetFKName = "file_id"),
        @Many2Many(other = Module.class,          join = "modules_measures",               sourceFKName = "measure_id", targetFKName = "module_id"),
        @Many2Many(other = Initializer.class,     join = "initializers_measures",          sourceFKName = "measure_id", targetFKName = "initializer_id"),
        @Many2Many(other = Method.class,          join = "methods_measures",               sourceFKName = "measure_id", targetFKName = "method_id"),
        @Many2Many(other = Type.class,            join = "types_measures",                 sourceFKName = "measure_id", targetFKName = "type_id"),
//        @Many2Many(other = Class.class,           join = "classes_measures",               sourceFKName = "measure_id", targetFKName = "class_id"),
//        @Many2Many(other = Enum.class,            join = "enums_measures",                 sourceFKName = "measure_id", targetFKName = "enum_id"),
//        @Many2Many(other = Interface.class,       join = "interfaces_measures",            sourceFKName = "measure_id", targetFKName = "interface_id"),
        @Many2Many(other = Constructor.class,     join = "constructors_measures",          sourceFKName = "measure_id", targetFKName = "constructor_id"),
        @Many2Many(other = Destructor.class,      join = "destructors_measures",           sourceFKName = "measure_id", targetFKName = "destructor_id")
})
public class Measure extends Model {

    public String getMeasureKey() {
        return getString("measureKey");
    }

    public String getMetricKey() {
        return getString("metricKey");
    }

    public double getValue() {
        return getDouble("value");
    }

    public void setValue(double value) {
        setDouble("value", value);
        save();
    }

    public void setReference(Reference ref) {
        List<Reference> refs = Lists.newArrayList(getReferences());
        for (Reference r : refs) {
            removeReference(r);
        }
        if (ref != null)
            add(ref);
        save();
    }

    public void removeReference(Reference ref) {
        remove(ref);
        save();
    }

    public List<Reference> getReferences() {
        return getAll(Reference.class);
    }

    public Reference getReference() {
        List<Reference> refs = getReferences();
        if (refs.isEmpty())
            return null;

        return getReferences().get(0);
    }

    public System getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystem();
        return null;
    }

    public Project getParentProject() {
        if (getAll(Project.class).isEmpty())
            return null;
        return getAll(Project.class).get(0);
    }

    public MetricRepository getParentMetricRepository() {
        Metric parent = getParentMetric();
        if (parent != null)
            return parent.getParentMetricRepository();
        return null;
    }

    public Metric getParentMetric() {
        if (getAll(Metric.class).isEmpty())
            return null;
        return getAll(Metric.class).get(0);
    }

    public static Measure of(String metricKey) {
        Measure m = Measure.create("metricKey", metricKey);
        m.save();
        Metric met = Metric.findFirst("metricKey = ?", metricKey);
        met.addMeasure(m);
        return m;
    }

    public Measure on(Measurable m) {
        Reference ref = Reference.createIt("refKey", m.getRefKey());
        set("measureKey", m.getRefKey() + ":" + getMetricKey());
        add(ref);
        save();
        m.addMeasure(this);
        return this;
    }

    public Measure on(Reference ref) {
        add(ref);
        save();
        return this;
    }

    public Measure withValue(double value) {
        setValue(value);
        save();
        MeasureTable.getInstance().addMeasure(getReference().getRefKey(), getMetricKey(), value);
        return this;
    }

    public void store() {
        save();
        MeasureTable.getInstance().addMeasure(getReference().getRefKey(), getMetricKey(), getValue());
    }

    public static Measure retrieve(Measurable m, String metricKey) {
        List<Measure> candidates = Measure.find("metricKey = ?", metricKey);
        for (Measure meas : candidates)
            if (!meas.getReferences().isEmpty() && meas.getReferences().get(0).getRefKey().equals(m.getRefKey()))
                return meas;

        return null;
    }

    public Measure copy(String oldPrefix, String newPrefix) {
        return Measure.of(this.getMetricKey())
                .on(this.getReferences().get(0).copy(oldPrefix, newPrefix))
                .withValue(this.getValue());
    }

    public static boolean hasMetric(Measurable comp, String metric) {
        return retrieve(comp, metric) != null;
    }

    public static List<Double> getAllClassValues(Project proj, String repo, String handle) {
        List<Double> values = Lists.newArrayList();

        proj.getAllTypes().forEach(type -> {
            List<Measure> m = type.get(Measure.class, "metricKey = ?", String.format("%s:%s", repo, handle));
            if (!m.isEmpty())
                values.add(m.get(0).getValue());
        });

        return values;
    }

    public static List<Double> getAllFileValues(Project proj, String repo, String handle) {
        List<Double> values = Lists.newArrayList();

        proj.getFiles().forEach(file -> {
            List<Measure> m = file.get(Measure.class, "metricKey = ?", String.format("%s:%s", repo, handle));
            if (!m.isEmpty())
                values.add(m.get(0).getValue());
        });

        return values;
    }

    public static List<Double> getAllMethodValues(Project proj, String repo, String handle) {
        List<Double> values = Lists.newArrayList();

        proj.getAllMethods().forEach(method -> {
            List<Measure> m = method.get(Measure.class, "metricKey = ?", String.format("%s:%s", repo, handle));
            if (!m.isEmpty())
                values.add(m.get(0).getValue());
        });

        return values;
    }

    public static Double getProjectMetric(Project proj, String repo, String handle) {
        return valueFor(repo, handle, proj);
    }

    public static double valueFor(String repoKey, String handle, Measurable comp) {
        String metricKey = repoKey + ":" + handle;
        String compKey = comp.getRefKey();
        if (MeasureTable.getInstance().contains(compKey, metricKey)) {
            return MeasureTable.getInstance().getValue(compKey, metricKey);
        } else {
            return comp.getValueFor(metricKey);
        }
    }

    public static boolean hasValueFor(String repoKey, String handle, Measurable comp) {
        String metricKey = repoKey + ":" + handle;
        String compKey = comp.getRefKey();
        Metric parent = Metric.findFirst("metricKey = ?", metricKey);

        for (Measure measure : parent.getMeasures()) {
            if (measure.getReference().getRefKey().equals(compKey)) {
                return true;
            }
        }
        return false;
    }
}
