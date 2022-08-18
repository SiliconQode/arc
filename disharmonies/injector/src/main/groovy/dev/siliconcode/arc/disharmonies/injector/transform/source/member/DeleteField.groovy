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
package dev.siliconcode.arc.disharmonies.injector.transform.source.member

import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteField extends BasicSourceTransform {

    Type type
    Field field

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    DeleteField(File file, Type type, Field field) {
        super(file)
        this.type = type
        this.field = field
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        start = field.getStart() - 1
        end = field.getEnd() - 1
        lines = ops.readLines().asList()
    }

    @Override
    void buildContent() {
        if (start != end) {
            def defLines = lines[start..end].join("\n")
            defLines = replace(defLines)
            (end - start + 1).times {
                lines.remove(start)
            }
            if (!defLines.isEmpty())
                lines.add(start, defLines)
        } else {
            def defLines = lines[start]
            defLines = replace(defLines)
            if (defLines.isEmpty())
                lines.remove(start)

            lines[start] = defLines
        }

        if (file.getFields().isEmpty() && lines[start].isEmpty()) {
            lines.remove(start)
        }
    }

    @Override
    void injectContent() {
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        updateContainingAndAllFollowing(start, start - end)

        updateImports()
    }

    private String replace(String defLines) {
        if (defLines =~ /,\s*${field.getName()}\s*.+,/) {
            defLines = defLines.replaceAll(/,\s+${field.getName()}\s*/, "")
        } else if (defLines =~ /,\s*${field.getName()}\s*.*;/) {
            defLines = defLines.replaceAll(/,\s+${field.getName()}\s*.*;/, ";")
        } else if (defLines =~ /${field.getName()}\s*,/) {
            defLines = defLines.replaceAll(/${field.getName()}\s*,\s+/, "")
        } else if (defLines =~ /${field.getName()}\s*;/) {
            defLines = ""
        } else if (!defLines.contains(",")) {
            defLines = ""
        }

        return defLines
    }
}
