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

import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Pattern
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.RefType
import dev.siliconcode.arc.datamodel.Reference
import dev.siliconcode.arc.datamodel.Role
import dev.siliconcode.arc.datamodel.RoleBinding
import dev.siliconcode.arc.datamodel.Type
import groovy.util.logging.Log4j2

@Singleton
@Log4j2
class PatternInstanceReader {

    void read(Project project) {
        String path = project.getFullPath().replace("./", "")
        File file = new File(new File(path), "instance.conf")
        log.info "Reading file: $file"

        if (file.exists() && file.canRead()) {
            ConfigSlurper slurper = new ConfigSlurper()
            ConfigObject instData = slurper.parse(file.text)
            instData.flatten()

            Pattern pattern = Pattern.findFirst("patternKey = ?", instData.instance.pattern)
            PatternInstance inst = PatternInstance.builder().instKey(project.getProjectKey() + ":" + instData.instance.instKey).create()
            inst.save()
            project.addPatternInstance(inst)
            pattern.addInstance(inst)

            instData.instance.bindings.each { item ->
                log.info "Item values: $item"
                Role role = Role.findFirst("roleKey = ?", item.role)
                Reference ref = null
                switch (item.refType) {
                    case "TYPE":
                        Type t = Type.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = t.createReference()
                        break
                    case "METHOD":
                        Method m = Method.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = m.createReference()
                        break
                    case "NAMESPACE":
                        Namespace ns = Namespace.findFirst("nsKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = ns.createReference()
                        break
                    case "FIELD":
                        Field f = Field.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = f.createReference()
                        break
                }
                inst.addRoleBinding(RoleBinding.of(role, ref))
            }
        }
    }
}
