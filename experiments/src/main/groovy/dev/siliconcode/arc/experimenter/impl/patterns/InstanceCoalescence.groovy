/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
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
package dev.siliconcode.arc.experimenter.impl.patterns

import dev.siliconcode.arc.experimenter.impl.pattern4.resultsdm.Pattern
import dev.siliconcode.arc.experimenter.impl.pattern4.resultsdm.PatternInstance
import dev.siliconcode.arc.experimenter.impl.pattern4.resultsdm.Project

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class InstanceCoalescence {

    def coalesce(Project proj) {
        // 1. Partition instances by pattern
        Map<Pattern, List<PatternInstance>> patternInstanceMap = partition(proj)
        // 2. Expand each instance via generalization and realizations, but not via associations or dependencies
        patternInstanceMap.values().each {
            it.each { inst ->
                expand(inst)
            }
        }
        // 3. Compare and Combine
        patternInstanceMap.each { key, val ->
            insts.removeAll(compareAndCombine(val))
        }
    }

    Map<Pattern, List<PatternInstance>> partition(Project proj) {
        Map<Pattern, List<PatternInstance>> map = [:]
        proj.patterns.each { pattern ->
            pattern.instances.each { i ->
                if (map[pattern])
                    map[pattern] << i
                else
                    map[pattern] = [i]
            }
        }
        map
    }

    def compareAndCombine(List<PatternInstance> insts) {
        def toRemove = []
        insts.each { inst1 ->
            if (!toRemove.contains(inst1)) {
                Set<Reference> inst1Refs = [] as Set
                insts.each { inst2 ->
                    if (inst1 != inst2 && !toRemove.contains(inst2)) {
                        Set<Reference> inst2Refs = [] as Set

                        if (inst1Refs.intersect(inst2Refs)) {
                            inst1.merge(inst2)
                            toRemove << inst2
                        }
                    }
                }
            }
        }
        toRemove
    }

    def expand(PatternInstance inst) {
//        List<Type> knownTypes = []
//        inst.roles.each { Role r ->
//            def projKey = mediator.currentProject().projKey
//            Type t = mediator.findType("$projKey:${r.element}")
//            if (t) knownTypes << t
//        }
//
//        Set<Type> expansionTypes = [] as Set
//        knownTypes.each {
//            expansionTypes += mediator.getGeneralizedFrom(it)
//            expansionTypes += mediator.getRealizedFrom(it)
//            expansionTypes += mediator.getGeneralizedTo(it)
//            expansionTypes += mediator.getRealizedTo(it)
//        }
//
//        expansionTypes.removeAll(knownTypes)
//        expansionTypes.each {
//            inst.addRole(Role.builder().element(it.key()).create())
//        }
    }

    static void main(String[] args) {
        String[] s = ["com.google.common.cache.LocalCache",
                      "com.google.common.cache.LocalCache\$EntryFactory"]

        s.each {
            def group = (it =~ /((\w+\b\.)+)((\w+)\$?(\w+))((::)?)((\w+)\(.*\))?/)
            println group[0]
        }
    }
}
