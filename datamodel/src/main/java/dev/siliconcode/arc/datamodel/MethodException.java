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

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

@Many2Many(other = TypeRef.class, join = "methodexceptions_typerefs", sourceFKName = "exception_id", targetFKName = "type_ref_id")
public class MethodException extends Model {

    public void removeTypeRef(TypeRef ref) {
        if (ref != null) {
            remove(ref);
            save();
        }
    }

    public void setTypeRef(TypeRef ref) {
        if (ref != null) {
            TypeRef rem = getTypeRef();
            if (rem != null) {
                removeTypeRef(rem);
            }
            add(ref);
        }
    }

    public TypeRef getTypeRef() {
        try {
            return getAll(TypeRef.class).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
