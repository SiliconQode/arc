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
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Method.class, Constructor.class, Destructor.class})
@Many2Many(other = TypeRef.class, join = "parameters_typerefs", sourceFKName = "parameter_id", targetFKName = "type_ref_id")
@Many2Many(other = Modifier.class, join = "parameters_modifiers", sourceFKName = "parameter_id", targetFKName = "modifier_id")
public class Parameter extends Model {

    public Parameter() {
    }

    @Builder(buildMethodName = "create")
    public Parameter(String name, TypeRef type, boolean varg) {
        setName(name);
        setVarg(varg);
        if (type != null) {
            setType(type);
        }
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setString("name", name);
        save();
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

    public boolean hasModifier(String name) {
        return !get(Modifier.class, "name = ?", name.toUpperCase()).isEmpty();
    }

    public boolean hasModifier(Modifier mod) {
        return hasModifier(mod.getName().toUpperCase());
    }

    public boolean hasModifier(Modifier.Values value) {
        return hasModifier(value.toString());
    }

    public List<Modifier> getModifiers() {
        return getAll(Modifier.class);
    }

    public boolean isVarg() {
        return getBoolean("varg");
    }

    public void setVarg(boolean varg) {
        setBoolean("varg", varg);
        save();
    }

    public void setType(TypeRef ref) {
        if (this.getAll(TypeRef.class).isEmpty()) {
            add(ref);
            save();
        } else {
            List<TypeRef> refs = Lists.newArrayList(this.getAll(TypeRef.class));
            for (TypeRef r : refs) {
                remove(r);
                saveIt();
            }

            add(ref);
            save();
        }
    }

    public TypeRef getType() {
        if (getAll(TypeRef.class).size() > 0)
            return getAll(TypeRef.class).get(0);
        else
            return null;
    }

    public List<System> getParentSystems() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentSystems();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentSystems();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentSystems();
        return Lists.newArrayList();
    }

    public List<Project> getParentProjects() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentProjects();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentProjects();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentProjects();
        return Lists.newArrayList();
    }

    public List<Module> getParentModules() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentModules();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentModules();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentModules();
        return Lists.newArrayList();
    }

    public List<Namespace> getParentNamespaces() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentNamespaces();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentNamespaces();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentNamespaces();
        return Lists.newArrayList();
    }

    public List<File> getParentFiles() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentFiles();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentFiles();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentFiles();
        return Lists.newArrayList();
    }

    public List<Type> getParentTypes() {
        if (parent(Method.class) != null)
            return parent(Method.class).getParentTypes();
        if (parent(Constructor.class) != null)
            return parent(Constructor.class).getParentTypes();
        if (parent(Destructor.class) != null)
            return parent(Destructor.class).getParentTypes();
        return Lists.newArrayList();
    }

    public List<Method> getParentMethods() {
        List<Method> methods = Lists.newArrayList();
        methods.add(this.parent(Method.class));
        methods.add(this.parent(Constructor.class));
        methods.add(this.parent(Destructor.class));
        return methods;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parameter) {
            Parameter param = (Parameter) o;
            return param.getName().equals(this.getName()) && param.getType().getTypeName().equals(this.getType().getTypeName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getType().getTypeName());
    }

    public Parameter copy(String oldPrefix, String newPrefix) {
        Parameter copy = Parameter.builder()
                .name(this.getName())
                .type(this.getType().copy(oldPrefix, newPrefix))
                .create();

        getModifiers().forEach(copy::addModifier);

        return copy;
    }
}
