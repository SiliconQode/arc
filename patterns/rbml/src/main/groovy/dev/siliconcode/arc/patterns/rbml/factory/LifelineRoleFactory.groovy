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

import dev.siliconcode.arc.patterns.rbml.model.Classifier
import dev.siliconcode.arc.patterns.rbml.model.IPS
import dev.siliconcode.arc.patterns.rbml.model.Lifeline
import dev.siliconcode.arc.patterns.rbml.model.Multiplicity
import dev.siliconcode.arc.patterns.rbml.PatternManager
import dev.siliconcode.arc.patterns.rbml.model.UnknownType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class LifelineRoleFactory extends AbstractFactory {

    boolean isLeaf() {
        return true
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        Lifeline inst = new Lifeline()

        if (value) {
            inst.setName(value.split(":")[0])
            String type = value.split(":")[1]
            if (PatternManager.current != null) {
                Classifier src = PatternManager.findClassifier(attributes["source"])
                if (src)
                    inst.setType(src)
                else
                    inst.setType(new UnknownType(name: type))
            } else {
                inst.setType(new UnknownType(name: type))
            }
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
        }
        attributes.clear()
        inst.setMult(new Multiplicity(lower: lower, upper: upper))

        if (builder.parentNode != null && builder.parentNode instanceof IPS) {
            builder.parentNode.lines << inst
        }

        inst
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
    }
}