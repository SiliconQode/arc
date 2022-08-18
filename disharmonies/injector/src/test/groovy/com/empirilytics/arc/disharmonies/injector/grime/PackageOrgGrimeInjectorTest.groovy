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

import com.google.common.graph.MutableGraph
import com.empirilytics.arc.datamodel.Namespace
import com.empirilytics.arc.datamodel.PatternInstance
import com.empirilytics.arc.datamodel.Type
import com.empirilytics.arc.disharmonies.injector.InjectionFailedException
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.testng.collections.Sets

@RunWith(JUnitParamsRunner.class)
class PackageOrgGrimeInjectorTest extends GrimeInjectorBaseTest {

    PackageOrgGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new PackageOrgGrimeInjector(inst, true, true)
    }

    @Test
    @Parameters([
            "true, false",
            "true, true",
            "false, false",
            "false, true"
    ])
    void inject(boolean internal, boolean closure) {
        // given
        fixture = new PackageOrgGrimeInjector(inst, internal, closure)

        // when
        try {
            fixture.inject()
        } catch (Exception e) {
            Assert.fail()
        }
    }

    @Test
    void "test selectPatternNamespace"() {
        // given
        List<Namespace> namespaces = [ns2, ns4, ns5]

        // when
        Namespace ns = fixture.selectPatternNamespace()[0]

        // then
        the(namespaces).shouldContain(ns)
    }

    @Test
    void "test selectExternalClass with namespace internal to pattern but with external classes"() {
        // given
        Namespace ns = ns2
        List<Type> types = [
                typeF, typeG, typeH
        ]

        // when
        Type type = fixture.selectExternalClass(ns)

        // then
        the(types).shouldContain(type)
    }

    @Test
    void "test selectExternalClass with empty namespace"() {
        // given
        Namespace ns = ns6

        // when
        Type type = fixture.selectExternalClass(ns)

        // then
        the(type).shouldBeNull()
    }

    @Test(expected = InjectionFailedException)
    void "test selectExternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }

    @Test
    void "test selectInternalClass with namespace internal to pattern"() {
        // given
        PatternInstance inst = PatternInstance.findFirst("instKey = ?", "Builder01")
        Set<Namespace> set = Sets.newHashSet()
        inst.getTypes().each {
            set.add(it.getParentNamespace())
        }
        Namespace ns = ns2

        // when
        Type type = fixture.selectOrCreateInternalClass(ns)

        // then
        the(set).shouldContain(type.getParentNamespace())
    }

    @Test
    void "test selectInternalClass with namespace external to pattern"() {
        // given
        Namespace ns = ns1

        // when
        Type type = fixture.selectOrCreateInternalClass(ns)

        // then
        the(type).shouldNotBeNull()
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectInternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }

    @Test
    void "test selectReachableNamespace happy path"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = ns2
        List<Namespace> reachable = [
                ns4,
                ns5
        ]

        // when
        Namespace result = fixture.selectOrCreateReachableNamespace(graph, ns)

        // then
        the(reachable).shouldContain(result)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectReachableNamespace null graph"() {
        // given
        MutableGraph<Namespace> graph = null
        Namespace ns = ns2

        // when
        fixture.selectOrCreateReachableNamespace(graph, ns)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectReachableNamespace null namespace"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = null

        // when
        fixture.selectOrCreateReachableNamespace(graph, ns)
    }

    @Test
    void "test selectUnreachableNamespace happy path"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = ns2
        List<Namespace> unreachable = [
                ns1,
                ns3
        ]

        // when
        Namespace result = fixture.selectOrCreateUnreachableNamespace(graph, ns)

        // then
        the(unreachable).shouldContain(result)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectUnreachableNamespace null graph"() {
        // given
        MutableGraph<Namespace> graph = null
        Namespace ns = ns2

        // when
        fixture.selectOrCreateUnreachableNamespace(graph, ns)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectUnreachableNamespace null namespace"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = null

        // when
        fixture.selectOrCreateUnreachableNamespace(graph, ns)
    }

    @Test
    void "test selectOrCreateExternalClass with empty namespace"() {
        // given
        Namespace ns = ns6

        // when
        Type type = fixture.selectOrCreateExternalClass(ns)

        // then
        the(type).shouldNotBeNull()
        the(type.getName()).shouldEqual("GenExternalType1")
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectOrCreateExternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }
}
