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

import dev.siliconcode.arc.patterns.rbml.model.Gate
import dev.siliconcode.arc.patterns.rbml.model.Lifeline
import dev.siliconcode.arc.patterns.rbml.model.Trace
import dev.siliconcode.arc.patterns.rbml.model.UnknownLifeline
import dev.siliconcode.arc.patterns.rbml.PatternManager
import dev.siliconcode.arc.patterns.rbml.model.CreateMessage

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CreateMessageFactory extends AbstractFactory {

    boolean isLeaf() {
        return true
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        CreateMessage msg = new CreateMessage()

        msg.setName(value)

        if (attributes) {
            if (!attributes['source'])
                msg.setSource(new Gate())
            else {
                if (PatternManager.current != null) {
                    Lifeline src = PatternManager.findLifeline(attributes["source"])
                    if (src)
                        msg.setSource(src)
                    else
                        msg.setSource(new UnknownLifeline(name: attributes["source"]))
                }
            }

            if (attributes['dest']) {
                if (PatternManager.current != null) {
                    Lifeline dest = PatternManager.findLifeline(attributes["dest"])
                    if (dest)
                        msg.setDest(dest)
                    else
                        msg.setDest(new UnknownLifeline(name: attributes["dest"]))
                } else {
                    msg.setDest(new UnknownLifeline(name: attributes["dest"]))
                }
            }
        }

        attributes.clear()

        msg
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent && parent instanceof Trace) {
            parent.fragments << child
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
    }
}
