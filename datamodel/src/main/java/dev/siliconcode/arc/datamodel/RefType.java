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

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum RefType {
    TYPE(1),
    METHOD(2),
    INITIALIZER(3),
    CONSTRUCTOR(4),
    FIELD(5),
    LITERAL(6),
    NAMESPACE(7),
    RELATION(8),
    PATTERN(9);

    private final int value;
    public static Map<Integer, RefType> map = Maps.newHashMap();

    static {
        map.put(1, TYPE);
        map.put(2, METHOD);
        map.put(3, INITIALIZER);
        map.put(4, CONSTRUCTOR);
        map.put(5, FIELD);
        map.put(6, LITERAL);
        map.put(7, NAMESPACE);
        map.put(8, RELATION);
        map.put(9, PATTERN);
    }

    RefType(int value) {
        this.value = value;
    }

    public int value() { return value; }

    public static RefType fromValue(int value) { return map.get(value); }

    public static RefType fromComponent(Component c) {
        if (c instanceof Type)
            return TYPE;
        if (c instanceof Initializer)
            return INITIALIZER;
        if (c instanceof Constructor)
            return CONSTRUCTOR;
        if (c instanceof Field)
            return FIELD;
        if (c instanceof Literal)
            return LITERAL;
        if (c instanceof Method)
            return METHOD;
        return null;
    }

    public static RefType fromPattern(PatternInstance inst) {
        return PATTERN;
    }
}
