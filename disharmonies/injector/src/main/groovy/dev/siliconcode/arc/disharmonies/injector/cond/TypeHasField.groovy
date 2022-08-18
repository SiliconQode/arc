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
package dev.siliconcode.arc.disharmonies.injector.cond

import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Type

/**
 * A condition which checks whether a matching field with the same name already exists in the type.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TypeHasField implements Condition {

    /**
     * The type which will contain the field
     */
    Type type
    /**
     * The field that will be added
     */
    Field field

    /**
     * Constructs a new TypeHasField condition
     * @param type Type that will contain the field
     * @param field The field
     */
    TypeHasField(Type type, Field field) {
        this.type = type
        this.field = field
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        if (!type)
            throw new IllegalArgumentException("typeHasField.check(): type cannot be null")
        if (!field)
            throw new IllegalArgumentException("typeHasField.check(): field cannot be null")

        Field f = type.fields.find { Field f ->
            f.name == field.name
        }

        f != null
    }
}
