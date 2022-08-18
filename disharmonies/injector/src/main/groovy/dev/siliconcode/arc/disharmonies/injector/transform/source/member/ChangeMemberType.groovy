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
import dev.siliconcode.arc.datamodel.TypeRef
import dev.siliconcode.arc.datamodel.TypedMember
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ChangeMemberType extends BasicSourceTransform {

    TypeRef retType
    TypedMember member

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    @Builder(buildMethodName = "create")
    ChangeMemberType(File file, TypedMember member, TypeRef retType) {
        super(file)
        this.member = member
        this.retType = retType
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        start = member.getStart() - 1
        end = member.getEnd() - 1

        if (start == end)
            text = lines[start]
        else
            text = lines[start..end].join("\n")
    }

    @Override
    void buildContent() {
        def matcher = text =~ /\s+(\w+\s+)*${member.getType().getTypeName()}/
        if (matcher.find()) {
            String content = matcher[0][0]
            def newContent = content.replace(member.getType().getTypeName(), retType.getTypeName())
            text = text.replace(content, newContent)
        }
    }

    @Override
    void injectContent() {
        def textLines = text.split("\n")
        for (int i = 0; i < textLines.size(); i++)
            lines[start + i] = textLines[i]

        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        member.setType(retType)

        updateImports()
    }
}
