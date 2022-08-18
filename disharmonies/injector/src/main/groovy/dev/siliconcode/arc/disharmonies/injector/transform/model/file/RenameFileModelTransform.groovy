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
import dev.siliconcode.arc.disharmonies.injector.transform.model.FileModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.RenameFile

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameFileModelTransform extends FileModelTransform {

    String name

    RenameFileModelTransform(File file, String name) {
        super(file)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. file does not already have name
        if (file.name == name)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Store previous state
        String oldName = file.getFullPath()
        // Execute Transform
        file.setName(name) // TODO modify for file names
        file.updateKey()
        // Generate Source Transform
        new RenameFile(file, oldName).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. file has new name
        assert (file.name == name)
    }
}
