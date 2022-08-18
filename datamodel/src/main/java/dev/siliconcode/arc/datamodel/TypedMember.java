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

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class TypedMember extends Member {

    public void setType(TypeRef ref) {
        if (this.getAll(TypeRef.class).size() == 0) {
            add(ref);
            save();
        }
        else {
            List<TypeRef> refs = this.getAll(TypeRef.class);
            for (TypeRef r : refs)
                remove(r);

            add(ref);
            save();
        }
    }

    public TypeRef getType() {
        if (getAll(TypeRef.class).size() > 0)
            return getAll(TypeRef.class).get(0);
        else
            return null;
    }

    public void addTemplateParam(TemplateParam param) {
        if (param != null)
            add(param);
        save();
    }

    public void removeTemplateParam(TemplateParam param) {
        if (param != null)
            remove(param);
        save();
    }

    public boolean hasTemplateParam(String name) {
        return getTemplateParam(name) != null;
    }

    public TemplateParam getTemplateParam(String name) {
        try {
            return get(TemplateParam.class, "name = ?", name).get(0);
        } catch(IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public List<TemplateParam> getTemplateParams() {
        return getAll(TemplateParam.class);
    }
}
