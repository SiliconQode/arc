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
package dev.siliconcode.arc.disharmonies.injector.transform.model.system

import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.SystemModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.RenameSystem

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameSystemModelTransform extends SystemModelTransform {

    String name

    RenameSystemModelTransform(System system, String name) {
        super(system)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // Pre-Conditions
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. system name is not already name
        if (sys.name == name)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Store previous state
        String oldName = sys.getBasePath()
        // Execute transform
        sys.setName(name)
        sys.updateKeys()
        // Generate source transform
        new RenameSystem(sys, oldName).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. system name is now name
        assert(sys.getName() == name)
        // 2. system key is correct
        assert(sys.getKey().endsWith(name))
    }
}
