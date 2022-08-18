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
package dev.siliconcode.arc.disharmonies.injector.transform.model.project

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.ProjectModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.AddModule

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddModuleModelTransform extends ProjectModelTransform {

    String name
    Module mod

    AddModuleModelTransform(Project proj, String name) {
        super(proj)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. a module with given name does not already exist in proj
        if (proj.modules.find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        mod = Module.builder().name(name).moduleKey(proj.getProjectKey() + ":" + name).create()
        proj.addModule(mod)
        // construct source transform
        new AddModule(mod, proj).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. proj contains new module mod
        assert(proj.modules.contains(mod))
        // 2. mod's parent is proj
        assert(mod.parent(Project.class) == proj)
    }
}
