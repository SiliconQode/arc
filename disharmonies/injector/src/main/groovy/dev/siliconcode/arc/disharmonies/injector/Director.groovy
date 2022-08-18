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
package dev.siliconcode.arc.disharmonies.injector

import dev.siliconcode.arc.datamodel.Pattern
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class Director {

    def inject(ConfigObject config) {
        Project proj = Project.findFirst("projKey = ?", (String) config.where.projectKey)

        String projKey = config.where.projectKey
        String[] comps = projKey.split(/:/)
        comps[1] = comps[1] + "_copy"
        projKey = comps.join(":")

        if (!Project.findFirst("projKey = ?", projKey)) {

            ProjectCopier copier = new ProjectCopier()
            proj = copier.execute(proj)

            Pattern pattern = Pattern.findFirst("patternKey = ?", (String) config.where.patternKey)
            String[] instKey = config.where.patternInst.split(/:/)
            instKey[1] = instKey[1] + "_copy"
            PatternInstance inst = PatternInstance.findFirst("instKey = ?", (String) instKey.join(":"))

            SourceInjector injector = selectInjector(inst, config)
            int min = config.what.min
            int max = config.what.max

            Random rand = new Random()
            if (max > min) {
                int number = rand.nextInt(max - min) + min
                number.times {
                    injector.inject()
                }
            }

            return ["Key2" : proj.projectKey,
                    "Path2": proj.getFullPath()]
        } else {
            Project p = Project.findFirst("projKey = ?", projKey)
            return ["Key2" : p.projectKey,
                    "Path2": p.getFullPath()]
        }
    }

    SourceInjector selectInjector(PatternInstance inst, ConfigObject config) {
        String type = config.what.type
        String form = config.what.form

        InjectorFactory.instance.createInjector(inst, type, form)
    }
}
