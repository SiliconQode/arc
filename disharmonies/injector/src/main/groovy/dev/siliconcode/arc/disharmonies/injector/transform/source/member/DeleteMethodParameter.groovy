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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Parameter
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteMethodParameter extends BasicSourceTransform {

    Type type
    Method method
    Parameter param

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    DeleteMethodParameter(File file, Type type, Method method, Parameter param) {
        super(file)
        this.type = type
        this.method = method
        this.param = param
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())

        start = method.start - 1
        end = method.end - 1

        lines = ops.readLines()

        if (start == end) {
            text = lines[start]
        } else {
            text = lines[start..end].join("\n")
        }
    }

    @Override
    void buildContent() {
        def matcher = text =~ /\(.*\)/
        if (matcher.find()) {
            String content = matcher[0]
            def newContent
            if (content =~ /,\s+${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*\)/) {
                newContent = content.replaceAll(/,\s+${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*/, "")
            } else if (content =~ /\(\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*\)/) {
                newContent = content.replaceAll(/\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*/, "")
            } else if (content =~ /\(\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*,\s*/) {
                newContent = content.replaceAll(/\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*,\s*/, "")
            } else if (content =~ /,\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*,/) {
                newContent = content.replaceAll(/,\s*${param.getType().getTypeName()}(<\.+>)?\s+${param.getName()}\s*/, "")
            } else {
                newContent = content
            }
            text = text.replace(content, newContent)
        }
    }

    @Override
    void injectContent() {
        def textLines = text.split("\n")
        for (int i = 0; i < textLines.size(); i++) {
            lines[start + i] = textLines[i]
        }
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        method.removeParameter(param)
        method.updateKey()
        param.thaw()

        updateImports()
    }
}
