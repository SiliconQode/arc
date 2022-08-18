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

import com.empirilytics.arc.datamodel.PatternInstance
import com.empirilytics.arc.disharmonies.injector.grime.ClassGrimeInjector
import com.empirilytics.arc.disharmonies.injector.grime.GrimeInjector
import com.empirilytics.arc.disharmonies.injector.grime.ModularGrimeInjector
import com.empirilytics.arc.disharmonies.injector.grime.ModularOrgGrimeInjector
import com.empirilytics.arc.disharmonies.injector.grime.PackageOrgGrimeInjector
import spock.lang.Specification

class InjectorFactoryTest extends Specification {

    def "test createInjector happy path"(PatternInstance inst, String type, String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector value = fixture.createInjector(inst, type, form)

        then:
        value in result

        where:
        inst | type    | form   | result
        null | "grime" | "pig"  | GrimeInjector.class
        null | "grime" | "what" | NullInjector.class
        null | "rot"   | "what" | NullInjector.class
        null | ""      | "pig"  | NullInjector.class
        null | "grime" | ""     | NullInjector.class
    }

    def "test createInjector exceptional path"(PatternInstance inst, String type, String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        fixture.createInjector(inst, type, form)

        then:
        thrown result

        where:
        inst | type    | form   | result
        null | null    | "pig"  | IllegalArgumentException.class
        null | "grime" | null   | IllegalArgumentException.class
    }

    def "test selectGrimeForm"(String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector val = fixture.selectGrimeForm(null, form)

        then:
        val in result

        where:
        form    | result
        "DIPG"  | ClassGrimeInjector.class
        "dipg"  | ClassGrimeInjector.class
        "DiPg"  | ClassGrimeInjector.class
        "disg"  | ClassGrimeInjector.class
        "depg"  | ClassGrimeInjector.class
        "iisg"  | ClassGrimeInjector.class
        "iesg"  | ClassGrimeInjector.class
        "iepg"  | ClassGrimeInjector.class
        "pig"   | ModularGrimeInjector.class
        "tig"   | ModularGrimeInjector.class
        "peeg"  | ModularGrimeInjector.class
        "peag"  | ModularGrimeInjector.class
        "teag"  | ModularGrimeInjector.class
        "teeg"  | ModularGrimeInjector.class
        "pecg"  | PackageOrgGrimeInjector.class
        "picg"  | PackageOrgGrimeInjector.class
        "perg"  | PackageOrgGrimeInjector.class
        "pirg"  | PackageOrgGrimeInjector.class
        "mpecg" | ModularOrgGrimeInjector.class
        "mpeug" | ModularOrgGrimeInjector.class
        "mpicg" | ModularOrgGrimeInjector.class
        "mpiug" | ModularOrgGrimeInjector.class
        "mtecg" | ModularOrgGrimeInjector.class
        "mteug" | ModularOrgGrimeInjector.class
        "mticg" | ModularOrgGrimeInjector.class
        "mtiug" | ModularOrgGrimeInjector.class
        ""      | NullInjector.class
    }

    def "test selectGrimeForm exceptional path"() {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector val = fixture.selectGrimeForm(null, null)

        then:
        thrown IllegalArgumentException
    }
}
