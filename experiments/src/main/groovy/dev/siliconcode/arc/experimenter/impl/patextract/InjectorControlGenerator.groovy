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
package dev.siliconcode.arc.experimenter.impl.patextract

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Finding
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.RefType
import dev.siliconcode.arc.datamodel.Type
import groovy.util.logging.Log4j2
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac D. Griffith
 * @version 1.3.0
 */
@Log4j2
class InjectorControlGenerator {

    List<Pair<PatternInstance, PatternInstance>> pairs
    File file

    void generate(PatternInstance first, PatternInstance second, File file) {
        this.file = file
        log.info "Started Injector Control File Generation"

        Set<String> baseSet = gatherGrime(first)
        Set<String> infSet = gatherGrime(second)
        Set<String> toGen = Sets.newHashSet(infSet)
        toGen.removeAll(baseSet)

        write(toGen)

        log.info "Completed Injector Control File Generation"
    }

    private Set<String> gatherGrime(PatternInstance inst) {
        Set<String> set = Sets.newHashSet()

        List<Finding> findings = Finding.getFindingsFor(inst.getRefKey())
        findings.each {
            set << convertFindingToInjectorControlString(inst, it)
        }

        return set
    }

    private String convertFindingToInjectorControlString(PatternInstance inst, Finding finding) {
        StringBuilder builder = new StringBuilder()
        String ruleName = finding.getParentRule().getName().split(" - ")[0]

        builder << ruleName
        finding.getReferences().each {
            if (it.getType() != RefType.PATTERN) {
                builder << ";"
                switch (it.getType()) {
                    case RefType.METHOD:
                        Method m = Method.findFirst("compKey = ?", it.getRefKey())
                        String sig = m.signature()
                        int ndex = sig.indexOf(m.getName())
                        builder << sig.substring(ndex)
                        break
                    case RefType.FIELD:
                        Field f = Field.findFirst("compKey = ?", it.getRefKey())
                        builder << f.getName()
                        break
                    case RefType.TYPE:
                        Type t = Type.findFirst("compKey = ?", it.getRefKey())
                        builder << t.getFullName()
                        break
                    case RefType.NAMESPACE:
                        Namespace t = Namespace.findFirst("nsKey = ?", it.getRefKey())
                        builder << t.getFullName()
                        break
                }
            }
        }

        return builder.toString()
    }

    private void write(Set<String> set) {
        if (file.exists())
            file.delete()
        file.createNewFile()

        file.text = set.toList().join("\n")
    }
}
