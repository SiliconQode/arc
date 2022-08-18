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

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Import
import com.empirilytics.arc.disharmonies.injector.transform.BaseSourceTransformSpec
import com.empirilytics.arc.disharmonies.injector.transform.source.BasicSourceTransform
import org.junit.Test

class UpdateImportsTest extends BaseSourceTransformSpec{

    // TOOD Case 1: TestFile2 (Need to add one import)
    @Test
    void testCaseOne() {
        File file = File.findFirst("name = ?", "Test1.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])

        String expected = """\
package test.test;

import java.util.*;
import java.lang.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test1.java").text
        the(actual).shouldEqual(expected)
    }

    // TOOD Case 2: TestFile2 (Need to add one import)
    @Test
    void testCaseTwo() {
        File file = File.findFirst("name = ?", "Test2.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])

        String expected = """\
package test.test;

import java.lang.*;

public interface Test2 {

    void method(Test3 param);
}"""
        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test2.java").text
        the(actual).shouldEqual(expected)
    }

    // TOOD Case 3: TestFile2 (Need to add two imports)
    @Test
    void testCaseThree() {
        File file = File.findFirst("name = ?", "Test2.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["java.util.*"])

        String expected = """\
package test.test;

import java.lang.*;
import java.util.*;

public interface Test2 {

    void method(Test3 param);
}"""
        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test2.java").text
        the(actual).shouldEqual(expected)
    }

    // TODO Case 4: TestFile7 (need to add one import)
    @Test
    void testCaseFour() {
        File file = File.findFirst("name = ?", "Test9.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.util.*"])

        String expected = """\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private String name9, other;

    public void method(Test3 param) {

    }
}
"""
        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test9.java").text
        the(actual).shouldEqual(expected)
    }

    // TODO Case 5: TestFile1 (need to add two imports)
    @Test
    void testCaseFive() {
        File file = File.findFirst("name = ?", "Test1.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["other.what.How"])

        String expected = """\
package test.test;

import java.util.*;
import java.lang.*;
import other.what.How;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test1.java").text
        the(actual).shouldEqual(expected)
    }

    // TOOD Case 6: TestFile2 (need to add two imports)
    @Test
    void testCaseSix() {
        File file = File.findFirst("name = ?", "Test2.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["other.what.How"])

        String expected = """\
package test.test;

import java.lang.*;
import other.what.How;

public interface Test2 {

    void method(Test3 param);
}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test2.java").text
        the(actual).shouldEqual(expected)
    }

    // TOOD Case 7: TestFile3 (need to add one import)
    @Test
    void testCaseSeven() {
        File file = File.findFirst("name = ?", "Test3.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])

        String expected = """\
import java.lang.*;

public enum Test3 {

    LITERAL1,
    LITERAL2,
    LITERAL3;
}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test3.java").text
        the(actual).shouldEqual(expected)
    }

    // TOOD Case 8: TestFile5 (need to add two imports)
    @Test
    void testCaseEight() {
        File file = File.findFirst("name = ?", "Test8.java")
        file.addImport(Import.findFirst("name = ?", "java.util.*"))
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["what.how.when.Now"])

        String expected = """\
import java.util.*;
import java.lang.*;
import what.how.when.Now;

public class Test8 {

    LITERAL4, LITERAL5, LITERAL6;
}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test8.java").text
        the(actual).shouldEqual(expected)
    }

    @Test
    void testCaseNine() {
        File file = File.findFirst("name = ?", "Test19.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["what.how.when.Now"])

        String expected = """\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package test.test;

import java.lang.*;
import what.how.when.Now;

public class Type19 {

}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test19.java").text
        the(actual).shouldEqual(expected)
    }

    @Test
    void testCaseTen() {
        File file = File.findFirst("name = ?", "Test20.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["what.how.when.Now"])

        String expected = """\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package test.test;

import java.util.*;
import java.lang.*;
import what.how.when.Now;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Type20 {

}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test20.java").text
        the(actual).shouldEqual(expected)
    }

    @Test
    void testCaseEleven() {
        File file = File.findFirst("name = ?", "Test21.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])
        trans.addImports(["what.how.when.Now"])

        String expected = """\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
import java.lang.*;
import what.how.when.Now;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Type21 {

}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test21.java").text
        the(actual).shouldEqual(expected)
    }

    @Test
    void testCaseTwelve() {
        File file = File.findFirst("name = ?", "Test22.java")
        TestTransform trans = new TestTransform(file)
        trans.addImports(["java.lang.*"])

        String expected = """\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package test.test;

import java.util.*;
import java.lang.*;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Type22 {

}"""

        String actual = new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test22.java").text
        the(actual).shouldEqual(expected)
    }
}

class TestTransform extends BasicSourceTransform {

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    TestTransform(File file) {
        super(file)
    }
}
