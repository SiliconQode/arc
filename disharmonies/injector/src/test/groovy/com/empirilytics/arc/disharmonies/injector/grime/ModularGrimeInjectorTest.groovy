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
package com.empirilytics.arc.disharmonies.injector.grime

import com.empirilytics.arc.datamodel.PatternInstance
import com.empirilytics.arc.datamodel.Type
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ModularGrimeInjectorTest extends GrimeInjectorBaseTest {

    ModularGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new ModularGrimeInjector(inst, true, true, true)
    }

    @Test
    @Parameters([
            "true, false, false",
            "true, false, true",
            "true, true, false",
            "true, true, true",
            "false, false, false",
            "false, false, true",
            "false, true, false",
            "false, false, true"
    ])
    void inject(boolean persist, boolean extern, boolean effer) {
        // given
        fixture = new ModularGrimeInjector(inst, persist, extern, effer)

        // when
        try {
            fixture.inject()
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail()
        }

    }

    @Test
    void selectExternClass() {
        // given
        def types = [Type.findFirst("name = ?", "TypeF"),
                     Type.findFirst("name = ?", "TypeG"),
                     Type.findFirst("name = ?", "TypeH")]

        // when
        def result = fixture.selectOrCreateExternClass()

        // then
        the(types).shouldContain(result)
    }

    @Test
    void selectPatternClass() {
        // given
        PatternInstance inst = PatternInstance.findFirst("instKey = ?", "Builder01")

        // when
        def result = fixture.selectOrCreatePatternClass()

        // then
        the(inst.getTypes().contains(result)).shouldBeTrue()
    }

    @Test
    void select2PatternClasses() {
        // given
        PatternInstance inst = PatternInstance.findFirst("instKey = ?", "Builder01")

        // when
        def (result1, result2) = fixture.selectOrCreate2PatternClasses()

        // then
        the(result1).shouldNotBeEqual(result2)
        the(inst.getTypes().contains(result1)).shouldBeTrue()
        the(inst.getTypes().contains(result2)).shouldBeTrue()
    }

    @Test
    @Parameters([
            "TypeA, TypeB",
            "TypeD, TypeF",
    ])
    void selectPersistentRel(String src, String dest) {
        // given
        Type srcType = Type.findFirst("name = ?", src)
        Type destType = Type.findFirst("name = ?", dest)

        // when
        RelationType result = fixture.selectPersistentRel(srcType, destType)

        // then
        the([RelationType.GEN, RelationType.ASSOC]).shouldContain(result)
    }

    @Test
    @Parameters([
            "TypeA, TypeB",
            "TypeD, TypeF",
    ])
    void selectTemporaryRel(String src, String dest) {
        // given
        Type srcType = Type.findFirst("name = ?", src)
        Type destType = Type.findFirst("name = ?", dest)

        // when
        RelationType result = fixture.selectTemporaryRel(srcType, destType)

        // then
        the([RelationType.USE_PARAM, RelationType.USE_VAR, RelationType.USE_RET]).shouldContain(result)
    }
}
