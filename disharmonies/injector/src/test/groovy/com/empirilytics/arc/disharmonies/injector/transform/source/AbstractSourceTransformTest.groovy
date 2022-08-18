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

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Import
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import com.empirilytics.arc.disharmonies.injector.transform.source.structural.RenameFile
import org.junit.Test

class AbstractSourceTransformTest extends BaseSourceTransformSpec {

    @Test
    void updateAllFollowing() {
        // given
        File file = File.builder().name("test").start(1).end(10).create()
        Import imp1 = Import.builder().name("import0").start(1).end(2).create()
        Import imp2 = Import.builder().name("import1").start(3).end(3).create()
        Type type = Type.builder().type(Type.CLASS).name("test").start(4).end(10).create()
        file.addType(type)
        file.addImport(imp1)
        file.addImport(imp2)

        int line = 2
        int length = 3

        AbstractSourceTransform fixture = new RenameFile(file, "test2")

        // when
        fixture.updateAllFollowing(file, line, length)
        file.refresh()
        imp1.refresh()
        imp2.refresh()
        type.refresh()

        // then
        the(file.getEnd()).shouldBeEqual(13)
        the(type.getStart()).shouldBeEqual(7)
        the(type.getEnd()).shouldBeEqual(13)
    }

    @Test
    void updateContainingAndAllFollowing() {
        // given
        File file = File.builder().name("test").start(1).end(10).create()
        Import imp1 = Import.builder().name("import0").start(1).end(2).create()
        Import imp2 = Import.builder().name("import1").start(3).end(3).create()
        Type type = Type.builder().type(Type.CLASS).name("test").start(4).end(10).create()
        file.addType(type)
        file.addImport(imp1)
        file.addImport(imp2)

        int line = 2
        int length = 3

        AbstractSourceTransform fixture = new RenameFile(file, "test2")

        // when
        fixture.updateContainingAndAllFollowing(line, length)
        file.refresh()
        imp1.refresh()
        imp2.refresh()
        type.refresh()

        // then
        the(file.getEnd()).shouldBeEqual(13)
        the(type.getStart()).shouldBeEqual(7)
        the(type.getEnd()).shouldBeEqual(13)
    }
}
