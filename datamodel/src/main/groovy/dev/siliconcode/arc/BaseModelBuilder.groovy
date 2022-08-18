/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc

import dev.siliconcode.arc.datamodel.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class BaseModelBuilder {

    System system
    Project proj
    Namespace namespace
    File file
    Stack<Type> types = []
    Stack<Member> methods = []
    Stack<Set<String>> scopes = []
    TemplateParam currentTypeParam
    Parameter currentParam

    BaseModelBuilder(Project proj, File file) {
        if (!proj || !file) {
            throw new IllegalArgumentException("Project and File must not be null")
        }
        if (proj.getParentSystem() == null) {
            throw new IllegalArgumentException("Project must be a part of a system")
        }
        if (proj.getModules().size() < 1) {
            throw new IllegalArgumentException("Project must have at least one module")
        }

        this.proj = proj
        this.system = proj.getParentSystem()
        this.file = file
        if (file.getParentNamespace() != null)
            namespace = file.getParentNamespace()
    }

    //////////////////////
    // Setup Methods
    //////////////////////
    void setFile(File file) {
        this.file = file
        if (file.getParentNamespace() != null) {
            namespace = file.getParentNamespace()
        }
    }

    //////////////////////
    // Structures
    //////////////////////
    void createNamespace(String name) {
        Namespace current = namespace
        if (current == null) {
            String[] packageNames = name.split(/\./)

            for (String seg : packageNames) {
                String pKey = current == null ? "" : current.getNsKey()
                String key = pKey.isEmpty() ? seg : String.join(".", pKey, seg) // TODO fix this

                if (!proj.hasNamespace(key)) {
                    Namespace parent = current
                    current = Namespace.builder()
                            .name(key)
                            .nsKey(key)
                            .relPath(key)
                            .create()
                    if (parent != null)
                        parent.addNamespace(current)
                    else
                        proj.getModules().get(0).addNamespace(current)
                    current.updateKey()
                } else {
                    Namespace parent = current
                    setParentNamespace(parent, current)
                    current = proj.findNamespace(key)
                }
            }

            current.addFile(file)
            namespace = current
            file.updateKey()
        }
    }

    void createImport(String name, int start, int end) {
        Import imp = Import.builder().name(name).start(start).end(end).create()

        file.addImport(imp)
    }

    //////////////////////
    // Types
    //////////////////////

    void endType() {
        if (!types) {
            Type type = types.pop()
            if (types.isEmpty())
                file.addType(type)
            else
                types.peek().addType(type)
            type.updateKey()
        }
    }

    void findClass(String name) {
        Type type
        if (types)
            type = Type.findFirst("key = ?", (String) "${types.peek().getCompKey()}.$name")
        else
            type = Type.findFirst("key = ?", (String) "${file.getFileKey()}:$name")
        types.push(type)
    }

    void createClass(String name, int start, int stop) {
        if (types.peek().getTypeByName(name) != null)
            types.push(types.peek()?.getTypeByName(name))
        else if (file.getTypeByName(name) != null) {
            types.push(file.getTypeByName(name))
        } else {
            Type cls = Type.builder().type(Type.CLASS)
                    .name(name)
                    .compKey(name)
                    .accessibility(Accessibility.PUBLIC)
                    .start(start)
                    .end(stop)
                    .create()
            if (types) {
                types.peek().addType(cls)
            } else {
                file.addType(cls)
            }
            cls.updateKey()
            types.push(cls)
        }
    }

    void findEnum(String name) {
        Type type
        if (types)
            type = Type.findFirst("key = ?", (String) "${types.peek().getCompKey()}.$name")
        else
            type = Type.findFirst("key = ?", (String) "${file.getFileKey()}:$name")
        types.push(type)
    }

    void createEnum(String name, int start, int stop) {
        if (types && types.peek().getTypeByName(name) != null)
            types.push(types.peek()?.getTypeByName(name))
        else if (file.getTypeByName(name) != null) {
            types.push(file.getTypeByName(name))
        } else {
            Type enm = Type.builder().type(Type.ENUM)
                    .name(name)
                    .compKey(name)
                    .accessibility(Accessibility.PUBLIC)
                    .start(start)
                    .end(stop)
                    .create()
            if (types) {
                types.peek().addType(enm)
            } else {
                file.addType(enm)
            }
            enm.updateKey()
            types.push(enm)
        }
    }

    void findInterface(String name) {
        Type type
        if (types)
            type = Type.findFirst("key = ?", (String) "${types.peek().getCompKey()}.$name")
        else
            type = Type.findFirst("key = ?", (String) "${file.getFileKey()}:$name")
        types.push(type)
    }

    void createInterface(String name, int start, int stop) {
        if (types.peek().getTypeByName(name) != null)
            types.push(types.peek()?.getTypeByName(name))
        else if (file.getTypeByName(name) != null) {
            types.push(file.getTypeByName(name))
        } else {
            Type ifc = Type.builder()
                    .type(Type.INTERFACE)
                    .name(name)
                    .compKey(name)
                    .accessibility(Accessibility.PUBLIC)
                    .start(start)
                    .end(stop)
                    .create()
            if (types) {
                types.peek().addType(ifc)
            } else {
                file.addType(ifc)
            }
            ifc.updateKey()
            types.push(ifc)
        }
    }

    void findAnnotation(String name) {
        findInterface(name)
    }

    void createAnnotation(String name, int start, int stop) {
        createInterface(name, start, stop)
    }

    void setTypeModifiers(List<String> modifiers) {
        if (types) {
            modifiers.each { mod ->
                Accessibility access = handleAccessibility(mod)
                Modifier modifier = handleNamedModifiers(mod)
                if (access) types.peek().setAccessibility(access)
                if (modifier) types.peek().addModifier(modifier)
            }
        }
    }

    void createTypeTypeParameter(String name) {
        if (types) {
            if (types.peek().hasTemplateParam(name))
                currentTypeParam = types.peek().getTemplateParam(name)
            else {
                currentTypeParam = TemplateParam.builder().name(name).create()

                if (types) {
                    types.peek().addTemplateParam(currentTypeParam)
                }
            }
        }
    }

    ///////////////////
    // Methods
    ///////////////////

    void findInitializer(String name, boolean instance) {
        Initializer init = types ? Initializer.findFirst("key = ?", (String) "${types.peek().getCompKey()}:$name") : null
        if (init) methods.push(init)
    }

    void createInitializer(String name, boolean instance, int start, int end) {
        if (types) {
            if (types.peek().hasInitializerWithName(name)) {
                methods.push(types.peek().getInitializerWithName(name))
            } else {
                Initializer init = Initializer.builder()
                        .name(name)
                        .compKey(name)
                        .accessibility(Accessibility.PUBLIC)
                        .start(start)
                        .end(end)
                        .instance(instance)
                        .create()
                types.peek().addMember(init)
                init.updateKey()
                methods.push(init)
            }
        }
    }

    void findMethod(String signature) {
        Method meth = types ? Method.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#$signature") : null
        if (meth) methods.push(meth)
    }

    void createMethod(String name, int start, int end) {
        if (types) {
            if (types.peek().hasMethodWithName(name)) {
                methods.push(types.peek().getMethodWithName(name))
            } else {
                Method meth = Method.builder()
                        .name(name)
                        .compKey(name)
                        .accessibility(Accessibility.PUBLIC)
                        .start(start)
                        .end(end)
                        .create()
                types.peek().addMember(meth)
                meth.updateKey()
                methods.push(meth)
            }
        }
    }

    void findConstructor(String signature) {
        Constructor cons = types ? Constructor.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#$signature") : null
        if (cons) methods.push(cons)
    }

    void createConstructor(String name, int start, int end) {
        if (types) {
            Constructor cons = Constructor.creator()
                    .name(name)
                    .compKey(name)
                    .accessibility(Accessibility.PUBLIC)
                    .start(start)
                    .end(end)
                    .create()
            types.peek().addMember(cons)
            cons.updateKey()
            methods.push(cons)
        }
    }

    void createMethodParameter() {
        currentParam = Parameter.builder().varg(false).create()
        if (methods && methods.peek() instanceof Method)
            ((Method) methods.peek()).addParameter(currentParam)
    }

    void setVariableParameter(boolean varg) {
        currentParam.setVarg(varg)
    }

    void addParameterModifier(String mod) {
        currentParam.addModifier(mod)
    }

    void setMethodModifiers(List<String> mods) {
        if (methods) {
            mods.each { mod ->
                Accessibility access = handleAccessibility(mod)
                Modifier modifier = handleNamedModifiers(mod)
                if (access) methods.peek().setAccessibility(access)
                if (modifier) methods.peek().addModifier(modifier)
            }
        }
    }

    void createMethodTypeParameter(String name) {
        currentTypeParam = TemplateParam.builder().name(name).create()
        if (methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).addTemplateParam(currentTypeParam)
        }
    }

    void setParameterType(String type) {
        Type t = findType(type)
        currentParam.setType(t.createTypeRef())
    }

    void setParameterPrimitiveType(String type) {
        currentParam.setType(TypeRef.createPrimitiveTypeRef(type))
    }

    void setMethodReturnType(String type) {
        Type t = findType(type)
        if (t && methods && methods.peek() instanceof Method)
            ((Method) methods.peek()).setReturnType(t.createTypeRef())
    }

    void setMethodReturnTypeVoid() {
        if (methods && methods.peek() instanceof Method)
            ((Method) methods.peek()).setReturnType(TypeRef.createPrimitiveTypeRef("void"))
    }

    void setMethodReturnPrimitiveType(String type) {
        if (methods && methods.peek() instanceof Method)
            ((Method) methods.peek()).setReturnType(TypeRef.createPrimitiveTypeRef(type))
    }

    void addMethodException(String type) {
        Type t = findType(type)
        if (t && methods && methods.peek() instanceof Method)
            ((Method) methods.peek()).addException(t.createTypeRef())
    }

    void finishMethod() {
        methods.pop()
    }

    ///////////////////
    // Fields
    ///////////////////
    void createField(String name, String fieldType, boolean primitive, int start, int end, List<String> modifiers) {
        if (types) {
            if (types.peek().hasFieldWithName(name))
                return
            else {
                Field field = Field.builder()
                        .name(name)
                        .compKey(name)
                        .accessibility(Accessibility.PUBLIC)
                        .start(start)
                        .end(end)
                        .create()
                if (primitive)
                    field.setType(TypeRef.createPrimitiveTypeRef(fieldType))
                else {
                    Type t = findType(fieldType)
                    if (t) field.setType(t.createTypeRef())
                }
                setFieldModifiers(field, modifiers)
            }
        }
    }

    void createEnumLiteral(String name, int start, int end) {
        if (types && types.peek().getType() == Type.ENUM) {
            Type enm = types.peek()
            if (enm.hasLiteralWithName(name))
                return
            else {
                Literal lit = Literal.builder()
                        .name(name)
                        .compKey(name)
                        .start(start)
                        .end(end)
                        .create()
                enm.addMember(lit)
                lit.updateKey()
            }
        }
    }

    void setFieldModifiers(Field field, List<String> modifiers) {
        modifiers.each { mod ->
            Accessibility access = handleAccessibility(mod)
            Modifier modifier = handleNamedModifiers(mod)
            if (access) field.setAccessibility(access)
            if (modifier) field.addModifier(modifier)
        }
    }

    ///////////////////
    // Relationships
    ///////////////////

    void addRealization(String typeName) {
        if (types) {
            types.peek().realizes(findType(typeName))
        }
    }

    void addGeneralization(String typeName) {
        if (types) {
            types.peek().generalizedBy(findType(typeName))
        }
    }

    void addAssociation(Type type) {
        if (types) {
            types.peek().associatedTo(type)
        }
    }

    void addUseDependency(Type type) {
        if (types) {
            types.peek().useTo(type)
        }
    }

    void addUseDependency(String type) {
        if (types) {
            types.peek().useTo(findType(type))
        }
    }

    void addLocalVarToScope(String varName) {
        if (scopes) {
            scopes.peek().add(varName)
        }
    }

    void processExpression(String expr) {
        expr = cleanExpression(expr)
        def list = expr.split(/\s+/)

        Type type
        list.each { str ->
            String[] components = str.split(/\./)
            components.eachWithIndex { comp, index ->
                if (!comp.contains("(")) {
                    type = handleNonMethodCall(type, comp, components, index)
                } else {
                    String args = comp.substring(comp.indexOf("(") + 1, comp.indexOf(")"))
                    int numParams = args.split(",").size()
                    type = handleMethodCall(type, comp, numParams)
                }
            }
        }
    }

    abstract String cleanExpression(String expr)

    Type handleNonMethodCall(Type type, String comp, String[] components, int index) {
        switch (comp) {
            case "this":
                return handleThis(type)
            case "super":
                String name = components[index + 1]
                return handleSuper(type, name)
            default:
                return handleIdentifier(type, comp)
        }
    }

    Type handleMethodCall(Type type, String comp, int numParams) {
        if (comp.startsWith("super")) {
            if (type) {
                findSuperTypeWithNParamConstructor(type, numParams)
            } else {
                findSuperTypeWithNParamConstructor(types.peek(), numParams)
            }
            return createConstructorCall(type, numParams)
        } else if (comp.startsWith("this")) {
            if (!type)
                type = types.peek()
            return createConstructorCall(type, numParams)
        } else {
            if (!type)
                type = types.peek()
            return createMethodCall(type, comp, numParams)
        }
    }

    Type handleThis(Type type) {
        if (!type)
            return ((Stack<Type>) types).peek()
        return type
    }

    Type handleSuper(Type type, String name) {
        if (type) {
            return findSuperTypeWithTypeFieldOrMethodNamed(type, name)
        } else {
            return findSuperTypeWithTypeFieldOrMethodNamed(types.peek(), name)
        }
    }

    Type handleIdentifier(Type type, String comp) {
        if (type) {
            if (type.hasFieldWithName(comp)) {
                return createFieldUse(type, comp)
            } else {
                return type.getTypeByName(comp)
            }
        } else {
            if (checkIsLocalVar(comp)) {
                // TODO Finish me
                // set type
            } else if (types.peek().hasFieldWithName(comp)) {
                return createFieldUse(types.peek(), comp)
            } else if (types.peek().hasTypeWithName(comp)) {
                return types.peek().getTypeByName(comp)
            }
        }

        return null
    }

    Type createMethodCall(Type current, String comp, int numParams) {
        Type type = current
        Method called = findMethodCalled(type, comp, numParams)

        if (type && called) {
            def m
            if (((Stack<Member>) methods).peek() instanceof Method) {
                m = (Method) ((Stack<Member>) methods).peek()
                if (((Method) m).getType().getType() == TypeRefType.Type)
                    type = m.getType().getType(proj.getProjectKey())
            } else if (((Stack<Member>) methods).peek() instanceof Initializer) {
                m = (Initializer) ((Stack<Member>) methods).peek()
            }

            if (m)
                m.callsMethod(called)
        }

        return type
    }

    Method findMethodCalled(Type type, String comp, int numParams) {
        if (type) {
            Method called = type.getMethodWithNameAndNumParams(comp, numParams)

            if (!called) {
                for (Type anc : type.getAncestorTypes()) {
                    if (anc.hasMethodWithNameAndNumParams(comp, numParams)) {
                        type = anc
                        break
                    }
                }
                called = type.getMethodWithNameAndNumParams(comp, numParams)
            }

            return called
        }

        return null
    }

    void createConstructorCall(Type type, int numParams) {
        if (type) {
            def m

            if (((Stack<Member>) methods).peek() instanceof Method) {
                m = (Method) ((Stack<Member>) methods).peek()
            } else if (((Stack<Member>) methods).peek() instanceof Initializer) {
                m = (Initializer) ((Stack<Member>) methods).peek()
            }

            Constructor cons = type.getConstructorWithNParams(numParams)
            if (m && cons)
                m.callsMethod(cons)
        }
    }

    Type findSuperTypeWithNParamConstructor(Type type, int numParams) {
        if (type) {
            for (Type anc : type.getAncestorTypes()) {
                if (anc.hasConstructorWithNParams(numParams)) {
                    return anc
                }
            }
        }

        return type
    }

    Type findSuperTypeWithTypeFieldOrMethodNamed(Type child, String name) {
        Type type = child

        if (type) {
            for (Type anc : type.getAncestorTypes()) {
                if (anc.hasTypeWithName(name) || anc.hasFieldWithName(name) || anc.hasMethodWithName(name)) {
                    return anc
                }
            }
        }

        return type
    }

    Type createFieldUse(Type current, String comp) {
        Type type = current

        if (type) {
            def m

            if (((Stack<Member>) methods).peek() instanceof Method) {
                m = (Method) ((Stack<Member>) methods).peek()
            } else if (((Stack<Member>) methods).peek() instanceof Initializer) {
                m = (Initializer) ((Stack<Member>) methods).peek()
            }

            Field f = type.getFieldWithName(comp)
            if (f && m) {
                m.usesField(f)
                if (f.getType().getType() == TypeRefType.Type)
                    type = (Type) f.getType().getType(proj.getProjectKey())
            }
        }

        return type
    }

    ///////////////////
    // Utilities
    ///////////////////

    def handleNamedModifiers(String mod) {
        try {
            return Modifier.forName(mod)
        } catch (IllegalArgumentException e) {

        }

        null
    }

    def handleAccessibility(String mod) {
        try {
            return Accessibility.valueOf(mod.toUpperCase())
        } catch (IllegalArgumentException e) {

        }

        null
    }

    /**
     * Adds the child namespace to the provided parent namespace
     * @param parent Parent of the child namespace
     * @param child Child namespace
     */
    void setParentNamespace(Namespace parent, Namespace child) {
        if (parent != null && !parent.containsNamespace(child))
            parent.addNamespace(child)
    }

    /**
     * Finds a type with the given name, first searching the current namespace, then searching fully specified imported types,
     * then checking the types defined in known language specific wildcard imports, then checking automatically included language
     * types, and finally checking unknown wildcard imports. If the type is not defined within the context of the system under analysis
     * it will be constructed as an UnknownType.
     *
     * @param name The name of the type
     * @return The Type corresponding to the provided type name.
     */
    Type findType(String name) { // FIXME
        List<String> general = []
        List<String> specific = []

        Type candidate

        if (notFullySpecified(name)) {
            for (String s : file.getImports()) {
                if (s.endsWith("*"))
                    general.add(s)
                else
                    specific.add(s)
            }

            // Check same package
            if (namespace != null)
                candidate = proj.findType("name", namespace.getName() + "." + name)
            else
                candidate = proj.findType("name", name)

            if (candidate == null) {
                // Check specific imports
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = proj.findType("name", "spec")
                    }

                    if (candidate != null)
                        break
                }
            }

            // Check general imports
            if (candidate == null) {
                for (String gen : general) {
                    String imp = gen.replace("*", name)
                    candidate = proj.findType("name", imp)
                    if (candidate != null)
                        break
                }
            }

            // else java.lang
            if (candidate == null) {
                candidate = proj.findType("name", "java.lang." + name)
            }
        } else {
            candidate = proj.findType("name", name)
        }

        // In the event that no valid candidate was found, then it is an unknown type
        if (candidate == null) {
            if (!specific.isEmpty()) {
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = Type.builder().type(Type.UNKNOWN).name(name).compKey(spec).create()
                        break
                    }
                }
            }

            if (candidate == null && !general.isEmpty()) {
                candidate = Type.builder().name(name).type(Type.UNKNOWN).compKey(general.get(0).substring(0, general.get(0).lastIndexOf(".")) + name).create()
            }

            if (candidate == null)
                candidate = Type.builder().name(name).type(Type.UNKNOWN).compKey("java.lang." + name).create()

//            if (candidate != null)
//                proj.addUnknownType((UnknownType) candidate)
        }

        candidate
    }

    /**
     * A heuristic to determine if the provided type name is fully defined. That is whether the type name has more than one "."
     * @param name Name to evaluate
     * @return true if the name has more than one ".", false otherwise.
     */
    boolean notFullySpecified(String name) {
        return name.count(".") > 1
    }

    /**
     * Retrieves the full name of the the type with the short name provided. This name is constructed by appending the current package name
     * to the provided name, if it is not already present.
     * @param name The name to evaluate.
     * @return The fully qualified name of the type
     */
    String getFullName(String name) {
        if (!types.empty()) {
            "${typeKey()}.${name}"
        } else {
            namespace == null ? name : "${namespace.getName()}\$${name}"
        }
    }

    String typeKey() {
        types.peek().getCompKey()
    }

}
