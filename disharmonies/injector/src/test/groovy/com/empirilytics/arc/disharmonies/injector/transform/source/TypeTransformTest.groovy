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
package com.empirilytics.arc.disharmonies.injector.transform.source

import com.empirilytics.arc.datamodel.*
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import com.empirilytics.arc.disharmonies.injector.transform.source.type.RenameType
import org.junit.Test

class TypeTransformTest extends BaseSourceTransformSpec {

    @Test
    void "kind for class"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.CLASS).name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("class")
    }

    @Test
    void "kind for interface"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.INTERFACE).name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("interface")
    }

    @Test
    void "kind for enum"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.ENUM).name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("enum")
    }

    @Test
    void "kind for other"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.UNKNOWN).name("test").create()
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("")
    }
}
