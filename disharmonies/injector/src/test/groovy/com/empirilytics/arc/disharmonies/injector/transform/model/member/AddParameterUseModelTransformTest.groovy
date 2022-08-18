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
import com.empirilytics.arc.datamodel.Parameter
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.datamodel.TypeRef
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddParameterUseModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Type use = Type.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()

        // then
        the(method.getParentType().hasUseTo(use)).shouldBeTrue()
        the(((Method) method).getParameterByName(use.getName().uncapitalize())).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is null"() {
        // given
        method = null
        Type use = Type.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type used is null"() {
        // given
        Type use = null
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is not a method"() {
        // given
        Type use = Type.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(field, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute method already has param with name"() {
        // given
        method.addParameter(Parameter.builder().name("typeZ").type(TypeRef.createPrimitiveTypeRef("int")).create())
        Type use = Type.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }
}
