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

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class RelationSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Relation rel = new Relation();
        a(rel).shouldBe("valid");
//        //a(rel.errors().get("author")).shouldBeEqual("Author must be provided");
        rel.set("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        a(rel).shouldBe("valid");
        rel.save();
        rel = (Relation) Relation.findAll().get(0);
        a(rel.getId()).shouldNotBeNull();
        a(rel.get("relKey")).shouldBeEqual("rel");
        a(rel.get("type")).shouldBeEqual(RelationType.ASSOCIATION.value());
        a(rel.getType()).shouldBeEqual(RelationType.ASSOCIATION);
        a(Relation.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddReferencePair() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(rel.getAll(Reference.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveReferencePair() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(rel.getAll(Reference.class).size()).shouldBeEqual(1);
        rel = (Relation) Relation.findAll().get(0);
        rel.remove(ref);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(0);
    }

    @Test
    public void addingToAndFromRefsWorks() {
        Relation rel = Relation.createIt("relKey", "rel");
        Reference to = Reference.createIt("refKey", "to");
        to.setType(RefType.TYPE);
        Reference from = Reference.createIt("refKey", "from");
        from.setType(RefType.TYPE);

        rel.setToAndFromRefs(to, from);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(2);

        Reference to2 = Reference.createIt("refKey", "to2");
        to.setType(RefType.TYPE);
        Reference from2 = Reference.createIt("refKey", "from2");
        from.setType(RefType.TYPE);
        rel.setToAndFromRefs(to2, from2);
        a(rel.getAll(Reference.class).size()).shouldBeEqual(2);
        a(Reference.count()).shouldBeEqual(2);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Relation rel = Relation.createIt("relKey", "rel");
        rel.setType(RelationType.ASSOCIATION);
        Reference ref = Reference.createIt("refKey", "ref");
        ref.setType(RefType.TYPE);
        rel.add(ref);
        rel.save();

        a(Relation.count()).shouldBeEqual(1);
        a(Reference.count()).shouldBeEqual(1);
        rel.delete(true);
        a(Relation.count()).shouldBeEqual(0);
        a(Reference.count()).shouldBeEqual(0);
    }
}
