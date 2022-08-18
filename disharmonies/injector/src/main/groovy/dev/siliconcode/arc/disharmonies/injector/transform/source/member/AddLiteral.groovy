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
import dev.siliconcode.arc.datamodel.Literal
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddLiteral extends BasicSourceTransform {

    Type type
    Literal literal

    int delta
    int loc
    List<String> textLines

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    AddLiteral(File file, Type type, Literal literal) {
        super(file)
        this.type = type
        this.literal = literal
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())

        start = type.getStart() - 1
        end = type.getEnd() - 1
        loc = start

        lines = ops.readLines()
        textLines

        if (start == end)
            textLines = [lines[start]]
        else
            textLines = lines[start..end]
    }

    @Override
    void buildContent() {
        if (type.getLiterals().isEmpty()) {
            // insert two lines after "{"
            if (textLines.size() == 1 && textLines[0].contains("{")) {
                textLines[i] = textLines[0].replace("{", "{\n\n    ${literal.getName()}")
                loc = start + 2
            } else {
                for (int i = 0; i < textLines.size(); i++) {
                    if (textLines[i].contains("{")) {
                        textLines[i] = textLines[i].replace("{", "{\n\n    ${literal.getName()}")
                        loc = start + i + 1
                    }
                }
            }

            for (int i = 0; i < textLines.size(); i++)
                lines[start + i] = textLines[i]
        }
        else {
            int ndx = 0
            String name = ""
            type.getLiterals().each {
                if (it.getStart() > ndx) {
                    ndx = it.getStart()
                    name = it.getName()
                }
            }

            def line = lines[ndx - 1]
            if (line.endsWith(";")) {
                if (line.contains(",")) {
                    line = line.replace(";", ", ${literal.getName()};")
                    loc = ndx
                } else {
                    line = line.replace(";", ",\n    ${literal.getName()};")
                    loc = ndx + 1
                }
            } else {
                if (line.contains(",")) {
                    line = "$line, ${literal.getName()}"
                    loc = ndx
                } else {
                    line = "$line,\n    ${literal.getName()}"
                    loc = ndx + 1
                }
            }
            lines[ndx - 1] = line
        }

        int oldLength = lines.size()
        lines = lines.join("\n").split("\n")
        int newLength = lines.size()
        delta = newLength - oldLength
    }

    @Override
    void injectContent() {
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        literal.setStart(loc)
        literal.setEnd(loc)
        updateAllFollowing(loc, delta)
        type.setEnd(type.getEnd() + delta)
        type.addMember(literal)
        literal.updateKey()
    }
}
