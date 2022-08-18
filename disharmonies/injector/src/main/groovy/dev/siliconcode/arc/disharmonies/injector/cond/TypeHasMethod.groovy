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

import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type

/**
 * A condition which determines whether a type already contains a method with a matching signature
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TypeHasMethod implements Condition {

    /**
     * The type that will contain the method
     */
    Type type
    /**
     * A method node representing the method to be added
     */
    Method method
    /**
     * A string name of the new method to be created
     */
    String name

    /**
     * Constructs a new TypeHasMethod condition for the given type and a method with the provided name
     * @param type Type to which the method will be added
     * @param name String name of the method
     */
    TypeHasMethod(Type type, String name) {
        this.type = type
        this.name = name
    }

    /**
     * Constructs a new TypeHasMethod condition for the given type and Method
     * @param type Type to which the method will be added
     * @param method The method to be added
     */
    TypeHasMethod(Type type, Method method) {
        this.type = type
        this.method = method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        if (!type)
            throw new IllegalArgumentException("typeHasMethod.check(): type cannot be null")
        if (!method && !name)
            throw new IllegalArgumentException("typeHasMethod.check(): method and name cannot both be null")

        Method other = null
        if (method) {
            other = type.methods.find { Method m ->
                m.signature() == method.signature()
            }
        } else if (name) {
            other = type.methods.find { Method m ->
                m.name == name
            }
        }

        other != null
    }
}
