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

import dev.siliconcode.arc.datamodel.Constructor
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type

/**
 * A condition which checks whether a type has a constructor with the matching signature as the one provided
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TypeHasConstructor implements Condition {

    /**
     * The type to which the constructor will be added
     */
    Type type
    /**
     * The constructor node that will be added
     */
    Constructor cons

    /**
     * Constructs a new TypeHasConstructor condition
     * @param type The type to which the constructor will be added
     * @param cons The constructor
     */
    TypeHasConstructor(Type type, Constructor cons) {
        this.type = type
        this.cons = cons
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        if (!type)
            throw new IllegalArgumentException("typeHasConstructor.check(): type cannot be null")
        if (!cons)
            throw new IllegalArgumentException("typeHasConstructor.check(): cons cannot be null")

        Method mnode = type.constructors.find { Constructor m ->
            m.signature() == cons.signature()
        }

        mnode != null
    }
}
