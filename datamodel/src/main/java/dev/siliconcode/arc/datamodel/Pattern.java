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
@BelongsTo(parent = PatternRepository.class, foreignKeyName = "pattern_repository_id")
public class Pattern extends Model {

    public Pattern() {}

    @Builder(buildMethodName = "create")
    public Pattern(String name, String key, String family) {
        if (name != null && !name.isEmpty()) setName(name);
        if (key != null && !key.isEmpty()) setString("patternKey", key);
        if (family != null && !family.isEmpty()) setFamily(family);
        save();
    }

    public String getPatternKey() { return getString("patternKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addInstance(PatternInstance inst) {
        if (inst != null)
            inst.setPatternID(this.getId());
    }

    public void removeInstance(PatternInstance inst) {
        if (inst != null && inst.getPatternID() != null && inst.getPatternID().equals(getId()))
            inst.setPatternID(null);
    }

    public List<PatternInstance> getInstances() {
        return PatternInstance.find("parent_pattern_id = ?", this.getId());
    }

    public void addRole(Role role) { add(role); save(); }

    public void removeRole(Role role) { remove(role); save(); }

    public List<Role> getRoles() { return getAll(Role.class); }

    public Role getRoleByName(String name) {
        try {
            return get(Role.class, "name = ?", name).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public PatternRepository getParentPatternRepository() {
        return parent(PatternRepository.class);
    }

    public String getFamily() { return getString("family"); }

    public void setFamily(String family) { setString("family", family); save(); }

    public List<Role> mandatoryRoles() {
        List<Role> roles = Lists.newArrayList();
        getRoles().forEach(role -> {
            if (role.isMandatory())
                roles.add(role);
        });
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pattern) {
            Pattern role = (Pattern) o;
            return role.getPatternKey().equals(this.getPatternKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPatternKey());
    }
}
