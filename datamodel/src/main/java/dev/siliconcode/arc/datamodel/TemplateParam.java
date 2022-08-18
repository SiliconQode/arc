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

import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("template_params")
@Many2Many(other = TypeRef.class, join = "template_params_typerefs", sourceFKName = "template_param_id", targetFKName = "typeref_id")
@Many2Many(other = Method.class, join = "methods_template_params", sourceFKName = "method_id", targetFKName = "template_param_id")
@Many2Many(other = Constructor.class, join = "constructors_template_params", sourceFKName = "constructor_id", targetFKName = "template_param_id")
@Many2Many(other = Destructor.class, join = "destructors_template_params", sourceFKName = "destructor_id", targetFKName = "template_param_id")
@Many2Many(other = Field.class, join = "fields_template_params", sourceFKName = "field_id", targetFKName = "template_param_id")
@Many2Many(other = Type.class, join = "types_template_params", sourceFKName = "type_id", targetFKName = "template_param_id")
//@Many2Many(other = Class.class, join = "classes_template_params", sourceFKName = "class_id", targetFKName = "template_param_id")
//@Many2Many(other = Interface.class, join = "interfaces_template_params", sourceFKName = "interface_id", targetFKName = "template_param_id")
//@Many2Many(other = Enum.class, join = "enums_template_params", sourceFKName = "enum_id", targetFKName = "template_param_id")
public class TemplateParam extends Model {

    public TemplateParam() {}

    @Builder(buildMethodName = "create")
    public TemplateParam(String name) {
        setString("name", name);
        save();
    }

    public void addTypeBound(TypeRef ref) {
        addTypeRef(ref);
    }

    public void removeTypeBound(TypeRef ref) {
        removeTypeRef(ref);
    }

    public void addTypeRef(TypeRef ref) {
        if (ref != null)
            add(ref);
        save();
    }

    public void removeTypeRef(TypeRef ref) {
        if (ref != null)
            remove(ref);
        save();
    }

    public List<TypeRef> getTypeBounds() { return getTypeRefs(); }

    public List<TypeRef> getTypeRefs() {
        return getAll(TypeRef.class);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TemplateParam) {
            TemplateParam param = (TemplateParam) o;
            return param.getId().equals(this.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public TemplateParam copy(String oldPrefix, String newPrefix) {
        TemplateParam copy = TemplateParam.createIt();
        for (TypeRef ref : getTypeRefs()) {
            copy.addTypeRef(ref.copy(oldPrefix, newPrefix));
        }

        return copy;
    }
}
