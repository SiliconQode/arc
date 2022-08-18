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
package com.empirilytics.arc.disharmonies.injector.transform.model.file

import com.empirilytics.arc.datamodel.Accessibility
import com.empirilytics.arc.datamodel.Modifier
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddTypeModelTransformTest extends FileModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()

        // then
        the(file.getAllTypes().find { it.name == name }).shouldNotBeNull()
    }

    @Test
    void "test execute happy path modifiers"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type, Modifier.forName("final"))
        fixture.execute()

        // then
        the(file.getAllTypes().find { it.name == name }).shouldNotBeNull()
        the(fixture.type.getModifiers()).shouldContain(Modifier.forName("final"))
    }

    @Test
    void "test execute happy path interface"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = "interface"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()

        // then
        the(file.getAllTypes().find { it.name == name }).shouldNotBeNull()
    }

    @Test
    void "test execute happy path enum"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = "enum"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()

        // then
        the(file.getAllTypes().find { it.name == name }).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType name is null"() {
        // given
        String name = null
        Accessibility access = Accessibility.PUBLIC
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType name is empty"() {
        // given
        String name = ""
        Accessibility access = Accessibility.PUBLIC
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType access is null"() {
        // given
        String name = "TestX"
        Accessibility access = null
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType type is null"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = null

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType type is empty"() {
        // given
        String name = "TestX"
        Accessibility access = Accessibility.PUBLIC
        String type = ""

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test addType already has type with name"() {
        // given
        String name = "Test1"
        Accessibility access = Accessibility.PUBLIC
        String type = "class"

        // when
        fixture = new AddTypeModelTransform(file, name, access, type)
        fixture.execute()
    }
}
