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

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.InjectionFailedException
import dev.siliconcode.arc.disharmonies.injector.SourceInjector
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.file.AddTypeModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.member.AddParameterUseModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.member.AddReturnTypeUseModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.namespace.AddFileModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddAssociationModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddGeneralizationModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddPrimitiveMethodModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddRealizationModelTransform
import groovy.util.logging.Log4j2

/**
 * Base class for Design Pattern Grime injectors
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
abstract class GrimeInjector implements SourceInjector {

    protected Random rand
    /**
     * The pattern into which the grime will be injected
     */
    protected PatternInstance pattern

    static int generatedIndex = 1

    static List<String> affectedEntities = []

    /**
     * Constructs a new GrimeInjector for the provided pattern instance
     * @param pattern Pattern instance into which grime will be injected
     */
    GrimeInjector(PatternInstance pattern) {
        this.pattern = pattern
        rand = new Random()
    }

    /**
     * Selects a type from the pattern instance, as the focus for the injection event
     * @return Type into which grime will be injected
     */
    Type selectOrCreatePatternClass() {
        log.info "Selecting/Creating Pattern Class"
        List<Type> types = pattern.getTypes()
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
            selected = createPatternType()
        }

        affectedEntities << selected.getCompKey()
        selected
    }

    Type createPatternType(Namespace ns = null) {
        log.info "Creating Pattern Type"
        Type type = createType(ns)
        Role role = pattern.getRoles().find {
            it.getType() == RoleType.CLASSIFIER
        }
        pattern.getParentPattern().addRole(role)
        pattern.addRoleBinding(RoleBinding.of(role, type.createReference()))

        type
    }

    Type createPatternType(String name) {
        log.info "Creating Pattern Type"
        Type type = createType(null, name)
        Role role = pattern.getRoles().find {
            it.getType() == RoleType.CLASSIFIER
        }
        pattern.getParentPattern().addRole(role)
        pattern.addRoleBinding(RoleBinding.of(role, type.createReference()))

        type
    }

    Type createType(Namespace ns = null) {
        log.info "Creating Type"
        if (!ns)
            ns = findPatternNamespaces().get(0)

        Type type = null
        boolean exception
        do {
            exception = false
            int genIndex = generatedIndex++
            try {
                AddFileModelTransform addFile = new AddFileModelTransform(ns, "GenExternalType${genIndex}.java", FileType.SOURCE)
                addFile.execute()
                addFile.file.refresh()
                AddTypeModelTransform addType = new AddTypeModelTransform(addFile.file, "GenExternalType${genIndex}", Accessibility.PUBLIC, "class")
                addType.execute()
                addFile.file.refresh()
                type = addType.type
                ns.addType(type)
                type.updateKey()
                type.refresh()
            } catch (ModelTransformPreconditionsNotMetException ex) {
                exception = true
            }
        } while (exception)

        type
    }

    Type createType(Namespace ns = null, String name) {
        log.info "Creating Type"
        if (!ns)
            ns = findPatternNamespaces().get(0)

        Type type = ns.getTypeByName(name)

        if (!type) {
            AddFileModelTransform addFile = new AddFileModelTransform(ns, "${name}.java", FileType.SOURCE)
            addFile.execute()
            addFile.file.refresh()
            AddTypeModelTransform addType = new AddTypeModelTransform(addFile.file, name, Accessibility.PUBLIC, "class")
            addType.execute()
            addFile.file.refresh()
            type = addType.type
            ns.addType(type)
            type.updateKey()
            type.refresh()
        }

        type
    }

    /**
     * Selects a relationship type to be used for the grime injection
     * @return Relationship type
     */
    RelationType selectRelationship(Type src, Type dest, boolean persistent) {
        log.info "Selecting Relationship"
        if (persistent) {
            selectPersistentRel(src, dest)
        } else {
            selectTemporaryRel(src, dest)
        }
    }

    /**
     * Selects a persistent relationship to inject between src and dest
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectPersistentRel(Type src, Type dest) {
        log.info "Selecting Persistent Relationship"
        if (src.getType() == Type.CLASS && dest.getType() == Type.CLASS) {
            if (src.isGeneralizedBy(dest) || src.getGeneralizedBy()) {
                return RelationType.ASSOC
            } else if (src.isAssociatedTo(dest)) {
                return RelationType.GEN
            } else {
                return RelationType.ASSOC
            }
        } else if (src.getType() == Type.CLASS && dest.getType() == Type.INTERFACE) {
            if (src.isRealizing(dest)) {
                return RelationType.ASSOC
            } else if (src.isAssociatedTo(dest)) {
                return RelationType.REAL
            } else {
                return RelationType.ASSOC
            }
        } else if (src.getType() == Type.INTERFACE && dest.getType() == Type.INTERFACE) {
            if (src.getGeneralizedBy())
                return RelationType.ASSOC
            else
                return RelationType.REAL
        } else {
            return RelationType.ASSOC
        }
    }

    /**
     * Selects a temporary relationship to inject between src and dest
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectTemporaryRel(Type src, Type dest) {
        log.info "Selecting Temporary Relationship"
        if (src.hasUseTo(dest)) {
            return null
        } else {
//            int val = rand.nextInt(3)
            int val = rand.nextInt(2)
            switch (val) {
                case 0:
                    return RelationType.USE_PARAM
                case 1:
                    return RelationType.USE_PARAM // should be USE_RET
//                case 2:
//                    return RelationType.USE_VAR
            }
        }
        null
    }

    /**
     * method which actually constructs the transform which will inject the grime relationship into the pattern instance
     * @param rel type of relationship to generate
     * @param src source type of the relationship
     * @param dest destination type of the relationship
     */
    static void createRelationship(RelationType rel, Type src, Type dest) {
        log.info "Creating Relationship"
        if (!src)
            throw new IllegalArgumentException("Src cannot be null")
        if (!dest)
            throw new IllegalArgumentException("Dest cannot be null")
        switch (rel) {
            case RelationType.ASSOC:
                new AddAssociationModelTransform(src, dest, dest.getName().uncapitalize(), src.getName().uncapitalize(), false).execute()
                break
            case RelationType.GEN:
                new AddGeneralizationModelTransform(src, dest).execute()
                break
            case RelationType.REAL:
                new AddRealizationModelTransform(src, dest).execute()
                break
            case RelationType.USE_PARAM:
                Method method = selectOrCreateMethod(src, [], true)
                new AddParameterUseModelTransform(method, dest).execute()
                break
            case RelationType.USE_RET:
                Method method = selectOrCreateMethod(src, [], true)
                new AddReturnTypeUseModelTransform(method, dest).execute()
                break
//            case RelationType.USE_VAR:
//                new AddVariableUseModelTransform(src, dest).execute()
//                break
        }
    }

    /**
     * Constructs a new method contained in the given type with the given name
     * @param type Type to contain the new method
     * @param methodName name of the new method
     * @return the newly created method
     */
    protected static Method createMethod(Type type, String methodName, int numParams = 0) {
        log.info "Creating Method"
        AddPrimitiveMethodModelTransform trans = new AddPrimitiveMethodModelTransform(type, methodName, "void", Accessibility.PUBLIC, numParams)
        trans.execute()
        return trans.getMethod()
    }

    /**
     * Selects or creates a method for grime injection from/in the given type
     * @param type Type to inject grime into
     * @return A method that was either selected/created from/in the given type
     */
    static Method selectOrCreateMethod(Type type, List<Method> selected, forParamInject = false) {
        if (!type)
            throw new InjectionFailedException()
        if (selected == null)
            throw new InjectionFailedException()

        log.info "Selecting/Creating Method"

        List<Method> methods = type.getAllMethods()
        methods.removeAll(selected)
        if (forParamInject)
            methods.removeAll { it.isOverriding() || it.isAbstract() || it instanceof Constructor || it instanceof Destructor }

        if (!methods.isEmpty()) {
            Collections.shuffle(methods)
            return methods.first()
        } else {
            return createMethod(type, "testMethod${generatedIndex++}")
        }
    }

    /**
     * Finds those namespaces in the data model which are pattern namespaces
     * @return List of pattern namespaces
     */
    List<Namespace> findPatternNamespaces() {
        log.info "Finding Pattern Namespaces"
        Set<Namespace> namespaces = [].toSet()
        pattern.getTypes().each {
            namespaces.add(it.getParentNamespace())
        }
        namespaces.toList()
    }
}
