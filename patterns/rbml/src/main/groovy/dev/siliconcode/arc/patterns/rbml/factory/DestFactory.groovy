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

import dev.siliconcode.arc.patterns.rbml.PatternManager
import dev.siliconcode.arc.patterns.rbml.events.DestTypeResolution
import dev.siliconcode.arc.patterns.rbml.model.Association
import dev.siliconcode.arc.patterns.rbml.model.Multiplicity

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DestFactory extends AbstractFactory {

    boolean isLeaf() {
        return false
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        Association assoc = null

        if (builder.current != null && builder.current instanceof Association) {
            assoc = (Association) builder.current

            int lower = 1
            int upper = 1
            if (attributes["mult"] != null) {
                String mult = attributes["mult"]
                mult = mult.replaceAll("\\*", "-1")
                if (mult.contains("..")) {
                    lower = new Integer(mult.split("\\.\\.")[0])
                    upper = new Integer(mult.split("\\.\\.")[1])
                }
            }
            if (attributes["role"] != null)
                PatternManager.instance.events << new DestTypeResolution(role: assoc, type: attributes["role"])
            assoc.setDestMult(new Multiplicity(lower: lower, upper: upper))
            assoc.setDestName(value?.toString())
        }
        attributes.clear()

        assoc
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
    }
}
