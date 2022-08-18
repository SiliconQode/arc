/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.disharmonies.injector.select

import dev.siliconcode.arc.datamodel.Component
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Role

/**
 * A Selector for Classes Internal to a Design Pattern
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SelectInternalClasses extends Selector {

    private int num

    SelectInternalClasses(int num) {
        this.num = num
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<Component> select(PatternInstance parent, Role binding) {
        if (!parent)
            throw new IllegalArgumentException("SelectInternalClasses.select(): parent cannot be null")
        if (!binding)
            throw new IllegalArgumentException("SelectInternalClasses.select(): binding cannot be null")
        if (num <= 0)
            throw new IllegalArgumentException("SelectInternalClasses.select(): num must be greater than 0")
        List<Component> types = parent.getTypesBoundTo(binding)
        if (types.size() <= num)
            return types
        else {
            return types.subList(0, num)
        }
    }
}
