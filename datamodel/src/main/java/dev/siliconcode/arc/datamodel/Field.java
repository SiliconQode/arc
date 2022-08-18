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
import com.google.common.collect.Sets;
import dev.siliconcode.arc.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Many(other = TypeRef.class, join = "fields_typerefs", sourceFKName = "field_id", targetFKName = "type_ref_id")
public class Field extends TypedMember {

    public Field() {}

    @Builder(buildMethodName = "create")
    public Field(String name, int start, int end, String compKey, Accessibility accessibility, TypeRef type) {
        set("name", name, "start", start, "end", end, "compKey", compKey);
        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);
        if (type != null)
            setType(type);
        save();
    }

    public Set<Method> getMethodsUsing() {
        Set<Member> membersUsing = DbUtils.getRelationTo(this, RelationType.USE);
        Set<Method> methodsUsing = Sets.newHashSet();
        membersUsing.forEach(member -> {
            if (member instanceof Method)
                methodsUsing.add((Method) member);
        });

        return methodsUsing;
    }

    @Override
    public Member copy(String oldPrefix, String newPrefix) {
        Field copy = Field.builder()
                .name(this.getName())
                .compKey(this.getCompKey().replace(oldPrefix, newPrefix))
                .accessibility(this.getAccessibility())
                .type(this.getType().copy(oldPrefix, newPrefix))
                .start(this.getStart())
                .end(this.getEnd())
                .create();

        getModifiers().forEach(copy::addModifier);
        getTemplateParams().forEach(templateParam -> copy.addTemplateParam(templateParam.copy(oldPrefix, newPrefix)));

        return copy;
    }

    public Reference createReference() {
        return Reference.builder().refKey(getCompKey()).refType(RefType.FIELD).create();
    }

    public void addMeasure(Measure meas) {
    }

    public void removeMeasure(Measure meas) {
    }

    public List<Measure> getMeasures() {
        return Lists.newArrayList();
    }
}
