/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.patterns.gen.generators.pb

import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.patterns.gen.cue.CueManager

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ProjectBuilder extends AbstractBuilder {

    def create() {
        if (!params.parent)
            throw new IllegalArgumentException("createProject: parent cannot be null")
        if (!params.name)
            throw new IllegalArgumentException("createProject: name cannot be empty or null")
        if (!params.version)
            throw new IllegalArgumentException("createProject: version cannot be empty or null")
        if (!params.pattern)
            throw new IllegalArgumentException("createProject: pattern cannot be empty or null")

        String key = "${((System) params.parent).getKey()}:${params.name}:${params.version}"

        Project proj = Project.builder()
                .name((String) params.name)
                .projKey(key)
                .relPath((String) params.name)
                .version((String) params.version)
                .create()

        proj.setSrcPath(ctx.srcPath)
        proj.setTestPath(ctx.testPath)
        proj.setBinPath(ctx.binPath)

        ((System) params.parent).addProject(proj)

        if (CueManager.instance.cues)
            ctx.loader.selectCue()
        if (CueManager.instance.current)
            ctx.projCueMap[proj.getProjectKey()] = CueManager.instance.getCurrent()

        ctx.modBuilder.init(parent: proj, name: "default", pattern: params.pattern)
        ctx.modBuilder?.create()

        proj.refresh()
        ctx.projRbmlMap[proj.getProjectKey()] = ctx.rbmlManager
        ctx.rbmlManager = null
        CueManager.instance.current = null

        proj
    }
}
