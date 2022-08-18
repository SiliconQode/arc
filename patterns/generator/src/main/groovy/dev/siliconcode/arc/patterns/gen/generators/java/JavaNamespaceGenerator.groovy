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

import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.patterns.gen.generators.NamespaceGenerator
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaNamespaceGenerator extends NamespaceGenerator {

    JavaNamespaceGenerator() {
    }

    @Override
    def generate() {
        FileTreeBuilder builder = (FileTreeBuilder) params.builder
        Namespace ns = (Namespace) params.ns
        log.info("Generating namespace ${ns.getName()}")

        generateRec(ns, builder)
        log.info("Done generating namespace")
    }

    private def generateRec(Namespace ns, FileTreeBuilder builder) {
        builder {
            builder."${ns.getName().replace(".", File.separator)}" {
//                ns.getFiles().each {
//                    ctx.fileGen.init(file: it, builder: builder)
//                    ctx.fileGen.generate()
//                }
//                ns.getNamespaces().each {
//                    generateRec(it, builder)
//                }
            }
        }
    }
}
