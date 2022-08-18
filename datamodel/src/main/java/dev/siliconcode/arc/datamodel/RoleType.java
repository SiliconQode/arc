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

public enum RoleType {

    CLASSIFIER(1),
    RELATION(2),
    STRUCT_FEAT(3),
    BEHAVE_FEAT(4);

    private final int value;
    private static Map<Integer, RoleType> map = Maps.newHashMap();

    static {
        map.put(1, CLASSIFIER);
        map.put(2, RELATION);
        map.put(3, STRUCT_FEAT);
        map.put(4, BEHAVE_FEAT);
    }

    RoleType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RoleType fromValue(int value) {
        return map.get(value);
    }
}
