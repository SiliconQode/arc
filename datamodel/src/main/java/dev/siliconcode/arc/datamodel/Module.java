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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Module extends Model implements Measurable, ComponentContainer {

    public Module() {
    }

    @Builder(buildMethodName = "create")
    public Module(String name, String moduleKey, String relPath, String srcPath, String testPath) {
        set("moduleKey", moduleKey);
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        setName(name);
        save();
    }

    public String getModuleKey() {
        return getString("moduleKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
        save();
        setRelPath(name);
    }

    public void addNamespace(Namespace ns) {
        if (ns != null) {
            ns.setParentModuleID(getId());
            ns.save();
        }
    }

    public void removeNamespace(Namespace ns) {
        if (ns != null && ns.getParentModuleID() != null && ns.getParentModuleID().equals(getId())) {
            ns.setParentModuleID(null);
            ns.save();
            ns.refresh();
        }
    }

    public List<Namespace> getNamespaces() {
        return Namespace.find("parent_mod_id = ?", getId());
    }

    public List<File> getFiles() {
        List<File> files = Lists.newArrayList();
        getNamespaces().forEach(ns -> files.addAll(ns.getFiles()));
        return files;
    }

    public List<File> getFilesByType(FileType type) {
        List<File> ret = Lists.newArrayList();
        for (File f : getFiles()) {
            if (f.getType().equals(type))
                ret.add(f);
        }

        return ret;
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getFiles().forEach(ns -> imports.addAll(ns.getImports()));
        return imports;
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        getNamespaces().forEach(ns -> types.addAll(ns.getAllTypes()));
        return types;
    }

    @Override
    public List<Type> getClasses() {
        List<Type> classes = Lists.newArrayList();
        getNamespaces().forEach(ns -> classes.addAll(ns.getClasses()));
        return classes;
    }

    @Override
    public List<Type> getInterfaces() {
        List<Type> interfaces = Lists.newArrayList();
        getNamespaces().forEach(ns -> interfaces.addAll(ns.getInterfaces()));
        return interfaces;
    }

    @Override
    public List<Type> getEnums() {
        List<Type> enums = Lists.newArrayList();
        getNamespaces().forEach(ns -> enums.addAll(ns.getEnums()));
        return enums;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getNamespaces().forEach(ns -> members.addAll(ns.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getNamespaces().forEach(ns -> literals.addAll(ns.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newArrayList();
        getNamespaces().forEach(ns -> initializers.addAll(ns.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getNamespaces().forEach(ns -> members.addAll(ns.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getNamespaces().forEach(ns -> fields.addAll(ns.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(ns -> methods.addAll(ns.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(ns -> methods.addAll(ns.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getNamespaces().forEach(ns -> constructors.addAll(ns.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getNamespaces().forEach(ns -> destructors.addAll(ns.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        Project proj = getParentProject();
        if (proj != null)
            return proj.getParentSystems();
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        Project proj = getParentProject();
        if (proj != null)
            return proj.getParentSystem();
        return null;
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
        if (getParentProject() != null)
            projects.add(getParentProject());
        return projects;
    }

    @Override
    public Project getParentProject() {
        return parent(Project.class);
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        return getParentProject();
    }

    @Override
    public String getRefKey() {
        return getString("moduleKey");
    }

    public void updateKey() {
        Project parent = parent(Project.class);
        String newKey;
        if (parent != null)
            newKey = parent.getProjectKey() + ":" + getName();
        else
            newKey = getName();

        setString("moduleKey", newKey);
        save();
        refresh();
        getNamespaces().forEach(Namespace::updateKey);
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        if (path == null || path.isEmpty()) return;
        setString("relPath", path);
        save();
    }

    public String[] getSrcPaths() {
        return getParentProject().getSrcPaths(this);
    }

    public String getSrcPath() {
        return getSrcPath(0);
    }

    public String getSrcPath(int index) {
        String[] paths = getSrcPaths();
        if (paths.length > 0)
            if (paths[index].contains(getRelPath()))
                return paths[index].replace(getRelPath() + java.io.File.separator, "");
            else return paths[index];
        else
            return getParentProject().getSrcPath(index);
    }

    public String[] getBinaryPaths() {
        return getParentProject().getBinaryPaths(this);
    }

    public String getBinaryPath() {
        return getBinaryPath(0);
    }

    public String getBinaryPath(int index) {
        String[] paths = getBinaryPaths();
        if (paths.length > 0)
            return paths[index];
        else
            return getParentProject().getBinaryPath(index);
    }

    public String[] getTestPaths() {
        return getParentProject().getTestPaths(this);
    }

    public String getTestPath() {
        return getTestPath(0);
    }

    public String getTestPath(int index) {
        String[] paths = getTestPaths();
        if (paths.length > 0)
            return paths[index];
        else
            return getParentProject().getTestPath(index);
    }

    public String getFullPath() {
        if (!getName().equalsIgnoreCase("default"))
            return getParentProject().getFullPath() + getRelPath() + java.io.File.separator;
        else
            return getParentProject().getFullPath();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Module) {
            Module comp = (Module) o;
            return comp.getModuleKey().equals(this.getModuleKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModuleKey());
    }

    public Type findInterface(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type t = ns.findInterface(attribute, value);
            if (t != null)
                return t;
        }
        return null;
    }

    public Type findClass(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type t = ns.findClass(attribute, value);
            if (t != null)
                return t;
        }
        return null;
    }

    public Type findEnum(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type t = ns.findEnum(attribute, value);
            if (t != null)
                return t;
        }
        return null;
    }

    public Module copy(String oldProjectKey, String newProjectKey) {
        return Module.builder()
                .name(this.getName())
                .moduleKey(newProjectKey + ":" + this.getName())
                .relPath(this.getRelPath())
                .srcPath(this.getSrcPath())
                .testPath(this.getTestPath())
                .create();
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
