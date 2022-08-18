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
package com.empirilytics.arc.disharmonies.injector

import com.empirilytics.arc.datamodel.*
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProjectCopierTest extends DBSpec {

    ProjectCopier fixture

    @Before
    void setUp() throws Exception {
        createModel()
        createDirectoryStructure()
        fixture = new ProjectCopier()
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test execute happy path"() {
        // given
        Project proj = Project.findFirst("name = ?", "testproj")

        // when
        Project result = fixture.execute(proj)
        java.io.File file = new java.io.File(result.getFullPath())

        // then
        the(result).shouldNotBeNull()
        the(result.getName()).shouldBeEqual("testproj_copy")
        the(file.isDirectory()).shouldBeTrue()
        the(file.listFiles().size() > 0).shouldBeTrue()
    }

    @Test(expected = InjectionFailedException.class)
    void "test execute null project"() {
        // given
        Project proj = null

        // when
        fixture.execute(proj)
    }

    protected static void createModel() {
        System sys = System.builder().name("testdata").key("TestData").basePath("testdata").create()
        Project proj = Project.builder().name("testproj").version("1.0").relPath("testproj").create()
        Namespace ns1 = Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        Namespace ns2 = Namespace.builder().name("test2").nsKey("test2").relPath("test/test").create()
        proj.addNamespace(ns2)
        ns1.addNamespace(ns2)
        proj.addNamespace(ns1)
        sys.addProject(proj)

        File file1 = File.builder().name("testdata/testproj/src/main/java/test/test/Test1.java").relPath("Test1.java").type(FileType.SOURCE).start(1).end(16).create()
        Import imp = Import.builder().name("java.util.*").start(3).end(3).create()
        Type type = Type.builder().type(Type.CLASS).name("Test1").accessibility(Accessibility.PUBLIC).start(5).end(16).create()
        Method m1 = Method.builder().name("method").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        Method m2 = Method.builder().name("main").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(13).end(15).create()
        Field f1 = Field.builder().name("name").type(TypeRef.createPrimitiveTypeRef("String")).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        type.addMember(m1)
        type.addMember(m2)
        type.addMember(f1)
        ns2.addType(type)
        file1.addType(type)
        file1.addImport(imp)
        proj.addFile(file1)
        ns2.addFile(file1)

        File file2 = File.builder().name("testdata/testproj/src/main/java/test/test/Test2.java").relPath("Test2.java").type(FileType.SOURCE).create()
        Type type2 = Type.builder().type(Type.INTERFACE).name("Test2").accessibility(Accessibility.PUBLIC).start(3).end(6).create()
        Method m3 = Method.builder().name("method").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(5).create()
        m3.addParameter(Parameter.builder().name("param").type(TypeRef.createPrimitiveTypeRef("Test3")).create())
        m3.addModifier(Modifier.forName("ABSTRACT"))
        type2.addMember(m3)
        ns2.addType(type2)
        file2.addType(type2)
        proj.addFile(file2)
        ns2.addFile(file2)

        File file3 = File.builder().name("testdata/testproj/src/main/java/test/test/Test3.java").relPath("Test3.java").type(FileType.SOURCE).create()
        Type type3 = Type.builder().type(Type.ENUM).name("Test3").accessibility(Accessibility.PUBLIC).start(1).end(6).create()
        Literal lit1 = Literal.builder().name("LITERAL1").compKey().start(3).end(3).create()
        Literal lit2 = Literal.builder().name("LITERAL2").compKey().start(4).end(4).create()
        Literal lit3 = Literal.builder().name("LITERAL3").compKey().start(5).end(5).create()
        type3.addMember(lit1)
        type3.addMember(lit2)
        type3.addMember(lit3)
        proj.addFile(file3)
        ns2.addType(type3)
        ns2.addFile(file3)
        file3.addType(type3)
        ns2.addFile(file3)

        sys.updateKeys()
    }

    protected void createDirectoryStructure() {
        FileTreeBuilder builder = new FileTreeBuilder(new java.io.File("testdata"))
        builder {
            "testproj" {
                "src" {
                    "main" {
                        "java" {
                            "test" {
                                "test" {}
                            }
                        }
                        "resources" {

                        }
                    }
                    "test" {
                        "java" {

                        }
                    }
                }
//                "build.gradle"(createBuildGradleFile())
//                "settings.gradle"(createSettingsGradleFile())
            }
        }

        new java.io.File("testdata/testproj/src/main/java/test/test/Test1.java").text = createTestFile1()
        new java.io.File("testdata/testproj/src/main/java/test/test/Test2.java").text = createTestFile2()
        new java.io.File("testdata/testproj/src/main/java/test/test/Test3.java").text = createTestFile3()
    }

    private static void deleteDirectoryStructure() {
        java.io.File dir = new java.io.File("testdata")
        dir.deleteDir()
    }

    private static String createTestFile1() {
        '''\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }
}
'''
    }

    private static String createTestFile2() {
        """\
package test.test;

public interface Test2 {

    void method(Test3 param);
}
"""
    }

    private static String createTestFile3() {
        """\
public enum Test3 {

    LITERAL1,
    LITERAL2,
    LITERAL3;
}
"""
    }
}
