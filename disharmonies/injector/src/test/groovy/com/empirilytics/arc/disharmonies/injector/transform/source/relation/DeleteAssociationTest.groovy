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
package com.empirilytics.arc.disharmonies.injector.transform.source.relation

import com.empirilytics.arc.datamodel.Field
import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.Test

class DeleteAssociationTest extends BaseSourceTransformSpec {

    @Test
    void "test execute bidirectional"() {
        // given
        File fromFile = File.findFirst("name = ?", "Test14.java")
        File toFile = File.findFirst("name = ?", "Test15.java")
        Type from = Type.findFirst("name = ?", "Test14")
        Type to = Type.findFirst("name = ?", "Test15")
        DeleteAssociation fixture = new DeleteAssociation(fromFile, from, toFile, to)
        java.io.File toActual = new java.io.File(toFile.getFullPath())
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())

        // when
        fixture.execute()

        // then
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(fromActual.text, """\
package test.test;

import java.util.*;

public class Test14 {


}""")
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(toActual.text, """\
package test.test;

import java.util.*;

public class Test15 {


}""")
        the(to.getAssociatedTo()).shouldNotContain(from)
        the(from.getAssociatedTo()).shouldNotContain(to)
    }

    @Test
    void "test execute unidirectional from to to"() {
        // given
        File fromFile = File.findFirst("name = ?", "Test14.java")
        File toFile = File.findFirst("name = ?", "Test15.java")
        Type from = Type.findFirst("name = ?", "Test14")
        Type to = Type.findFirst("name = ?", "Test15")
        makeUniDirectional(from, to)
        DeleteAssociation fixture = new DeleteAssociation(fromFile, from, toFile, to)
        java.io.File toActual = new java.io.File(toFile.getFullPath())
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())

        // when
        fixture.execute()

        // then
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(fromActual.text, """\
package test.test;

import java.util.*;

public class Test14 {


}""")
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(toActual.text, """\
package test.test;

import java.util.*;

public class Test15 {

    private Test14 test14;

}
""")
        the(to.getAssociatedTo()).shouldNotContain(from)
        the(from.getAssociatedTo()).shouldNotContain(to)
    }

    @Test
    void "test execute unidirectional to to from"() {
        // given
        File fromFile = File.findFirst("name = ?", "Test14.java")
        File toFile = File.findFirst("name = ?", "Test15.java")
        Type from = Type.findFirst("name = ?", "Test14")
        Type to = Type.findFirst("name = ?", "Test15")
        makeUniDirectional(to, from)
        DeleteAssociation fixture = new DeleteAssociation(fromFile, from, toFile, to)
        java.io.File toActual = new java.io.File(toFile.getFullPath())
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())

        // when
        fixture.execute()

        // then
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(fromActual.text, """\
package test.test;

import java.util.*;

public class Test14 {

    private Test15 test15;

}
""")
        //the(toActual.text).shouldEqual("""\
        Assert.assertEquals(toActual.text, """\
package test.test;

import java.util.*;

public class Test15 {


}""")
        the(to.getAssociatedTo()).shouldNotContain(from)
        the(from.getAssociatedTo()).shouldNotContain(to)
    }

    def makeUniDirectional(Type source, Type dest) {
        Field toRemove = dest.getFields().find {
            it.getType().getTypeFullName() == source.getFullName()
        }

        dest.removeMember(toRemove)
        dest.removeAssociatedTo(source)
    }
}
