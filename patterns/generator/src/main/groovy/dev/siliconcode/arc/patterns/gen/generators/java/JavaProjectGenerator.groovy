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
package dev.siliconcode.arc.patterns.gen.generators.java

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.patterns.gen.cue.CueManager
import dev.siliconcode.arc.patterns.gen.generators.ProjectGenerator
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaProjectGenerator extends ProjectGenerator {

    JavaProjectGenerator() {
    }

    @Override
    def generate() {
        Project proj = (Project) params.proj
        log.info("Generating Project ${proj.getName()}")
        CueManager.instance.setCurrent(ctx.projCueMap[proj.getProjectKey()])
        FileTreeBuilder builder = (FileTreeBuilder) params.builder

        if (proj.getModules().size() > 1) {
            ctx.dirGen.init(project: proj, module: null, tree: builder, num: params.num, pattern: params.pattern)
            ctx.dirGen.generate()
            proj.getModules().each { Module m ->
                builder."${m.getName()}" {
                    ctx.modGen.init(project: proj, mod: m, builder: builder, subproject: true, num: params.num, pattern: params.pattern)
                    ctx.modGen.generate()
                }
            }
        } else if (proj.getModules().size() == 1) {
            ctx.modGen.init(project: proj, mod: proj.getModules().first(), builder: builder, subproject: false, num: params.num, pattern: params.pattern)
            ctx.modGen.generate()
        }
        log.info("Done generating project")
    }
}
