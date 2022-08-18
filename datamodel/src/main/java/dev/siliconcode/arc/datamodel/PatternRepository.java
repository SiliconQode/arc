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
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PatternRepository extends Model {

    public PatternRepository() {}

    @Builder(buildMethodName = "create")
    public PatternRepository(String name, String key, String toolName) {
        if (name != null && !name.isEmpty()) setName(name);
        if (key != null && !key.isEmpty()) set("repoKey", key);
        if (toolName != null && !toolName.isEmpty()) setToolName(toolName);

        save();
    }

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addPattern(Pattern pattern) { add(pattern); save(); }

    public void removePattern(Pattern pattern) { remove(pattern); save(); }

    public List<Pattern> getPatterns() { return getAll(Pattern.class); }

    public String getToolName() { return getString("toolName"); }

    public void setToolName(String toolName) {
        setString("toolName", toolName);
        save();
    }

    public List<PatternInstance> getPatternInstances() {
        Set<PatternInstance> insts = Sets.newHashSet();
        getPatterns().forEach(pattern -> {
            insts.addAll(pattern.getInstances());
        });
        return Lists.newArrayList(insts);
    }

    public List<Role> getRoles() {
        List<Pattern> patterns = getPatterns();
        List<Role> roles = Lists.newLinkedList();
        for (Pattern p : patterns)
            roles.addAll(p.getRoles());

        return roles;
    }

    public List<RoleBinding> getRoleBindings() {
        Set<RoleBinding> bindings = Sets.newHashSet();
        getPatternInstances().forEach(inst -> {
            bindings.addAll(inst.getRoleBindings());
        });
        return Lists.newArrayList(bindings);
    }
}
