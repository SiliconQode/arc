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
package dev.siliconcode.arc.disharmonies.injector.transform.model.type

import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.TypeModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.AddFieldSetter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldSetterModelTransform extends TypeModelTransform {

    Field field
    Method method

    AddFieldSetterModelTransform(Type type, Field field) {
        super(type)
        this.field = field
    }

    @Override
    void verifyPreconditions() {
        // 1. field is not null
        if (!field)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type contains field
        if (!type.getFields().contains(field))
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type does not already contain getter
        if (type.getMethods().find { it.getName() == "set${field.name.capitalize()}" } != null)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        AddFieldSetter trans = new AddFieldSetter(type.getParentFile(), type, field)
        trans.execute()
        method = trans.getMethod()
    }

    @Override
    void verifyPostconditions() {
        assert(type.getMethods().find { it.getName() == "set${field.name.capitalize()}" } != null)
    }
}
