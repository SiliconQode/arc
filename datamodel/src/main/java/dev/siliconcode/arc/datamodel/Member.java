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

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Member extends Component {

    public List<System> getParentSystems() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentSystems();
        return Lists.newArrayList();
    }

    public List<Project> getParentProjects() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentProjects();
        return Lists.newArrayList();
    }

    @Override
    public Project getParentProject() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentProject();
        return null;
    }

    public List<Module> getParentModules() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentModules();
        return Lists.newArrayList();
    }

    public Module getParentModule() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentModule();
        return null;
    }

    public List<Namespace> getParentNamespaces() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentNamespaces();
        return Lists.newArrayList();
    }

    public Namespace getParentNamespace() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentNamespace();
        return null;
    }

    public List<File> getParentFiles() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentFiles();
        return Lists.newArrayList();
    }

    public File getParentFile() {
        Type parent = getParentType();
        if (parent != null)
            return parent.getParentFile();
        return null;
    }

    public List<Type> getParentTypes() {
//        List<Type> types = Lists.newLinkedList();
//        try {
//            types.add(parent(Class.class));
//        } catch (IllegalArgumentException e) {}
//        try {
//            types.add(parent(Enum.class));
//        } catch (IllegalArgumentException e) {}
//        try {
//            types.add(parent(Interface.class));
//        } catch (IllegalArgumentException e) {}
//        return types;
        List<Type> types = Lists.newLinkedList();
        types.add(getParentType());
        return types;
    }

    public Type getParentType() {
//        Type parent;
//        try {
//            parent = parent(Class.class);
//        } catch (IllegalArgumentException ex) {
//            try {
//                parent = parent(Interface.class);
//            } catch (IllegalArgumentException ee) {
//                parent = parent(Enum.class);
//            }
//        }
//
//        return parent;
        return parent(Type.class);
    }

    /**
     * @return The parent Measurable of this Measurable
     */
    @Override
    public Measurable getParent() {
        return getParentType();
    }

    @Override
    public String getRefKey() {
        return getString("compKey");
    }

    public void updateKey() {
//        Type parent = null;
//        try {
//            if (parent(Class.class) != null)
//                parent = parent(Class.class);
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            if (parent(Interface.class) != null)
//                parent = parent(Class.class);
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            if (parent(Enum.class) != null)
//                parent = parent(Enum.class);
//        } catch (IllegalArgumentException e) {
//        }
        Type parent = parent(Type.class);

        String newKey;
        String oldKey = getCompKey();
        if (parent != null)
            newKey = parent.getCompKey() + "#" + getName();
        else
            newKey = getName();

        setString("compKey", newKey);
        save();
        refresh();
        updateReferences(oldKey);
    }

    public abstract Member copy(String oldPrefix, String newPrefix);
}
