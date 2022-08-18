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

import com.empirilytics.arc.datamodel.Project
import com.empirilytics.arc.datamodel.System
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import com.empirilytics.arc.disharmonies.injector.transform.model.SystemModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.source.structural.AddProject

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddProjectModelTransform extends SystemModelTransform {

    String name
    String version
    Project proj

    AddProjectModelTransform(System system, String name, String version) {
        super(system)
        this.name = name
        this.version = version
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. version is not null or empty
        if (!version)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. sys does not contain a project with the same name and version
        if (sys.getProjects().find { it.name == name && it.version == version })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute transform
        proj = Project.builder().name(name).projKey(name).version(version).relPath(name).create()
        sys.addProject(proj)
        proj.updateKeys()
        // Generate source transform
        new AddProject(proj, sys).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. sys now contains proj
        assert(sys.getProjects().contains(proj))
        // 2. proj parent is sys
        assert(proj.parent(System.class) == sys)
    }
}
