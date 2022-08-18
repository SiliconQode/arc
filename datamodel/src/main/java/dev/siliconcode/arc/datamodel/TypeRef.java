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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Many(other = TypeRef.class, join = "typerefs_typerefs", sourceFKName = "parent_id", targetFKName = "child_id")
public class TypeRef extends Model {

    public TypeRef() {
    }

    @Builder(buildMethodName = "create")
    public TypeRef(String typeName, String typeFullName, String dimensions, TypeRefType type, Reference ref) {
        set("typeName", typeName, "dimensions", dimensions, "typeFullName", typeFullName);
        if (type != null)
            setType(type);
        if (ref != null)
            setReference(ref);
        save();
    }

    public String getDimensions() {
        return getString("dimensions");
    }

    public void setDimensions(String dim) {
        set("dimensions", dim);
        save();
    }

    public String getTypeName() {
        return getString("typeName");
    }

    public void setTypeName(String typeName) {
        set("typeName", typeName);
        save();
    }

    public String getTypeFullName() { return getString("typeFullName"); }

    public TypeRefType getType() {
        return TypeRefType.fromValue(getInteger("type"));
    }

    public void setType(TypeRefType type) {
        set("type", type.value());
        save();
    }

    public void addBound(TypeRef bound) {
        if (bound != null) {
            add(bound);
            bound.set("is_bound", 1);
            bound.save();
            save();
        }
    }

    public void removeBound(TypeRef bound) {
        if (bound != null) {
            remove(bound);
            bound.set("is_bound", 0);
            bound.save();
            save();
        }
    }

    public List<TypeRef> getBounds() {
        return get(TypeRef.class, "is_bound = ?", 1);
    }

    public void addTypeArg(TypeRef arg) {
        if (arg != null) {
            add(arg);
            arg.set("is_bound", 0);
            arg.save();
            save();
        }
    }

    public void removeTypeArg(TypeRef arg) {
        if (arg != null) {
            remove(arg);
            save();
        }
    }

    public List<TypeRef> getTypeArgs() {
        return get(TypeRef.class, "is_bound = ?", 0);
    }

    public void setReference(Reference ref) {
//        List<Reference> refs = Lists.newArrayList(getReferences());
//        for (Reference r : refs) {
//            removeReference(r);
//        }
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
        try {
            return getReferences().get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public static TypeRef createPrimitiveTypeRef(String key) {
        if (TypeRef.findFirst("typeName = ?", key) != null)
            return TypeRef.findFirst("typeName = ?", key);
        else
            return TypeRef.builder().typeName(key).typeFullName(key).type(TypeRefType.Primitive).create();
    }

    public static TypeRef createWildCardTypeRef() {
        if (TypeRef.findFirst("typeName = ?", "?") != null)
            return TypeRef.findFirst("typeName = ?", "?");
        else
            return TypeRef.createIt("typeName", "?", "type", TypeRefType.WildCard.value());
    }

    public static TypeRef createTypeVarTypeRef(String typeVar) {
        if (TypeRef.findFirst("typeName = ?", typeVar) != null)
            return TypeRef.findFirst("typeName = ?", typeVar);
        else
            return builder().typeName(typeVar).type(TypeRefType.Type).create();
    }

    public static List<TypeRef> knownTypes() {
        return findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getTypeName() + " ");
        List<TypeRef> bounds = getBounds();
        if (!bounds.isEmpty()) {
            builder.append("extends ");
            for (TypeRef it : bounds) {
                builder.append(it.toString());
                if (it != bounds.get(bounds.size() - 1))
                    builder.append(" & ");
            }
        } else {
            List<TypeRef> typeArgs = getTypeArgs();
            if (typeArgs != null && !typeArgs.isEmpty()) {
                builder.append("<");
                for (TypeRef arg : typeArgs) {
                    builder.append(arg.toString());
                    if (arg != typeArgs.get(typeArgs.size() - 1))
                        builder.append(", ");
                }
                builder.append(">");
            }
            if (getDimensions() != null)
                builder.append(" " + getDimensions());
        }
        return builder.toString().trim();
    }

    public boolean isKnownType() {
        return KnownTypeMultiton.set.contains(getTypeName());
    }

    public Type getType(String projKey) {
        List<Type> types = Lists.newLinkedList();
        if (getReference() != null) {
//            types.addAll(Class.find("compKey = ?", getReference().getRefKey()));
//            types.addAll(Interface.find("compKey = ?", getReference().getRefKey()));
//            types.addAll(Enum.find("compKey = ?", getReference().getRefKey()));
            types.addAll(Type.find("compKey = ?", getReference().getRefKey()));

            for (Type t : types) {
                if (t.getParentProject() != null) {
                    if (t.getParentProject().getProjectKey().equals(projKey))
                        return t;
                }
            }
        }

        return null;
    }

    private static class KnownTypeMultiton {
        public static Set<String> set = Sets.newHashSet();

        static {
            // Shared Java and C# Primitive Types
            set.add("int");
            set.add("long");
            set.add("char");
            set.add("byte");
            set.add("short");
            set.add("double");
            set.add("float");
            set.add("void");

            // Java Primitive Types
            set.add("boolean");

            // C# Primitive Types
            set.add("sbyte");
            set.add("ushort");
            set.add("uint");
            set.add("ulong");
            set.add("object");
            set.add("string");
            set.add("decimal");
            set.add("bool");
            set.add("DataTime");
            set.add("DateSpan");

            set.add("?");
        }
    }

    public TypeRef copy(String oldPrefix, String newPrefix) {
        if (getType().equals(TypeRefType.Primitive))
            return this;

        return TypeRef.builder()
                .typeFullName(this.getTypeFullName().replace(oldPrefix, newPrefix))
                .typeName(this.getTypeName())
                .dimensions(this.getDimensions())
                .ref(this.getReference().copy(oldPrefix, newPrefix))
                .type(this.getType())
                .create();
    }
}
