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
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.TypeTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveType extends TypeTransform {

    File to

    int newStart
    java.io.File toFile
    List<String> toLines

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveType(Type type, File from, File to) {
        super(from, type)
        this.type = type
        this.to = to
    }

    @Override
    void setup() {
        toFile = new java.io.File(to.getFullPath())
        ops = new java.io.File(file.getFullPath())

        start = type.getStart() - 1
        end = type.getEnd() - 1
        text = ops.readLines()[start..end].join("\n")
        lines = text.split("\n")

        // get location of where to add to in new file
        newStart = findTypeInsertionPoint(to)
        toLines = toFile.readLines().asList()
    }

    @Override
    void buildContent() {
        toLines.add(newStart, "")
    }

    @Override
    void injectContent() {
        type.setStart(1)
        type.setEnd(1)
        file.removeType(type)
        deleteFromOldLocation()

        to.addType(type)
        type.updateKey()
        injectInNewLocation()
    }
    void deleteFromOldLocation() {
        // delete from from file
        ops.text = ops.text.replace(text, "").stripTrailing()
    }
    void injectInNewLocation() {
        // add content to new file
        newStart++
        for (int i = 0; i < lines.size(); i++) {
            toLines.add(newStart + i, lines[i])
        }
        toFile.text = toLines.join("\n")
    }

    @Override
    void updateModel() {
        int delta = lines.size()
        // update start and end of type and new file
        type.setStart(newStart)
        type.setEnd(newStart + delta)
        // update end of old file
        // updated all following in both files
        updateAllFollowing(file, start, -1 * delta)
        updateAllFollowing(to, newStart, delta)
//        updateMembers() // update contained members from oldbase (start) to new base (newStart)
        // update imports in both files
        updateImports(file)
        updateImports(to)
    }

    private int findTypeInsertionPoint(File file) {
        if (!file.getAllTypes().isEmpty())
            file.getAllTypes().max { it.getEnd() }.getEnd()
        else
            return file.getEnd() - 1
    }

    private void updateMembers() {
        type.getAllMembers().each {
            int os = it.getStart()
            int oe = it.getEnd()
            int delta = os - start
            it.setStart(newStart + delta)
            it.setEnd(newStart + delta + (oe - os))
        }
    }
}
