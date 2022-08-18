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
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class System extends Model implements Measurable, Structure {

    public System() {
    }

    @Builder(buildMethodName = "create")
    public System(String name, String key, String basePath) {
        set("name", name, "sysKey", key, "basePath", basePath);
        save();
    }

    public void setName(String name) {
        setBasePath(name);
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public void setKey(String key) {
        set("sysKey", key);
        save();
    }

    public String getKey() {
        return getString("sysKey");
    }

    public void setVersion(String version) {
        set("version", version);
        save();
    }

    public String getVersion() {
        return getString("version");
    }

    public void addProject(Project p) {
        add(p);
        save();
    }

    public void removeProject(Project p) {
        remove(p);
        save();
    }

    public List<Project> getProjects() {
        return getAll(Project.class);
    }

    public Project getProjectByName(String name) {
        try {
            return get(Project.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasProjectWithName(String name) {
        return getProjectByName(name) != null;
    }

    public void addPatternChain(PatternChain p) {
        add(p);
        save();
    }

    public void removePatternChain(PatternChain p) {
        remove(p);
        save();
    }

    public List<PatternChain> getPatternChains() {
        return getAll(PatternChain.class);
    }

    public List<SCM> getSCMs() {
        List<SCM> scms = Lists.newArrayList();
        getProjects().forEach(proj -> scms.addAll(proj.getSCMs()));
        return scms;
    }

    public List<Module> getModules() {
        List<Module> modules = Lists.newArrayList();
        getProjects().forEach(proj -> modules.addAll(proj.getModules()));
        return modules;
    }

    public List<Namespace> getNamespaces() {
        List<Namespace> namespaces = Lists.newArrayList();
        getProjects().forEach(proj -> namespaces.addAll(proj.getNamespaces()));
        return namespaces;
    }

    public Namespace findNamespace(String name) {
        AtomicReference<Namespace> ns = new AtomicReference<>();
        getProjects().forEach(proj -> {
            Namespace n = proj.findNamespace(name);
            if (n != null)
                ns.set(n);
        });
        return ns.get();
    }

    public boolean hasNamespace(String name) {
        return findNamespace(name) != null;
    }

    @Override
    public List<File> getFiles() {
        List<File> files = Lists.newArrayList();
        getProjects().forEach(proj -> files.addAll(proj.getFiles()));
        return files;
    }

    @Override
    public List<File> getFilesByType(FileType type) {
        List<File> files = Lists.newArrayList();
        getProjects().forEach(proj -> files.addAll(proj.getFilesByType(type)));
        return files;
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getProjects().forEach(proj -> imports.addAll(proj.getImports()));
        return imports;
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        getProjects().forEach(proj -> types.addAll(proj.getAllTypes()));
        return types;
    }

    public Type findType(String name) {
        if (hasClass(name))
            return findClass(name);
        if (hasInterface(name))
            return findInterface(name);
        if (hasEnum(name))
            return findEnum(name);
        return null;
    }

    public boolean hasType(String name) {
        return findType(name) != null;
    }

    @Override
    public List<Type> getClasses() {
        List<Type> classes = Lists.newArrayList();
        getProjects().forEach(proj -> classes.addAll(proj.getClasses()));
        return classes;
    }

    public Type findClass(String name) {
        AtomicReference<Type> type = new AtomicReference<>();
        getProjects().forEach(proj -> {
            Type t = proj.findClass("name", name);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public boolean hasClass(String name) {
        return findClass(name) != null;
    }

    @Override
    public List<Type> getInterfaces() {
        List<Type> interfaces = Lists.newArrayList();
        getProjects().forEach(proj -> interfaces.addAll(proj.getInterfaces()));
        return interfaces;
    }

    public Type findInterface(String name) {
        AtomicReference<Type> type = new AtomicReference<>();
        getProjects().forEach(proj -> {
            Type t = proj.findInterface("name", name);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public boolean hasInterface(String name) {
        return findInterface(name) != null;
    }

    @Override
    public List<Type> getEnums() {
        List<Type> enums = Lists.newArrayList();
        getProjects().forEach(proj -> enums.addAll(proj.getEnums()));
        return enums;
    }

    public Type findEnum(String name) {
        AtomicReference<Type> type = new AtomicReference<>();
        getProjects().forEach(proj -> {
            Type t = proj.findEnum("name", name);
            if (t != null)
                type.set(t);
        });
        return type.get();
    }

    public boolean hasEnum(String name) {
        return findEnum(name) != null;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getProjects().forEach(proj -> members.addAll(proj.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getProjects().forEach(proj -> literals.addAll(proj.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newArrayList();
        getProjects().forEach(proj -> initializers.addAll(proj.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getProjects().forEach(proj -> members.addAll(proj.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getProjects().forEach(proj -> fields.addAll(proj.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getProjects().forEach(proj -> methods.addAll(proj.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getProjects().forEach(proj -> methods.addAll(proj.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getProjects().forEach(proj -> constructors.addAll(proj.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getProjects().forEach(proj -> destructors.addAll(proj.getDestructors()));
        return destructors;
    }

    public List<PatternInstance> getPatternInstances() {
        List<PatternInstance> instances = Lists.newArrayList();
        getProjects().forEach(proj -> instances.addAll(proj.getPatternInstances()));
        return instances;
    }

    public List<Language> getLanguages() {
        List<Language> languages = Lists.newArrayList();
        getProjects().forEach(proj -> languages.addAll(proj.getLanguages()));
        return languages;
    }

    public List<RoleBinding> getRoleBindings() {
        List<RoleBinding> bindings = Lists.newArrayList();
        getProjects().forEach(proj -> bindings.addAll(proj.getRoleBindings()));
        return bindings;
    }

    public List<Finding> getFindings() {
        List<Finding> findings = Lists.newArrayList();
        getProjects().forEach(proj -> findings.addAll(proj.getFindings()));
        return findings;
    }

    public List<Relation> getRelations() {
        List<Relation> relations = Lists.newArrayList();
        getProjects().forEach(proj -> relations.addAll(proj.getRelations()));
        return relations;
    }

    @Override
    public String getRefKey() {
        return getIdName();
    }

    public void setBasePath(String path) {
        setString("basePath", path);
        save();
    }

    public String getBasePath() {
        return getString("basePath");
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        return null;
    }

    @Override
    public Project getParentProject() { return null; }

    public void updateKeys() {
        this.setKey(getName());
        refresh();
        getProjects().forEach(Project::updateKeys);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof System) {
            System comp = (System) o;
            return comp.getKey().equals(this.getKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    /**
     * @return The parent file of this Measurable
     */
    @Override
    public File getParentFile() {
        return null;
    }

    public void addMeasure(Measure meas) {
        add(meas);
        save();
    }

    public void removeMeasure(Measure meas) {
        remove(meas);
        save();
    }

    public double getMeasureValueByName(String key) {
        List<Measure> measures = getMeasures();
        for (Measure m : measures) {
            if (m.getMetricKey().equals(key))
                return m.getValue();
        }
        return 0.0d;
    }

    public List<Measure> getMeasures() {
        return getAll(Measure.class);
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
}
