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

public class Utilities {

    public static Component findComponent(RefType type, String compKey) {
        switch(type) {
            case CONSTRUCTOR:
                return findConstructor(compKey);
            case FIELD:
                return findField(compKey);
            case INITIALIZER:
                return findInitializer(compKey);
            case LITERAL:
                return findLiteral(compKey);
            case METHOD:
                return findMethod(compKey);
            case TYPE:
                return findType(compKey);
        }

        return null;

    }

    private static Type findType(String compKey) {
//        Type val = findClass(compKey);
//        if (val == null) val = findEnum(compKey);
//        if (val == null) val = findInterface(compKey);
//        return val;
        return Type.findFirst("compKey = ?", compKey);
    }

    private static Type findClass(String compKey) {
        return Type.findFirst("compKey = ? and type = ?", compKey, Type.CLASS);
    }

    private static Type findEnum(String compKey) {
        return Type.findFirst("compKey = ? and type = ?", compKey, Type.ENUM);
    }

    private static Type findInterface(String compKey) {
        return Type.findFirst("compKey = ? and type = ?", compKey, Type.INTERFACE);
    }

    private static Member findInitializer(String compKey) {
        return Initializer.findFirst("compKey = ?", compKey);
    }

    private static Member findLiteral(String compKey) {
        return Literal.findFirst("compKey = ?", compKey);
    }

    private static Member findField(String compKey) {
        return Field.findFirst("compKey = ?", compKey);
    }

    private static Member findMethod(String compKey) {
        return Method.findFirst("compKey = ?", compKey);
    }

    private static Member findConstructor(String compKey) {
        return Constructor.findFirst("compKey = ?", compKey);
    }
}
