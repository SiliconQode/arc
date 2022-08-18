/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen.generators.pb

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.patterns.rbml.model.Classifier
import dev.siliconcode.arc.patterns.rbml.model.GeneralizationHierarchy
import dev.siliconcode.arc.patterns.rbml.model.Role
import dev.siliconcode.arc.patterns.rbml.model.SPS
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class PatternBuilder extends AbstractBuilder {

    Map<String, Integer> patternCounts = [:]

    def create() {
        if (ctx.patterns)
            log.info "Generating pattern ${++ctx.num}/${ctx.numInstances * ctx.patterns.size()}"
        if (!params.pattern)
            throw new IllegalArgumentException("create: pattern cannot be null or empty")
        if (!params.parent)
            throw new IllegalArgumentException("create: parent cannot be null")

        String pattern = params.pattern

        log.info "Generating Pattern of type: $pattern"

        Namespace ns = (Namespace) params.parent
        List<Project> projects = ns.getParentProjects()
        Project parentProj = null
        if (!projects.isEmpty())
            parentProj = projects.first()

        SPS rbml = ctx.loader.loadPattern(pattern)
        if (rbml) {
            ctx.relationBuilder.init(ns: ns, rbml: rbml)
            ctx.relationBuilder.create()
        }

        ctx.rbmlManager.getRoles().each {
            if (it instanceof Classifier)
                ctx.clsBuilder.createFeatures(it, pattern)
        }

        createInstance(rbml, pattern, parentProj)
    }

    private PatternInstance createInstance(SPS rbml, String pattern, Project parentProj) {
        if (!parentProj)
            return null

        PatternInstance inst = PatternInstance.builder()
                .instKey("${parentProj.projectKey}:${pattern}-${getCount(pattern)}")
                .create()
        parentProj.addPatternInstance(inst)

        def nameComps = []
        pattern.replace("_", ' ').split(/\s/).each {
            nameComps << it.capitalize()
        }
        String patternName = nameComps.join(" ")

        log.info "Creating an instance of ${patternName}"

        Pattern pat = (Pattern) Pattern.find("name = ?", patternName).first()
        if (pat) {
            pat.addInstance(inst)
            updateRoles(pat, rbml)

            ctx.rbmlManager.getRoles().each { Role role ->
                String name = role.name
                def roles = pat.getRoles().find { it.name == name }
                if (roles) {
                    def types = ctx.rbmlManager.getTypes(role)

                    roles.each { dev.siliconcode.arc.datamodel.Role r ->
                        types.each { Type t ->
                            RoleBinding binding = createRoleBinding(r, t)
                            if (binding != null)
                                inst.addRoleBinding(binding)
                            inst.save()
                        }
                    }
                }
            }

            ctx.rbmlManager.getFields().each { Field f ->
                Role role = ctx.rbmlManager.getRole(f)
                if (role)
                    processComponent(f, role, pat, inst)
            }

            ctx.rbmlManager.getMethods().each { Method m ->
                Role role = ctx.rbmlManager.getRole(m)
                processComponent(m, role, pat, inst)
            }
        }

        inst
    }

    private int getCount(String pattern) {
        if (patternCounts[pattern])
            patternCounts[pattern] += 1
        else
            patternCounts[pattern] = 1

        patternCounts[pattern]
    }

    private void processComponent(Component comp, Role role, Pattern pat, PatternInstance inst) {
        String name = role.getName()
        dev.siliconcode.arc.datamodel.Role r = dev.siliconcode.arc.datamodel.Role.findFirst("roleKey = ?", (String) "${pat.getPatternKey()}:${name}")
        if (r != null && comp != null) {
            RoleBinding binding = createRoleBinding(r, comp)
            if (binding != null) {
                inst.addRoleBinding(binding)
            }
            inst.save()
        }
    }

    private void updateRoles(Pattern pat, SPS rbml) {
        rbml.classifiers.each {
            if (it instanceof Classifier && !it.ghMember) {
                buildClassifierRole((Classifier) it, pat)
            }
        }

        rbml.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                GeneralizationHierarchy gh = (GeneralizationHierarchy) it

                buildClassifierRole(gh.root, pat)
                gh.children.each { Role r ->
                    buildClassifierRole((Classifier) r, pat)
                }
            }
        }

        rbml.relations.each {
            if (dev.siliconcode.arc.datamodel.Role.findFirst("roleKey = ?", (String) "${pat.patternKey}:${it.name}") == null) {
                dev.siliconcode.arc.datamodel.Role role = dev.siliconcode.arc.datamodel.Role.builder()
                        .name(it.name)
                        .roleKey("${pat.patternKey}:${it.name}")
                        .type(RoleType.RELATION)
                        .create()
                pat.addRole(role)
                pat.save()
            }
        }
    }

    private void buildClassifierRole(Classifier c, Pattern pat) {

        if (dev.siliconcode.arc.datamodel.Role.findFirst("roleKey = ?", (String) "${pat.patternKey}:${c.name}") == null) {
            dev.siliconcode.arc.datamodel.Role role = dev.siliconcode.arc.datamodel.Role.builder()
                    .name(c.name)
                    .roleKey("${pat.patternKey}:${c.name}")
                    .type(RoleType.CLASSIFIER)
                    .create()
            pat.addRole(role)
            pat.save()

            c.structFeats.each {
                if (dev.siliconcode.arc.datamodel.Role.findFirst("roleKey = ?", (String) "${pat.patternKey}:${it.name}") == null) {
                    dev.siliconcode.arc.datamodel.Role struct = dev.siliconcode.arc.datamodel.Role.builder()
                            .name(it.name)
                            .roleKey("${pat.patternKey}:${it.name}")
                            .type(RoleType.STRUCT_FEAT)
                            .create()
                    pat.addRole(struct)
                    pat.save()
                }
            }

            c.behFeats.each {
                if (dev.siliconcode.arc.datamodel.Role.findFirst("roleKey = ?", (String) "${pat.patternKey}:${it.name}") == null) {
                    dev.siliconcode.arc.datamodel.Role behav = dev.siliconcode.arc.datamodel.Role.builder()
                            .name(it.name)
                            .roleKey("${pat.patternKey}:${it.name}")
                            .type(RoleType.BEHAVE_FEAT)
                            .create()
                    pat.addRole(behav)
                    pat.save()
                }
            }
        }
    }

    private RoleBinding createRoleBinding(dev.siliconcode.arc.datamodel.Role role, Component comp) {
        if (role == null || comp == null)
            return null
        return RoleBinding.of(role, comp.createReference())
    }
}
