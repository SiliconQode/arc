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
package com.empirilytics.arc.disharmonies.injector.transform.source.structural

import com.empirilytics.arc.datamodel.Project
import com.empirilytics.arc.datamodel.System
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class MoveProjectTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        Project proj = Project.findFirst("name = ?", "testproj")
        System from = System.findFirst("name = ?", "testdata")
        System to = System.findFirst("name = ?", "testdata2")
        MoveProject fixture = new MoveProject(proj, from, to)
        File locOld = new File("testdata/testproj")
        File locNew = new File("testdata2/testproj")

        // when
        to.addProject(proj)
        from.removeProject(proj)
        proj.thaw()
        proj.updateKeys()
        fixture.execute()

        // then
        the(locOld.exists()).shouldBeFalse()
        the(locNew.exists()).shouldBeTrue()
    }
}
