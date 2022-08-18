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
package com.empirilytics.arc.disharmonies.injector.transform.source.member

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Literal
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class DeleteLiteralTest extends BaseSourceTransformSpec {

    @Test
    void "test execute last literal"() {
        // given
        Literal literal = Literal.findFirst("name = ?", "LITERAL3")
        Type type = Type.findFirst("name = ?", "Test3")
        File file = File.findFirst("name = ?", "Test3.java")
        DeleteLiteral fixture = new DeleteLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(literal)
        literal.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
public enum Test3 {

    LITERAL1,
    LITERAL2;
}""")
    }

    @Test
    void "test execute middle literal"() {
        // given
        Literal literal = Literal.findFirst("name = ?", "LITERAL2")
        Type type = Type.findFirst("name = ?", "Test3")
        File file = File.findFirst("name = ?", "Test3.java")
        DeleteLiteral fixture = new DeleteLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(literal)
        literal.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
public enum Test3 {

    LITERAL1,
    LITERAL3;
}""")
    }

    @Test
    void "test execute single line literal"() {
        // given
        Literal literal = Literal.findFirst("name = ?", "LITERAL5")
        Type type = Type.findFirst("name = ?", "Test8")
        File file = File.findFirst("name = ?", "Test8.java")
        DeleteLiteral fixture = new DeleteLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(literal)
        literal.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public class Test8 {

    LITERAL4, LITERAL6;
}""")
    }

    @Test
    void "test execute single line literal end"() {
        // given
        Literal literal = Literal.findFirst("name = ?", "LITERAL6")
        Type type = Type.findFirst("name = ?", "Test8")
        File file = File.findFirst("name = ?", "Test8.java")
        DeleteLiteral fixture = new DeleteLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        type.removeMember(literal)
        literal.thaw()
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public class Test8 {

    LITERAL4, LITERAL5;
}""")
    }
}
