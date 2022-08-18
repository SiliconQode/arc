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
package dev.siliconcode.arc.disharmonies.injector.transform.source

import dev.siliconcode.arc.datamodel.*

/**
 * Base transform class for those transforms which add a member to a type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AddMember extends BasicSourceTransform {

    /**
     * Constructs a new AddMember transform
     * @param file the file which will be modified
     */
    AddMember(File file) {
        super(file)
    }

    /**
     * Identifies the insertion point for a new method
     * @param type Type to be modified
     * @return line number where a new method can be inserted
     */
    int findMethodInsertionPoint(Type type) {
        int line = 0

//        if (type.getMethods().isEmpty()) {
//            if (type.getFields().isEmpty()) {
//                if (type.getLiterals().isEmpty()) {
//                    line = type.getStart()
//                } else {
//                    type.getLiterals().each {
//                        if (it.getEnd() > line)
//                            line = it.getEnd()
//                    }
//                }
//            } else {
//                type.getFields().each() {
//                    if (it.getEnd() > line)
//                        line = it.getEnd()
//                }
//            }
//            if (type.getFields()) {
//                line = type.getFields().max {it.refresh(); it.getEnd() }.getEnd()
//            }
//            else if (!type.getFields() && type.getLiterals()) {
//                line = type.getLiterals().max { it.getEnd() }.getEnd() - 1
//            }
//        } else {
//        if (!type.getMethods().isEmpty()) {
//            line = type.getMethods().max {it.refresh(); it.getEnd() }.getEnd()
//        }

//        println "Type End: ${type.getEnd()}"
        type.refresh()
        if (line == 0)
            line = type.getEnd() - 1
//        println "Insertion Point: $line"
        if (line >= lines.size())
            line = lines.size() - 1
//        println "Revised Insertion Point: $line"

        return line
    }

    /**
     * Identifies the insertion point for a new field
     * @param type Type to be modified
     * @return line number where a new field can be inserted
     */
    int findFieldInsertionPoint(Type type) {
        int line = -1
//        int mstart = Integer.MAX_VALUE
//
        if (type.getFields().size() > 0) {
            for (Field field : (List<Field>) type.getFields()) {
                if (field.getEnd() > line)
                    line = field.getEnd()
            }
        }
//        } else {
//            if (type.getMethods().size() > 0) {
//                for (Method method : (List<Method>) type.getMethods()) {
//                    if (method.getStart() < mstart)
//                        mstart = method.getStart()
//                }
//            }
//        }
//
//        if (line < 0 && mstart == Integer.MAX_VALUE)
        if (line < 0)
            line = type.getStart() + 1
//        if (line < mstart && mstart == Integer.MAX_VALUE)
//            return line + 1
//        if (line < mstart && mstart < Integer.MAX_VALUE)
//            return mstart - 1
//
        return line
    }

    /**
     * Identifies the insertion point for a new enum literal
     * @param Enum The Enum to be modified
     * @return line number where a new enum literal can be inserted
     */
    int findEnumItemInsertionPoint(Type Enum) {
        return 0
    }

    /**
     * Identifies the insertion point for a new constructor
     * @param type Type to be modified
     * @return line number where a new constructor can be inserted
     */
    int findConstructorInsertionPoint(Type type) {
        int line

        if (!type.getMethods().isEmpty()) {
            if (!type.getConstructors().isEmpty()) {
                int lastLine = 0
                type.getConstructors().each { Constructor c ->
                    if (c.end >= lastLine)
                        lastLine = c.end
                }
                line = lastLine
            } else {
                int lastLine = type.end
                type.getMethods().each { Method m ->
                    if (m.start <= lastLine)
                        lastLine = m.start
                }
                line = lastLine
            }
        } else {
            if (!type.getFields().isEmpty()) {
                int lastLine = 0
                type.getFields().each { Field fld ->
                    if (fld.end >= lastLine)
                        lastLine = fld.end
                }
                line = lastLine
            } else {
                line = type.end
            }
        }

        line
    }
}
