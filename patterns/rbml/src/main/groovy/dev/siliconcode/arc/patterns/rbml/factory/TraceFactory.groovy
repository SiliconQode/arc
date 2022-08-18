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

import dev.siliconcode.arc.patterns.rbml.model.AltFragment
import dev.siliconcode.arc.patterns.rbml.model.BreakFragment
import dev.siliconcode.arc.patterns.rbml.model.CriticalFragment
import dev.siliconcode.arc.patterns.rbml.model.GuardedTrace
import dev.siliconcode.arc.patterns.rbml.model.IPS
import dev.siliconcode.arc.patterns.rbml.model.OptFragment
import dev.siliconcode.arc.patterns.rbml.model.SeqFragment
import dev.siliconcode.arc.patterns.rbml.model.StrictFragment
import dev.siliconcode.arc.patterns.rbml.model.Trace
import dev.siliconcode.arc.patterns.rbml.model.LoopFragment
import dev.siliconcode.arc.patterns.rbml.model.ParFragment

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class TraceFactory extends AbstractFactory {

    boolean isLeaf() {
        return false
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        Trace trace

        if (value)
            trace = new GuardedTrace(guard: value)
        else
            trace = new Trace()

        trace
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent) {
            if (parent instanceof IPS) {
                parent.setTrace(child)
            }
            else if (parent instanceof AltFragment) {
                parent.guardedTraces << child
            }
            else if (parent instanceof BreakFragment) {
                parent.setTrace(child)
            }
            else if (parent instanceof CriticalFragment) {
                parent.setTrace(child)
            }
            else if (parent instanceof LoopFragment) {
                parent.setTrace(child)
            }
            else if (parent instanceof OptFragment) {
                parent.guardedTraces << child
            }
            else if (parent instanceof ParFragment) {
                parent.guardedTraces << child
            }
            else if (parent instanceof SeqFragment) {
                parent.guardedTraces << child
            }
            else if (parent instanceof StrictFragment) {
                parent.guardedTraces << child
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
    }
}
