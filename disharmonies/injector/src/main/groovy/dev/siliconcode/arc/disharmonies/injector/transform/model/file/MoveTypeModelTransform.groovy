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
package dev.siliconcode.arc.disharmonies.injector.transform.model.file

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.FileModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.type.MoveType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveTypeModelTransform extends FileModelTransform {

    Type type
    File newParent

    MoveTypeModelTransform(File file, Type type, File newParent) {
        super(file)
        this.type = type
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. type is not null
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. file contains type
        if (!file.getAllTypes().contains(type))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain a type with the same name
        if (newParent.getAllTypes().find { it.getName() == type.getName() })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        file.getParentNamespace()?.removeType(type)
        type.thaw()
        newParent.getParentNamespace()?.addType(type)

        // Generate Source Transform
        new MoveType(type, file, newParent).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. file no longer contains type
        assert(!file.getAllTypes().contains(type))
        // 2. newParent contains type
        assert(newParent.getAllTypes().contains(type))
        // 3. type's parent is newParent
        assert(type.getParentFile() == newParent)
    }
}
