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

import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypeRefType
import dev.siliconcode.arc.disharmonies.injector.transform.source.AddMember
import groovy.transform.builder.Builder

/**
 * Transform to add a new Type to a given file
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddField extends AddMember {

    /**
     * Type to which the field will be added
     */
    Type type
    /**
     * Field to add
     */
    Field field
    String init
    int delta

    /**
     * Constructs a new Add Field transform
     * @param file File to be modified
     * @param type Type to add field to
     * @param field Field to be added
     */
    @Builder(buildMethodName = "create")
    AddField(File file, Type type, Field field, String init = "") {
        super(file)
        this.type = type
        this.field = field
        this.init = init
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        // 1. find line of last field in type
        start = findFieldInsertionPoint(type)
//        if (type.getFields().size() >= 1)
//            start = start - 1
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        // 2. add field to content
        String content

        start = start - 1
        if (type.getFields().size() >= 1) {
            content = String.format("    %s%s%s %s%s", getAccessibility(), getModifierString(), getTypeString(), getName(), getInit())
            delta = 1
        } else {
            if (type.getMethods().size() > 0)
                content = String.format("\n    %s%s%s %s%s", getAccessibility(), getModifierString(), getTypeString(), getName(), getInit())
            else
                content = String.format("\n    %s%s%s %s%s", getAccessibility(), getModifierString(), getTypeString(), getName(), getInit())
            delta = 2
        }
        lines.add(start, content)

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        // 3. update field start and end
        field.start = start + delta
        field.end = start + delta
        updateContainingAndAllFollowing(start + delta, delta)

        // 4. Add field to type
        type.addMember(field)
        field.updateKey()

        // 6. check if an import is needed
        updateImports()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        // 5. update all following items by the length of the insert
        lines = lines.join("\n").split("\n")

        // 7. update file content
        ops.text = lines.join("\n")
    }

    /**
     * @return String representation of the Accessibility
     */
    private String getAccessibility() {
        if (field.accessibility != Accessibility.DEFAULT)
            return "${field.getAccessibility().toString().toLowerCase()} "
        return ""
    }

    /**
     * @return String representation of the modifiers for the field
     */
    private String getModifierString() {
        StringBuilder builder = new StringBuilder()
        field.modifiers.each {
            builder << "${it.getName()} "
        }

        builder.toString()
    }

    /**
     * @return String representation of the Type of the field
     */
    private String getTypeString() {
        field.getType().getTypeName()
    }

    /**
     * @return Name of the field
     */
    private String getName() {
        field.getName()
    }

    private String getInit() {
        if (init)
            return " = $init;"
        else
            return ";"
    }
}
