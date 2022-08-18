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
package dev.siliconcode.arc.datamodel.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.siliconcode.arc.datamodel.*;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class DbUtils {

    public static Set<Type> getRelationTo(Type t, RelationType relType) {
        try {
            List<Reference> refs = Lists.newArrayList();
            Reference.find("refKey = ?", t.getRefKey()).forEach( ref -> {
                Relation.find("to_id = ? AND type = ?", ref.getId(), relType.value()). forEach(rel -> {
                    refs.add(Reference.findById(rel.getString("from_id")));
                });
            });

            Set<Type> types = Sets.newHashSet();

            refs.forEach (ref -> {
//                if (!Enum.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Enum.find("compKey = ?", ref.getRefKey()).get(0));
//                else if (!Interface.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Interface.find("compKey = ?", ref.getRefKey()).get(0));
//                else if (!Class.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Class.find("compKey = ?", ref.getRefKey()).get(0));
                if (Type.findFirst("compKey = ?", ref.getRefKey()) != null)
                    types.add(Type.findFirst("compKey = ?", ref.getRefKey()));
            });

            return types;
        } catch (Exception e) {
            e.printStackTrace();
            return Sets.newHashSet();
        }
    }

    public static Set<Type> getRelationFrom(Type t, RelationType relType) {
        try {
            List<Reference> refs = Lists.newArrayList();
            Reference.find("refKey = ?", t.getRefKey()).forEach( ref -> {
                Relation.find("from_id = ? AND type = ?", ref.getId(), relType.value()). forEach(rel -> {
                    refs.add(Reference.findById(rel.getString("to_id")));
                });
            });

            Set<Type> types = Sets.newHashSet();

            refs.forEach (ref -> {
//                if (!Enum.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Enum.find("compKey = ?", ref.getRefKey()).get(0));
//                else if (!Interface.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Interface.find("compKey = ?", ref.getRefKey()).get(0));
//                else if (!Class.find("compKey = ?", ref.getRefKey()).isEmpty())
//                    types.add((Type) Class.find("compKey = ?", ref.getRefKey()).get(0));
                if (Type.findFirst("compKey = ?", ref.getRefKey()) != null)
                    types.add(Type.findFirst("compKey = ?", ref.getRefKey()));
            });

            return types;
        } catch (Exception e) {
            e.printStackTrace();
            return Sets.newHashSet();
        }
    }

    public static Set<Member> getRelationFrom(Member m, RelationType relType) {
        try {
            List<Reference> refs = Lists.newArrayList();
            Reference.find("refKey = ?", m.getRefKey()).forEach( ref -> {
                Relation.find("from_id = ? AND type = ?", ref.getId(), relType.value()). forEach(rel -> {
                    refs.add(Reference.findById(rel.getString("to_id")));
                });
            });

            Set<Member> members = Sets.newHashSet();

            refs.forEach (ref -> {
                if (!Method.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Method.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Constructor.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Constructor.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Destructor.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Destructor.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Field.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Field.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Initializer.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Initializer.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Literal.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Literal.find("compKey = ?", ref.getRefKey()).get(0));
            });

            return members;
        } catch (Exception e) {
            e.printStackTrace();
            return Sets.newHashSet();
        }
    }

    public static Set<Member> getRelationTo(Member m, RelationType relType) {
        try {
            List<Reference> refs = Lists.newArrayList();
            Reference.find("refKey = ?", m.getRefKey()).forEach( ref -> {
                Relation.find("to_id = ? AND type = ?", ref.getId(), relType.value()). forEach(rel -> {
                    refs.add(Reference.findById(rel.getString("from_id")));
                });
            });

            Set<Member> members = Sets.newHashSet();

            refs.forEach (ref -> {
                if (!Method.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Method.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Constructor.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Constructor.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Destructor.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Destructor.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Field.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Field.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Initializer.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Initializer.find("compKey = ?", ref.getRefKey()).get(0));
                else if (!Literal.find("compKey = ?", ref.getRefKey()).isEmpty())
                    members.add((Member) Literal.find("compKey = ?", ref.getRefKey()).get(0));
            });

            return members;
        } catch (Exception e) {
            e.printStackTrace();
            return Sets.newHashSet();
        }
    }
}
