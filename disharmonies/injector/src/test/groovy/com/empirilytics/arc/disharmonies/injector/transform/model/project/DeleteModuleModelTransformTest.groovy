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
package com.empirilytics.arc.disharmonies.injector.transform.model.project

import com.empirilytics.arc.datamodel.Module
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class DeleteModuleModelTransformTest extends ProjectModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Module mod = Module.findFirst("name = ?", "testmod")

        // when
        fixture = new DeleteModuleModelTransform(proj, mod)
        fixture.execute()

        // then
        the(proj.getModules()).shouldNotContain(mod)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute mod is null"() {
        // given
        Module mod = null

        // when
        fixture = new DeleteModuleModelTransform(proj, mod)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute mod not in proj"() {
        // given
        Module mod = Module.builder().name("testmod3").relPath("testmod3").srcPath("src/main/java").create()

        // when
        fixture = new DeleteModuleModelTransform(proj, mod)
        fixture.execute()
    }
}
