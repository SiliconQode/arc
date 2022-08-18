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
package com.empirilytics.arc.disharmonies.injector.transform.source.type

import com.empirilytics.arc.datamodel.*
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import org.junit.Test

class MoveTypeTest extends BaseSourceTransformSpec {

    @Test
    void "test execute from file with multiple types to file with single type"() {
        // given
        File from = File.findFirst("name = ?", "Test1.java")
        srcFileMultipleTypes(from)
        File to = File.findFirst("name = ?", "Test2.java")
        destFileSingleType(to)
        Type type = Type.findFirst("name = ?", "Test5")
        MoveType fixture = new MoveType(type, from, to)
        java.io.File fromActual = new java.io.File(from.getFullPath())
        java.io.File toActual = new java.io.File(to.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}""")
        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public interface Test2 {

    void method(Test3 param);
}

public class Test5 {

}""")
    }

    @Test
    void "test execute from file with single type to file with single type"() {
        // given
        File from = File.findFirst("name = ?", "Test1.java")
        srcFileSingleType(from)
        File to = File.findFirst("name = ?", "Test2.java")
        destFileSingleType(to)
        Type type = Type.findFirst("name = ?", "Test5")
        MoveType fixture = new MoveType(type, from, to)
        java.io.File fromActual = new java.io.File(from.getFullPath())
        java.io.File toActual = new java.io.File(to.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;""")
        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public interface Test2 {

    void method(Test3 param);
}

public class Test5 {

}""")
    }

    @Test
    void "test execute from file with multiple types to file with no types"() {
        // given
        File from = File.findFirst("name = ?", "Test1.java")
        srcFileMultipleTypes(from)
        File to = File.findFirst("name = ?", "Test2.java")
        destFileNoType(to)
        Type type = Type.findFirst("name = ?", "Test5")
        MoveType fixture = new MoveType(type, from, to)
        java.io.File fromActual = new java.io.File(from.getFullPath())
        java.io.File toActual = new java.io.File(to.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}""")
        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test5 {

}""")
    }

    @Test
    void "test execute from file with single type to file with no types"() {
        // given
        File from = File.findFirst("name = ?", "Test1.java")
        srcFileSingleType(from)
        File to = File.findFirst("name = ?", "Test2.java")
        destFileNoType(to)
        Type type = Type.findFirst("name = ?", "Test5")
        MoveType fixture = new MoveType(type, from, to)
        java.io.File fromActual = new java.io.File(from.getFullPath())
        java.io.File toActual = new java.io.File(to.getFullPath())

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;""")
        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test5 {

}""")
    }

    private srcFileMultipleTypes(File file) {
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.text = """\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}

public class Test5 {

}
"""
        Type type = Type.builder()
                .name("Test5")
                .compKey("Test5")
                .accessibility(Accessibility.PUBLIC)
                .start(18)
                .end(20)
                .create()
        file.addType(type)
        type.updateKey()
        file.setEnd(20)
        file.save()
    }

    private srcFileSingleType(File file) {
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.text = """\
package test.test;

import java.util.*;

public class Test5 {

}
"""
        Type type = Type.builder()
                .name("Test5")
                .compKey("Test5")
                .accessibility(Accessibility.PUBLIC)
                .start(5)
                .end(7)
                .create()
        file.addType(type)
        file.removeType(Type.findFirst("name = ?", "Test1"))
        type.updateKey()
        file.setEnd(20)
        file.save()
    }

    private destFileSingleType(File file) {
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.text = """\
package test.test;

public interface Test2 {

    void method(Test3 param);
}
"""
    }

    private destFileNoType(File file) {
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.text = """\
package test.test;
"""
        file.removeType(Type.findFirst("name = ?", "Test2"))
        file.setEnd(2)
        file.save()
    }
}
