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
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import dev.siliconcode.arc.datamodel.cfg.ControlFlowGraph;
import dev.siliconcode.arc.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Many2Many;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Many(other = TypeRef.class, join = "methods_typerefs", sourceFKName = "method_id", targetFKName = "type_ref_id")
public class Method extends TypedMember {

    public Method() {

    }

    @Builder(buildMethodName = "create")
    public Method(String name, int start, int end, String compKey, Accessibility accessibility, TypeRef type) {
        set("name", name, "start", start, "end", end, "compKey", compKey);
        set("localVars", 0, "returnStmts", 1, "numStmts", 0, "numDecisionPoints", 0);
        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);
        if (type == null) {
            setReturnType(TypeRef.createPrimitiveTypeRef("void"));
        } else
            setReturnType(type);
        save();
    }

    public void addParameter(Parameter param) {
        add(param);
        save();
    }

    public void removeParameter(Parameter param) {
        remove(param);
        save();
    }

    public List<Parameter> getParams() {
        return getAll(Parameter.class);
    }

    public void setReturnType(TypeRef ref) {
        setType(ref);
    }

    public void setTypeParams(List<TemplateParam> params) {
        if (params == null || params.isEmpty())
            return;

        for (TemplateParam param : params)
            addTemplateParam(param);
    }

    public void addException(TypeRef excep) {
        if (excep != null) {
            // Wrap in MethodException object
            MethodException me = MethodException.createIt();
            me.add(excep);
            me.set("name", excep.getTypeName());
            me.save();
            add(me);
            save();
        }
    }

    public void removeException(TypeRef excep) {
        if (excep != null) {
            List<MethodException> exceptions = getExceptions();
            MethodException toRemove = null;
            for (MethodException me : exceptions) {
                if (me.getTypeRef().equals(excep))
                    toRemove = me;
            }
            if (toRemove != null) {
                toRemove.removeTypeRef(excep);
                toRemove.save();
                remove(toRemove);
                save();
            }
        }
    }

    public List<MethodException> getExceptions() {
        return getAll(MethodException.class);
    }

    public boolean isAbstract() {
        return hasModifier("ABSTRACT");
    }

    public boolean isOverriding() {
        Queue<Type> parents = Queues.newArrayDeque();
        parents.offer(getParentType());
        while (!parents.isEmpty()) {
            Type p = parents.poll();
            parents.addAll(p.getParentTypes());
            if (p.hasMethodWithMatchingSignature(this))
                return true;
        }

        return false;
    }

    public String signature() {
        StringBuilder sig = new StringBuilder();
        sig.append(getType() != null ? getType().getTypeName() + ' ' + getName() : getName());
        sig.append("(");
        for (Parameter param : getParams()) {
            sig.append(param.getType().getTypeName());
            sig.append(", ");
        }

        String retVal = sig.toString();
        if (retVal.endsWith(", ")) {
            retVal = retVal.trim();
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        retVal += ")";

        return retVal;
    }

    public String sigWithOutType() {
        StringBuilder sig = new StringBuilder();
        sig.append(getName());
        sig.append("(");
        for (Parameter param : getParams()) {
            sig.append(param.getType().getTypeName());
            sig.append(", ");
        }

        String retVal = sig.toString();
        if (retVal.endsWith(", ")) {
            retVal = retVal.trim();
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        retVal += ")";

        return retVal;
    }

    public Parameter getParameterByName(String param) {
        List<Parameter> named = get(Parameter.class, "name = ?", param);
        if (!named.isEmpty())
            return named.get(0);
        return null;
    }

    public MethodException getExceptionByName(String exception) {
        return get(MethodException.class, "name = ?", exception).get(0);
    }

    public boolean isAccessor() {
        String name = getName();
        List<Parameter> params = getParams();
        return (name.startsWith("get") && params.size() == 0 && (getType() != null && !getType().getTypeName().equals("void")));
    }

    public boolean isMutator() {
        String name = getName();
        List<Parameter> params = getParams();
        return (name.startsWith("set") && params.size() == 1 && (getType() != null && getType().getTypeName().equals("void")));
    }

    public ControlFlowGraph getCfg() {
        ControlFlowGraph cfg = ControlFlowGraph.fromString(getString("cfg"));
        if (cfg == null)
            return new ControlFlowGraph(GraphBuilder.directed().build(), null, null);
        return cfg;
    }

    public void setCfg(ControlFlowGraph cfg) {
        set("cfg", cfg.cfgToString());
        save();
    }

    public Set<Field> getFieldsUsed() {
        Set<Member> membersCalled = DbUtils.getRelationFrom(this, RelationType.USE);
        Set<Field> fieldsUsed = Sets.newHashSet();
        membersCalled.forEach(member -> {
            if (member instanceof Field)
                fieldsUsed.add((Field) member);
        });

        return fieldsUsed;
    }

    public Set<Field> getFieldsUsedSameClass() {
        Set<Field> set = Sets.newHashSet();
        getFieldsUsed().forEach(member -> {
            if (member.getParentType().equals(this.getParentType()))
                set.add(member);
        });
        return set;
    }

    public Set<Method> getMethodsCalled() {
        Set<Member> membersCalled = DbUtils.getRelationFrom(this, RelationType.USE);
        Set<Method> methodsCalled = Sets.newHashSet();
        membersCalled.forEach(member -> {
            if (member instanceof Method)
                methodsCalled.add((Method) member);
        });

        return methodsCalled;
    }

    public Set<Method> getMethodsUsedSameClass() {
        Set<Method> set = Sets.newHashSet();
        getMethodsCalled().forEach(member -> {
            if (member.getParentType().equals(this.getParentType()))
                set.add(member);
        });
        return set;
    }

    public List<Type> getTypesUsing() {
        return Lists.newArrayList();
    }

    public Set<Method> getMethodsCalling() {
        Set<Member> membersCalling = DbUtils.getRelationTo(this, RelationType.USE);
        Set<Method> methodsCalling = Sets.newHashSet();
        membersCalling.forEach(member -> {
            if (member instanceof Method)
                methodsCalling.add((Method) member);
        });

        return methodsCalling;
    }

    public boolean isVisible() {
        return true;
    }

    @Override
    public void updateKey() {
        Type parent = getPType();

        String newKey;
        String oldKey = getCompKey();
        if (parent != null)
            newKey = parent.getCompKey() + "#" + sigWithOutType();
        else
            newKey = sigWithOutType();

        setString("compKey", newKey);
        save();
        refresh();
        updateReferences(oldKey);
    }

    @Nullable
    private Type getPType() {
//        Type parent = null;
//        try {
//            if (parent(Class.class) != null)
//                parent = parent(Class.class);
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            if (parent(Interface.class) != null)
//                parent = parent(Class.class);
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            if (parent(Enum.class) != null)
//                parent = parent(Enum.class);
//        } catch (IllegalArgumentException e) {
//        }
//        return parent;
        return parent(Type.class);
    }

    public void callsMethod(Method method) {
        createRelation(method, this, RefType.METHOD, RefType.METHOD, RelationType.USE);
    }

    public void removeCalledMethod(Method method) {
        deleteRelation(method, this, RefType.METHOD, RefType.METHOD, RelationType.USE);
    }

    public void calledByMethod(Method method) {
        createRelation(this, method, RefType.METHOD, RefType.METHOD, RelationType.USE);
    }

    public void removeCalledByMethod(Method method) {
        deleteRelation(this, method, RefType.METHOD, RefType.METHOD, RelationType.USE);
    }

    public void usesField(Field field) {
        createRelation(field, this, RefType.FIELD, RefType.METHOD, RelationType.USE);
    }

    public void removeFieldUse(Field field) {
        deleteRelation(field, this, RefType.FIELD, RefType.METHOD, RelationType.USE);
    }

    public Reference createReference() {
        String newKey;
        Type parent = getPType();
        if (parent != null)
            newKey = parent.getCompKey() + "#" + sigWithOutType();
        else
            newKey = sigWithOutType();
        return Reference.builder().refKey(newKey).refType(RefType.METHOD).create();
    }

    @Override
    public Member copy(String oldPrefix, String newPrefix) {
        Method copy = Method.builder()
                .name(this.getName())
                .compKey(this.getCompKey().replace(oldPrefix, newPrefix))
                .accessibility(this.getAccessibility())
                .type(this.getType().copy(oldPrefix, newPrefix))
                .start(this.getStart())
                .end(this.getEnd())
                .create();

//        copy.setLocalVarCount(getLocalVarCount());
        getModifiers().forEach(copy::addModifier);
        getTemplateParams().forEach(param -> copy.addTemplateParam(param.copy(oldPrefix, newPrefix)));
        getExceptions().forEach(excep -> copy.addException(excep.getTypeRef().copy(oldPrefix, newPrefix)));
        getParams().forEach(param -> copy.addParameter(param.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public void setLocalVarCount(int count) {
        setInteger("localVars", count);
        save();
    }

    public void incrementLocalVarCount() {
        setInteger("localVars", getInteger("localVars") + 1);
        save();
    }

    public int getLocalVarCount() {
        return getInteger("localVars");
    }

    public void incrementReturnStmts() {
        setInteger("returnStmts", getInteger("returnStmts") + 1);
        save();
    }

    public int getReturnStmts() {
        return getInteger("returnStmts");
    }

    public void incrementNumberOfStmts() {
        setInteger("numStmts", getInteger("numStmts") + 1);
        save();
    }

    public int getNumStmts() {
        return getInteger("numStmts");
    }

    public void incrementNumDecisionPoints() {
        setInteger("numDecisionPoints", getInteger("numDecisionPoints") + 1);
        save();
    }

    public void setCounts(int varCount, int returnCount, int stmtCount, int decisionCount) {
        set("localVars", varCount, "returnStmts", returnCount, "numStmts", stmtCount, "numDecisionPoints", decisionCount);
        save();
    }

    public int getNumDecisionPoints() {
        return getInteger("numDecisionPoints");
    }
}
