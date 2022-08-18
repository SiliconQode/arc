/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.datamodel.pattern

import dev.siliconcode.arc.datamodel.*
import org.javalite.activejdbc.test.DBSpec
import org.junit.Before
import org.junit.Test

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
//@RunWith(JUnitParamsRunner.class)
class ChainIdentifierSpec extends DBSpec {

    ChainIdentifier fixture
    System sys
    List<Project> projects
    List<Namespace> namespaces

    @Before
    void setup() {
        fixture = new ChainIdentifier()
        projects = []
        namespaces = []
        genSystemAndProjects()
    }

    @Test
    void "test findChains happy path"() {
        // given
        System input = sys

        // when
        fixture.findChains(input)

        // then
        the(sys.patternChains.isEmpty()).shouldBeFalse()
        the(sys.patternChains.size()).shouldBeEqual(4)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test findChains system is null"() {
        // given
        System input = null

        // when
        fixture.findChains(input)
    }

    @Test
    void "test createChains happy path"() {
        // given
        List<PatternInstance> insts = PatternInstance.findAll()

        // when
        List<PatternChain> chains = fixture.createChains(insts)

        // then
        the(chains).shouldNotBeNull()
        the(chains.isEmpty()).shouldBeFalse()
        the(chains.size()).shouldBeEqual(insts.size())
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createChains insts list is null"() {
        // given
        List<PatternInstance> insts = null

        // when
        fixture.createChains(insts)
    }

    @Test
    void "test createChains insts list is empty"() {
        // given
        List<PatternInstance> insts = []

        // when
        List<PatternChain> chains = fixture.createChains(insts)

        // then
        the(chains).shouldNotBeNull()
        the(chains.isEmpty()).shouldBeTrue()
    }

    @Test
    void "test createChain happy path"() {
        // given
        PatternInstance inst = PatternInstance.findById(1)

        // when
        PatternChain chain = fixture.createChain(inst)

        // then
        the(chain).shouldNotBeNull()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createChain inst is null"() {
        // given
        PatternInstance inst = null

        // when
        fixture.createChain(inst)
    }

    @Test
    void "test matches happy path"() {
        // given
        Pattern p = Pattern.findFirst("patternKey = ?", "gof:state")
        PatternChain chain = fixture.createChain(p.getInstances()[0])
        PatternInstance inst = p.getInstances()[1]

        // when
        boolean result = fixture.matches(chain, inst)

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test matches different parent patterns"() {
        // given
        Pattern p = Pattern.findFirst("patternKey = ?", "gof:state")
        PatternChain chain = fixture.createChain(p.getInstances()[0])
        PatternInstance inst = p.getInstances()[1]

        // when
        boolean result = fixture.matches(chain, inst)

        // then
        the(result).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test matches chain is null"() {
        // given
        PatternChain chain = null
        PatternInstance inst = PatternInstance.findById(1)

        // when
        fixture.matches(chain, inst)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test matches inst is null"() {
        // given
        PatternChain chain = fixture.createChain(PatternInstance.findById(1))
        PatternInstance inst = null

        // when
        fixture.matches(chain, inst)
    }

    void genSystemAndProjects() {
        sys = System.builder().name("testdata").key("TestData").basePath("testdata").create()

        projects << Project.builder().name("testproj").version("1.0.0").relPath("testproj").create()
        Module mod1 = Module.builder().name("testmod").relPath("testmod").srcPath("src/main/java").create()
        namespaces << Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        mod1.addNamespace(namespaces[0])
        projects[0].addModule(mod1)
        sys.addProject(projects[0])

        projects << Project.builder().name("testproj").version("1.1.0").relPath("testproj").create()
        Module mod2 = Module.builder().name("testmod").relPath("testmod").srcPath("src/main/java").create()
        namespaces << Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        mod2.addNamespace(namespaces[1])
        projects[1].addModule(mod2)
        sys.addProject(projects[1])

        projects << Project.builder().name("testproj").version("1.2.0").relPath("testproj").create()
        Module mod3 = Module.builder().name("testmod").relPath("testmod").srcPath("src/main/java").create()
        namespaces << Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        mod3.addNamespace(namespaces[2])
        projects[2].addModule(mod3)
        sys.addProject(projects[2])

        for (int i = 0; i < projects.size(); i++) {
            genPatternInstance1(i + 1, projects[i], namespaces[i])
            genPatternInstance2(i + 1, projects[i], namespaces[i])
            genPatternInstance3(i + 1, projects[i], namespaces[i])
            genPatternInstance4(i + 1, projects[i], namespaces[i])
        }

        sys.updateKeys()
    }

    /**
     * Generates an instance of the State pattern
     * @param iteration
     * @param proj
     */
    void genPatternInstance1(int iteration, Project proj, Namespace ns) {
        // File and Type structure
        File fileA = File.builder().name("TypeA.java").type(FileType.SOURCE).relPath("TypeA.java").fileKey("TypeA.java").start(1).end(10).create()
        Type typeA = Type.builder().type(Type.CLASS).name("TypeA").compKey("TypeA").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileA.addType(typeA)
        ns.addFile(fileA)
        File fileB = File.builder().name("TypeB.java").type(FileType.SOURCE).relPath("TypeB.java").fileKey("TypeB.java").start(1).end(10).create()
        Type typeB = Type.builder().type(Type.CLASS).name("TypeB").compKey("TypeB").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileB.addType(typeB)
        ns.addFile(fileB)
        File fileC = File.builder().name("TypeC.java").type(FileType.SOURCE).relPath("TypeC.java").fileKey("TypeC.java").start(1).end(10).create()
        Type typeC = Type.builder().type(Type.CLASS).name("TypeC").compKey("TypeC").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileC.addType(typeC)
        ns.addFile(fileC)

        // Type Relationships

        // Pattern Structure
        Pattern p = Pattern.findFirst("patternKey = ?", "gof:state")
        Role a = Role.builder().name("roleA").roleKey("roleA").type(RoleType.CLASSIFIER).mandatory(true).create()
        Role b = Role.builder().name("roleB").roleKey("roleB").type(RoleType.CLASSIFIER).mandatory(true).create()
        Role c = Role.builder().name("roleC").roleKey("roleC").type(RoleType.CLASSIFIER).mandatory(true).create()
        p.addRole(a)
        p.addRole(b)
        p.addRole(c)

        // Pattern Instance Structure
        PatternInstance inst = PatternInstance.builder().instKey("state-" + iteration).create()
        RoleBinding rbA = RoleBinding.of(a, Reference.to(typeA))
        RoleBinding rbB = RoleBinding.of(b, Reference.to(typeB))
        RoleBinding rbC = RoleBinding.of(c, Reference.to(typeC))
        inst.addRoleBinding(rbA)
        inst.addRoleBinding(rbB)
        inst.addRoleBinding(rbC)

        if (iteration == 1 || iteration == 2) {
            File fileD = File.builder().name("TypeD.java").type(FileType.SOURCE).relPath("TypeD.java").fileKey("TypeD.java").start(1).end(10).create()
            Type typeD = Type.builder().type(Type.CLASS).name("TypeD").compKey("TypeD").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileD.addType(typeD)
            ns.addFile(fileD)

            Role d = Role.builder().name("roleD").roleKey("roleD").type(RoleType.CLASSIFIER).mandatory(false).create()
            p.addRole(d)

            RoleBinding rbD = RoleBinding.of(d, Reference.to(typeD))
            inst.addRoleBinding(rbD)

            if (iteration == 2) {
                File fileE = File.builder().name("TypeE.java").type(FileType.SOURCE).relPath("TypeE.java").fileKey("TypeE.java").start(1).end(10).create()
                Type typeE = Type.builder().type(Type.CLASS).name("TypeE").compKey("TypeE").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
                fileE.addType(typeE)
                ns.addFile(fileE)

                Role e = Role.builder().name("roleE").roleKey("roleE").type(RoleType.CLASSIFIER).mandatory(false).create()
                p.addRole(e)

                RoleBinding rbE = RoleBinding.of(e, Reference.to(typeE))
                inst.addRoleBinding(rbE)
            }
        }

        p.addInstance(inst)
        proj.addPatternInstance(inst)
    }

    /**
     * Generates an instance of the Strategy pattern
     * @param iteration
     * @param proj
     */
    void genPatternInstance2(int iteration, Project proj, Namespace ns) {
        // File and Type structure
        File fileF = File.builder().name("TypeF.java").type(FileType.SOURCE).relPath("TypeF.java").fileKey("TypeF.java").start(1).end(10).create()
        Type typeF = Type.builder().type(Type.CLASS).name("TypeF").compKey("TypeF").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileF.addType(typeF)
        ns.addFile(fileF)
        File fileG = File.builder().name("TypeG.java").type(FileType.SOURCE).relPath("TypeG.java").fileKey("TypeG.java").start(1).end(10).create()
        Type typeG = Type.builder().type(Type.CLASS).name("TypeG").compKey("TypeB").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileG.addType(typeG)
        ns.addFile(fileG)
        File fileH = File.builder().name("TypeH.java").type(FileType.SOURCE).relPath("TypeH.java").fileKey("TypeH.java").start(1).end(10).create()
        Type TypeH = Type.builder().type(Type.CLASS).name("TypeH").compKey("TypeH").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
        fileH.addType(TypeH)
        ns.addFile(fileH)

        // Type Relationships

        // Pattern Structure
        Pattern p = Pattern.findFirst("patternKey = ?", "gof:strategy")
        Role f = Role.builder().name("roleF").roleKey("roleF").type(RoleType.CLASSIFIER).mandatory(true).create()
        Role g = Role.builder().name("roleG").roleKey("roleG").type(RoleType.CLASSIFIER).mandatory(true).create()
        Role h = Role.builder().name("roleH").roleKey("roleH").type(RoleType.CLASSIFIER).mandatory(true).create()
        p.addRole(f)
        p.addRole(g)
        p.addRole(h)

        // Pattern Instance Structure
        PatternInstance inst = PatternInstance.builder().instKey("").create()
        RoleBinding rbF = RoleBinding.of(f, Reference.to(typeF))
        RoleBinding rbG = RoleBinding.of(g, Reference.to(typeG))
        RoleBinding rbH = RoleBinding.of(h, Reference.to(TypeH))
        inst.addRoleBinding(rbF)
        inst.addRoleBinding(rbG)
        inst.addRoleBinding(rbH)

        if (iteration == 2) {
            File fileI = File.builder().name("TypeI.java").type(FileType.SOURCE).relPath("TypeI.java").fileKey("TypeI.java").start(1).end(10).create()
            Type typeI = Type.builder().type(Type.CLASS).name("TypeI").compKey("TypeI").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileI.addType(typeI)
            ns.addFile(fileI)

            Role i = Role.builder().name("roleI").roleKey("roleI").type(RoleType.CLASSIFIER).mandatory(false).create()
            p.addRole(i)

            RoleBinding rbI = RoleBinding.of(i, Reference.to(typeI))
            inst.addRoleBinding(rbI)
        }

        p.addInstance(inst)
        proj.addPatternInstance(inst)
    }

    /**
     * Generates an instance of the template method pattern
     * @param iteration
     * @param proj
     */
    void genPatternInstance3(int iteration, Project proj, Namespace ns) {
        if (iteration == 1 || iteration == 3) {
            // File and Type structure
            File fileJ = File.builder().name("TypeJ.java").type(FileType.SOURCE).relPath("TypeJ.java").fileKey("TypeJ.java").start(1).end(10).create()
            Type typeJ = Type.builder().type(Type.CLASS).name("TypeJ").compKey("TypeJ").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileJ.addType(typeJ)
            ns.addFile(fileJ)
            File fileK = File.builder().name("TypeK.java").type(FileType.SOURCE).relPath("TypeK.java").fileKey("TypeK.java").start(1).end(10).create()
            Type typeK = Type.builder().type(Type.CLASS).name("TypeK").compKey("TypeK").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileK.addType(typeK)
            ns.addFile(fileK)
            File fileL = File.builder().name("TypeL.java").type(FileType.SOURCE).relPath("TypeL.java").fileKey("TypeL.java").start(1).end(10).create()
            Type typeL = Type.builder().type(Type.CLASS).name("TypeL").compKey("TypeL").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileL.addType(typeL)
            ns.addFile(fileL)

            // Type Relationships

            // Pattern Structure
            Pattern p = Pattern.findFirst("patternKey = ?", "gof:template_method")
            Role j = Role.builder().name("roleJ").roleKey("roleJ").type(RoleType.CLASSIFIER).mandatory(true).create()
            Role k = Role.builder().name("roleK").roleKey("roleK").type(RoleType.CLASSIFIER).mandatory(true).create()
            Role l = Role.builder().name("roleL").roleKey("roleL").type(RoleType.CLASSIFIER).mandatory(true).create()
            p.addRole(j)
            p.addRole(k)
            p.addRole(l)

            // Pattern Instance Structure
            PatternInstance inst = PatternInstance.builder().instKey("template_method-$iteration").create()
            RoleBinding rbJ = RoleBinding.of(j, Reference.to(typeJ))
            RoleBinding rbK = RoleBinding.of(k, Reference.to(typeK))
            RoleBinding rbL = RoleBinding.of(l, Reference.to(typeL))
            inst.addRoleBinding(rbJ)
            inst.addRoleBinding(rbK)
            inst.addRoleBinding(rbL)

            if (iteration == 2) {
                File fileM = File.builder().name("TypeM.java").type(FileType.SOURCE).relPath("TypeM.java").fileKey("TypeM.java").start(1).end(10).create()
                Type typeM = Type.builder().type(Type.CLASS).name("TypeM").compKey("TypeM").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
                fileM.addType(typeM)
                ns.addFile(fileM)

                Role m = Role.builder().name("roleM").roleKey("roleM").type(RoleType.CLASSIFIER).mandatory(false).create()
                p.addRole(m)

                RoleBinding rbM = RoleBinding.of(m, Reference.to(typeM))
                inst.addRoleBinding(rbM)
            }

            p.addInstance(inst)
            proj.addPatternInstance(inst)
        }
    }

    /**
     * Generates an instance of the Singleton pattern
     * @param iteration
     * @param proj
     */
    void genPatternInstance4(int iteration, Project proj, Namespace ns) {
        if (iteration == 3) {
            // File and Type structure
            File fileN = File.builder().name("TypeN.java").type(FileType.SOURCE).relPath("TypeN.java").fileKey("TypeN.java").start(1).end(10).create()
            Type typeN = Type.builder().type(Type.CLASS).name("TypeN").compKey("TypeN").accessibility(Accessibility.PUBLIC).start(1).end(10).create()
            fileN.addType(typeN)
            ns.addFile(fileN)

            // Type Relationships

            // Pattern Structure
            Pattern p = Pattern.findFirst("patternKey = ?", "gof:singleton")
            Role n = Role.builder().name("roleN").roleKey("roleN").type(RoleType.CLASSIFIER).mandatory(true).create()
            p.addRole(n)

            // Pattern Instance Structure
            PatternInstance inst = PatternInstance.builder().instKey("singleton-$iteration").create()
            RoleBinding rb1 = RoleBinding.of(n, Reference.to(typeN))
            inst.addRoleBinding(rb1)
            p.addInstance(inst)
            proj.addPatternInstance(inst)
        }
    }
}
