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
import com.empirilytics.arc.disharmonies.injector.transform.model.file.AddTypeModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.model.member.AddFieldUseModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.model.namespace.AddFileModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.model.type.AddFieldModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.model.type.AddMethodModelTransform
import com.empirilytics.arc.disharmonies.injector.transform.source.member.CreateEncapsulatedField
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ComplicatedTransformsTest  extends BaseSourceTransformSpec {

    @Test
    void testCaseOne() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TestX.java"
        String typeName = "TestX"
        String fieldName = "typeX"
        String methodName = "methodX"

        // When
        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(27)

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(37)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(37)

        Type fieldType = Type.findFirst("name = ?", "Test1")
        AddFieldModelTransform addField = new AddFieldModelTransform(type, fieldName, fieldType, Accessibility.PRIVATE)
        addField.execute()
        Field field = addField.getField()
        file.refresh()
        type.refresh()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(39)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(39)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)

        AddMethodModelTransform addMethod = new AddMethodModelTransform(type, methodName, TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        addMethod.execute()
        Method method = addMethod.getMethod()
        file.refresh()
        type.refresh()

        // Then
        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TestX {

    private Test1 typeX;


    public void methodX() {
        throw new UnsupportedOperationException();
    }
}''')
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(43)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(43)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(40)
        the(method.getEnd()).shouldBeEqual(42)
    }

    @Test
    void testCaseTwo() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test23.java")
        Type type = Type.findFirst("name = ?", "Test23")
        Method method = Method.findFirst("name = ?", "aMethod23")
        String fieldName = "typeX"
        Type fieldType = Type.findFirst("name = ?", "Test1")

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(43)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(43)
        the(method.getStart()).shouldBeEqual(40)
        the(method.getEnd()).shouldBeEqual(42)

        // When
        AddFieldModelTransform addField = new AddFieldModelTransform(type, fieldName, fieldType, Accessibility.PRIVATE)
        addField.execute()
        Field field = addField.getField()
        file.refresh()
        type.refresh()
        method.refresh()

        // Then
        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
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

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test23 {

    private Test1 typeX;

    /**
     * A method that does something
     */
    public void aMethod23() {

    }
}''')

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(45)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(45)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(42)
        the(method.getEnd()).shouldBeEqual(44)
    }

    @Test
    void testCaseThree2() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TestX.java"
        String typeName = "TestX"
        String fieldName = "test"

        // When
        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(27)

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(37)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(37)

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, TypeRef.createPrimitiveTypeRef("int"), fieldName, Accessibility.PRIVATE)
        encFld.execute()

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()

        // Then
        file.refresh()
        type.refresh()
        the(file.getEnd()).shouldBeEqual(47)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(47)

        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TestX {

    private int test;


    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
        Method getter = type.getMethodWithName("getTest")
        the(getter.getStart()).shouldBeEqual(40)
        the(getter.getEnd()).shouldBeEqual(42)
        Method setter = type.getMethodWithName("setTest")
        the(setter.getStart()).shouldBeEqual(44)
        the(setter.getEnd()).shouldBeEqual(46)
        Field field = type.getFieldWithName("test")
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
    }

    @Test
    void testCaseThree() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test23.java")
        Type type = Type.findFirst("name = ?", "Test23")
        Method method = Method.findFirst("name = ?", "aMethod23")
        String fieldName = "test"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()

        // Then
        the(file.getEnd()).shouldBeEqual(53)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(53)

        the(text).shouldBeEqual('''\
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

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test23 {

    private int test;

    /**
     * A method that does something
     */
    public void aMethod23() {

    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
    }

    @Test
    void testCaseFour() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TestX.java"
        String typeName = "TestX"
        String fieldName = "test"
        String methodName = "aMethodX"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        AddMethodModelTransform addMethod = new AddMethodModelTransform(type, methodName, TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        addMethod.execute()
        Method method = addMethod.getMethod()
        file.refresh()
        type.refresh()

        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()
        file.refresh()
        type.refresh()
        method.refresh()

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TestX {

    private int test;


    public void aMethodX() {
        throw new UnsupportedOperationException();
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(51)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(51)
        the(method.getStart()).shouldBeEqual(40)
        the(method.getEnd()).shouldBeEqual(42)
    }

    @Test
    void testCaseFive() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test24.java")
        Type type = Type.findFirst("name = ?", "Test24")
        String fieldName = "test"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()
        file.refresh()
        type.refresh()

        Field field = type.getFieldWithName("test")
        field.refresh()
        Method method = type.getMethodWithName("aMethod24")
        method.refresh()
        Method getter = type.getMethodWithName("getTest")
        getter.refresh()
        Method setter = type.getMethodWithName("setTest")
        setter.refresh()

        // Then
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(55)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(53)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(42)
//        the(method.getEnd()).shouldBeEqual(44)
//        the(getter.getStart()).shouldBeEqual(46)
//        the(getter.getEnd()).shouldBeEqual(48)
//        the(setter.getStart()).shouldBeEqual(50)
//        the(setter.getEnd()).shouldBeEqual(52)

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
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

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test24 {

    private int test;

    /**
     * A method that does something
     */
    public void aMethod24() {

    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
    }

    @Test
    @Parameters([
            "1,7", "2,7", "3,7", "4,7", "5,7",
            "6,6", "7,6", "8,5", "9,4", "10,4",
            "11,3", "12,3", "13,3", "14,3", "15,2",
            "16,2", "17,2", "18,2", "19,1", "20,1",
            "21,1", "22,1", "23,0", "24,0", "25,0",
    ])
    void testCaseSix(int line, int expected) {
        File file = File.findFirst("name = ?", "Test16.java")

        the(file.following(line).size()).shouldBeEqual(expected)
    }

    @Test
    @Parameters([
            "1,0", "2,0", "3,0", "4,0", "5,0",
            "6,1", "7,1", "8,1", "9,1", "10,1",
            "11,1", "12,1", "13,1", "14,1", "15,1",
            "16,1", "17,1", "18,1", "19,1", "20,1",
            "21,1", "22,1", "23,1", "24,1", "25,1",
    ])
    void testCaseSeven(int line, int expected) {
        File file = File.findFirst("name = ?", "Test16.java")

        the(file.containing(line).size()).shouldBeEqual(expected)
    }

    @Test
    void testCaseEight() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TypeTest.java"
        String typeName = "TypeTest"
        String fieldName = "test"
        String methodName = "aMethodTest"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(27)

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        AddMethodModelTransform ammt = new AddMethodModelTransform(type, methodName, TypeRef.createPrimitiveTypeRef("int"), Accessibility.PUBLIC)
        ammt.execute()
        Method method = ammt.getMethod()

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()
        file.refresh()
        type.refresh()

        Field field = type.getFieldWithName("test")
        field.refresh()
        method.refresh()
        Method getter = type.getMethodWithName("getTest")
        getter.refresh()
        Method setter = type.getMethodWithName("setTest")
        setter.refresh()
        field.refresh()

        AddMethodModelTransform trans = new AddMethodModelTransform(type, "test1", (Type) Type.findFirst("name = ?", "TypeZ"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test2", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test3", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test4", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test5", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test6", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test7", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()
        trans = new AddMethodModelTransform(type, "test8", TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        trans.execute()

        AddFieldModelTransform afmt = new AddFieldModelTransform(type, "newField", Type.findFirst("name = ?", "TypeY"), Accessibility.PRIVATE)
        afmt.execute()

        AddFieldUseModelTransform afumt = new AddFieldUseModelTransform(method, afmt.field)
        afumt.execute()

        Method test1 = type.getMethodWithName("test1")
        Method test2 = type.getMethodWithName("test2")
        Method test3 = type.getMethodWithName("test3")
        Method test4 = type.getMethodWithName("test4")
        Method test5 = type.getMethodWithName("test5")
        Method test6 = type.getMethodWithName("test6")
        Method test7 = type.getMethodWithName("test7")
        Method test8 = type.getMethodWithName("test8")
        Field newField = afmt.field
        newField.refresh()

        // Then
        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

import test4.TypeZ;
import test4.TypeY;

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TypeTest {

    private TypeY newField;
    private int test;


    public int aMethodTest() {

        System.out.println(this.newField);

        throw new UnsupportedOperationException();
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public TypeZ test1() {
        throw new UnsupportedOperationException();
    }

    public void test2() {
        throw new UnsupportedOperationException();
    }

    public void test3() {
        throw new UnsupportedOperationException();
    }

    public void test4() {
        throw new UnsupportedOperationException();
    }

    public void test5() {
        throw new UnsupportedOperationException();
    }

    public void test6() {
        throw new UnsupportedOperationException();
    }

    public void test7() {
        throw new UnsupportedOperationException();
    }

    public void test8() {
        throw new UnsupportedOperationException();
    }
}''')

        file.refresh()
        type.refresh()
        method.refresh()
        field.refresh()
        getter.refresh()
        setter.refresh()
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(90)
        the(type.getStart()).shouldBeEqual(38)
        the(type.getEnd()).shouldBeEqual(90)
        the(newField.getStart()).shouldBeEqual(40)
        the(newField.getEnd()).shouldBeEqual(40)
        the(field.getStart()).shouldBeEqual(41)
        the(field.getEnd()).shouldBeEqual(41)
        the(method.getStart()).shouldBeEqual(44)
        the(method.getEnd()).shouldBeEqual(49)
        the(getter.getStart()).shouldBeEqual(51)
        the(getter.getEnd()).shouldBeEqual(53)
        the(setter.getStart()).shouldBeEqual(55)
        the(setter.getEnd()).shouldBeEqual(57)
        the(test1.getStart()).shouldBeEqual(59)
        the(test1.getEnd()).shouldBeEqual(61)
        the(test2.getStart()).shouldBeEqual(63)
        the(test2.getEnd()).shouldBeEqual(65)
        the(test3.getStart()).shouldBeEqual(67)
        the(test3.getEnd()).shouldBeEqual(69)
        the(test4.getStart()).shouldBeEqual(71)
        the(test4.getEnd()).shouldBeEqual(73)
        the(test5.getStart()).shouldBeEqual(75)
        the(test5.getEnd()).shouldBeEqual(77)
        the(test6.getStart()).shouldBeEqual(79)
        the(test6.getEnd()).shouldBeEqual(81)
        the(test7.getStart()).shouldBeEqual(83)
        the(test7.getEnd()).shouldBeEqual(85)
        the(test8.getStart()).shouldBeEqual(87)
        the(test8.getEnd()).shouldBeEqual(89)
    }
}
