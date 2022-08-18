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
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
//@BelongsTo(parent = Namespace.class, foreignKeyName = "namespace_id")
public class Enum /*extends Classifier*/ {

    public Enum() {

    }

//    @Builder(buildMethodName = "create")
//    public Enum(String name, int start, int end, String compKey, Accessibility accessibility) {
//        set("name", name, "start", start, "end", end, "compKey", compKey, "qualified_name", name);
//        if (accessibility != null)
//            setAccessibility(accessibility);
//        else
//            setAccessibility(Accessibility.PUBLIC);
//
//        save();
//    }
//
//    @Override
//    protected Type copyType(String oldPrefix, String newPrefix) {
//        return Enum.builder()
//                .name(this.getName())
//                .compKey(this.getCompKey().replace(oldPrefix, newPrefix))
//                .accessibility(this.getAccessibility())
//                .start(this.getStart())
//                .end(this.getEnd())
//                .create();
//    }

//    public boolean hasLiteralWithName(String name) {
//        return getLiteralWithName(name) != null;
//    }
//
//    public Literal getLiteralWithName(String name) {
//        try {
//            return get(Literal.class, "name = ?", name).get(0);
//        } catch (IndexOutOfBoundsException ex) {
//            return null;
//        }
//    }
}
