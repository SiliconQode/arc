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
package dev.siliconcode.arc.disharmonies.injector

import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.disharmonies.injector.grime.ClassGrimeInjector
import dev.siliconcode.arc.disharmonies.injector.grime.ClassGrimeTypes
import dev.siliconcode.arc.disharmonies.injector.grime.ModularGrimeInjector
import dev.siliconcode.arc.disharmonies.injector.grime.ModularGrimeTypes
import dev.siliconcode.arc.disharmonies.injector.grime.ModularOrgGrimeInjector
import dev.siliconcode.arc.disharmonies.injector.grime.OrgGrimeTypes
import dev.siliconcode.arc.disharmonies.injector.grime.PackageOrgGrimeInjector

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class InjectorFactory {

    SourceInjector createInjector(PatternInstance inst, String type, String form) {
        if (type == null)
            throw new IllegalArgumentException("createInjector: type cannot be null")
        if (form == null)
            throw new IllegalArgumentException("createInjector: form cannot be null")

        switch (type.toLowerCase()) {
            case "grime":
                return selectGrimeForm(inst, form)
            case "rot":
                return selectRotForm(inst, form)
            default:
                return new NullInjector()
        }
    }

    SourceInjector selectGrimeForm(PatternInstance inst, String form) {
        if (form == null)
            throw new IllegalArgumentException("selectGrimeForm: form cannot be null")

        switch (form.toUpperCase()) {
            case ClassGrimeTypes.DIPG:
                return ClassGrimeInjector.builder().direct(true).internal(true).pair(true).pattern(inst).create()
            case ClassGrimeTypes.DISG:
                return ClassGrimeInjector.builder().direct(true).internal(true).pair(false).pattern(inst).create()
            case ClassGrimeTypes.DEPG:
                return ClassGrimeInjector.builder().direct(true).internal(false).pair(true).pattern(inst).create()
            case ClassGrimeTypes.DESG:
                return ClassGrimeInjector.builder().direct(true).internal(false).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IISG:
                return ClassGrimeInjector.builder().direct(false).internal(true).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IIPG:
                return ClassGrimeInjector.builder().direct(false).internal(true).pair(true).pattern(inst).create()
            case ClassGrimeTypes.IESG:
                return ClassGrimeInjector.builder().direct(false).internal(false).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IEPG:
                return ClassGrimeInjector.builder().direct(false).internal(false).pair(true).pattern(inst).create()
            case ModularGrimeTypes.PIG:
                return ModularGrimeInjector.builder().persistent(true).external(false).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.TIG:
                return ModularGrimeInjector.builder().persistent(false).external(false).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.TEEG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(true).pattern(inst).create()
            case ModularGrimeTypes.TEAG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.PEEG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(true).pattern(inst).create()
            case ModularGrimeTypes.PEAG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(false).pattern(inst).create()
            case OrgGrimeTypes.PECG:
                return PackageOrgGrimeInjector.builder().internal(false).closure(true).pattern(inst).create()
            case OrgGrimeTypes.PERG:
                return PackageOrgGrimeInjector.builder().internal(false).closure(false).pattern(inst).create()
            case OrgGrimeTypes.PICG:
                return PackageOrgGrimeInjector.builder().internal(true).closure(true).pattern(inst).create()
            case OrgGrimeTypes.PIRG:
                return PackageOrgGrimeInjector.builder().internal(true).closure(false).pattern(inst).create()
            case OrgGrimeTypes.MPECG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(false).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MPEUG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(false).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MPICG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(true).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MPIUG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(true).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MTECG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(false).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MTEUG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(false).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MTICG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(true).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MTIUG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(true).cyclical(false).pattern(inst).create()
            default:
                return new NullInjector()
        }
    }

    SourceInjector selectRotForm(PatternInstance inst, String form) {
        return new NullInjector()
    }
}
