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
package dev.siliconcode.arc.disharmonies.injector.grime

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.InjectionFailedException
import dev.siliconcode.arc.disharmonies.injector.transform.model.file.AddTypeModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.module.AddNamespaceToModuleModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.namespace.AddFileModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.namespace.SplitNamespaceModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.project.AddNamespaceToProjectModelTransform
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2
import org.apache.commons.lang3.tuple.Pair

/**
 * Injector strategy for Modular type Organizational Grime
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ModularOrgGrimeInjector extends OrgGrimeInjector {

    /**
     * Flag indicating persistent (true) or temporary (false) org grime
     */
    protected boolean persistent
    /**
     * Flag indicating internal (true) or external (false) org grime
     */
    protected boolean internal
    /**
     * Flag indicating cyclical (true) or unstable (false) org grime
     */
    protected boolean cyclical
    /**
     * Static counter for generated types
     */
    protected static int generatedIndex = 1

    Type src
    Type dest

    /**
     * Constructs a new ModularOrgGrime Injector for the given pattern and parameterized by the provided flags
     * @param pattern Pattern to inject grime into
     * @param persistent Flag indicating persistent (true) or temporary (false) org grime
     * @param internal Flag indicating internal (true) or external (false) org grime
     * @param cyclical Flag indicating cyclical (true) or unstable (false) org grime
     */
    @Builder(buildMethodName = "create")
    ModularOrgGrimeInjector(PatternInstance pattern, boolean persistent, boolean internal, boolean cyclical) {
        super(pattern)
        this.persistent = persistent
        this.internal = internal
        this.cyclical = cyclical
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        log.info "Starting Injection"
        Namespace ns1, ns2
        RelationType rel

        if (internal) {
            (ns1, ns2) = selectPatternNamespace(2)
            if (!ns2) {
                (ns1, ns2) = splitNamespace(ns1, true)
            }
        } else {
            ns1 = selectPatternNamespace()[0]
            ns2 = selectOrCreateExternNamespace()
        }

        rel = selectRelationship(ns1, ns2, persistent)

        if (cyclical) {
            createCyclicalDependency(ns1, ns2, rel)
        } else {
            addInstability(ns1, ns2, rel)
        }

        createFinding(persistent, internal, cyclical, ns1)
        log.info "Injection Complete"
    }

    @Override
    void inject(Project proj, String ... params) {
        log.info "Starting Injection"
        proj.save()
        proj.refresh()
        pattern = proj.getPatternInstances().first()
        pattern.save()
        pattern.refresh()
        Namespace ns1 = proj.findNamespace(params[0])
        if (!ns1)
            ns1 = selectPatternNamespace()[0]
        Namespace ns2 = proj.findNamespace(params[1])
        ns1.save()
        ns1.refresh()

        if (internal && !ns2) {
            ns2 = selectPatternNamespace()
            if (!ns2)
                (ns1, ns2) = splitNamespace(ns1, true)
        } else ns2 = createExternNamespace(proj, params[1])

        RelationType rel = selectRelationship (ns1, ns2, persistent)

        if (cyclical) {
            createCyclicalDependency(ns1, ns2, rel)
        } else {
            addInstability(ns1, ns2, rel)
        }

        createFinding(persistent, internal, cyclical, ns1)
        log.info "Injection Complete"
    }

    void createFinding(boolean persistent, boolean internal, boolean cyclical, Namespace ns) {
        log.info "Creating Finding"
        if (persistent) {
            if (internal) {
                if (cyclical) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MPICG"]).injected().on(ns)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MPIUG"]).injected().on(ns)
                }
            } else {
                if (cyclical) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MPECG"]).injected().on(ns)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MPEUG"]).injected().on(ns)
                }
            }
        } else {
            if (internal) {
                if (cyclical) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MTICG"]).injected().on(ns)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MTIUG"]).injected().on(ns)
                }
            } else {
                if (cyclical) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MTECG"]).injected().on(ns)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["MTEUG"]).injected().on(ns)
                }
            }
        }
        log.info "Finding Created"
    }

    /**
     * Adds an instability to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def addInstability(Namespace srcNs, Namespace destNs, RelationType rel) {
        // increase number of abstract classes in srcNs

        // add an outgoing relationship from srcNs to destNs
        log.info "Adding Instability"
        createRelationship(rel, src, dest)
        log.info "Instability added"
    }

    /**
     * Adds a cycle to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def createCyclicalDependency(Namespace srcNs, Namespace destNs, RelationType rel) {
        log.info "Creating Cyclical Dependency"
        if (!hasRelationship(srcNs, destNs)) {
            createRelationship(rel, src, dest)
        }
        if (!hasRelationship(destNs, srcNs)) {
            List<Pair<Type, Type>> pairs = findTypePairs(destNs, srcNs, dest, src)
            if (pairs.isEmpty()) {
                createRelationship(rel, dest, src)
            } else {
                Pair<Type, Type> pair = pairs[rand.nextInt(pairs.size())]
                createRelationship(rel, pair.left, pair.right)
            }
        }
        log.info "Cyclical Dependency Created"
    }

    static boolean hasRelationship(Namespace srcNs, Namespace destNs) {
        if (!srcNs)
            throw new InjectionFailedException()
        if (!destNs)
            throw new InjectionFailedException()

        log.info "Checking for relationship"
        List<Type> srcTypes = srcNs.getAllTypes()
        def destTypes = destNs.getAllTypes()

        for (Type t : srcTypes) {
            Set<Type> types = new HashSet<>()
            types.addAll(t.getAssociatedTo())
            types.addAll(t.getRealizes())
            types.addAll(t.getUseTo())
            types.addAll(t.getGeneralizedBy())

            for (Type o : destTypes) {
                if (types.contains(o))
                    return true
            }
        }

        return false
    }

    private static def findTypePairs(Namespace srcNs, Namespace destNs, Type excludedSrc = null, Type excludedDest = null) {
        log.info "Finding Type Pairs"
        List<Pair<Type, Type>> pairs = []

        for (Type src : srcNs.getAllTypes()) {
            if (!excludedSrc || src != excludedSrc) {
                for (Type dest : destNs.getAllTypes()) {
                    if (!excludedDest || dest != excludedDest) {
                        if (!src.isAssociatedTo(dest) && !src.isGeneralizedBy(dest) && !src.isRealizing(dest)) {
                            pairs << Pair.of(src, dest)
                        }
                    }
                }
            }
        }

        pairs
    }

    def selectRelationship(Namespace srcNs, Namespace destNs, boolean persistent) {
        log.info "Selecting Relationship"
        src = selectOrCreateType(srcNs)
        dest = selectOrCreateType(destNs)
        if (persistent) {
            selectPersistentRel(src, dest)
        } else {
            selectTemporaryRel(src, dest)
        }
    }

    static def selectOrCreateType(Namespace namespace) {
        if (!namespace)
            throw new InjectionFailedException()

        log.info "Selecting/Creating Type"
        List<Type> types = namespace.getAllTypes()
        Type selected = null

        if (affectedEntities.size() < types.size()) {
            for (Type type : types) {
                if (!affectedEntities.contains(type.getCompKey())) {
                    selected = type
                    break
                }
            }
        }

        if (!selected) {
            int genIndex = generatedIndex++
            AddFileModelTransform addFile = new AddFileModelTransform(namespace, "GenExternalTypeMOG${genIndex}.java", FileType.SOURCE)
            addFile.execute()
            AddTypeModelTransform addType = new AddTypeModelTransform(addFile.file, "GenExternalTypeMOG${genIndex}", Accessibility.PUBLIC, "class")
            addType.execute()
            selected = addType.type
        }

        log.info "Type Selected/Created"
        affectedEntities << selected.getCompKey()
        selected
    }

    /**
     * Selects or creates an external namespace for use by the injector
     * @return the namespace selected or created
     */
    Namespace selectOrCreateExternNamespace() {
        log.info "Selecting/Creating External Namespace"
        Project project = pattern.getParentProject()
        List<Namespace> namespaces = project.getNamespaces()
        List<Namespace> patternNS = findPatternNamespaces()

        namespaces.removeAll(patternNS)

        namespaces = namespaces.findAll {
            it.getAllTypes().size() > 0
        }

        if (!namespaces.isEmpty()) {
            Collections.shuffle(namespaces)
            namespaces.first()
        } else {
            Module mod = project.getModules().first()
            AddNamespaceToModuleModelTransform trans = new AddNamespaceToModuleModelTransform(mod, "genexternns${generatedIndex}")
            trans.execute()
            Namespace ns = trans.ns
            ns.saveIt()
            ns.refresh()
            ns
        }
    }

    Namespace createExternNamespace(Project project, String name) {
        log.info "Creating External Namespace"
        Namespace ns = project.findNamespace(name)
        if (!ns) {
            AddNamespaceToProjectModelTransform trans = new AddNamespaceToProjectModelTransform(project, name)
            trans.execute()
            ns = trans.ns
            ns.saveIt()
        }
        ns.save()
        ns.refresh()
        ns
    }

    /**
     * Splits a given namespace into two or more namespaces
     * @param namespace Namespace to be split
     * @return a pair of namespaces split from the provided one
     */
    @Override
    RelationType selectRelationship(Type src, Type dest, boolean persistent) {
        return super.selectRelationship(src, dest, persistent)
    }

    def splitNamespace(Namespace namespace, boolean onPatternBoundary) {
        if (!namespace)
            throw new InjectionFailedException()
        if (namespace.getId() == null)
            throw new InjectionFailedException()

        log.info "Spliting Namespace"
        List<File> left = []
        List<File> right = []

        if (onPatternBoundary) {
            List<Type> patternTypes = pattern.getTypes()
            namespace.getAllTypes().each {
                if (patternTypes.contains(it))
                    left << it.getParentFile()
                else
                    right << it.getParentFile()
            }

            if (left.isEmpty()) {
                left << createPatternType(namespace).getParentFile()
            }
            if (right.isEmpty()) {
                right << createType(namespace).getParentFile()
            }
        } else {
            List<File> files = Lists.newArrayList(namespace.getFiles())
            Collections.shuffle(files)
            for (int i = 0; i < files.size() / 2; i++) {
                left << files[i]
            }

            for (int i = files.size() / 2; i < files.size(); i++) {
                right << files[i]
            }

            boolean isPattern = new Random().nextBoolean()
            if (left.isEmpty()) {
                left << isPattern ? createPatternType(namespace).getParentFile() : createType(namespace).getParentFile()
            }
            if (right.isEmpty()) {
                right << isPattern ? createPatternType(namespace).getParentFile() : createType(namespace).getParentFile()
            }
        }

        SplitNamespaceModelTransform trans = SplitNamespaceModelTransform.builder().ns(namespace).left(left).right(right).create()
        trans.execute()

        log.info "Namespace Split"
        [trans.ns1, trans.ns2]
    }
}
