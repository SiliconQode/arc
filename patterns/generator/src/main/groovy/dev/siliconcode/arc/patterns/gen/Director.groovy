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
package dev.siliconcode.arc.patterns.gen

import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager
import dev.siliconcode.arc.patterns.gen.cue.CueManager

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Director {

    GeneratorContext context
    DBManager manager

    void initialize() {
        context = GeneratorContext.getInstance()
        context.resetPatternBuilderComponents()
        context.resetComponentGenerators()
        manager = DBManager.instance
    }

    void execute() {
        DBCredentials creds = DBCredentials.builder()
                .driver(context.db.driver)
                .type(context.db.type)
                .url(context.db.url)
                .user(context.db.user)
                .pass(context.db.pass)
                .create()
        if (context.resetDb) {
            manager.createDatabase(creds)
        }

        List<System> systems = []

        if (!context.resetOnly) {
            // Open DB Connection
            manager.open(creds)

            Set<String> patterns = new HashSet<>()
            context.results.rowMap().each { String id, Map<String, String> map ->
                patterns << map.get("PatternType")
            }

            if (!context.generateOnly) {
                patterns.each { pattern ->
                    Map<String, String> map = context.results.column("PatternType").findAll { String id, String value -> value == pattern }
                    context.loader.loadPatternCues(pattern, "java")

                    map.each { id, val ->
                        System sys = System.findFirst("name = ?", pattern)
                        if (!sys) {
                            context.sysBuilder.init(pattern: pattern, id: id)
                            sys = context.sysBuilder.create()
                            systems << sys
                        } else {
                            context.sysBuilder.init(pattern: pattern, system: sys, id: id)
                            context.sysBuilder.create()
                            if (!systems.contains(sys))
                                systems << sys
                        }
                        context.resetPatternBuilderComponents()
                    }
                }
            }

            if (!context.dataOnly) {
                systems.each {sys ->
                    context.sysGen.init(sys: sys, builder: new FileTreeBuilder(new File(context.getOutput())), pattern: sys.getName())
                    context.sysGen.generate()
                }
            }

            // Close DB Connection
            manager.close()
        }
    }
}
