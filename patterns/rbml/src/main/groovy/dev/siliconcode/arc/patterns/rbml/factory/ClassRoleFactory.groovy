/*
 * The MIT License (MIT)
 *
 * Empirilytics RBML DSL
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
package dev.siliconcode.arc.patterns.rbml.factory

import dev.siliconcode.arc.patterns.rbml.model.ClassRole
import dev.siliconcode.arc.patterns.rbml.model.GeneralizationHierarchy
import dev.siliconcode.arc.patterns.rbml.model.Multiplicity
import dev.siliconcode.arc.patterns.rbml.model.SPS

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ClassRoleFactory extends AbstractFactory {

    boolean isLeaf() {
        return false
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        ClassRole inst = new ClassRole()

        if (value) {
            inst.setName(value)
        }

        int lower = 1
        int upper = 1
        if (attributes) {
            if (attributes["mult"] != null) {
                String mult = attributes["mult"]
                mult = mult.replaceAll("\\*", "-1")
                if (mult.contains("..")) {
                    lower = new Integer(mult.split("\\.\\.")[0])
                    upper = new Integer(mult.split("\\.\\.")[1])
                }
            }
            if (attributes["root"])
                inst.root = attributes["root"]
            if (attributes["abstrct"])
                inst.abstrct = true
            else
                inst.abstrct = false
        }
        attributes.clear()
        inst.setMult(new Multiplicity(lower: lower, upper: upper))

        if (builder.parentNode && builder.parentNode instanceof SPS) {
            builder.parentNode.classifiers << inst
        }

        inst
    }

    @Override
    void setParent(FactoryBuilderSupport builder, parent, child) {
        if (parent && parent instanceof GeneralizationHierarchy) {
            if (child.root)
                parent.root = child
            else
                parent.children << child
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
    }
}
