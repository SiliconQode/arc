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

import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddAssociationModelTransformTest extends TypeModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Type assoc = Type.findFirst("name = ?", "TypeY")
        String fromName = "from"
        String toName = "to"
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()

        // then
        the(type.getAssociatedTo()).shouldContain(assoc)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type is null"() {
        // given
        Type assoc = null
        String fromName = "from"
        String toName = "to"
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute toName is null"() {
        // given
        Type assoc = Type.findFirst("name = ?", "TypeY")
        String fromName = "from"
        String toName = null
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute toName is empty"() {
        // given
        Type assoc = Type.findFirst("name = ?", "TypeY")
        String fromName = "from"
        String toName = ""
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute fromName is null"() {
        // given
        Type assoc = Type.findFirst("name = ?", "TypeY")
        String fromName = null
        String toName = "to"
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute fromName is empty"() {
        // given
        Type assoc = Type.findFirst("name = ?", "TypeY")
        String fromName = ""
        String toName = "to"
        boolean bidirect = true

        // when
        fixture = new AddAssociationModelTransform(type, assoc, fromName, toName, bidirect)
        fixture.execute()
    }
}
