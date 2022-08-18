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
package dev.siliconcode.arc.patterns.gen.methodnames

import dev.siliconcode.arc.patterns.gen.GeneratorContext
import groovy.yaml.YamlSlurper

class NameGenLoader {

    GeneratorContext ctx

    NameGenLoader(GeneratorContext ctx) {
        this.ctx = ctx
    }

    void load(String pattern) {
        if (!pattern)
            return

        YamlSlurper ys = new YamlSlurper()
        def config = null
        try (Reader r = new InputStreamReader(NameGenLoader.class.getResourceAsStream("/mues/$pattern.mu"))) {
            config = ys.parse(r)
        } catch (Exception ex) {

        }

        if (config) {
            config.roles.each { role ->
                String id = role.id
                String type = role.type
                switch (type) {
                    case "specific":
                        String name = role.name
                        NameGenManager.instance.addNameGenerator(pattern, id, new SpecificMethodNameGenerator(ctx, name))
                        break
                    case "roleBased":
                        String dependsOn = role.dependsOn
                        String prefix = role.prefix
                        NameGenManager.instance.addNameGenerator(pattern, id, new RoleBasedMethodNameGenerator(ctx, dependsOn, prefix))
                        break
                }
            }
        }
    }
}
