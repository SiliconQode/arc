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

import spock.lang.Specification


class MultiplicitySpec extends Specification {

    def "single value range"() {
        given:
            Multiplicity m = new Multiplicity(5)

        expect:
            m.lower == 5
            m.upper == 5
    }

    def "range is 1..1"() {
        given:
            Multiplicity m = new Multiplicity(1, 1)

        expect:
            m.lower == 1
            m.upper == 1
    }

    def "range is 1..*"() {
        given:
            Multiplicity m = new Multiplicity(1, -1)

        expect:
            m.lower == 1
            m.upper == -1
    }

    def "fromString '1..1'"() {
        given:
            Multiplicity m = Multiplicity.fromString("1..1")

        expect:
            m.lower == 1
            m.upper == 1
    }

    def "fromString '1..*'"() {
        given:
            Multiplicity m = Multiplicity.fromString("1..*")

        expect:
            m.lower == 1
            m.upper == -1
    }

    def "invalid range 5..1"() {
        given:
            Multiplicity m = new Multiplicity(5, 1)

        expect:
            m.lower == 1
            m.upper == 5
    }

    def "invalid range from string '5..1'"() {
        given:
            Multiplicity m = Multiplicity.fromString("5..1")

        expect:
            m.lower == 1
            m.upper == 5
    }

    def "null input to Multiplicity.fromString"() {
        given:
            Multiplicity m

        when:
            m = Multiplicity.fromString(null)

        then:
            thrown IllegalArgumentException
    }
}
