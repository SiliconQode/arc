/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen.cue

import dev.siliconcode.arc.datamodel.Method
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.javalite.activejdbc.test.DBSpec
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class MethodCueTest  extends DBSpec {

    MethodCue fixture
    Method m

    @Before
    void setUp() {
        fixture = new MethodCue(name: "aMethod")
        m = Method.builder().name("Test").compKey("Test").create()
    }

    @Test
    void "GetCueForRole known rolename"() {
        // Given
        String roleName = "aMethod"

        // When
        Cue actual = fixture.getCueForRole(roleName, m)

        // Then
        the(actual).shouldBeEqual(fixture)
    }

    @Test
    void "GetCueForRole unknown rolename"() {
        // Given
        String roleName = "Unknown"

        // When
        Cue actual = fixture.getCueForRole(roleName, m)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole null rolename"() {
        // Given
        String roleName = null

        // When
        Cue actual = fixture.getCueForRole(roleName, m)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole empty rolename"() {
        // Given
        String roleName = ""

        // When
        Cue actual = fixture.getCueForRole(roleName, m)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole null component"() {
        // Given
        String roleName = "aMethod"

        // When
        Cue actual = fixture.getCueForRole(roleName, null)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    @Parameters([
            "aMethod, true",
            "Unknown, false",
            ", false"
    ])
    void "HasCueForRole"(String roleName, boolean expected) {
        // When
        boolean actual = fixture.hasCueForRole(roleName, m)

        // Then
        the(actual).shouldBeEqual(expected)
    }
}
