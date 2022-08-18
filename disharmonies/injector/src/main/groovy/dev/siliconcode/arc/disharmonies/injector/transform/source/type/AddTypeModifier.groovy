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
package dev.siliconcode.arc.disharmonies.injector.transform.source.type

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Modifier
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.TypeTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddTypeModifier extends TypeTransform {

    Modifier mod

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    AddTypeModifier(File file, Type type, Modifier mod) {
        super(file, type)
        this.mod = mod
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())

        start = type.start - 1
        end = type.end - 1

        lines = ops.readLines()[start..end]
        text = lines.join("\n")
    }

    @Override
    void buildContent() {
        def matcher = text =~ /${type.accessibility.toString().toLowerCase()}\s+(\w+\s+)*${kind()}\s+${type.name}/
        if (matcher.find()) {
            String innerContent = matcher[0][0]
            String mods = innerContent.split(/${kind()}/)[0]
            mods += "${mod.getName().toLowerCase()} "
            String newContent = innerContent.replace(innerContent.substring(0, innerContent.indexOf(kind())), mods)
            text = text.replace(innerContent, newContent)
        }
    }

    @Override
    void injectContent() {
        lines = ops.readLines()
        def tlines = text.split("\n")
        for (int i = 0; i < tlines.size(); i++) {
            lines[i + start] = tlines[i]
        }
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {}
}
