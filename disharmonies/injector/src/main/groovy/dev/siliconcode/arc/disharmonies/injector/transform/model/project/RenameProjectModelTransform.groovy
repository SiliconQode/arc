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

import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.ProjectModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.RenameProject

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameProjectModelTransform extends ProjectModelTransform {

    String name
    String parentKey

    RenameProjectModelTransform(Project proj, String name, String parentKey) {
        super(proj)
        this.name = name
        this.parentKey = parentKey
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. project does not already have the same name
        if (proj.name == name)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. parentKey is not null or empty
        if (!parentKey)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        String oldName = proj.getFullPath()
        proj.setName(name) // make change
        proj.updateKeys()
        new RenameProject(proj, oldName).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. project name is now new name
        assert(proj.name == name)
        // 2. project key now contains new name
        assert(proj.projectKey.endsWith(":${name}:${proj.version}"))
    }
}
