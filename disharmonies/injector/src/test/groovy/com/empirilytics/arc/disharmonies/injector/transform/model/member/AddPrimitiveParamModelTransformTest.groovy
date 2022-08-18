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
package com.empirilytics.arc.disharmonies.injector.transform.model.member

import com.empirilytics.arc.datamodel.Method
import com.empirilytics.arc.datamodel.Modifier
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddPrimitiveParamModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String name = "paramX"
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()

        // then
        the(method.getParameterByName(name)).shouldNotBeNull()
    }

    @Test
    void "test execute happy path with modifiers"() {
        // given
        String name = "paramX"
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type, Modifier.forName("final"))
        fixture.execute()

        // then
        the(((Method) method).getParameterByName(name)).shouldNotBeNull()
        the(((Method) method).getParameterByName(name).getModifiers()).shouldContain(Modifier.forName("final"))
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is null"() {
        // given
        String name = null
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is empty"() {
        // given
        String name = ""
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type is null"() {
        // given
        String name = "paramX"
        String type = null

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type is empty"() {
        // given
        String name = "paramX"
        String type = ""

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is same as existing param"() {
        // given
        String name = "x"
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(method, name, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException)
    void "test execute member is not a method"() {
        // given
        String name = "x"
        String type = "int"

        // when
        fixture = new AddPrimitiveParamModelTransform(field, name, type)
        fixture.execute()
    }
}
