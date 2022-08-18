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
import dev.siliconcode.arc.disharmonies.injector.transform.source.AddMember

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveField extends AddMember {

    Type from
    Type to
    Field field
    File toFile

    int current
    int toOriginal
    int toCurrent
    java.io.File toOps
    int insert
    int original
    def newLine
    def toLines

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveField(File file, Type from, File toFile, Type to, Field field) {
        super(file)
        this.from = from
        this.to = to
        this.field = field
        this.toFile = toFile
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        toOps = new java.io.File(toFile.getFullPath())

        insert = findFieldInsertionPoint(to) - 1
        start = field.start - 1
        end = field.end - 1
        lines = ops.readLines()
        original = lines.size()
        toLines = toOps.readLines()

        if (start != end) {
            text = lines[start..end].join("\n")
        }
        else {
            text = lines[start]
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        (newLine, text) = extractContent(text)
        transformFromContent()
        transformToContent()
    }

    private void transformFromContent() {
        (end - start + 1).times {
            lines.remove(start)
        }

        if (lines[start].trim().isEmpty())
            lines.remove(start)

        if (text)
            lines.add(start, text)

        current = lines.size()
    }

    private void transformToContent() {
        toOriginal = toLines.size()
        toLines.add(insert, newLine)
//        if (to.getFields().isEmpty()) {
//            toLines.add(insert + 1, "")
//        }
        toCurrent = toLines.size()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        ops.text = lines.join("\n")
        toOps.text = toLines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        from.removeMember(field)
        field.thaw()
        updateContainingAndAllFollowing(start, current - original)
        updateImports(file)

        to.addMember(field)
        updateContainingAndAllFollowing(insert, toCurrent - toOriginal, this.toFile) // FIXME
        updateImports(this.toFile) // FIXME
    }

    private String createNewLine(String content) {
        StringBuilder builder = new StringBuilder()
        def access = field.getAccessibility().toString()
        if (access)
            builder << "    ${access} "
        else
            builder << "    "
        field.getModifiers().each {
            builder << "${it.getName()} "
        }
        builder << field.getType().getTypeName()
        builder << " "
        builder << content
        builder << ";"
        builder.toString()
    }

    private String[] extractContent(String content) {
        if (content ==~ /^\s*(\w+\s+)*(\w+(<.+>)?)(\s+\w+(\s*=\s*.+)?)(\s*,\s*\w+(\s*=\s*.+)?)*;\s*$/) {
            def loc = content.substring(content.indexOf("${field.getName()}"))
            def toMove
            if (loc.contains(",")) {
                toMove = loc.substring(0, loc.indexOf(","))
            } else {
                toMove = loc.substring(0, loc.indexOf(";"))
            }

            toMove = toMove.replaceAll("\n", " ")
            def newLine = createNewLine(toMove)
            content = content.replace(toMove, "")
            if (content =~ /,\s*,/)
                content = content.replaceAll(/,\s*,/, ",")
            if (content =~ /,\s*;/)
                content = content.replaceAll(/,\s*;/, ";")
            if (content =~ /${field.getType().getTypeName()}\s+;/)
                content = ""

            return [newLine, content]
        }

        ["", content]
    }
}
