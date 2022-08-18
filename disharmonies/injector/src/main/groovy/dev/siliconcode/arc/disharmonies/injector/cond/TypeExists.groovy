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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type

/**
 * A Condition that checks whether a given type already exists in a given type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TypeExists implements Condition {

    /**
     * The file in which the type will be added
     */
    File file
    /**
     * The type to check
     */
    String type

    /**
     * Constructs a new TypeExists condition
     * @param file The file to which the type will be added
     * @param type The type to add
     */
    TypeExists(File file, String type) {
        this.file = file
        this.type = type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        if (!type)
            throw new IllegalArgumentException("TypeExists.check(): type cannot be null or empty")
        if (!file)
            throw new IllegalArgumentException("TypeExists.check(): file cannot be null")

        Type t = file.allTypes.find { it.name == type }
        t != null
    }
}
