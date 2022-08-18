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
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Many2Manies;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Many(other = RoleBinding.class, join = "roles_role_bindings", sourceFKName = "role_id", targetFKName = "role_binding_id")
@BelongsTo(parent = Pattern.class, foreignKeyName = "pattern_id")
public class Role extends Model {

    public Role() {}

    @Builder(buildMethodName = "create")
    public Role(String roleKey, String name, RoleType type, boolean mandatory) {
        set("roleKey", roleKey);
        setName(name);
        setType(type);
        setBoolean("mandatory", mandatory);
        save();
    }

    public RoleType getType() {
        if (get("type") == null)
            return null;
        else
            return RoleType.fromValue(getInteger("type"));
    }

    public void setType(RoleType type) {
        if (type == null)
            set("type", null);
        else
            set("type", type.value());
        save();
    }

    public String getRoleKey() { return getString("roleKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public PatternRepository getParentPatternRepository() {
        Pattern parent = getParentPattern();
        if (parent != null)
            return parent.getParentPatternRepository();
        return null;
    }

    public Pattern getParentPattern() {
        return parent(Pattern.class);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Role) {
            Role role = (Role) o;
            return role.getRoleKey().equals(this.getRoleKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRoleKey());
    }

    public boolean isMandatory() {
        return getBoolean("mandatory");
    }
}
