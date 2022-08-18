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

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.RelationType
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypeRef
import dev.siliconcode.arc.patterns.gen.cue.Cue
import dev.siliconcode.arc.patterns.gen.cue.CueManager
import dev.siliconcode.arc.patterns.rbml.model.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RelationshipBuilder extends AbstractComponentBuilder {

    Map<Role, Set<Type>> map = [:]
    Map<String, Map<String, Set<Type>>> ghmap = [:]

    def create() {
        if (!params.rbml)
            throw new IllegalArgumentException("create: rbml cannot be null")
        if (!params.ns)
            throw new IllegalArgumentException("create: ns cannot be null")

        Namespace ns = (Namespace) params.ns
        SPS sps = (SPS) params.rbml

        sps.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                ctx.ghBuilder.init(ns: ns, gh: (GeneralizationHierarchy) it)
                ctx.ghBuilder.ghmap = ghmap
                ctx.ghBuilder.create()
            }
        }

        sps.relations.each { r ->
            Relationship rel = r as Relationship
            processRole(ns, rel.source(), rel.getSrcPort())
            processRole(ns, rel.dest(), rel.getDestPort())
            selectAndCreateRelationship(rel)
        }
    }

    void processRole(Namespace ns, Role role, String port) {
        if (!role)
            throw new IllegalArgumentException("processRole: role cannot be null")
        if (role instanceof GeneralizationHierarchy && !port)
            throw new IllegalArgumentException("processRole: if role is a generalization hierarchy then port cannot be null or empty")

        if (role instanceof GeneralizationHierarchy) {
            processGHRole(ns, role, port)
        } else if (role instanceof Classifier) {
            generateRelClassifier(ns, (Classifier) role)
        }
    }

    Set<Type> generateRelClassifier(Namespace ns, Classifier role) {
        if (!role)
            throw new IllegalArgumentException("generateClassifier: role cannot be null")

        if (!ctx.rbmlManager.getTypes(role).isEmpty())
            return new HashSet<>(ctx.rbmlManager.getTypes(role))

        Random rand = new Random()
        int num

        if (role.mult.lower == role.mult.upper) {
            num = (Integer) (role.mult.upper < ctx.maxBreadth ? role.mult.upper : ctx.maxBreadth)
        } else if (role.mult.upper == -1) {
            num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
        } else {
            if (role.mult.upper < ctx.maxBreadth) {
                num = rand.nextInt(role.mult.upper) + role.mult.lower
            } else {
                num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
            }
        }

        boolean ghMember = isGHMember(role)
        Set<Type> types = Sets.newHashSet(ctx.rbmlManager.getTypes(role))

        if (types)
            num = num - types.size()

        if (!ghMember && num > 0) {
            num.times {
                ctx.clsBuilder.init(ns: ns, classifier: role)
                ctx.clsBuilder.create()
            }
            return new HashSet<>(ctx.rbmlManager.getTypes(role))
        }

        return new HashSet<>()
    }

    Set<Type> generateGHClassifier(Namespace ns, Classifier role, boolean ghRoot = false) {
        if (!role)
            throw new IllegalArgumentException("generateClassifier: role cannot be null")

        Random rand = new Random()
        int num

        if (ghRoot) {
            num = 1
        } else {
            if (role.mult.lower == role.mult.upper) {
                num = (Integer) (role.mult.upper < ctx.maxBreadth ? role.mult.upper : ctx.maxBreadth)
            } else if (role.mult.upper == -1) {
                num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
            } else {
                if (role.mult.upper < ctx.maxBreadth) {
                    num = rand.nextInt(role.mult.upper) + role.mult.lower
                } else {
                    num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
                }
            }
        }

        Set<Type> types = Sets.newHashSet(ctx.rbmlManager.getTypes(role))

        if (types)
            num = num - types.size()

        num.times {
            ctx.clsBuilder.init(ns: ns, classifier: role)
            ctx.clsBuilder.create()
        }
        return new HashSet<>(ctx.rbmlManager.getTypes(role))
    }

    private boolean isGHMember(Role role) {
        SPS sps = params.rbml as SPS
        sps.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                if ((it as GeneralizationHierarchy).root == role)
                    return true
                if ((it as GeneralizationHierarchy).children.contains(role))
                    return true
            }
        }
        return false
    }

    void processGHRole(Namespace ns, Role role, String port) {
        if (!role)
            throw new IllegalArgumentException("processGHRole: role cannot be null")
        if (!port)
            throw new IllegalArgumentException("processGHRole: port cannot be empty or null")

        Classifier toGen
        boolean isRoot = false
        boolean nonterm = false

        if (role instanceof GeneralizationHierarchy) {
            String name = role.name
            if (port == name) // root
            {
                // TODO Continue with this
                toGen = (Classifier) ((GeneralizationHierarchy) role).root
                isRoot = true
            } else {
                Role child = role.children.find {
                    it.name == port
                }
                if (child instanceof ClassRole) { // leaf
                    toGen = child
                } else { // root
                    toGen = (Classifier) child
                    nonterm = true
                }
            }
            Set<Type> generated = generateGHClassifier(ns, toGen, isRoot)
            if (!ghmap[name]) {
                ghmap[name] = new HashMap<String, Set<Type>>()
                ghmap[name]["roots"] = [] as Set<Type>
                ghmap[name]["leaves"] = [] as Set<Type>
                ghmap[name]["nonterms"] = [] as Set<Type>
            }

            if (isRoot) {
                ghmap[name]["roots"] += generated
            } else if (nonterm) {
                ghmap[name]["nonterms"] += generated
            } else {
                ghmap[name]["leaves"] += generated
            }
        }
    }

    void selectAndCreateRelationship(Relationship rel) {
        if (!rel)
            throw new IllegalArgumentException("selectAndCreateRelationship: rel cannot be null")

        String srcName = ""
        String destName = ""
        int srcUpper = 0
        int destUpper = 0
        if (rel instanceof Association) {
            srcName = rel.sourceName
            destName = rel.destName
            srcUpper = rel.sourceMult.upper
            destUpper = rel.destMult.upper
        }

        switch (rel) {
            case Generalization:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.GENERALIZATION, srcName, destName, srcUpper, destUpper)
                break
            case Realization:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.REALIZATION, srcName, destName, srcUpper, destUpper)
                break
            case Usage:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.USE, srcName, destName, srcUpper, destUpper)
                break
            case Aggregation || Composition:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.COMPOSITION, srcName, destName, srcUpper, destUpper)
                break
            case Association:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.ASSOCIATION, srcName, destName, srcUpper, destUpper)
                break
        }
    }

    List<Role> findGenHierarchyComponents(GeneralizationHierarchy gh, String port) {
        if (!gh)
            throw new IllegalArgumentException("findGenHierarchyComponents: gh cannot be null")
        if (!port)
            throw new IllegalArgumentException("findGenHierarchyComponents: port cannot be null or empty")

        List<Role> roles = []
        if (gh.name == port) {
            roles << gh.root
        } else {
            def role = gh.children.find {
                it.name == port
            }
            if (role)
                roles << role
        }

        roles
    }

    void prepareAndCreateRelationship(Role src, Role dest, String srcPort, String destPort, RelationType type, String srcName = "", String destName = "", int srcUpper = 0, int destUpper = 0) {
        if (!src)
            throw new IllegalArgumentException("prepareAndCreateRelationship: src cannot be null")
        if (!dest)
            throw new IllegalArgumentException("prepareAndCreateRelationship: dest cannot be null")
        if (src instanceof GeneralizationHierarchy && !srcPort)
            throw new IllegalArgumentException("prepareAndCreateRelationship: if src is instance of GeneralizationHierarchy then srcPort cannot be null or empty")
        if (dest instanceof GeneralizationHierarchy && !destPort)
            throw new IllegalArgumentException("prepareAndCreateRelationship: if dest is instance of GeneralizationHierarchy then destPort cannot be null or empty")
        if (!type)
            throw new IllegalArgumentException("prepareAndCreateRelationship: type cannot be null")

        List<Type> sources = ctx.rbmlManager.getTypes(src)
        List<Type> dests = ctx.rbmlManager.getTypes(dest)

        if (sources.size() == dests.size()) {
            for (int i = 0; i < sources.size(); i++) {
                createRelationship(sources[i], dests[i], type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() == 1 && dests.size() > 1 && sources.first()) {
            dests.each {
                createRelationship(sources.first(), it, type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() > 1 && dests.size() == 1 && dests.first()) {
            sources.each {
                createRelationship(it, dests[0], type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() >= 1 && dests.size() >= 1) {
            if (sources.size() < dests.size()) {
                for (int i = 1; i <= dests.size(); i++) {
                    createRelationship(sources[(sources.size() % i) - 1], dests[i - 1], type, srcName, destName, srcUpper, destUpper)
                }
            } else {
                for (int i = 1; i <= sources.size(); i++) {
                    createRelationship(sources[i - 1], dests[(dests.size() % i) - 1], type, srcName, destName, srcUpper, destUpper)
                }
            }
        }

    }

    void createRelationship(Type src, Type dest, RelationType type, String srcName = "", String destName = "", int srcUpper = 0, int destUpper = 0) {
        if (!src)
            throw new IllegalArgumentException("createRelationship: src cannot be null")
        if (!dest)
            throw new IllegalArgumentException("createRelationship: dest cannot be null")
        if (!type)
            throw new IllegalArgumentException("createRelationship: type cannot be null")

        switch (type) {
            case RelationType.GENERALIZATION:
                src.generalizedBy(dest)
                break
            case RelationType.COMPOSITION:
                src.composedTo(dest)
                createFields(src, dest, srcName, destName, srcUpper, destUpper)
                break
            case RelationType.ASSOCIATION:
                src.associatedTo(dest)
                createFields(src, dest, srcName, destName, srcUpper, destUpper)
                break
            case RelationType.REALIZATION:
                src.realizes(dest)
                break
            case RelationType.USE:
                src.useTo(dest)
                break
        }
    }

    private void createFields(Type src, Type dest, String srcName, String destName, int srcUpper, int destUpper) {
        Cue cue = CueManager.instance.getCurrent()

        if (!src.hasFieldWithName(destName)) {
            Accessibility access = Accessibility.PRIVATE
            if (src.isAbstract())
                access = Accessibility.PROTECTED

            TypeRef destRef = createTypeRef(dest)
            Field srcField = Field.builder()
                    .name(destName)
                    .accessibility(access)
                    .compKey(src.getCompKey() + "." + destName)
                    .type(destRef)
                    .create()
            srcField.save()
            src.addMember(srcField)
            srcField.updateKey()
            srcField.save()
            srcField.refresh()
            ctx.rbmlManager.addMapping(destName, srcField)
        }

        if (srcUpper == -1 && destUpper == -1) {
            if (!dest.hasFieldWithName(srcName)) {
                TypeRef srcRef = createTypeRef(src)
                Accessibility access = Accessibility.PRIVATE
                if (dest.isAbstract())
                    access = Accessibility.PROTECTED

                Field destField = Field.builder()
                        .name(srcName)
                        .accessibility(access)
                        .compKey(dest.getCompKey() + "." + srcName)
                        .type(srcRef)
                        .create()
                destField.save()
                dest.addMember(destField)
                destField.updateKey()
                destField.save()
                destField.refresh()
                ctx.rbmlManager.addMapping(srcName, destField)
            }
        }
    }

    @Override
    TypeRef createDefaultTypeRef() {
        return null
    }
}
