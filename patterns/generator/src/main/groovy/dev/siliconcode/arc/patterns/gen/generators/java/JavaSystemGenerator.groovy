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

import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.patterns.gen.generators.SystemGenerator
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaSystemGenerator extends SystemGenerator {

    @Override
    def generate() {
        if (!params.sys)
            throw new IllegalArgumentException("System cannot be null!")
        if (!params.builder)
            throw new IllegalArgumentException("FileTreeBuilder cannot be null")

        System sys = (System) params.sys
        log.info("Generating system ${sys.getName()}")
        FileTreeBuilder builder = (FileTreeBuilder) params.builder

        builder {
            "${sys.name}" {
                sys.getProjects().each { proj ->
                    proj.refresh()
                    ctx.rbmlManager = ctx.projRbmlMap[proj.getProjectKey()]
                    "${proj.name}" {
                        ctx.projGen.init(proj: proj, builder: builder, num: proj.getName().split("-")[1], pattern: sys.getName())
                        ctx.projGen.generate()
                    }
                }
            }
        }

        sys.getProjects().each {proj ->
            proj.getFiles().each {
                ctx.fileGen.init(file: it)
                ctx.fileGen.generate()
            }
        }
        log.info("Done generating system")
    }
}
