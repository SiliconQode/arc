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
import dev.siliconcode.arc.disharmonies.injector.transform.model.member.AddFieldUseModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.member.AddMethodCallModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddFieldGetterModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddFieldModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.type.AddFieldSetterModelTransform
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2

/**
 * Injection Strategy for Class Grime
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ClassGrimeInjector extends GrimeInjector {

    /**
     * Flag indicating direct (true) and indirect (false) class grime
     */
    protected boolean direct
    /**
     * Flag indicating internal (true) and external (false) class grime
     */
    protected boolean internal
    /**
     * Flag indicating pair (true) and singular (false) class grime
     */
    protected boolean pair

    private static int genIndex = 1

    /**
     * Constructs a new ClassGrimeInjector for the given pattern and parameterized by the given flags
     * @param pattern Pattern instance to be injected with class grime
     * @param direct Flag indicating either direct or indirect grime
     * @param internal Flag indicating either internal or external grime
     * @param pair Flag indicating either pair or singular grime
     */
    @Builder(buildMethodName = "create")
    ClassGrimeInjector(PatternInstance pattern, boolean direct, boolean internal, boolean pair) {
        super(pattern)
        this.direct = direct
        this.internal = internal
        this.pair = pair
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        log.info "Starting Injection"
        Type type = selectOrCreatePatternClass()

        Field field = createField(type, "test", selectType(type))

        Method method1 = null
        Method method2 = null

        while (!method1) {
            if (internal) {
                method1 = selectOrCreatePatternMethod(type)
            } else {
                method1 = selectOrCreateMethod(type, [])
            }
        }

        if (pair) {
//            while (!method2 && method2 != method1) {
            method2 = selectOrCreateMethod(type, [method1])
//            }
        }

        if (direct) {
            createFieldUse(method1, field)
            if (pair) {
                createFieldUse(method2, field)
            }
        } else {
            Method mutator = createGetter(type, field)
            createMethodCall(type, method1, mutator)
            if (pair) {
                createMethodCall(type, method2, mutator)
            }
        }

        createFinding(direct, internal, pair, method1)
        if (pair)
            createFinding(direct, internal, pair, method2)
        log.info "Injection Complete"
    }

    @Override
    void inject(Project proj, String ... params) {
        proj.save()
        proj.refresh()
        pattern = proj.getPatternInstances().first()
        pattern.save()
        pattern.refresh()

        Type type = pattern.getParentProject().findTypeByQualifiedName(params[0])
        log.info "Type found: $type"
        type.save()
        type.refresh()

        String name
        int numParam
        (name, numParam) = extractMethodInfo(params[1])
        Method method1 = type.getMethodWithNameAndNumParams(name, numParam)
        log.info "Method1 found: $method1"

        Method method2 = null
        if (params[2].contains("(")) {
            (name, numParam) = extractMethodInfo(params[2])
            method2 = type.getMethodWithNameAndNumParams(name, numParam)
            log.info "Method2 found: $method2"
        }

        if (!method1 && method2) {
            String temp = params[1]
            params[1] = params[2]
            params[2] = temp

            method1 = method2
            method2 = null
        }

        Component dest
        log.info "dest found: $dest"
        if (params.length == 4) {
            if (params[3].contains("(")) {
                (name, numParam) = extractMethodInfo(params[3])
                dest = type.getMethodWithNameAndNumParams(name, numParam)
            }
            else dest = type.getFieldWithName(params[3])
        } else {
            if (params[2].contains("(")) {
                (name, numParam) = extractMethodInfo(params[2])
                dest = type.getMethodWithNameAndNumParams(name, numParam)
            }
            else dest = type.getFieldWithName(params[2])
        }

        if (pair && !method2) {
            (name, numParam) = extractMethodInfo(params[2])
            method2 = createMethod(type, name, numParam)
            if (direct) {
                if (!dest) dest = createField(type, "genField${genIndex++}", selectType(type))
                createFieldUse(method2, (dest as Field))
            }
            else {
                if (dest instanceof Field) {
                    if (type.hasMethodWithName("get${dest.name.capitalize()}" as String))
                        dest = type.getMethodWithName("get${dest.name.capitalize()}" as String)
                    else dest = createGetter(type, (dest as Field))
                }
                createMethodCall(type, method2, (dest as Method))
            }
        } else {
            if (direct) {
                if (!dest) dest = createField(type, "genField${genIndex++}", selectType(type))
                createFieldUse(method1, (dest as Field))
            }
            else {
                if (dest instanceof Field) {
                    if (type.hasMethodWithName("get${dest.name.capitalize()}" as String))
                        dest = type.getMethodWithName("get${dest.name.capitalize()}" as String)
                    else dest = createGetter(type, (dest as Field))
                }
                createMethodCall(type, method1, (dest as Method))
            }
        }

        createFinding(direct, internal, pair, method1)
        if (pair)
            createFinding(direct, internal, pair, method2)
    }

    private String extractMethodName(String key) {
        if (key.contains("#")) key = key.split(/#/)[1]
        if (key.contains("(")) key = key.split(/\(/)[0]
        return key
    }

    void createFinding(boolean direct, boolean internal, boolean pair, Method method) {
        log.info "Creating finding"
        affectedEntities << method.getParentType().getCompKey()
        if (direct) {
            if (internal) {
                if (pair) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["DIPG"]).injected().on(method)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["DISG"]).injected().on(method)
                }
            } else {
                if (pair) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["DEPG"]).injected().on(method)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["DESG"]).injected().on(method)
                }
            }
        } else {
            if (internal) {
                if (pair) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["IIPG"]).injected().on(method)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["IISG"]).injected().on(method)
                }
            } else {
                if (pair) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["IEPG"]).injected().on(method)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["IESG"]).injected().on(method)
                }
            }
        }
        log.info "Finding created"
    }

    Type selectType(Type type) {
        log.info "Selecting Type"
        List<Type> knownTypes = type.getParentProject().getAllTypes()
        knownTypes.remove(type)

        if (knownTypes) {
            Collections.shuffle(knownTypes)
            return knownTypes.first()
        }

        null
    }

    /**
     * Selects a method from the given type that is specified by the pattern definition or used by such a method
     * @param type Type
     * @return a method that is part of the pattern and is found within the given type
     */
    Method selectOrCreatePatternMethod(Type type) {
        log.info "Selecting/Creating Pattern Method"
        if (!type)
            throw new InjectionFailedException()

        List<Method> methods = []
        pattern.getRoleBindings().each { RoleBinding rb ->
            if (rb.reference && rb.reference.type == RefType.METHOD) {
                Method method = Method.findFirst("compKey = ?", rb.reference.refKey)
                if (method && method.getParentType() == type)
                    methods << method
            }
        }

        if (methods.size() >= 1) {
            Collections.shuffle(methods)
            return methods.first()
        } else {
            Method method = createMethod(type, "roleBound")
            Role role = pattern.getRoles().find {
                it.getType() == RoleType.BEHAVE_FEAT
            }
            if (!role) {
                role = Role.builder().type(RoleType.BEHAVE_FEAT).name("_test_role_").roleKey("${pattern.getParentPattern().patternKey}:_test_role_").create()
            }
            pattern.getParentPattern().addRole(role)
            pattern.addRoleBinding(RoleBinding.of(role, method.createReference()))
            return method
        }
    }

    /**
     * Constructs a new field with the given name, in the given type
     * @param fieldName Name of the new field to create
     * @param type Type in which the new field will be contained
     * @return The field node to be created
     */
    protected Field createField(Type parent, String fieldName, Type type) {
        log.info "Creating Field"
        AddFieldModelTransform trans = new AddFieldModelTransform(parent, fieldName, type, Accessibility.PRIVATE)
        trans.execute()
        trans.getField()
    }

    /**
     * Constructs a new method with a field use for the specified field in the given type
     * @param methodName Name of the method to be created
     * @param type Type which will contain the new method and which already contains the field to be used
     * @param field the field to be referenced
     */
    protected void createMethodWithFieldUse(String methodName, Type type, Field field) {
        log.info "Creating Method with Field Use"
        Method extMethod = createMethod(type, methodName)
        createFieldUse(extMethod, field)
    }

    /**
     * Constructs a new field use for the given field in the given method
     * @param method Method into which to inject the field use
     * @param field the field to be used
     */
    protected void createFieldUse(Method method, Field field) {
        log.info "Creating Field Use"
        new AddFieldUseModelTransform(method, field).execute()
    }

    /**
     * Constructs a getter method contained in the given type and for the given field
     * @param type Type to contain the getter method
     * @param field Field the getter is for
     * @return the newly constructed getter method
     */
    protected Method createGetter(Type type, Field field) {
        log.info "Creating Getter"
        AddFieldGetterModelTransform trans = new AddFieldGetterModelTransform(type, field)
        trans.execute()
        trans.getMethod()
    }

    /**
     * Constructs a setter method contained in the given type and for the given field
     * @param type Type to contain the getter method
     * @param field Field the setter is for
     * @return the newly constructed setter method
     */
    protected Method createSetter(Type type, Field field) {
        log.info "Creating Setter"
        AddFieldSetterModelTransform trans = new AddFieldSetterModelTransform(type, field)
        trans.execute()
        trans.getMethod()
    }

    /**
     *
     * @param type
     * @param callee
     * @param call
     */
    def createMethodCall(Type type, Method callee, Method call) {
        log.info "Creating Method Call"
        new AddMethodCallModelTransform(callee, call).execute()
    }

    def extractMethodInfo(String s) {
        String[] comps = s.split(/\(/)
        String name = comps[0]
        String paramList = comps[1]
        paramList = paramList.replace(")", "")
        int numParam = 0
        if (!paramList.isBlank() && !paramList.isEmpty()) {
            numParam = paramList.split(/,/).size()
        }
        [name, numParam]
    }
}
