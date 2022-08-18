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

@Singleton
class NameGenManager {

    Map<String, Map<String, MethodNameGenerator>> map = [:]
    NameGenLoader loader
    GeneratorContext ctx
    int min
    int max

    MethodNameGenerator getNameGeneratorFor(String pattern, String typeRole, String methodRole) {
        MethodNameGenerator generator = new RandomMethodNameGenerator(ctx, min, max)

        if (!pattern || !typeRole || !methodRole)
            return generator

        if (!map[pattern]) {
            loader = new NameGenLoader(ctx)
            loader.load(pattern)
        }

        if (map[pattern]) {
            Map<String, MethodNameGenerator> gens = map[pattern]
            if (gens["$typeRole.$methodRole"]) {
                generator = gens["$typeRole.$methodRole"]
            }
        }

        return generator
    }

    void addNameGenerator(String pattern, String id, MethodNameGenerator gen) {
        if (!pattern || !id || !gen)
            return

        if (map.containsKey(pattern)) {
            map[pattern].put(id, gen)
        } else {
            map.put(pattern, [:])
            map[pattern].put(id, gen)
        }
    }
}
