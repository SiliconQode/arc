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
package dev.siliconcode.arc.disharmonies.injector.transform.model.module

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModuleModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.RenameModule

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameModuleModelTransform extends ModuleModelTransform {

    String name

    RenameModuleModelTransform(Module mod, String name) {
        super(mod)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // Pre-Conditions
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. mod does not already have this name
        if (mod.name == name)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Logic
        // store prior state
        String oldName = mod.getFullPath()
        // execute transform
        mod.setName(name)
        mod.updateKey()
        // create source transform
        new RenameModule(mod, oldName).execute()
    }

    @Override
    void verifyPostconditions() {
        // Post-Conditions
        // 1. the name of mod is now name
        assert(mod.name == name)
        // 2. the module key is updated to include the new name
        assert(mod.moduleKey.endsWith(":${name}"))
    }
}
