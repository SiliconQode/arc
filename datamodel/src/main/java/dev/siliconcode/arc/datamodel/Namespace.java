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
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.HasMany;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsTo(parent = Project.class, foreignKeyName = "project_id")
public class Namespace extends Model implements Measurable, ComponentContainer {

    public Namespace() {
    }

    public static Namespace createDefaultNamespace(Project project) {
        Namespace ns = Namespace.findFirst("nsKey = ?", project.getProjectKey() + ":" + "[DEFAULT]");
        if (ns == null) {
            ns = Namespace.builder()
                    .name("")
                    .nsKey(project.getProjectKey() + ":" + "[DEFAULT]")
                    .relPath("")
                    .create();
            project.addNamespace(ns);
        }

        return ns;
    }

    @Builder(buildMethodName = "create")
    public Namespace(String nsKey, String name, String relPath) {
        set("nsKey", nsKey);
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        setName(name);
        saveIt();
    }

    public String getNsKey() {
        return getString("nsKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setRelPath(name);
        set("name", name);
        save();
    }

    public void addFile(File file) {
        if (file != null) {
            file.setParentNSID(getId());
        }
    }

    public void removeFile(File file) {
        if (file != null && file.getParentNSID() != null && file.getParentNSID().equals(getId())) {
            file.setParentNSID(null);
        }
    }

    public List<File> getFiles() {
        return File.find("parent_ns_id = ?", getId());
    }

    public File getFileByName(String name) {
        for (File file : getFiles()) {
            if (file.getName().equals(name))
                return file;
        }
        return null;
    }

    public void addNamespace(Namespace ns) {
        if (ns != null && !ns.getName().equals(this.getName())) {
            ns.setParentNSID(getId());
            ns.save();
        }
    }

    public void removeNamespace(Namespace ns) {
        if (ns != null && ns.getParentNSID() != null && ns.getParentNSID().equals(getId())) {
            ns.setParentNSID(null);
            ns.save();
        }
    }

    public void setParentNSID(Object id) {
        set("parent_ns_id", id);
        save();
    }

    public Object getParentNSID() {
        return get("parent_ns_id");
    }

    public List<Namespace> getNamespaces() {
        return Namespace.find("parent_ns_id = ?", getId());
    }

    public boolean containsNamespace(Namespace ns) {
        return ns != null && ns.getParentNSID() != null && ns.getParentNSID().equals(getId());
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getFiles().forEach(f -> imports.addAll(f.getImports()));
        getNamespaces().forEach(ns -> imports.addAll(ns.getImports()));
        return imports;
    }

    public void addType(Type type) {
        if (type != null) {
            add(type);
            if (type.getContainingType() == null) {
                if (getName().isEmpty() || getName().isBlank())
                    type.setQualifiedName(type.getName());
                else
                    type.setQualifiedName(getName() + "." + type.getName());
            }
        }
        save();
    }

    public void removeType(Type type) {
        if (type != null)
            remove(type);
        save();
    }

    @Override
    public List<Type> getAllTypes() {
//        List<Type> types = Lists.newArrayList();
//        types.addAll(getClasses());
//        types.addAll(getInterfaces());
//        types.addAll(getEnums());
//        return types;
        return getAll(Type.class);
    }

    public Type getTypeByName(String name) {
        return findType("name", name);
    }

    @Override
    public List<Type> getClasses() {
        return get(Type.class, "type = ?", Type.CLASS);
        //getNamespaces().forEach(ns -> classes.addAll(ns.getClasses()));
//        return classes;
    }

    public Type getClassByName(String name) {
//        LazyList<Class> list = get(Class.class, "name = ?", name);
//        if (!list.isEmpty())
//            return list.get(0);
//        else
//            return null;
        LazyList<Type> list = get(Type.class, "type = ? and name = ?", Type.CLASS, name);
        if (!list.isEmpty())
            return list.get(0);
        else
            return null;
    }

    @Override
    public List<Type> getInterfaces() {
//        List<Interface> interfaces = Lists.newArrayList(getAll(Interface.class));
        //getNamespaces().forEach(ns -> interfaces.addAll(ns.getInterfaces()));
        return get(Type.class, "type = ?", Type.INTERFACE);
//        return interfaces;
    }

    public Type getInterfaceByName(String name) {
//        LazyList<Interface> list = get(Interface.class, "name = ?", name);
//        if (!list.isEmpty())
//            return list.get(0);
//        else
//            return null;
        LazyList<Type> list = get(Type.class, "type = ? and name = ?", Type.INTERFACE, name);
        if (!list.isEmpty())
            return list.get(0);
        else
            return null;
    }

    @Override
    public List<Type> getEnums() {
//        List<Enum> enums = Lists.newArrayList(getAll(Enum.class));
        //getNamespaces().forEach(ns -> enums.addAll(ns.getEnums()));
//        return enums;
        return get(Type.class, "type = ?", Type.ENUM);
    }

    public Type getEnumByName(String name) {
//        LazyList<Enum> list = get(Enum.class, "name = ?", name);
//        if (!list.isEmpty())
//            return list.get(0);
//        else
//            return null;
        LazyList<Type> list = get(Type.class, "type = ? and name = ?", Type.ENUM, name);
        if (!list.isEmpty())
            return list.get(0);
        else
            return null;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getAllTypes().forEach(f -> members.addAll(f.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getEnums().forEach(enm -> literals.addAll(enm.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> initializers = Lists.newArrayList();
        getAllTypes().forEach(f -> initializers.addAll(f.getInitializers()));
        return initializers;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getAllTypes().forEach(f -> members.addAll(f.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getAllTypes().forEach(f -> fields.addAll(f.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(f -> methods.addAll(f.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(f -> methods.addAll(f.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getAllTypes().forEach(f -> constructors.addAll(f.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getAllTypes().forEach(f -> destructors.addAll(f.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        if (getParentProject() != null)
            getParentProject().getParentSystems();
        return Lists.newArrayList();
    }

    public System getParentSystem() {
        if (getParentProject() != null)
            return getParentProject().getParentSystem();
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

    public String getFullName() {
        if (!this.getName().contains(".") && getParentNamespace() != null)
            return getParentNamespace().getFullName() + "." + this.getName();
        return this.getName();
    }

    public Namespace getParentNamespace() {
        if (getParentNSID() != null)
            return Namespace.findById(getParentNSID());
        return null;
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        Namespace ns = getParentNamespace();
        if (ns == null)
            return getParentProject();
        else
            return ns;
    }

    public Object getParentModuleID() {
        return get("parent_mod_id");
    }

    public void setParentModuleID(Object id) {
        set("parent_mod_id", id);
    }

    public List<Module> getParentModules() {
        List<Module> modules = Lists.newLinkedList();
        Module mod = getParentModule();
        if (mod != null)
            modules.add(mod);
        return modules;
    }

    public Module getParentModule() {
        if (getParentModuleID() != null)
            return Module.findById(getParentModuleID());
        return null;
    }

    @Override
    public String getRefKey() {
        return getString("nsKey");
    }

    public void updateKey() {
        Project proj = parent(Project.class);
        String parentKey = null;
        String newKey;

        if (proj != null)
            parentKey = proj.getProjectKey();

        if (parentKey != null) {
            newKey = parentKey + ":" + getFullName();
        } else {
            newKey = getFullName();
        }

        setString("nsKey", newKey);
        save();
        refresh();
        getAllTypes().forEach(Type::updateKey);
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        if (path.contains("."))
            path = path.substring(path.lastIndexOf(".") + 1);
        setString("relPath", path);
        save();
    }

    public String getFullPath(FileType type, int index) {
        String path = "";
        Namespace ns = getParentNamespace();
        Module mod = getParentModule();
        Project proj = getParentProject();
        if (ns != null) {
            path = ns.getFullPath(type, index);
        } else if (mod != null) {
            path = mod.getFullPath();
            if (type == FileType.SOURCE)
                path += mod.getSrcPath(index);
            else if (type == FileType.TEST)
                path += mod.getTestPath(index);
        } else {
            path = proj.getFullPath();
            if (type == FileType.SOURCE)
                path += proj.getSrcPath(index);
            else if (type == FileType.TEST)
                path += proj.getTestPath(index);
        }

        if (!path.endsWith(java.io.File.separator))
            path += java.io.File.separator;

        return path + getRelPath() + java.io.File.separator;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Namespace) {
            Namespace comp = (Namespace) o;
            return comp.getNsKey().equals(this.getNsKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNsKey());
    }

    public Type findInterface(String attribute, String value) {
//        try {
//            return get(Interface.class, attribute + " = ?", value).get(0);
//        } catch (IndexOutOfBoundsException ex) {
//            return null;
//        }
        try {
            return get(Type.class, attribute + " = ? and type = ?", value, Type.INTERFACE).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Type findType(String attribute, String value) {
        try {
            return get(Type.class, attribute + " = ?", value).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Type findClass(String attribute, String value) {
//        try {
//            return get(Class.class, attribute + " = ?", value).get(0);
//        } catch (IndexOutOfBoundsException ex) {
//            return null;
//        }
        try {
            return get(Type.class, attribute + " = ? and type = ?", value, Type.CLASS).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Type findEnum(String attribute, String value) {
//        try {
//            return get(Enum.class, attribute + " = ?", value).get(0);
//        } catch (IndexOutOfBoundsException ex) {
//            return null;
//        }
        try {
            return get(Type.class, attribute + " = ? and type = ?", value, Type.ENUM).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Namespace copy(String oldPrefix, String newPrefix) {
        Namespace copy = Namespace.builder()
                .name(this.getName())
                .nsKey(this.getNsKey().replace(oldPrefix, newPrefix))
                .relPath(this.getRelPath())
                .create();

        getAllTypes().forEach(type -> copy.addType(type.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public Reference createReference() {
        return Reference.builder().refKey(getNsKey()).refType(RefType.NAMESPACE).create();
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
