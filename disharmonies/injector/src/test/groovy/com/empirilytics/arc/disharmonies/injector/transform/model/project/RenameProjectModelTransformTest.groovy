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

import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class RenameProjectModelTransformTest extends ProjectModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String newName = "projX"

        // when
        fixture = new RenameProjectModelTransform(proj, newName, sys.getKey())
        fixture.execute()

        // then
        the(proj.name).shouldEqual(newName)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute empty new name"() {
        // given
        String newName = ""

        // when
        fixture = new RenameProjectModelTransform(proj, newName, sys.getKey())
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute null new name"() {
        // given
        String newName = null

        // when
        fixture = new RenameProjectModelTransform(proj, newName, sys.getKey())
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with same name"() {
        // given
        String newName = "testproj"

        // when
        fixture = new RenameProjectModelTransform(proj, newName, sys.getKey())
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with empty sys key"() {
        // given
        String newName = "projX"

        // when
        fixture = new RenameProjectModelTransform(proj, newName, "")
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with null sys key"() {
        // given
        String newName = "projX"

        // when
        fixture = new RenameProjectModelTransform(proj, newName, null)
        fixture.execute()
    }
}
