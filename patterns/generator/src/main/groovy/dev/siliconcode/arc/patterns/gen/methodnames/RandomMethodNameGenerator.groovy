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

import com.google.common.collect.Sets
import dev.siliconcode.arc.patterns.gen.GeneratorContext

class RandomMethodNameGenerator extends MethodNameGenerator {

    int min
    int max

    RandomMethodNameGenerator(GeneratorContext ctx, int min, int max) {
        super(ctx)
        this.min = min
        this.max = max
    }

    @Override
    Set<String> generate(Set<String> allNames) {
        Set<String> set = Sets.newHashSet()
        Random rand = new Random()
        int num
        if (min == max)
            num = min
        else
            num = rand.nextInt(max - min) + min

        for (int i = 0; set.size() < num; ) {
            String s = ctx.methBuilder.getMethodName()
            if (!allNames.contains(s)) {
                set << s
                i++
            }
        }
        return set
    }
}
