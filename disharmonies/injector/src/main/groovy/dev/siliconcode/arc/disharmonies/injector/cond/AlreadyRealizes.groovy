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


import dev.siliconcode.arc.datamodel.Type

/**
 * A condition used to check if a class already realizes some interface
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AlreadyRealizes implements Condition {

    /**
     * Potential interface the type will realize
     */
    private final String real
    Type type


    /**
     * Constructs a new AlreadyRealizes condition
     * @param type The type in question
     * @param real The interface to realize
     */
    AlreadyRealizes(Type type, String real) {
        this.type = type
        this.real = real
    }

    /**
     * {@inheritDoc}
     */
    boolean check() {
        if (!type)
            throw new IllegalArgumentException("AlreadyRealizes.check(): type cannot be null")
        if (!real)
            throw new IllegalArgumentException("AlreadyRealizes.check(): real cannot be null or empty")

        type.getRealizes().find {
            it.name == real
        } != null
    }
}
