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
package com.empirilytics.arc.disharmonies.injector.transform

import com.empirilytics.arc.datamodel.*
import org.javalite.activejdbc.InitException
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before

abstract class BaseSourceTransformSpec extends DBSpec {

    @Before
    void setup() {
        try {
            createModel()
            createDirectoryStructure()
            localSetup()
        } catch(InitException ex) {}
    }

    @After
    void teardown() {
        deleteDirectoryStructure()
    }

    protected void localSetup() {}

    protected static void createModel() {
        System sys = System.builder().name("testdata").key("TestData").basePath("testdata").create()
        System.builder().name("testdata2").key("TestData2").basePath("testdata2").create()
        Project proj = Project.builder().name("testproj").version("1.0").relPath("testproj").create()
        Project proj2 = Project.builder().name("testproj2").version("2.0").relPath("testproj2").create()
//        Module mod = Module.builder().name("testmod").relPath("testmod").srcPath("src/main/java").create()
        Module mod = Module.builder().name("testmod").relPath("testmod").create()
//        Module mod2 = Module.builder().name("testmod2").relPath("testmod2").srcPath("src/main/java").create()
        Module mod2 = Module.builder().name("testmod2").relPath("testmod2").create()
        Namespace ns1 = Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        Namespace ns2 = Namespace.builder().name("test.test").nsKey("test2").relPath("test").create()
        Namespace ns3 = Namespace.builder().name("test.test3").nsKey("test3").relPath("test3").create()
        Namespace ns4 = Namespace.builder().name("test4").nsKey("test4").relPath("test4").create()
        Namespace ns5 = Namespace.builder().name("test.test.test5").nsKey("test5").relPath("test5").create()
        ns1.addNamespace(ns2)
        ns1.addNamespace(ns3)
        mod.addNamespace(ns1)
        mod.addNamespace(ns4)
        ns2.addNamespace(ns5)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)
        proj.addNamespace(ns3)
        proj.addNamespace(ns4)
        proj.addNamespace(ns5)
        proj.addModule(mod)
        proj.addModule(mod2)
        sys.addProject(proj)
        sys.addProject(proj2)
        proj.setSrcPath("testmod/src/main/java", "testmod2/src/main/java")

        File file1 = File.builder().name("Test1.java").relPath("Test1.java").type(FileType.SOURCE).start(1).end(16).create()
        Import imp = Import.builder().name("java.util.*").start(3).end(3).create()
        Type type = Type.builder().type(Type.CLASS).name("Test1").accessibility(Accessibility.PUBLIC).start(5).end(16).create()
        Method m1 = Method.builder().name("method").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        Method m2 = Method.builder().name("main").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(13).end(15).create()
        Field f1 = Field.builder().name("name").type(TypeRef.createPrimitiveTypeRef("String")).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        type.addMember(m1)
        type.addMember(m2)
        type.addMember(f1)
        file1.addType(type)
        file1.addImport(imp)
        proj.addFile(file1)
        ns2.addType(type)
        ns2.addFile(file1)

        File file2 = File.builder().name("Test2.java").relPath("Test2.java").start(1).end(6).type(FileType.SOURCE).create()
        Type type2 = Type.builder().type(Type.INTERFACE).name("Test2").accessibility(Accessibility.PUBLIC).start(3).end(6).create()
        Method m3 = Method.builder().name("method").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(5).create()
        m3.addParameter(Parameter.builder().name("param").type(TypeRef.createPrimitiveTypeRef("Test3")).create())
        m3.addModifier(Modifier.forName("ABSTRACT"))
        type2.addMember(m3)
        file2.addType(type2)
        ns2.addFile(file2)
        proj.addFile(file2)
        ns2.addType(type2)

        File file3 = File.builder().name("Test3.java").relPath("Test3.java").type(FileType.SOURCE).start(1).end(6).create()
        Type type3 = Type.builder().type(Type.ENUM).name("Test3").accessibility(Accessibility.PUBLIC).start(1).end(6).create()
        Literal lit1 = Literal.builder().name("LITERAL1").compKey().start(3).end(3).create()
        Literal lit2 = Literal.builder().name("LITERAL2").compKey().start(4).end(4).create()
        Literal lit3 = Literal.builder().name("LITERAL3").compKey().start(5).end(5).create()
        type3.addMember(lit1)
        type3.addMember(lit2)
        type3.addMember(lit3)
        file3.addType(type3)
        ns2.addFile(file3)
        ns2.addType(type3)
        proj.addFile(file3)

        File file4 = File.builder().name("Test7.java").relPath("Test7.java").start(1).end(8).type(FileType.SOURCE).create()
        Type type4 = Type.builder().type(Type.CLASS).name("Test7").accessibility(Accessibility.PUBLIC).start(3).end(8).create()
        Literal lit14 = Literal.builder().name("LITERAL1").compKey().start(5).end(5).create()
        Literal lit24 = Literal.builder().name("LITERAL2").compKey().start(6).end(6).create()
        Literal lit34 = Literal.builder().name("LITERAL3").compKey().start(7).end(7).create()
        type4.addMember(lit14)
        type4.addMember(lit24)
        type4.addMember(lit34)
        file4.addType(type4)
        file4.addImport(imp)
        ns2.addFile(file4)
        proj.addFile(file4)
        ns2.addType(type4)

        File file5 = File.builder().name("Test8.java").relPath("Test8.java").type(FileType.SOURCE).start(1).end(6).create()
        Type type5 = Type.builder().type(Type.ENUM).name("Test8").accessibility(Accessibility.PUBLIC).start(3).end(6).create()
        Literal lit4 = Literal.builder().name("LITERAL4").compKey().start(5).end(5).create()
        Literal lit5 = Literal.builder().name("LITERAL5").compKey().start(5).end(5).create()
        Literal lit6 = Literal.builder().name("LITERAL6").compKey().start(5).end(5).create()
        type5.addMember(lit4)
        type5.addMember(lit5)
        type5.addMember(lit6)
        file5.addType(type5)
        ns2.addFile(file5)
        ns2.addType(type5)
        proj.addFile(file5)

        File file6 = File.builder().name("Test9.java").relPath("Test9.java").type(FileType.SOURCE).start(1).end(12).create()
        Type type6 = Type.builder().type(Type.CLASS).name("Test9").accessibility(Accessibility.PUBLIC).start(5).end(12).create()
        Method m16 = Method.builder().name("method").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        Field f16 = Field.builder().name("name9").type().accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        Field f26 = Field.builder().name("other").type().accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        type6.addMember(m16)
        type6.addMember(f16)
        type6.addMember(f26)
        file6.addType(type6)
        file6.addImport(imp)
        ns2.addFile(file6)
        ns2.addType(type6)
        proj.addFile(file6)

        File file7 = File.builder().name("Test10.java").relPath("Test10.java").type(FileType.SOURCE).start(1).end(12).create()
        Type type7 = Type.builder().type(Type.CLASS).name("Test10").accessibility(Accessibility.PUBLIC).start(5).end(12).create()
        Method m17 = Method.builder().name("method4").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(11).create()
        Parameter param = Parameter.builder().name("param").type(TypeRef.createPrimitiveTypeRef("Test3")).create()
        m17.addParameter(param)
        m17.addModifier(Modifier.forName("static"))
        Field f17 = Field.builder().name("COUNT").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        f17.addModifier(Modifier.forName("volatile"))
        f17.addModifier(Modifier.forName("static"))
        f17.addModifier(Modifier.forName("final"))
        type7.addMember(m17)
        type7.addMember(f17)
        file7.addType(type7)
        file7.addImport(imp)
        ns2.addFile(file7)
        ns2.addType(type7)
        proj.addFile(file7)

        File file8 = File.builder().name("Test11.java").relPath("Test11.java").type(FileType.SOURCE).start(1).end(10).create()
        Type type8 = Type.builder().type(Type.CLASS).name("Test11").accessibility(Accessibility.PUBLIC).start(5).end(10).create()
        Method m18 = Method.builder().name("paramsTest").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(7).end(9).create()
        Parameter p1 = Parameter.builder().name("param1").create()
        p1.setType(TypeRef.createPrimitiveTypeRef("int"))
        Parameter p2 = Parameter.builder().name("param2").type(TypeRef.createPrimitiveTypeRef("int")).create()
        Parameter p3 = Parameter.builder().name("param3").type(TypeRef.createPrimitiveTypeRef("int")).create()
        m18.addParameter(p1)
        m18.addParameter(p2)
        m18.addParameter(p3)
        type8.addMember(m18)
        file8.addType(type8)
        file8.addImport(imp)
        ns2.addFile(file8)
        ns2.addType(type8)
        proj.addFile(file8)

        File file9 = File.builder().name("Test12.java").relPath("Test12.java").type(FileType.SOURCE).start(1).end(8).create()
        Type type9 = Type.builder().type(Type.CLASS).name("Test12").accessibility(Accessibility.PUBLIC).start(5).end(8).create()
        Method m19 = Method.builder().name("paramsTest2").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(7).end(7).create()
        Parameter p19 = Parameter.builder().name("param1").type(TypeRef.createPrimitiveTypeRef("int")).create()
        Parameter p29 = Parameter.builder().name("param2").type(TypeRef.createPrimitiveTypeRef("int")).create()
        Parameter p39 = Parameter.builder().name("param3").type(TypeRef.createPrimitiveTypeRef("int")).create()
        m19.addParameter(p19)
        m19.addParameter(p29)
        m19.addParameter(p39)
        m19.addModifier("abstract")
        type9.addMember(m19)
        type9.addModifier("abstract")
        file9.addType(type9)
        file9.addImport(imp)
        ns2.addFile(file9)
        ns2.addType(type9)
        proj.addFile(file9)

        File file10 = File.builder().name("Test13.java").relPath("Test13.java").type(FileType.SOURCE).start(1).end(9).create()
        Type type10 = Type.builder().type(Type.CLASS).name("Test13").accessibility(Accessibility.PUBLIC).start(5).end(9).create()
        Field f110 = Field.builder().name("COUNTX").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        f110.addModifier(Modifier.forName("volatile"))
        f110.addModifier(Modifier.forName("static"))
        f110.addModifier(Modifier.forName("final"))
        type10.addMember(f110)
        file10.addType(type10)
        file10.addImport(imp)
        ns2.addFile(file10)
        ns2.addType(type10)
        proj.addFile(file10)

        File file11 = File.builder().name("Test14.java").relPath("Test14.java").type(FileType.SOURCE).start(1).end(9).create()
        Type type11 = Type.builder().type(Type.CLASS).name("Test14").accessibility(Accessibility.PUBLIC).start(5).end(9).create()
        file11.addType(type11)
        file11.addImport(imp)
        ns2.addFile(file11)
        ns2.addType(type11)
        proj.addFile(file11)

        File file12 = File.builder().name("Test15.java").relPath("Test15.java").type(FileType.SOURCE).start(1).end(9).create()
        Type type12 = Type.builder().type(Type.CLASS).name("Test15").accessibility(Accessibility.PUBLIC).start(5).end(9).create()
        file12.addType(type12)
        file12.addImport(imp)
        ns2.addFile(file12)
        ns2.addType(type12)
        proj.addFile(file12)

        Field f111 = Field.builder().name("test15").type(type12.createTypeRef()).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        type11.addMember(f111)

        Field f112 = Field.builder().name("test14").type(type11.createTypeRef()).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        type12.addMember(f112)
        type11.associatedTo(type12)
        type12.associatedTo(type11)

        File file16 = File.builder().name("Test16.java").relPath("Test16.java").type(FileType.SOURCE).start(1).end(25).create()
        Type type16 = Type.builder().type(Type.CLASS).name("Test16").accessibility(Accessibility.PUBLIC).start(5).end(25).create()
        file16.addType(type16)
        file16.addImport(imp)
        ns2.addFile(file16)
        ns2.addType(type16)
        proj.addFile(file16)

        File file17 = File.builder().name("Test17.java").relPath("Test17.java").type(FileType.SOURCE).start(1).end(8).create()
        Type type17 = Type.builder().type(Type.CLASS).name("Test17").accessibility(Accessibility.PUBLIC).start(5).end(8).create()
        file17.addType(type17)
        file17.addImport(imp)
        ns2.addFile(file17)
        ns2.addType(type17)
        proj.addFile(file17)

        File file18 = File.builder().name("Test18.java").relPath("Test18.java").type(FileType.SOURCE).start(1).end(8).create()
        Type type18 = Type.builder().type(Type.CLASS).name("Test18").accessibility(Accessibility.PUBLIC).start(5).end(8).create()
        file18.addType(type18)
        file18.addImport(imp)
        ns2.addFile(file18)
        ns2.addType(type18)
        proj.addFile(file18)

        Field f161 = Field.builder().name("xy").type(type17.createTypeRef()).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        Field f162 = Field.builder().name("yz").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(8).end(8).create()
        Method m161 = Method.builder().name("testXY").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(10).end(12).create()
        Method m162 = Method.builder().name("testXX").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(14).end(16).create()
        Method m163 = Method.builder().name("testYY").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(18).end(20).create()
        m163.addParameter(Parameter.builder().type(type18.createTypeRef()).name("xx").create())
        Method m164 = Method.builder().name("testZZ").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(22).end(24).create()
        f162.addModifier(Modifier.forName("static"))
        type16.addMember(f161)
        type16.addMember(f162)
        type16.addMember(m161)
        type16.addMember(m162)
        type16.addMember(m163)
        type16.addMember(m164)

        Field f171 = Field.builder().name("bb").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PROTECTED).start(7).end(7).create()
        type17.addMember(f171)

        Field f181 = Field.builder().name("zz").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PROTECTED).start(7).end(7).create()
        type18.addMember(f181)

        File fileX = File.builder().name("TypeX.java").relPath("TypeX.java").type(FileType.SOURCE).start(1).end(5).create()
        Type typeX = Type.builder().type(Type.CLASS).name("TypeX").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileX.addType(typeX)

        File fileY = File.builder().name("TypeY.java").relPath("TypeY.java").type(FileType.SOURCE).start(1).end(5).create()
        Type typeY = Type.builder().type(Type.INTERFACE).name("TypeY").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileY.addType(typeY)

        File fileZ = File.builder().name("TypeZ.java").relPath("TypeZ.java").type(FileType.SOURCE).start(1).end(10).create()
        Type typeZ = Type.builder().type(Type.CLASS).name("TypeZ").accessibility(Accessibility.PUBLIC).start(3).end(10).create()
        typeZ.addModifier(Modifier.forName("final"))
        Method mZ = Method.builder().name("methodZ").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(8).end(9).create()
        Field fZ = Field.builder().name("fieldZ").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(5).end(5).create()
        typeZ.addMember(mZ)
        typeZ.addMember(fZ)
        fileZ.addType(typeZ)

        File fileXY = File.builder().name("TypeXY.java").relPath("TypeXY.java").type(FileType.SOURCE).start(1).end(5).create()
        Type typeXY = Type.builder().type(Type.CLASS).name("TypeXY").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileXY.addType(typeXY)

        File fileYY = File.builder().name("TypeYY.java").relPath("TypeYY.java").type(FileType.SOURCE).start(1).end(5).create()
        Type typeYY = Type.builder().type(Type.INTERFACE).name("TypeYY").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileYY.addType(typeYY)

        File fileA = File.builder().name("TypeA.java").relPath("TypeA.java").type(FileType.SOURCE).start(1).end(6).create()
        Type typeA = Type.builder().type(Type.ENUM).name("TypeA").accessibility(Accessibility.PUBLIC).start(3).end(6).create()
        Literal litA = Literal.builder().name("LITERALA").compKey().start(5).end(5).create()
        typeA.addMember(litA)
        fileA.addType(typeA)

        File fileB = File.builder().name("TypeB.java").relPath("TypeB.java").type(FileType.SOURCE).start(1).end(5).create()
        Type typeB = Type.builder().type(Type.ENUM).name("TypeB").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileB.addType(typeB)

        ns4.addFile(fileX)
        ns4.addFile(fileY)
        ns4.addFile(fileZ)
        ns4.addFile(fileXY)
        ns4.addFile(fileYY)
        ns4.addFile(fileA)
        ns4.addFile(fileB)
        ns4.addType(typeX)
        ns4.addType(typeY)
        ns4.addType(typeZ)
        ns4.addType(typeXY)
        ns4.addType(typeYY)
        ns4.addType(typeA)
        ns4.addType(typeB)
        proj.addFile(fileX)
        proj.addFile(fileY)
        proj.addFile(fileZ)
        proj.addFile(fileXY)
        proj.addFile(fileYY)
        proj.addFile(fileA)
        proj.addFile(fileB)

        Field ftXY = Field.builder().name("typeXY").type(typeXY.createTypeRef()).accessibility(Accessibility.PRIVATE).start(6).end(6).create()
        typeZ.addMember(ftXY)

        File fileC = File.builder().name("TypeC.java").relPath("TypeC.java").type(FileType.SOURCE).start(1).end(19).create()
        Type typeC = Type.builder().type(Type.CLASS).name("TypeC").accessibility(Accessibility.PUBLIC).start(3).end(19).create()
        Method mC1 = Method.builder().name("methodC1").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(9).end(12).create()
        mC1.addParameter(Parameter.builder().type(TypeRef.createPrimitiveTypeRef("int")).name("x").create())
        mC1.addModifier(Modifier.Values.FINAL.name())
        Method mC2 = Method.builder().name("methodC2").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(14).end(15).create()
        Method mC3 = Method.builder().name("methodC3").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(17).end(18).create()
        Field fC1 = Field.builder().name("cX").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(5).end(5).create()
        Field fC2 = Field.builder().name("cY").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(6).end(6).create()
        Field fC3 = Field.builder().name("cZ").type(TypeRef.createPrimitiveTypeRef("int")).accessibility(Accessibility.PRIVATE).start(7).end(7).create()
        typeC.addMember(mC1)
        typeC.addMember(mC2)
        typeC.addMember(mC3)
        typeC.addMember(fC1)
        typeC.addMember(fC2)
        typeC.addMember(fC3)
        fileC.addType(typeC)

        File file19 = File.builder().name("Test19.java").relPath("Test19.java").type(FileType.SOURCE).start(1).end(31).create()
        Type type19 = Type.builder().type(Type.CLASS).name("Test19").accessibility(Accessibility.PUBLIC).start(29).end(31).create()
        file19.addType(type19)
        proj.addFile(file19)
        ns2.addType(type19)
        ns2.addFile(file19)

        File file20 = File.builder().name("Test20.java").relPath("Test20.java").type(FileType.SOURCE).start(1).end(37).create()
        Type type20 = Type.builder().type(Type.CLASS).name("Test20").accessibility(Accessibility.PUBLIC).start(35).end(37).create()
        file20.addType(type20)
        file20.addImport(imp)
        proj.addFile(file20)
        ns2.addType(type20)
        ns2.addFile(file20)

        File file21 = File.builder().name("Test21.java").relPath("Test21.java").type(FileType.SOURCE).start(1).end(35).create()
        Type type21 = Type.builder().type(Type.CLASS).name("Test21").accessibility(Accessibility.PUBLIC).start(32).end(34).create()
        file21.addType(type21)
        proj.addFile(file21)
        ns2.addType(type21)
        ns2.addFile(file21)

        File file22 = File.builder().name("Test22.java").relPath("Test22.java").type(FileType.SOURCE).start(1).end(37).create()
        Type type22 = Type.builder().type(Type.CLASS).name("Test22").accessibility(Accessibility.PUBLIC).start(35).end(37).create()
        file22.addType(type22)
        file22.addImport(imp)
        proj.addFile(file22)
        ns2.addType(type22)
        ns2.addFile(file22)

        File file23 = File.builder().name("Test23.java").relPath("Test23.java").type(FileType.SOURCE).start(1).end(43).create()
        Type type23 = Type.builder().type(Type.CLASS).name("Test23").accessibility(Accessibility.PUBLIC).start(35).end(43).create()
        Method m231 = Method.builder().name("aMethod23").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(40).end(42).create()
        type23.addMember(m231)
        file23.addType(type23)
        file23.addImport(imp)
        proj.addFile(file23)
        ns2.addType(type23)
        ns2.addFile(file23)

        File file24 = File.builder().name("Test24.java").relPath("Test24.java").type(FileType.SOURCE).start(1).end(45).create()
        Type type24 = Type.builder().type(Type.CLASS).name("Test24").accessibility(Accessibility.PUBLIC).start(35).end(43).create()
        Method m241 = Method.builder().name("aMethod24").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(40).end(42).create()
        type24.addMember(m241)
        file24.addType(type24)
        file24.addImport(imp)
        proj.addFile(file24)
        ns2.addType(type24)
        ns2.addFile(file24)

        ns4.addFile(fileC)
        ns4.addType(typeC)
        proj.addFile(fileC)

        sys.updateKeys()
        ns2.refresh()
        ns1.refresh()
        ns3.refresh()
        ns4.refresh()
        ns5.refresh()
        mod.refresh()
        mod2.refresh()
        proj.refresh()
        proj2.refresh()
        file1.refresh()
        file2.refresh()
        file3.refresh()
        file4.refresh()
        file5.refresh()
        file6.refresh()
        file7.refresh()
        file8.refresh()
        file9.refresh()
        file10.refresh()
        file11.refresh()
        file12.refresh()
        fileX.refresh()
        fileY.refresh()
        fileZ.refresh()
        fileA.refresh()
        fileB.refresh()
        fileC.refresh()
        fileXY.refresh()
        file16.refresh()
        file17.refresh()
        file18.refresh()
        file19.refresh()
        file20.refresh()
        file21.refresh()
        file22.refresh()

        typeZ.generalizedBy(typeX)
        typeZ.associatedTo(typeXY)
        typeZ.realizes(typeY)

        mC1.usesField(fC1)
        mC1.callsMethod(mC2)
    }

    protected void createDirectoryStructure() {
        new java.io.File("testdata2").mkdirs()
        FileTreeBuilder builder = new FileTreeBuilder(new java.io.File("testdata"))
        builder {
            "testproj" {
                "testmod" {
                    "src" {
                        "main" {
                            "java" {
                                "test" {
                                    "test" {
                                        'Test1.java'()
                                        'Test2.java'()
                                        'Test3.java'()
                                        'Test7.java'()
                                    }
                                    "test3" {}
                                }
                                "test4" {}
                            }
                            "resources" {

                            }
                        }
                        "test" {
                            "java" {

                            }
                        }
                    }
                }
                "testmod2" {
                    "src" {
                        "main" {
                            "java" {}
                            "resources" {}
                        }
                        "test" {
                            "java" {}
                        }
                    }
                }
//                "build.gradle"(createBuildGradleFile())
//                "settings.gradle"(createSettingsGradleFile())
            }
            "testproj2" {

            }
        }

        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test1.java").text = createTestFile1()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test2.java").text = createTestFile2()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test3.java").text = createTestFile3()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test7.java").text = createTestFile4()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test8.java").text = createTestFile5()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test9.java").text = createTestFile6()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test10.java").text = createTestFile7()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test11.java").text = createTestFile8()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test12.java").text = createTestFile9()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test13.java").text = createTestFile10()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test14.java").text = createTestFile11()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test15.java").text = createTestFile12()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test16.java").text = createTestFile13()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test17.java").text = createTestFile14()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test18.java").text = createTestFile15()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test19.java").text = createTestFile24()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test20.java").text = createTestFile25()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test21.java").text = createTestFile26()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test22.java").text = createTestFile27()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test23.java").text = createTestFile28()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/Test24.java").text = createTestFile29()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeX.java").text = createTestFile16()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeY.java").text = createTestFile17()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeZ.java").text = createTestFile18()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeXY.java").text = createTestFile19()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeYY.java").text = createTestFile20()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeA.java").text = createTestFile21()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeB.java").text = createTestFile22()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeC.java").text = createTestFile23()
    }

    private static void deleteDirectoryStructure() {
        java.io.File dir = new java.io.File("testdata")
        java.io.File dir2 = new java.io.File("testdata2")
        dir.deleteDir()
        dir2.deleteDir()
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

    private static String createTestFile4() {
        """\
import java.util.*;

public class Test7 extends Test1 {

    LITERAL1,
    LITERAL2,
    LITERAL3;
}
"""
    }

    private static String createTestFile5() {
        """\
import java.util.*;

public class Test8 {

    LITERAL4, LITERAL5, LITERAL6;
}
"""
    }

    private static String createTestFile6() {
        '''\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private String name9, other;

    public void method(Test3 param) {

    }
}
'''
    }

    private static String createTestFile7() {
        '''\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    private volatile static final int COUNT;

    public static void method4(Test3 param) {

    }
}
'''
    }

    private static String createTestFile8() {
        '''\
package test.test;

import java.util.*;

public class Test11 implements Test2 {

    public void paramsTest(int param1, int param2, int param3) {

    }
}
'''
    }

    private static String createTestFile9() {
        '''\
package test.test;

import java.util.*;

public abstract class Test12 {

    public abstract void paramsTest(int param1, int param2, int param3);
}
'''
    }

    private static String createTestFile10() {
        '''\
package test.test;

import java.util.*;

public class Test13 {

    private volatile static final int COUNTX;

}
'''
    }

    private static String createTestFile11() {
        '''\
package test.test;

import java.util.*;

public class Test14 {

    private Test15 test15;

}
'''
    }

    private static String createTestFile12() {
        '''\
package test.test;

import java.util.*;

public class Test15 {

    private Test14 test14;

}
'''
    }

    private static String createTestFile13() {
        '''\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}
'''
    }

    private static String createTestFile14() {
        '''\
package test.test;

import java.util.*;

public class Test17 {

    private int bb;
}
'''
    }

    private static String createTestFile15() {
        '''\
package test.test;

import java.util.*;

public class Test18 {

    private int zz;
}
'''
    }

    private static String createTestFile16() {
        '''\
package test.test;

public class TypeX {

}
'''
    }

    private static String createTestFile17() {
        '''\
package test.test;

public interface TypeY {

}
'''
    }

    private static String createTestFile18() {
        '''\
package test.test;

public final class TypeZ extends TypeX implements TypeY {

    private int fieldZ
    private TypeXY typeXY

    public void methodZ() {
    }
}
'''
    }

    private static String createTestFile19() {
        '''\
package test.test;

public class TypeXY {

}
'''
    }

    private static String createTestFile20() {
        '''\
package test.test;

public interface TypeYY {

}
'''
    }

    private static String createTestFile21() {
        '''\
package test.test;

public enum TypeA {

    LITERALA
}
'''
    }

    private static String createTestFile22() {
        '''\
package test.test;

public enum TypeB {

}
'''
    }

    private static String createTestFile23() {
        '''\
package test4;

public class TypeC {

    private int cX;
    private int cY;
    private int cZ;

    public final void methodC1(int x) {
        this.cX = X;
        this.methodC2();
    }

    public void methodC2() {
    }

    public void methodC3() {
    }
}
'''
    }

    private static createTestFile24() {
        '''\
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

public class Type19 {

}
'''
    }

    private static createTestFile25() {
        '''\
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
public class Type20 {

}
'''
    }

    private static createTestFile26() {
        '''\
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

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Type21 {

}
'''
    }

    private static createTestFile27() {
        '''\
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
public class Type22 {

}
'''
    }

    private static createTestFile28() {
        '''\
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

    /**
     * A method that does something
     */
    public void aMethod23() {

    }
}
'''
    }

    private static createTestFile29() {
        '''\
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

    /**
     * A method that does something
     */
    public void aMethod24() {

    }
}


'''
    }
}
