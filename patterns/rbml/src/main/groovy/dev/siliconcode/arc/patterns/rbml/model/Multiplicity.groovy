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
package dev.siliconcode.arc.patterns.rbml.model

import groovy.transform.EqualsAndHashCode

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@EqualsAndHashCode
class Multiplicity {

    int lower
    int upper

    Multiplicity(int lower, int upper) {
        this.lower = lower
        this.upper = upper

        if (lower > upper && upper != -1)
        {
            this.upper = lower
            this.lower = upper
        }
    }

    Multiplicity(int lower) {
        this(lower, lower)
    }

    static Multiplicity fromString(String val) {
        if (val == null)
            throw new IllegalArgumentException()
        int lower, upper
        if (val.contains("..")) {
            def (l, u) = val.split(/\.\./)
            if (l == "*")
                lower = -1
            else
                lower = Integer.parseInt(l)
            if (u == "*")
                upper = -1
            else
                upper = Integer.parseInt(u)

            new Multiplicity(lower, upper)
        }
        else {
            if (val == "*")
                lower = -1
            else
                lower = Integer.parseInt(val)

            new Multiplicity(lower)
        }
    }

    @Override
    String toString() {
        if (upper >= 0)
            "$lower..$upper"
        else
            "$lower..*"
    }

    def toRange() {
        if (upper < 0)
            return (lower..Integer.MAX_VALUE - 1)
        else
            return (lower..upper)
    }

    boolean inRange(int count) {
        toRange().contains(count)
    }
}
