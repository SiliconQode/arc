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
import dev.siliconcode.arc.datamodel.util.DbUtils;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * The base class at the head of the types representing a language type system
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("types")
@Many2Many(other = Type.class, sourceFKName = "sealed_type_id", targetFKName = "permitted_type_id", join = "sealed_types")
@BelongsTo(parent = Namespace.class, foreignKeyName = "namespace_id")
@Log4j2
public class Type extends Component implements ComponentContainer {

    public static int UNKNOWN = 0;
    public static int CLASS = 1;
    public static int ANNOTATION = 2;
    public static int INTERFACE = 2;
    public static int ENUM = 3;

//    @Builder(buildMethodName = "create")
//    public Type(String name, int start, int end, String compKey, Accessibility accessibility) {
//        set("name", name, "start", start, "end", end, "compKey", compKey);
//        if (accessibility != null)
//            setAccessibility(accessibility);
//        else
//            setAccessibility(Accessibility.PUBLIC);
//    }

    public Type() {
    }

    @Builder(buildMethodName = "create")
    public Type(String name, int start, int end, String compKey, Accessibility accessibility, int type) {
        set("name", name, "start", start, "end", end, "compKey", compKey, "qualified_name", name, "type", type);

        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);

        save();
    }

    protected Type copyType(String oldPrefix, String newPrefix) {
        log.info("Copying Class: " + getCompKey());
        return Type.builder()
                .name(this.getName())
                .type(this.getType())
                .compKey(this.getCompKey().replace(oldPrefix, newPrefix))
                .accessibility(this.getAccessibility())
                .start(this.getStart())
                .end(this.getEnd())
                .create();
    }

    /**
     * @return true if this type is abstract; false otherwise.
     */
    public boolean isAbstract() {
        return hasModifier("abstract");
    }

    public int getType() {
        return getInteger("type");
    }

    public void setType(int type) {
        if (type >= UNKNOWN && type <= Type.ENUM) {
            setInteger("type", type);
            save();
        }
    }

    /**
     * Marks this type as abstract or not.
     *
     * @param abst the new value of the abstract field
     */
    public void setAbstract(boolean abst) {
        if (abst)
            addModifier("abstract");
        else
            removeModifier("abstract");
        save();
    }

    public List<System> getParentSystems() {
        List<System> systems = Lists.newLinkedList();
        if (getParentSystem() != null)
            systems.add(getParentSystem());
        return systems;
    }

    /**
     * @return The parent system of this type or null if no such parent has been defined
     */
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

    /**
     * @return The parent project of this type or null if no such parent has been defined
     */
    @Override
    public Project getParentProject() {
        if (this.getType() == Type.UNKNOWN) {
            List<Project> projects = getAll(Project.class);
            if (!projects.isEmpty())
                return projects.get(0);
        }

        File file = getParentFile();
        if (file != null)
            return file.getParentProject();
        return null;
    }

    public List<Module> getParentModules() {
        List<Module> parents = Lists.newLinkedList();
        if (getParentModule() != null)
            parents.add(getParentModule());
        return parents;
    }

    /**
     * @return The parent module of this type or null if no such parent has been defined
     */
    public Module getParentModule() {
        if (this.getType() == Type.UNKNOWN)
            return null;

        Namespace ns = getParentNamespace();
        if (ns != null)
            return ns.getParentModule();
        return null;
    }

    public List<Namespace> getParentNamespaces() {
        List<Namespace> parents = Lists.newLinkedList();
        if (getParentNamespace() != null)
            parents.add(getParentNamespace());
        return parents;
    }

    public Namespace getParentNamespace() {
        if (this.getType() == Type.UNKNOWN)
            return null;
        return parent(Namespace.class);
    }

    public List<File> getParentFiles() {
        List<File> files = Lists.newLinkedList();
        if (getParentFile() != null)
            files.add(getParentFile());
        return files;
    }

    public File getParentFile() {
        if (getFileID() != null)
            return File.findById(getFileID());
        else
            return null;
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        if (getType() == Type.UNKNOWN)
            return getParentProject();
        return getParentFile();
    }

    public void addMember(Member member) {
        if (member != null) {
            add(member);
            save();
        }
    }

    public void removeMember(Member member) {
        if (member != null) {
            remove(member);
            save();
        }
    }

    public List<Constructor> getConstructors() {
        return getAll(Constructor.class);
    }

    @Override
    public List<Method> getMethods() {
        return getAll(Method.class);
    }

    public Method getMethodWithName(String name) {
        try {
            return get(Method.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            try {
                return get(Constructor.class, "name = ?", name).get(0);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    public boolean hasMethodWithName(String name) {
        return getMethodWithName(name) != null;
    }

    @Override
    public List<Destructor> getDestructors() {
        return getAll(Destructor.class);
    }

    @Override
    public List<Field> getFields() {
        return getAll(Field.class);
    }

    @Override
    public List<Initializer> getInitializers() {
        return getAll(Initializer.class);
    }

    public Initializer getStaticInitializer(int num) {
        try {
            return get(Initializer.class, "instance = ? AND number = ?", false, num).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Initializer getInstanceInitializer(int num) {
        try {
            return get(Initializer.class, "instance = ? AND number = ?", true, num).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Initializer getInitializerWithName(String name) {
        try {
            return get(Initializer.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasInitializerWithName(String name) {
        return getInitializerWithName(name) != null;
    }

    public List<Literal> getLiterals() {
        return getAll(Literal.class);
    }

    @Override
    public String getRefKey() {
        return getString("compKey");
    }

    public Set<Type> getRealizes() {
        return DbUtils.getRelationFrom(this, RelationType.REALIZATION);
    }

    public boolean isRealizing(Type type) {
        return getRealizes().contains(type);
    }

    public void realizes(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getGeneralizes() {
        return DbUtils.getRelationFrom(this, RelationType.GENERALIZATION);
    }

    public void generalizes(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public Set<Type> getContained() {
        return DbUtils.getRelationFrom(this, RelationType.CONTAINMENT);
    }

    public void contains(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.CONTAINMENT);
    }

    public boolean doesContain(Type other) {
        return getContained().contains(other);
    }

    public boolean isContainedBy(Type other) {
        return getContainedBy().contains(other);
    }

    public Set<Type> getAssociatedTo() {
        return DbUtils.getRelationFrom(this, RelationType.ASSOCIATION);
    }

    public boolean isAssociatedTo(Type type) {
        return getAssociatedTo().contains(type);
    }

    public void associatedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public boolean isAssociatedFrom(Type other) {
        return getAssociatedFrom().contains(other);
    }

    public void removeAssociatedTo(Type other) {
        deleteRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public Set<Type> getAggregatedTo() {
        return DbUtils.getRelationFrom(this, RelationType.AGGREGATION);
    }

    public void aggregatedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.AGGREGATION);
    }

    public Set<Type> getComposedTo() {
        return DbUtils.getRelationFrom(this, RelationType.COMPOSITION);
    }

    public void composedTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.COMPOSITION);
    }


    public Set<Type> getDependencyTo() {
        return DbUtils.getRelationFrom(this, RelationType.DEPENDENCY);
    }

    public void dependencyTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.DEPENDENCY);
    }

    public Set<Type> getUseTo() {
        return DbUtils.getRelationFrom(this, RelationType.USE);
    }

    public boolean hasUseTo(Type type) {
        return getUseTo().contains(type);
    }

    public boolean hasUseFrom(Type type) {
        return getUseFrom().contains(type);
    }

    public void useTo(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.USE);
    }

    public Set<Type> getRealizedBy() {
        return DbUtils.getRelationTo(this, RelationType.REALIZATION);
    }

    public void realizedBy(Type other) {
        createRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getGeneralizedBy() {
        return DbUtils.getRelationTo(this, RelationType.GENERALIZATION);
    }

    public boolean isGeneralizedBy(Type type) {
        return getGeneralizedBy().contains(type);
    }

    public void generalizedBy(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public void removeGeneralizedBy(Type other) {
        deleteRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.GENERALIZATION);
    }

    public void removeRealizes(Type other) {
        deleteRelation(other, this, RefType.TYPE, RefType.TYPE, RelationType.REALIZATION);
    }

    public Set<Type> getContainedBy() {
        return DbUtils.getRelationTo(this, RelationType.CONTAINMENT);
    }

    public void containedBy(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.CONTAINMENT);
    }

    public Set<Type> getAssociatedFrom() {
        return DbUtils.getRelationTo(this, RelationType.ASSOCIATION);
    }

    public void associatedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.ASSOCIATION);
    }

    public Set<Type> getAggregatedFrom() {
        return DbUtils.getRelationTo(this, RelationType.AGGREGATION);
    }

    public void aggregatedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.AGGREGATION);
    }

    public Set<Type> getComposedFrom() {
        return DbUtils.getRelationTo(this, RelationType.COMPOSITION);
    }

    public void composedFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.COMPOSITION);
    }

    public Set<Type> getDependencyFrom() {
        return DbUtils.getRelationTo(this, RelationType.DEPENDENCY);
    }

    public void dependencyFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.DEPENDENCY);
    }

    public Set<Type> getUseFrom() {
        return DbUtils.getRelationTo(this, RelationType.USE);
    }

    public void useFrom(Type other) {
        createRelation(this, other, RefType.TYPE, RefType.TYPE, RelationType.USE);
    }

    public List<Type> getParentTypes() {
        List<Type> parents = Lists.newArrayList();
        parents.addAll(getGeneralizedBy());
        parents.addAll(getRealizes());

        return parents;
    }

    public List<Type> getAncestorTypes() {
        List<Type> ancestors = Lists.newArrayList();
        Queue<Type> queue = Lists.newLinkedList();

        queue.offer(this);
        while (!queue.isEmpty()) {
            Type t = queue.poll();
            if (!t.equals(this)) {
                if (!ancestors.contains(t))
                    ancestors.add(t);
            }

            queue.addAll(t.getParentTypes());
        }

        return ancestors;
    }

    public List<Type> getChildTypes() {
        List<Type> children = Lists.newArrayList();
        children.addAll(getGeneralizedBy());
        children.addAll(getRealizedBy());

        return children;
    }

    public List<Type> getDescendentTypes() {
        List<Type> descendants = Lists.newArrayList();
        Queue<Type> queue = Lists.newLinkedList();

        queue.offer(this);
        while (!queue.isEmpty()) {
            Type t = queue.poll();
            if (!t.equals(this)) {
                if (!descendants.contains(t))
                    descendants.add(t);
            }

            queue.addAll(t.getChildTypes());
        }

        return descendants;
    }

    public Type getContainingType() {
        String type = getParentTypeType();
        Object id = getParentTypeID();
        Type parent = null;
        if (type != null && id != null) {
//            if (type.equals(Class.class.getName())) {
//                parent = Class.findById(id);
//            } else if (type.equals(Interface.class.getName())) {
//                parent = Interface.findById(id);
//            } else if (type.equals(Enum.class.getName())) {
//                parent = Enum.findById(id);
//            }
            if (type.equals(Type.class.getName()))
                parent = Type.findById(id);
        }
        return parent;
    }

    public List<Type> getContainedTypes() {
        List<Type> types = Lists.newArrayList();
        types.addAll(getClasses());
        types.addAll(getInterfaces());
        types.addAll(getEnums());
        return types;
    }

    public List<Method> findOverridingMethods(List<Method> given) {
        List<Method> overriding = Lists.newArrayList();

        for (Method give : given) {
            for (Method curr : getMethods()) {
                if (curr.signature().equals(give.signature())) {
                    overriding.add(curr);
                }
            }
        }

        return overriding;
    }

    public List<Method> findOverridingMethods() {
        List<Method> overriding = Lists.newArrayList();
        List<Method> currMethods = Lists.newArrayList(getMethods());
        List<Type> ancestors = getAncestorTypes();

        outer:
        for (Type anc : ancestors) {
            if (anc.getType() == Type.CLASS) {
                for (Method ancMethod : anc.getMethods()) {
                    List<Method> toRemove = Lists.newArrayList();
                    for (Method currMethod : getMethods()) {
                        if (currMethod.signature().equals(ancMethod.signature())) {
                            overriding.add(currMethod);
                            toRemove.add(currMethod);
                        }
                    }
                    currMethods.removeAll(toRemove);
                    if (currMethods.isEmpty())
                        break outer;
                }
            }
        }

        return overriding;
    }

    public Member findMemberInRange(int start, int end) {
        Optional<Member> opt = getAllMembers().stream().filter(member -> member.getStart() >= start && member.getEnd() <= end).findFirst();
        return opt.orElse(null);
    }

    public List<TemplateParam> getTemplateParams() {
        return getAll(TemplateParam.class);
    }

    public void setTemplateParams(List<TemplateParam> params) {
        if (params == null || params.isEmpty())
            return;

        for (TemplateParam param : params)
            add(param);
        save();
    }

    public void addTemplateParam(TemplateParam param) {
        if (param != null) {
            add(param);
            save();
        }
    }

    public void removeTemplateParam(TemplateParam param) {
        if (param != null) {
            remove(param);
            save();
        }
    }

    public Method findMethodBySignature(String sig) {
        for (Method m : getAllMethods()) {
            if (m.signature().equals(sig))
                return m;
        }

        return null;
    }

    public boolean hasMethodWithMatchingSignature(Method method) {
        for (Method m : getMethods()) {
            if (m.signature().equals(method.signature()))
                return true;
        }

        return false;
    }

    public String getFullName() {
        if (getType() == Type.UNKNOWN)
            return this.getName();
        return getQualifiedName();
    }

    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        types.addAll(getClasses());
        types.addAll(getEnums());
        types.addAll(getInterfaces());
        return types;
    }

    public List<Type> getClasses() {
        return Type.find("parent_type_id = ? and parent_type_type = ?", this.getId(), this.getClass().getName());
    }

    public List<Type> getEnums() {
        return Type.find("parent_type_id = ? and parent_type_type = ?", this.getId(), this.getClass().getName());
    }

    public List<Type> getInterfaces() {
        return Type.find("parent_type_id = ? and parent_type_type = ?", this.getId(), this.getClass().getName());
    }

    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        members.addAll(getLiterals());
        members.addAll(getInitializers());
        members.addAll(getAllTypedMembers());
        return members;
    }

    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        members.addAll(getFields());
        members.addAll(getAllMethods());
        return members;
    }

    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        methods.addAll(getMethods());
        methods.addAll(getConstructors());
        return methods;
    }

    public void updateKey() {
        Namespace parent = getParentNamespace();
        String oldKey = getCompKey();
        String newKey = null;
        if (parent != null)
            newKey = parent.getNsKey() + ":" + getName();
        else {
            if (getType() == Type.UNKNOWN) {
                Project proj = getParentProject();
                if (proj != null)
                    setCompKey(proj.getProjectKey() + ":" + getName());
            } else {
                newKey = getName();
            }
        }

        if (newKey != null) {
            setString("compKey", newKey);
            save();
            refresh();
            updateReferences(oldKey);
            getAllMembers().forEach(Member::updateKey);
        }
    }

    public TypeRef createTypeRef() {
//        if (TypeRef.findFirst("typeFullName = ?", getFullName()) != null) {
//            return TypeRef.findFirst("typeFullName = ?", getFullName());
//        } else {
        this.refresh();
        Reference ref = createReference();
        TypeRef tref = TypeRef.builder().typeName(getName()).typeFullName(getFullName()).type(TypeRefType.Type).create();
        tref.setReference(ref);
        return tref;
//        }
    }

    public Reference createReference() {
        return Reference.to(this);
    }

    public void addType(Type type) {
        if (type != null) {
            type.setParentTypeID(getId());
            type.setParentTypeType(this.getClass().getName());
            type.setQualifiedName(getQualifiedName() + "." + getName());
            this.contains(type);
            type.containedBy(this);
        }
    }

    public void removeType(Type type) {
        if (type != null) {
            type.setParentTypeID(null);
            type.setParentTypeType(null);
        }
    }

    public Type getTypeByName(String name) {
        return getClassByName(name) != null ? getClassByName(name) :
                (getInterfaceByName(name) != null ? getInterfaceByName(name) : getEnumByName(name));
    }

    private Type getClassByName(String name) {
        try {
            return Type.findFirst("name = ? and parent_type_id = ? and type = ?", name, getId(), Type.CLASS);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private Type getInterfaceByName(String name) {
        try {
            return Type.findFirst("name = ? and parent_type_id = ? and type = ?", name, getId(), Type.INTERFACE);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private Type getEnumByName(String name) {
        try {
            return Type.findFirst("name = ? and parent_type_id = ? and type = ?", name, getId(), Type.ENUM);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasTypeWithName(String name) {
        return getTypeByName(name) != null;
    }

    public boolean hasTemplateParam(String name) {
        return getTemplateParam(name) != null;
    }

    public TemplateParam getTemplateParam(String name) {
        try {
            return get(TemplateParam.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Type copy(String oldPrefix, String newPrefix) {
        return copyType(oldPrefix, newPrefix);
    }

    public void copyContentsInto(Type copy, String oldPrefix, String newPrefix) {
        getModifiers().forEach(copy::addModifier);
        getAllMembers().forEach(member -> copy.addMember(member.copy(oldPrefix, newPrefix)));
        getTemplateParams().forEach(param -> copy.addTemplateParam(param.copy(oldPrefix, newPrefix)));
        getContained().forEach(type -> copy.addType(type.copy(oldPrefix, newPrefix)));
        copy.save();
        copy.refresh();
    }

    public Field getFieldWithName(String name) {
        try {
            return get(Field.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean hasFieldWithName(String name) {
        return getFieldWithName(name) != null;
    }

    public Constructor getConstructorWithNParams(int numParams) {
        for (Constructor c : getConstructors()) {
            if (c.getParams().size() == numParams)
                return c;
        }

        return null;
    }

    public boolean hasConstructorWithNParams(int numParams) {
        return getConstructorWithNParams(numParams) != null;
    }

    public Method getMethodWithNameAndNumParams(String name, int numParams) {
        for (Method m : get(Method.class, "name = ?", name)) {
            if (m.getParams().size() == numParams)
                return m;
        }

        return null;
    }

    public boolean hasMethodWithNameAndNumParams(String name, int numParams) {
        return getMethodWithNameAndNumParams(name, numParams) != null;
    }

    public void setFileID(Object id) {
        set("parent_file_id", id);
        save();
    }

    public Object getFileID() {
        return get("parent_file_id");
    }

    public void setParentTypeID(Object id) {
        set("parent_type_id", id);
        save();
    }

    public Object getParentTypeID() {
        return get("parent_type_id");
    }

    public void setParentTypeType(String type) {
        set("parent_type_type", type);
        save();
    }

    public String getParentTypeType() {
        return getString("parent_type_type");
    }

    public String getQualifiedName() {
        return getString("qualified_name");
    }

    public void setQualifiedName(String name) {
        if (name != null)
            setString("qualified_name", name);
        save();
    }

    public boolean hasLiteralWithName(String name) {
        return getLiteralWithName(name) != null;
    }

    public Literal getLiteralWithName(String name) {
        try {
            return get(Literal.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void addPermittedType() {

    }

    public void removePermittedType() {

    }

    public List<Type> permittedTypes() {
        return null;
    }

    public boolean isSealed() {
        return getBoolean("sealed");
    }

    public void setSealed(boolean sealed) {
        setBoolean("sealed", sealed);
        save();
    }
}
