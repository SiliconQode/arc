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
import lombok.extern.log4j.Log4j2;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class Project extends Model implements Measurable, ComponentContainer {

    public Project() {
    }

    @Builder(buildMethodName = "create")
    public Project(String projKey, String name, String version, String relPath, SCM scm) {
        set("projKey", projKey, "name", name, "version", version);
        save();
        if (relPath != null && !relPath.isEmpty())
            setRelPath(relPath);
        if (scm != null)
            addSCM(scm);
    }

    public Namespace getDefaultNamespace() {
        return Namespace.createDefaultNamespace(this);
    }

    public String getProjectKey() {
        return getString("projKey");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setRelPath(name);
        set("name", name);
        save();
    }

    public String getVersion() {
        return getString("version");
    }

    public void setVersion(String version) {
        set("version", version);
        save();
    }

    public void addLanguage(Language lang) {
        add(lang);
        save();
    }

    public void removeLanguage(Language lang) {
        remove(lang);
        save();
    }

    public List<Language> getLanguages() {
        return getAll(Language.class);
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

    public double getMeasuredValue(Component comp, String repo, String handle) {
        try {
            return Objects.requireNonNull(Measure.retrieve(comp, String.format("%s:%s", repo, handle))).getValue();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public void addFinding(Finding find) {
        add(find);
        save();
    }

    public void removeFinding(Finding find) {
        remove(find);
        save();
    }

    public List<Finding> getFindings() {
        return getAll(Finding.class);
    }

    public List<Finding> getFindings(String rule) {
        List<Finding> findings = Lists.newArrayList();
        getFindings().forEach(f -> {
            if (f.getParentRule().getName().equals(rule))
                findings.add(f);
        });

        return findings;
    }

    public void addPatternInstance(PatternInstance inst) {
        add(inst);
        save();
    }

    public void removePatternInstance(PatternInstance inst) {
        remove(inst);
        save();
    }

    public List<PatternInstance> getPatternInstances() {
        return getAll(PatternInstance.class);
    }

    public PatternInstance findPatternInstance(Pattern pattern, String name) {
        for (PatternInstance inst : getPatternInstances()) {
            if (inst.getParentPattern().equals(pattern) && inst.getInstKey().endsWith(name)) {
                return inst;
            }
        }

        return null;
    }

    public List<RoleBinding> getRoleBindings() {
        List<RoleBinding> bindings = Lists.newArrayList();
        getPatternInstances().forEach(inst -> bindings.addAll(inst.getRoleBindings()));
        return bindings;
    }

    public void addSCM(SCM scm) {
        add(scm);
        save();
    }

    public void removeSCM(SCM scm) {
        remove(scm);
        save();
    }

    public List<SCM> getSCMs() {
        return getAll(SCM.class);
    }

    public SCM getSCM(SCMType type) {
        for (SCM scm : getSCMs()) {
            if (scm.getType().equals(type)) {
                return scm;
            }
        }
        return null;
    }

    public void addModule(Module mod) {
        add(mod);
        save();
    }

    public void removeModule(Module mod) {
        remove(mod);
        save();
    }

    public List<Module> getModules() {
        return getAll(Module.class);
    }

    public void addRelation(Relation rel) {
        add(rel);
        save();
    }

    public void removeRelation(Relation rel) {
        remove(rel);
        save();
    }

    public List<Relation> getRelations() {
        return getAll(Relation.class);
    }

    public List<Namespace> getNamespaces() {
        return getAll(Namespace.class);
    }

    public List<Namespace> getRootNamespaces() {
        return getNamespaces().stream().filter(namespace -> namespace.getParentNamespace() == null).collect(Collectors.toList());
    }

    public Namespace findNamespace(String name) {
        if (name != null && !name.isEmpty()) {
            try {
                return get(Namespace.class, "name = ?", name).get(0);
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }
        }
        return null;
    }

    public Module findModule(String name) {
        if (name != null && !name.isEmpty()) {
            try {
                return get(Module.class, "name = ?", name).get(0);
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }
        }
        return null;
    }

    public boolean hasNamespace(String name) {
        return findNamespace(name) != null;
    }

    public List<Import> getImports() {
        List<Import> imports = Lists.newArrayList();
        getFiles().forEach(file -> imports.addAll(file.getImports()));
        return imports;
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        getNamespaces().forEach(namespace -> types.addAll(namespace.getAllTypes()));
        return types;
    }

    public Type findType(String attribute, String value) {
        if (hasClass(attribute, value))
            return findClass(attribute, value);
        if (hasInterface(attribute, value))
            return findInterface(attribute, value);
        if (hasEnum(attribute, value))
            return findEnum(attribute, value);
        if (isUnknownType(attribute, value))
            return findUnknownType(attribute, value);
        return null;
    }

    public Type findTypeByQualifiedName(String name) {
        String nsName;
        String compName;
        if (name.contains(".")) {
            nsName = name.substring(0, name.lastIndexOf("."));
            if (!hasNamespace(nsName)) {
                return null;
            }
            compName = name.replace(nsName, "").substring(1);
        } else {
            nsName = "";
            compName = name;
        }

        Namespace ns;
        if (nsName.isEmpty()) {
            ns = getDefaultNamespace();
        } else {
            ns = findNamespace(nsName);
        }

        Type type = null;
        if (ns != null) {
            type = ns.getTypeByName(compName);
        }

        return type;
    }

    public boolean hasType(String attribute, String value) {
        return findType(attribute, value) != null;
    }

    @Override
    public List<Type> getClasses() {
        List<Type> classes = Lists.newArrayList();
        getNamespaces().forEach(mod -> classes.addAll(mod.getClasses()));
        return classes;
    }

    public Type findClass(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type type = ns.findClass(attribute, value);
            if (type != null) return type;
        }
        return null;
    }

    public boolean hasClass(String attribute, String value) {
        return findClass(attribute, value) != null;
    }

    @Override
    public List<Type> getInterfaces() {
        List<Type> interfaces = Lists.newArrayList();
        getNamespaces().forEach(mod -> interfaces.addAll(mod.getInterfaces()));
        return interfaces;
    }

    public Type findInterface(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type type = ns.findInterface(attribute, value);
            if (type != null) return type;
        }
        return null;
    }

    public boolean hasInterface(String attribute, String value) {
        return findInterface(attribute, value) != null;
    }

    @Override
    public List<Type> getEnums() {
        List<Type> enums = Lists.newArrayList();
        getNamespaces().forEach(mod -> enums.addAll(mod.getEnums()));
        return enums;
    }

    public Type findEnum(String attribute, String value) {
        for (Namespace ns : getNamespaces()) {
            Type type = ns.findEnum(attribute, value);
            if (type != null) return type;
        }
        return null;
    }

    public boolean hasEnum(String attribute, String value) {
        return findEnum(attribute, value) != null;
    }

    public boolean isUnknownType(String attribute, String value) {
        return findUnknownType(attribute, value) != null;
    }

    public Type findUnknownType(String attribute, String value) {
        try {
            return get(Type.class, attribute + " = ? and type = ?", value, Type.UNKNOWN).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getNamespaces().forEach(mod -> members.addAll(mod.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getNamespaces().forEach(mod -> literals.addAll(mod.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> inits = Lists.newArrayList();
        getNamespaces().forEach(mod -> inits.addAll(mod.getInitializers()));
        return inits;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getNamespaces().forEach(mod -> members.addAll(mod.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getNamespaces().forEach(mod -> fields.addAll(mod.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(mod -> methods.addAll(mod.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(mod -> methods.addAll(mod.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getNamespaces().forEach(mod -> constructors.addAll(mod.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getNamespaces().forEach(mod -> destructors.addAll(mod.getDestructors()));
        return destructors;
    }

    public List<System> getParentSystems() {
        List<System> systems = Lists.newLinkedList();
        if (getParentSystem() != null)
            systems.add(getParentSystem());
        return systems;
    }

    public System getParentSystem() {
        return parent(System.class);
    }

    @Override
    public Project getParentProject() {
        return this;
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        return getParentSystem();
    }

    @Override
    public String getRefKey() {
        return getString("projKey");
    }

    public void addRelation(Component from, Component to, RelationType type) {
        Reference refFrom = Reference.createIt("refKey", from.getRefKey(), "type", RefType.fromComponent(from));
        Reference refTo = Reference.createIt("refKey", from.getRefKey(), "type", RefType.fromComponent(to));

        Relation rel = Relation.createIt();
        rel.setToAndFromRefs(refTo, refFrom);
        rel.setType(type);
        rel.save();

        add(rel);
    }

    public String getRelPath() {
        return getString("relPath");
    }

    public void setRelPath(String path) {
        setString("relPath", path);
        save();
    }

    public void updateKeys() {
        String nameVer = getVersion() == null ? getName() : getName() + ":" + getVersion();
        String newKey = getParentSystem() == null ? nameVer : getParentSystem().getKey() + ":" + nameVer;
        setString("projKey", newKey);
        save();
        refresh();
        getModules().forEach(Module::updateKey);
        getNamespaces().forEach(Namespace::updateKey);
        getFiles().forEach(File::updateKey);
//        getPatternInstances().forEach(PatternInstance::updateKey);
    }

    public String getFullPath() {
        String path = "";

        if (parent(System.class) != null) {
            path = parent(System.class).getBasePath();
        }
        if (!path.endsWith(java.io.File.separator)) {
            path += java.io.File.separator;
        }

        if (getRelPath() != null)
            path += getRelPath();

        if (!path.endsWith(java.io.File.separator)) {
            path += java.io.File.separator;
        }

        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Project) {
            Project comp = (Project) o;
            return comp.getProjectKey().equals(this.getProjectKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProjectKey());
    }

    public Project copy(String newKey, String relPath) {
        String key;
        if (getVersion() != null)
            key = getParentSystem().getKey() + ":" + newKey + ":" + getVersion();
        else
            key = getParentSystem().getKey() + ":" + newKey;

        log.info("Derived key: " + key);

        Project copy = Project.builder()
                .name(newKey)
                .projKey(key)
                .version(this.getVersion() != null ? this.getVersion() : null)
                .relPath(relPath)
                .create();
        copy.setSrcPath(getSrcPath());
        copy.setBinPath(getBinaryPaths());
        copy.setTestPath(getTestPath());
        copy.save();
        this.getParentSystem().addProject(copy);
        copy.updateKeys();
        copy.save();
        copy.refresh();

        getModules().forEach(mod -> copy.addModule(mod.copy(this.getProjectKey(), copy.getProjectKey())));
        getSCMs().forEach(scm -> copy.addSCM(scm.copy(this.getProjectKey(), copy.getProjectKey())));
        getNamespaces().forEach(ns -> {
            Namespace nsCopy = ns.copy(this.getProjectKey(), copy.getProjectKey());
            copy.addNamespace(nsCopy);
            nsCopy.save();
            nsCopy.refresh();
        });
        copy.refresh();
        getFiles().forEach(file -> copy.addFile(file.copy(this.getProjectKey(), copy.getProjectKey(), this.getRelPath(), copy.getRelPath())));
        getLanguages().forEach(copy::addLanguage);
        getMeasures().forEach(measure -> copy.addMeasure(measure.copy(this.getProjectKey(), copy.getProjectKey())));
        getFindings().forEach(finding -> copy.addFinding(finding.copy(this.getProjectKey(), copy.getProjectKey())));
        getPatternInstances().forEach(instance -> copy.addPatternInstance(instance.copy(this.getProjectKey(), copy.getProjectKey())));
        getRelations().forEach(rel -> copy.addRelation(rel.copy(this.getProjectKey(), copy.getProjectKey())));

        copy.getAllTypes().forEach(copyType -> {
            Namespace origNs = findNamespace(copyType.getParentNamespace().getName());
            Type origType = origNs.getTypeByName(copyType.getName());
            origType.copyContentsInto(copyType, getProjectKey(), copy.getProjectKey());
        });
        copy.save();
        copy.refresh();
        copy.updateKeys();
        return copy;
    }

    public void addFile(File file) {
        if (file != null)
            add(file);
        save();
    }

    public void removefile(File file) {
        if (file != null)
            remove(file);
        save();
    }

    public File getFileByName(String name) {
        try {
            return get(File.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public List<File> getFilesByType(FileType type) {
        return Lists.newArrayList(get(File.class, "type = ?", type.value()));
    }

    public List<File> getFiles() {
        return getAll(File.class);
    }

    public File getFile(String key) {
        try {
            return get(File.class, "fileKey = ?", key).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasFile(String key) {
        return getFile(key) != null;
    }

    public String[] getSrcPaths() {
        String srcPaths = getString("srcPath");
        if (srcPaths != null)
            return srcPaths.split(",");
        else
            return new String[0];
    }

    public String[] getSrcPaths(Module mod) {
        List<String> paths = Lists.newArrayList();
        String modPath = mod.getRelPath();
        for (String path : getSrcPaths()) {
            if (path.startsWith(modPath))
                paths.add(path);
        }
        return paths.toArray(new String[0]);
    }

    public String getSrcPath() {
        return getSrcPath(0);
    }

    public String getSrcPath(int index) {
        String[] paths = getSrcPaths();
        if (paths.length > 0)
            return paths[index];
        else
            return "";
    }

    public void setSrcPath(String path) {
        String[] paths = {path};
        setSrcPath(paths);
    }

    public void setSrcPath(String[] paths) {
        setPath("srcPath", paths);
    }

    private void setPath(String name, String[] paths) {
        if (paths.length <= 0)
            return;

        StringBuilder path = new StringBuilder();
        path.append(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            path.append(",");
            path.append(paths[i]);
        }
        setString(name, path.toString());
        save();
    }

    public String[] getBinaryPaths() {
        String binPaths = getString("binPath");
        if (binPaths != null)
            return binPaths.split(",");
        else
            return new String[0];
    }

    public String[] getBinaryPaths(Module mod) {
        List<String> paths = Lists.newArrayList();
        String modPath = mod.getFullPath();
        for (String path : getBinaryPaths()) {
            if (path.startsWith(modPath))
                paths.add(path);
        }
        return paths.toArray(new String[0]);
    }

    public String getBinaryPath() {
        return getBinaryPath(0);
    }

    public String getBinaryPath(int index) {
        String[] paths = getBinaryPaths();
        if (paths.length > 0)
            return paths[index];
        else
            return "";
    }

    public void setBinPath(String[] paths) {
        setPath("binPath", paths);
    }

    public String[] getTestPaths() {
        String testPaths = getString("testPath");
        if (testPaths != null)
            return testPaths.split(",");
        else
            return new String[0];
    }

    public String[] getTestPaths(Module mod) {
        List<String> paths = Lists.newArrayList();
        String modPath = mod.getFullPath();
        for (String path : getTestPaths()) {
            if (path.startsWith(modPath))
                paths.add(path);
        }
        return paths.toArray(new String[0]);
    }

    public String getTestPath() {
        return getTestPath(0);
    }

    public String getTestPath(int index) {
        String[] paths = getTestPaths();
        if (paths.length > 0)
            return paths[index];
        else
            return "";
    }

    public void setTestPath(String path) {
        String[] paths = {path};
        setTestPath(paths);
    }

    public void setTestPath(String[] path) {
        setPath("testPath", path);
    }

    public List<Type> getUnknownTypes() {
        return getAll(Type.class);
    }

    public void addUnknownType(Type type) {
        if (type != null)
            add(type);
        save();
    }

    public void removeUnknownType(Type type) {
        if (type != null)
            remove(type);
        save();
    }

    public void addNamespace(Namespace ns) {
        if (ns != null)
            add(ns);
        save();
    }

    public void removeNamespace(Namespace ns) {
        if (ns != null)
            remove(ns);
        save();
    }

    /**
     * @return The parent file of this Measurable
     */
    @Override
    public File getParentFile() {
        return null;
    }

    public double getMeasureValueByName(String key) {
        List<Measure> measures = getMeasures();
        for (Measure m : measures) {
            if (m.getMetricKey().equals(key))
                return m.getValue();
        }
        return 0.0d;
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

    public List<Snapshot> getSnapshots() {
        return getAll(Snapshot.class);
    }

    public List<Snapshot> getPartialsSinceExceptID(Date date, long id) {
        return get(Snapshot.class, "type = ? AND datetime > ? AND id <> ?", SnapshotType.PARTIAL.value(), date, "datetime", id);
    }

    public Snapshot getLastFull() {
        List<Snapshot> list = get(Snapshot.class, "type = ? AND ORDER BY ?", SnapshotType.FULL.value(), "datetime");
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
