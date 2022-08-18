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
import org.javalite.activejdbc.annotations.BelongsTo;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsTo(parent = Project.class, foreignKeyName = "project_id")
public class PatternInstance extends Model implements Measurable, ComponentContainer {

    public PatternInstance() {}

    @Builder(buildMethodName = "create")
    public PatternInstance(String instKey) {
        set("instKey", instKey);
        set("matched", false);
        set("extract", false);
        saveIt();
    }

    public String getInstKey() { return getString("instKey"); }

    public boolean isMatched() { return getBoolean("matched"); }

    public void setMatched(boolean matched) { setBoolean("matched", matched); save(); }

    public void addRoleBinding(RoleBinding binding) { add(binding); save(); }

    public List<RoleBinding> getRoleBindings() { return getAll(RoleBinding.class); }

    public void removeRoleBinding(RoleBinding binding) { remove(binding); save(); }

    public List<Role> getRoles() {
        Pattern parent = getParentPattern();
        if (parent != null)
            return parent.getRoles();
        return Lists.newArrayList();
    }

    public Pattern getParentPattern() {
        if (getPatternID() != null)
            return Pattern.findById(getPatternID());
        else
            return null;
    }

    public List<System> getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public List<Project> getParentProjects() {
        List<Project> projects = Lists.newLinkedList();
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

    public PatternChain getParentPatternChain() {
        if (getChainID() != null)
            return PatternChain.findById(getChainID());
        else
            return null;
    }

    @Override
    public String getRefKey() {
        return getString("instKey");
    }

    public List<Type> getTypes() {
        List<Type> types = Lists.newArrayList();
        List<RoleBinding> bindings = getRoleBindings();

        bindings.forEach(binding -> {
            Reference ref = binding.getReference();
            if (ref != null) {
                Component t = binding.getReference().getReferencedComponent(getParentProjects().get(0));
                if (t instanceof Type)
                    types.add((Type) t);

//            if (getParentProjects().size() > 0) {
//                Type t = getParentProjects().get(0).findType("compKey", ref.getRefKey());
//                if (t != null)
//                    types.add(t);
//            }
            }
        });

        return types;
    }

    public List<Type> getTypesBoundTo(Role role) {
        List<Type> types = Lists.newArrayList();
        List<RoleBinding> bindings = getRoleBindings();
        bindings.forEach(binding -> {
            Reference ref = binding.getReference();
            if (ref != null && binding.getRole().equals(role)) {
                if (getParentProjects().size() > 0) {
                    Type t = getParentProjects().get(0).findType("compKey", ref.getRefKey());
                    if (t != null)
                        types.add(t);
                }
            }
        });
        return types;
    }

    public Role getRoleBoundTo(Type type) {
        Role role = null;
        List<RoleBinding> bindings = getRoleBindings();
        for (RoleBinding binding : bindings) {
            Reference ref = binding.getReference();
            if (ref != null && ref.getRefKey().equals(type.getCompKey())) {
                role = binding.getRole();
                break;
            }
        }

        return role;
    }

    public List<Type> getAllTypes() { return getTypes(); }

    public List<Type> getClasses() {
        List<Type> classes = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t.getType() == Type.CLASS)
                classes.add(t);
        });
        return classes;
    }

    public List<Type> getEnums() {
        List<Type> enums = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t.getType() == Type.ENUM)
                enums.add(t);
        });
        return enums;
    }

    public List<Type> getInterfaces() {
        List<Type> interfaces = Lists.newArrayList();
        getTypes().forEach(t -> {
            if (t.getType() == Type.INTERFACE)
                interfaces.add(t);
        });
        return interfaces;
    }

    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getAllTypes().forEach(t -> members.addAll(t.getAllMembers()));
        return members;
    }

    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getAllTypes().forEach(t -> literals.addAll(t.getLiterals()));
        return literals;
    }

    public List<Initializer> getInitializers() {
        List<Initializer> inits = Lists.newArrayList();
        getAllTypes().forEach(t -> inits.addAll(t.getInitializers()));
        return inits;
    }

    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getAllTypes().forEach(t -> members.addAll(t.getAllTypedMembers()));
        return members;
    }

    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getAllTypes().forEach(t -> fields.addAll(t.getFields()));
        return fields;
    }

    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(t -> methods.addAll(t.getAllMethods()));
        return methods;
    }

    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getAllTypes().forEach(t -> methods.addAll(t.getMethods()));
        return methods;
    }

    public List<Constructor> getConstructors() {
        List<Constructor> cons = Lists.newArrayList();
        getAllTypes().forEach(t -> cons.addAll(t.getConstructors()));
        return cons;
    }

    public List<Destructor> getDestructors() {
        List<Destructor> dests = Lists.newArrayList();
        getAllTypes().forEach(t -> dests.addAll(t.getDestructors()));
        return dests;
    }

    public String getFamily() {
        Pattern parent = getParentPattern();
        if (parent != null) {
            return parent.getFamily();
        } else
            return null;
    }

    public PatternInstance copy(String oldPrefix, String newPrefix) {
        PatternInstance copy = PatternInstance.builder().instKey(this.getInstKey().replace(oldPrefix, newPrefix)).create();

        getRoleBindings().forEach(bind -> copy.addRoleBinding(bind.copy(oldPrefix, newPrefix)));
        getParentPattern().addInstance(copy);
        copy.save();
        copy.refresh();

        return copy;
    }

    public List<RoleBinding> bindingsFor(Role r) {
        List<RoleBinding> bindings = Lists.newArrayList();
        getRoleBindings().forEach(rb -> {
           if (rb.getRole().equals(r))
               bindings.add(rb);
        });

        return bindings;
    }

    public Role findRole(String name) {
        for (Role r : getRoles()) {
            if (r.getName().equals(name))
                return r;
        }

        return null;
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

    public void setPatternID(Object id) {
        set("parent_pattern_id", id);
        save();
    }

    public Object getPatternID() {
        return get("parent_pattern_id");
    }

    public void setChainID(Object id) {
        set("parent_chain_id", id);
        save();
    }

    public void markForExtraction() {
        setBoolean("extract", true);
        save();
    }

    public boolean shouldExtract() {
        return getBoolean("extract");
    }

    public Object getChainID() {
        return get("parent_chain_id");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PatternInstance) {
            PatternInstance inst = (PatternInstance) o;
            return inst.getInstKey().equals(this.getInstKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstKey());
    }
}
