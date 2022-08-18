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

class AddLiteralTest extends BaseSourceTransformSpec {

    @Test
    void "test execute with empty enum not inline"() {
        // given
        adjustToEmpty(false)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    TEST

}""")
    }

    @Test
    void "test execute with empty enum inline"() {
        // given
        adjustToEmpty(false)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    TEST

}""")
    }

    @Test
    void "test execute with enum inline with semi"() {
        // given
        adjustInLine(true)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    LITERAL4, LITERAL5, LITERAL6, TEST;
}""")
    }

    @Test
    void "test execute with enum inline without semi"() {
        // given
        adjustInLine(false)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    LITERAL4, LITERAL5, LITERAL6, TEST
}""")
    }

    @Test
    void "test execute with enum multiline with semi"() {
        // given
        adjustMultiLine(true)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    LITERAL4,
    LITERAL5,
    LITERAL6,
    TEST;
}""")
    }

    @Test
    void "test execute with enum multiline without semi"() {
        // given
        adjustMultiLine(false)
        File file = File.findFirst("name = ?", "Test8.java")
        Type type = Type.findFirst("name = ?", "Test8")
        Literal literal = Literal.builder().name("TEST").compKey("TEST").create()
        AddLiteral fixture = new AddLiteral(file, type, literal)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
import java.util.*;

public enum Test8 {

    LITERAL4,
    LITERAL5,
    LITERAL6,
    TEST
}""")
    }

    private void adjustMultiLine(boolean semi) {
        File file = File.findFirst("name = ?","Test8.java")
        file.setEnd(8)
        Type type = Type.findFirst("name = ?","Test8")
        type.setStart(3)
        type.setEnd(8)
        Literal lit1 = Literal.findFirst("name = ?", "LITERAL4")
        lit1.setStart(5)
        lit1.setEnd(5)
        Literal lit2 = Literal.findFirst("name = ?", "LITERAL5")
        lit2.setStart(6)
        lit2.setEnd(6)
        Literal lit3 = Literal.findFirst("name = ?", "LITERAL6")
        lit3.setStart(7)
        lit3.setEnd(7)
        String end = ""
        if (semi)
            end = ";"
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.setText("""\
import java.util.*;

public enum Test8 {

    LITERAL4,
    LITERAL5,
    LITERAL6$end
}
""")
    }

    private void adjustInLine(boolean semi) {
        File file = File.findFirst("name = ?","Test8.java")
        file.setEnd(6)
        Type type = Type.findFirst("name = ?","Test8")
        type.setStart(3)
        type.setEnd(6)
        Literal lit1 = Literal.findFirst("name = ?", "LITERAL4")
        lit1.setStart(5)
        lit1.setEnd(5)
        Literal lit2 = Literal.findFirst("name = ?", "LITERAL5")
        lit2.setStart(5)
        lit2.setEnd(5)
        Literal lit3 = Literal.findFirst("name = ?", "LITERAL6")
        lit3.setStart(5)
        lit3.setEnd(5)
        String end = ""
        if (semi)
            end = ";"
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.setText("""\
import java.util.*;

public enum Test8 {

    LITERAL4, LITERAL5, LITERAL6$end
}
""")
    }

    private void adjustToEmpty(boolean inline) {
        File file = File.findFirst("name = ?","Test8.java")
        file.setEnd(5)
        Type type = Type.findFirst("name = ?","Test8")
        type.setStart(3)
        type.setEnd(5)
        Literal lit1 = Literal.findFirst("name = ?", "LITERAL4")
        Literal lit2 = Literal.findFirst("name = ?", "LITERAL5")
        Literal lit3 = Literal.findFirst("name = ?", "LITERAL6")
        type.removeMember(lit1)
        type.removeMember(lit2)
        type.removeMember(lit3)
        String content = ""
        if (!inline)
            content = "\n\n"
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.setText("""\
import java.util.*;

public enum Test8 {$content}
""")
    }
}
