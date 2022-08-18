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
package com.empirilytics.arc.disharmonies.injector.transform.model.type

import com.empirilytics.arc.datamodel.Accessibility
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddMethodModelTransformTest extends TypeModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String name = "methodX"
        Type methodType = Type.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PUBLIC

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()

        // then
        the(type.getMethods().find { it.name == name }).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is null"() {
        // given
        String name = null
        Type methodType = Type.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PUBLIC

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is empty"() {
        // given
        String name = ""
        Type methodType = Type.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PUBLIC

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type has method"() {
        // given
        String name = "methodZ"
        Type methodType = Type.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PUBLIC

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute method type is null"() {
        // given
        String name = "methodX"
        Type methodType = null
        Accessibility access = Accessibility.PUBLIC

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute access is null"() {
        // given
        String name = "methodX"
        Type methodType = Type.findFirst("name = ?", "TypeX")
        Accessibility access = null

        // when
        fixture = new AddMethodModelTransform(type, name, methodType, access)
        fixture.execute()
    }
}
