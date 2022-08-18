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
package dev.siliconcode.arc.experimenter.impl.pattern4

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.collector.FileCollector
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2
import groovy.xml.XmlSlurper

import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class Pattern4Collector extends FileCollector {

    Pattern4Tool owner
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")

    @Builder(buildMethodName = "create")
    Pattern4Collector(Pattern4Tool owner, String resultFile) {
        super(Pattern4Constants.PATTERN4_COLL_NAME, resultFile)
        this.owner = owner
    }

    @Override
    void execute(ArcContext context) {
        log.atInfo().log("Collecting Pattern4 reported patterns.")

        this.project = context.getProject()
        def data = new XmlSlurper().parseText(new File(resultsFile).text)

        // for each pattern instance's roles we need to modify the key to be consistent with the current keying scheme
        AtomicInteger instNum = new AtomicInteger(1)
//        GParsExecutorsPool.withPool(8) {
            data.pattern.each { pattern ->
                String patternName = pattern.@name
                log.atInfo().log("Pattern: $patternName")
                String rbmlName = this.owner.getProvider().rbmlNameFor(patternName)
                log.atInfo().log("RBML: $rbmlName")

                context.open()
                Pattern patt = null
                try {
                    patt = Pattern.find("patternKey = ?", "gof:$rbmlName".toString()).get(0)
                } catch (IndexOutOfBoundsException ex) {}
                context.close()

                pattern.instance.each { instance ->
                    context.open()
                    PatternInstance inst = PatternInstance.builder()
                            .instKey("${project.getProjectKey()}:$patternName-${instNum.getAndAdd(1)}")
                            .create()
                    context.close()

                    log.atInfo().log("Instance")
                    instance.role.each { role ->
                        String roleName = role.@name
                        String element = role.@element

                        log.atInfo().log("Role: $roleName  Element: $element")
                        String rbmlRole = this.owner.getProvider().rbmlRoleNameFor(rbmlName, roleName)
                        log.atInfo().log("RBML Role: $rbmlRole")

                        context.open()
                        log.atInfo().log("RoleName: $roleName")
                        log.atInfo().log("Looking for: $rbmlRole")
                        Role r = patt.getRoleByName(rbmlRole)
                        Component comp = getComponent(element)
                        if (!r || !comp) {
                            log.atInfo().log("r = $r")
                            log.atInfo().log("comp = $comp")
                        } else {
                            inst.addRoleBinding(RoleBinding.of(r, comp.createReference())) // figure out why comp is null
                        }
                        context.close()
                    }

                    context.open()
                    patt.addInstance(inst)
                    project.addPatternInstance(inst)
                    context.close()
                }
            }
//        }

        log.atInfo().log("Finished Collecting Pattern4 reported patterns.")
    }

    Component getComponent(String name) {
        String typeAndNS = name
        String member = null
        log.atInfo().log("Name: $name")
        if (name.contains("::")) {
            String[] components = name.split("::")
            typeAndNS = components[0]
            member = components[1]
        }

        log.info "Type and Namespace: $typeAndNS"
        Type type = project.findTypeByQualifiedName(typeAndNS)

        if (member && type) {
            String[] memberComps = member.split(":")
            String memberName = memberComps[0]

            log.atInfo().log("Member Name: $memberName")

            if (memberName.contains"(") {
                String methodName = memberName.substring(0, memberName.indexOf("("))
                int numParams
                if (memberName.contains"()")
                    numParams = 0
                else
                    numParams = 1 + memberName.count(",")

                log.atInfo().log("Num Params: $numParams")

                return type.getMethodWithNameAndNumParams(methodName, numParams)
            } else {
                return type.getFieldWithName(memberName)
            }
        } else {
            return type
        }
    }
}
