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
package com.empirilytics.arc.disharmonies.injector.transform.model.system

import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test;

class AddProjectModelTransformTest extends SystemModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String name = "ProjectX"
        String version = "1.0"

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()

        // then
        the(sys.getProjects().find { it.name == name && it.version == version }).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with null name"() {
        // given
        String name = null
        String version = "1.0"

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with empty name"() {
        // given
        String name = ""
        String version = "1.0"

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with null version"() {
        // given
        String name = "ProjectX"
        String version = null

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with empty version"() {
        // given
        String name = "ProjectX"
        String version = ""

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute with same name and version"() {
        // given
        String name = "testproj"
        String version = "1.0"

        // when
        fixture = new AddProjectModelTransform(sys, name, version)
        fixture.execute()
    }

}
