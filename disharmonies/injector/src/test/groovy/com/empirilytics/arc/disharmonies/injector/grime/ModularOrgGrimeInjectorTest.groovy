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
package com.empirilytics.arc.disharmonies.injector.grime

import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.datamodel.PatternInstance
import com.empirilytics.arc.datamodel.Role
import com.empirilytics.arc.datamodel.RoleBinding
import com.empirilytics.arc.datamodel.RoleType
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.InjectionFailedException
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ModularOrgGrimeInjectorTest extends GrimeInjectorBaseTest {

    ModularOrgGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new ModularOrgGrimeInjector(inst, true, true, true)
    }

    @Test
    @Parameters([
            "true, false, false",
            "true, false, true",
            "true, true, false",
            "true, true, true",
            "false, false, false",
            "false, false, true",
            "false, true, false",
            "false, false, true"
    ])
    void inject(boolean persistent, boolean internal, boolean cyclical) {
        // given
        // try {
        fixture = new ModularOrgGrimeInjector(inst, persistent, internal, cyclical)
        // } catch (Exception e) {
        //     Assert.fail()
        // }
    }

    @Test
    void "test hasRelationship happy path true"() {
        // given
        Namespace src = ns4
        Namespace dest = ns2

        // when
        boolean result = ModularOrgGrimeInjector.hasRelationship(src, dest)

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test hasRelationship happy path false"() {
        // given
        Namespace src = ns2
        Namespace dest = ns4

        // when
        boolean result = ModularOrgGrimeInjector.hasRelationship(src, dest)

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = InjectionFailedException.class)
    void "test hasRelationship src ns is null"() {
        // given
        Namespace src = null
        Namespace dest = ns4

        // when
        ModularOrgGrimeInjector.hasRelationship(src, dest)
    }

    @Test(expected = InjectionFailedException.class)
    void "test hasRelationship dest ns is null"() {
        // given
        Namespace src = ns4
        Namespace dest = null

        // when
        ModularOrgGrimeInjector.hasRelationship(src, dest)
    }

    @Test
    void "test selectType happy path"() {
        // given
        Namespace ns = ns2

        // when
        Type type = fixture.selectOrCreateType(ns)

        // then
        the(ns.getAllTypes()).shouldContain(type)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectType null namespace"() {
        // given
        Namespace ns = null

        // when
        Type type = fixture.selectOrCreateType(ns)
    }

    @Test
    void "test selectOrCreateExternNamespace happy path"() {
        // given
        def namespaces = [ns1, ns3]

        // when
        Namespace ns = fixture.selectOrCreateExternNamespace()

        // then
        the(namespaces).shouldContain(ns)
    }

    @Test
    void "test selectOrCreateExternNamespace create extern happy path"() {
        // given
        Role roleF = Role.builder().name("RoleF").type(RoleType.CLASSIFIER).create()
        Role roleH = Role.builder().name("RoleH").type(RoleType.CLASSIFIER).create()
        inst.getParentPattern().addRole(roleF)
        inst.getParentPattern().addRole(roleH)
        RoleBinding rbF = RoleBinding.of(roleF, typeF.createReference())
        RoleBinding rbH = RoleBinding.of(roleH, typeH.createReference())
        inst.addRoleBinding(rbF)
        inst.addRoleBinding(rbH)

        // when
        Namespace ns = fixture.selectOrCreateExternNamespace()

        // then
        the(ns.getName()).shouldBeEqual("genexternns1")
    }

    @Test
    void "test splitNamespace happy path no boundary"() {
        // given
        boolean boundary = false

        // when
        Namespace n1, n2
        (n1, n2) = fixture.splitNamespace(ns2, boundary)

        // then
        the(n1.getFiles().size()).shouldBeEqual(2)
        the(n2.getFiles().size()).shouldBeEqual(2)
    }

    @Test
    void "test splitNamespace happy path on boundary"() {
        // given
        boolean boundary = true

        // when
        Namespace n1, n2
        (n1, n2) = fixture.splitNamespace(ns2, boundary)

        // then
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeB.java"))
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeD.java"))
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeE.java"))
//        the(n2.getFiles()).shouldContain(File.findFirst("name = ?", "TypeG.java"))
    }

    @Test(expected = InjectionFailedException.class)
    void "test splitNamespace null namespace"() {
        // given
        boolean boundary = true

        // when
        Namespace n1, n2
        fixture.splitNamespace(null, boundary)
    }

    @Test
    void "test findPatternNamespaces happy path"() {
        // given
        List<Namespace> namespaces = [ns2, ns4, ns5]

        // when
        def result = fixture.findPatternNamespaces()

        // then
        result.each {
            the(namespaces).shouldContain(it)
        }
    }
}
