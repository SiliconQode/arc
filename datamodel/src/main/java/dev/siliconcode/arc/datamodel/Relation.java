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
import org.apache.commons.lang3.tuple.Pair;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Relation extends Model {

    public String getRelKey() {
        return getString("relKey");
    }

    public void setToAndFromRefs(Reference to, Reference from) {
        setToReference(to);
        setFromReference(from);
    }

    public void setToReference(Reference ref) {
        int value = 0;
        if (get("to_id") != null)
            value = getInteger("to_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("to_id", 0);
        else {
            addReference(ref);
            set("to_id", ref.getId());
        }
        save();
    }

    public void setFromReference(Reference ref) {
        int value = 0;
        if (get("from_id") != null)
            value = getInteger("from_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("from_id", 0);
        else {
            addReference(ref);
            set("from_id", ref.getId());
        }
        save();
    }

    private void addReference(Reference ref) {
        add(ref);
        save();
    }

    private void removeReference(Reference ref) {
        remove(ref);
        save();
    }

    public RelationType getType() {
        return RelationType.fromValue(getInteger("type"));
    }

    public void setType(RelationType type) {
        set("type", type.value());
        save();
    }

    public List<Reference> getReferences() {
        return getAll(Reference.class);
    }

    public static Relation findBetween(Type src, Type dest, RelationType type) {

        List<Reference> fromRefs = Reference.find("refKey = ?", src.getRefKey());
        List<Reference> toRefs = Reference.find("refKey = ?", dest.getRefKey());
        List<Pair<Reference, Reference>> pairs = Lists.newArrayList();
        fromRefs.forEach(from -> toRefs.forEach(to -> pairs.add(Pair.of(from, to))));

        List<Relation> rels = Lists.newArrayList();
        pairs.forEach(pair -> rels.addAll(Relation.find("from_id = ? AND to_id = ? AND type = ?", pair.getLeft().getId(), pair.getRight().getId(), type.value())));

        if (rels.isEmpty())
            return null;
        else
            return rels.get(0);
    }

    public Reference createReference() {
        return Reference.builder().refType(RefType.RELATION).refKey(getRelKey()).create();
    }

    public Relation copy(String oldPrefix, String newPrefix) {
        Reference to = Reference.findById(getInteger("to_id"));
        Reference from = Reference.findById(getInteger("from_id"));
        Reference toCopy = to.copy(oldPrefix, newPrefix);
        Reference fromCopy = from.copy(oldPrefix, newPrefix);
        String fromKey = fromCopy.getRefKey();
        String toKey = toCopy.getRefKey();
        Relation copy = Relation.createIt("relKey", fromKey + "-" + toKey);
        copy.saveIt();
        copy.setToAndFromRefs(toCopy, fromCopy);
        copy.setType(this.getType());
        copy.saveIt();

        return copy;
    }
}
