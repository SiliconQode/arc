/*
 * The MIT License (MIT)
 *
 * Empirilytics Parsers
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
package dev.siliconcode.arc.parsers

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
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
    DBCredentials credentials
    Stack<Integer> decisionCounts = new Stack<>()
    Stack<Integer> returnCounts = new Stack<>()
    Stack<Integer> stmtCounts = new Stack<>()
    Stack<Integer> variableCounts = new Stack<>()

    Map<String, Type> typeMap = [:]

    void withDb(Closure cl) {
        DBManager.getInstance().open(credentials)
        cl.call()
        DBManager.getInstance().close()
    }

    BaseModelBuilder(Project proj, File file, DBCredentials credentials) {
        this.credentials = credentials

        if (!proj || !file) {
            throw new IllegalArgumentException("Project and File must not be null")
        }

        this.system = proj.getParentSystem()

        if (this.system == null) {
            throw new IllegalArgumentException("Project must be a part of a system")
        }

        this.proj = proj
        this.file = file

        if (file.getParentNamespace() != null)
            namespace = file.getParentNamespace()
        else
            namespace = proj.getDefaultNamespace()
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
        log.atInfo().log("Finding/Creating Namespace: " + name)
        String[] packageNames
        if (name.contains("."))
            packageNames = name.split(/\./)
        else
            packageNames = [name]

        Namespace current = null
        for (String seg : packageNames) {
            String relPath = seg
            boolean hasNamespace = false

            String n = current == null ? seg : String.join(".", current.getName(), seg)

            hasNamespace = proj.hasNamespace(n)

            if (!hasNamespace) {
                log.atInfo().log("Creating Namespace: " + n)
                Namespace parent = current

                current = Namespace.builder()
                        .name(n)
                        .nsKey(n)
                        .relPath(relPath)
                        .create()
                if (parent != null)
                    parent.addNamespace(current)
                proj.addNamespace(current)
                current.updateKey()
            } else {
                Namespace parent = current
                setParentNamespace(parent, current)
                current = proj.findNamespace(n)
            }

            current.addFile(file)
            namespace = current

            file.updateKey()
            file.refresh()
        }
    }

    void createImport(String name, int start, int end) {
        Import imp = Import.findFirst("name = ?", name)
        if (!imp) {
            log.atInfo().log("Creating import with name: " + name)
            imp = Import.builder().name(name).start(start).end(end).create()
        }

        file.addImport(imp)

    }

    //////////////////////
    // Types
    //////////////////////
    void endType() {
        if (types) {
            Type type = types.pop()
//            if (types.isEmpty()) {
//                if (file.getTypeByName(type.getName()) == null)
//                    file.addType(type)
//            } else {
//                if (types.peek().getTypeByName(type.getName()) == null)
//                    types.peek().addType(type)
//            }

            type.updateKey()
        }
    }

    void findOrCreateType(String typeName, int typeType, int start = 1, int stop = 1) {
        Type type
        if (types) {
            type = types.peek().getTypeByName(typeName)
        } else {
            type = namespace.getTypeByName(typeName)
        }

        if (type != null) {
            type.setStart(start)
            type.setEnd(stop)
            type.save()
            types.push(type)
        } else
            createType(typeName, typeType, start, stop)
    }

    void createType(String typeName, int typeType, int start, int stop) {
        log.info "Creating Type with name: $typeName"
        Type type = findType(typeName)

        if (type) {
            type.setStart(start)
            type.setEnd(stop)
            type.setType(typeType)
            type.save()
            type.refresh()

            if (typeType != Type.UNKNOWN) {
                if (types) {
                    types.peek().addType(type)
                }

                namespace.addType(type)
                file.addType(type)
                proj.removeUnknownType(type)
                type.updateKey()
                types.push(type)
            }
        } else if (types && types.peek().getTypeByName(typeName) != null)
            types.push(types.peek().getTypeByName(typeName))
        else if (namespace.getTypeByName(typeName) != null) {
            type = namespace.getTypeByName(typeName)
            type.setEnd(stop)
            type.setStart(start)
            type.save()
            types.push(type)
        } else {
            type = Type.builder()
                    .type(typeType)
                    .name(typeName)
                    .compKey(typeName)
                    .accessibility(Accessibility.DEFAULT)
                    .start(start)
                    .end(stop)
                    .create()
            if (types) {
                types.peek().addType(type)
            }

            namespace.addType(type)
            file.addType(type)
            type.updateKey()
            types.push(type)
        }
    }

    void setTypeModifiers(List<String> modifiers) {
        if (types)
            setModifiers(modifiers, types.peek())
    }

    void setModifiers(List<String> modifiers, Component comp) {
        modifiers.each { mod ->
            Accessibility access = handleAccessibility(mod)
            Modifier modifier = handleNamedModifiers(mod)
            if (access) comp.setAccessibility(access)
            if (modifier) comp.addModifier(modifier)
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
            currentTypeParam = null
        }
    }

    ///////////////////
    // Methods
    ///////////////////

    void findInitializer(String name, boolean instance) {
        Initializer init
        init = !types.isEmpty() ? types.peek().getInitializerWithName(name) : null

        if (init) {
            methods.push(init)
            scopes.push(Sets.newHashSet())
        }
    }

    void createInitializer(String name, int number, boolean instance, int start, int end) {
        if (types) {
            if (types.peek().hasInitializerWithName(name)) {
                Initializer i = types.peek().getInitializerWithName(name)
                i.setEnd(end)
                i.setStart(start)
                i.setInteger("number", number)
                i.save()
                methods.push(i)
            } else {
                Initializer init = Initializer.builder()
                        .name(name)
                        .compKey(name)
                        .accessibility(Accessibility.PUBLIC)
                        .start(start)
                        .end(end)
                        .instance(instance)
                        .create()
                init.setInteger("number", number)
                types.peek().addMember(init)
                init.updateKey()
                methods.push(init)
            }
        }

        scopes.push(Sets.newHashSet())
        decisionCounts.push(0)
        returnCounts.push(0)
        stmtCounts.push(0)
        variableCounts.push(0)
    }

    void findMethod(String signature) {
        Method meth
        meth = types ? (Method) Method.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#$signature") : null

        if (meth) {
            methods.push(meth)
            scopes.push(Sets.newHashSet())
        }
    }

    void createMethod(String name, int start, int end) {
        log.info "Creating Method with name: $name"
        if (types) {
            Method method = Method.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#${name}()")

            if (method) {
                method.setEnd(end)
                method.setStart(start)
                method.save()
                methods.push(method)
            } else {
                Accessibility access
                if (types.peek().getType() == Type.INTERFACE)
                    access = Accessibility.PUBLIC
                else
                    access = Accessibility.DEFAULT

                method = Method.builder()
                        .name(name)
                        .compKey(name)
                        .accessibility(access)
                        .start(start)
                        .end(end)
                        .create()
                if (types.peek().getType() == Type.INTERFACE)
                    method.addModifier("ABSTRACT")
                types.peek().addMember(method)
                method.updateKey()
                methods.push(method)
            }
        }

        scopes.push(Sets.newHashSet())
        decisionCounts.push(0)
        returnCounts.push(0)
        stmtCounts.push(0)
        variableCounts.push(0)
    }

    void findConstructor(String signature) {
        Constructor cons = types ? (Constructor) Constructor.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#$signature") : null as Constructor

        if (cons) {
            methods.push(cons)
            scopes.push(Sets.newHashSet())
        }
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
        scopes.push(Sets.newHashSet())
        decisionCounts.push(0)
        returnCounts.push(0)
        stmtCounts.push(0)
        variableCounts.push(0)
    }

    void createMethodParameter() {
        currentParam = Parameter.builder().varg(false).create()
        if (methods && methods.peek() != null) {
            ((Method) methods.peek()).addParameter(currentParam)
        }
    }

    void setVariableParameter(boolean varg) {
        currentParam.setVarg(varg)
        currentParam.save()
    }

    void setParameterName(String name) {
        currentParam.setName(name)
        currentParam.save()
    }

    void addParameterModifier(String mod) {
        currentParam?.addModifier(mod)
    }

    void setMethodModifiers(List<String> mods) {
        if (methods) {
//            println "Mods: $mods"
            setModifiers(mods, methods.peek())
        }
    }

    void createMethodTypeParameter(String name) {
        currentTypeParam = TemplateParam.builder().name(name).create()
        if (methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).addTemplateParam(currentTypeParam)
        }
        currentTypeParam = null
    }

    void createFieldTypeParameter(String name) {
        currentTypeParam = TemplateParam.builder().name(name).create()
    }

    void setParameterType(String type) {
        Type t = findType(type, true)

        currentParam.setType(t.createTypeRef())

        addUseDependency(t as Type)
    }

    void setParameterPrimitiveType(String type) {
        currentParam.setType(TypeRef.createPrimitiveTypeRef(type))
    }

    void setMethodReturnType(String type) {
        Type t = findType(type, true)

        if (t && methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).setReturnType(t.createTypeRef())
        }

        addUseDependency(t)
    }

    void setMethodReturnTypeVoid() {
        if (methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).setReturnType(TypeRef.createPrimitiveTypeRef("void"))
        }

    }

    void setMethodReturnPrimitiveType(String type) {
        if (methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).setReturnType(TypeRef.createPrimitiveTypeRef(type))
        }
    }

    void addMethodException(String type) {
        Type t = findType(type, true)

        if (t && methods && methods.peek() instanceof Method) {
            ((Method) methods.peek()).addException(t.createTypeRef())
        }
    }

    void finishMethod() {
        if (!methods.isEmpty()) {
            Member member = methods.pop()
            if (member instanceof Method) {
                ((Method) member).setCounts(variableCounts.pop(), returnCounts.pop(), stmtCounts.pop(), decisionCounts.pop())
            } else if (member instanceof Initializer) {
                ((Initializer) member).setCounts(variableCounts.pop(), returnCounts.pop(), stmtCounts.pop(), decisionCounts.pop())
            }

        }

    }

    ///////////////////
    // Fields
    ///////////////////
    void createField(String name, String fieldType, boolean primitive, int start, int end, List<String> modifiers) {
        log.info "Creating Field with Name: $name"
        if (types) {
            Field field = null

            field = Field.findFirst("compKey = ?", (String) "${types.peek().getCompKey()}#${name}")


            if (field) {
                field.setEnd(end)
                field.setStart(start)
                field.save()
                setModifiers(modifiers, field)
                if (currentTypeParam)
                    field.addTemplateParam(currentTypeParam)
            } else {
                Accessibility access
                if (types.peek().getType() == Type.INTERFACE)
                    access = Accessibility.PUBLIC
                else
                    access = Accessibility.DEFAULT
                String typeKey = types.peek().getCompKey()
                field = Field.builder()
                        .name(name)
                        .compKey("${typeKey}#${name}")
                        .accessibility(access)
                        .start(start)
                        .end(end)
                        .create()
                if (types.peek().getType() == Type.INTERFACE) {
                    field.addModifier("STATIC")
                    field.addModifier("FINAL")
                }
                types.peek().addMember(field)
                if (currentTypeParam)
                    field.addTemplateParam(currentTypeParam)
                field.refresh()

                if (primitive) {
                    field.setType(TypeRef.createPrimitiveTypeRef(fieldType))
                } else {
                    Type t = findType(fieldType, true)

                    if (t) {
                        field.setType(t.createTypeRef())
                    }

                    addAssociation(t)
                }
                setModifiers(modifiers, field)
            }
        }
    }

    void createEnumLiteral(String name, int start, int end) {
        if (types && types.peek().getType() == Type.ENUM) {
            Type enm = types.peek()

            if (enm.hasLiteralWithName(name)) {
                Literal l = enm.getLiteralWithName(name)
                l.setEnd(end)
                l.setStart(start)
                l.save()
            } else {
                String typeName = types.peek().getCompKey()
                Literal lit = Literal.builder()
                        .name(name)
                        .compKey("${typeName}#${name}")
                        .start(start)
                        .end(end)
                        .create()
                enm.addMember(lit)
                lit.updateKey()
            }
        }
    }

    ///////////////////
    // Relationships
    ///////////////////
    void addRealization(String typeName) {
        if (types) {
            Type t = findType(typeName, true)

            if (t) {
                types.peek().realizes(t)
            }
        }
    }

    void addGeneralization(String typeName) {
        if (types) {
            Type t = findType(typeName, true)

            if (t) {
                types.peek().generalizedBy(t)
            }
        }
    }

    void addAssociation(Type type) {
        if (types && type && !types.peek().isAssociatedTo(type)) {
            types.peek().associatedTo(type)
        }
    }

    void addUseDependency(Type type) {
        if (types && type && type != types.peek()) {
            if (!types.peek().hasUseTo(type))
                types.peek().useTo(type)
        }
    }

    void addUseDependency(String type) {
        if (types) {
            Type t = findType(type, true)

            if (t) {
                types.peek().useTo(t)
            }
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
                if (comp.find(/\(.*\)/)) {
                    String args = comp.find(/\(.*\)/)
                    args = args.substring(args.indexOf("(") + 1, args.lastIndexOf(")"))
                    int numParams = args.split(",").size()
                    type = handleMethodCall(type, comp, numParams)
                } else
                    type = handleNonMethodCall(type, comp, components, index)
                addUseDependency(type as Type)
            }
        }
    }

    abstract String cleanExpression(String expr)

    Type handleNonMethodCall(Type type, String comp, String[] components, int index) {
        switch (comp) {
            case "this":
                return handleThis(type)
            case "super":
                if (components.length > index + 1) {
                    String name = components[index + 1]
                    return handleSuper(type, name)
                } else {
                    return type
                }
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
        Type t = null

        if (type) {
            if (type.hasFieldWithName(comp)) {
                t = createFieldUse(type, comp)
            } else {
                t = type.getTypeByName(comp)
            }
        } else {
            if (checkIsLocalVar(comp)) {
                // TODO Finish me
                // set type
            } else if (types.peek().hasFieldWithName(comp)) {
                t = createFieldUse(types.peek(), comp)
            } else if (types.peek().hasTypeWithName(comp)) {
                t = types.peek().getTypeByName(comp)
            }
        }


        return t
    }

    protected boolean checkIsLocalVar(String name) {
        !scopes.isEmpty() && scopes.peek().contains(name)
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
            if (m && !m.getMethodsCalled().contains(called))
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
        if (type && !methods.isEmpty()) {
            def m
            if (((Stack<Member>) methods).peek() instanceof Method) {
                m = (Method) ((Stack<Member>) methods).peek()
            } else if (((Stack<Member>) methods).peek() instanceof Initializer) {
                m = (Initializer) ((Stack<Member>) methods).peek()
            }

            Constructor cons = type.getConstructorWithNParams(numParams)
            if (m && cons && !m.getMethodsCalled().contains(cons))
                m.callsMethod(cons)
        }
    }

    Type findSuperTypeWithNParamConstructor(Type type, int numParams) {
        if (type) {
            Type ancestor = null

            for (Type anc : type.getAncestorTypes()) {
                if (anc.hasConstructorWithNParams(numParams)) {
                    ancestor = anc
                    break
                }
            }

            if (ancestor)
                return ancestor
        }

        return type
    }

    Type findSuperTypeWithTypeFieldOrMethodNamed(Type child, String name) {
        Type type = child

        if (type) {
            Type ancestor = null

            for (Type anc : type.getAncestorTypes()) {
                if (anc.hasTypeWithName(name) || anc.hasFieldWithName(name) || anc.hasMethodWithName(name)) {
                    ancestor = anc
                    break
                }
            }

            if (ancestor)
                return ancestor
        }

        return type
    }

    Type createFieldUse(Type current, String comp) {
        Type type = current

        if (type) {
            Member m

            if (!methods.isEmpty()) {
                if (((Stack<Member>) methods).peek() instanceof Method) {
                    m = (Method) ((Stack<Member>) methods).peek()
                } else if (((Stack<Member>) methods).peek() instanceof Initializer) {
                    m = (Initializer) ((Stack<Member>) methods).peek()
                }
            }

            Field f = type.getFieldWithName(comp)
            if (f && m && m instanceof Method && !m.getFieldsUsed().contains(f)) {
                (m as Method).usesField(f)
                if (f.getType().getType() == TypeRefType.Type) {
                    type = (Type) f.getType().getType(proj.getProjectKey())
                }
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
    Type findType(String name, boolean createUnknown = false) {
        log.info "Finding type with name: $name"
        Type candidate = null

        if (name.contains("<"))
            name = name.split("<")[0]

        if (notFullySpecified(name)) {
            candidate = this.findTypeInNamespace(name)
            if (candidate == null) candidate = this.findTypeUsingSpecificImports(name)
            if (candidate == null) candidate = this.findTypeUsingGeneralImports(name)
            if (candidate == null) candidate = this.findTypeInDefaultNamespace(name)
            if (candidate == null) candidate = this.findUnknownType(name)
        }
        if (!candidate) {
            candidate = this.findTypeByQualifiedName(name)
        }

        // In the event that no valid candidate was found, then it is an unknown type
        if (candidate == null && createUnknown) candidate = this.createUnknownType(name)

        if (candidate)
            log.info "Found type with name ${candidate.name}"

        candidate
    }

    protected Type findTypeByQualifiedName(String name) {
        log.info "Finding type by qualified name"
//        Type candidate = typeMap[name]
//        if (!candidate) candidate = proj.findTypeByQualifiedName(name)
//        return candidate
        return proj.findTypeByQualifiedName(name)
    }

    protected Type createUnknownType(String name) {
        log.info "Creating unknown type"
        String typeName = determineTypeName(name)
        findOrCreateUnknownType(typeName)
    }

    protected Type findOrCreateUnknownType(String typeName) {
        Type candidate = findUnknownType(typeName)
        if (!candidate) {
            candidate = Type.builder()
                    .name(typeName)
                    .type(Type.UNKNOWN)
                    .compKey(typeName)
                    .create()
            proj.addUnknownType(candidate)
            candidate.updateKey()
        }
        candidate
    }

    protected Type findUnknownType(String typeName) {
        log.info "Finding unknown type with name $typeName"
        return Type.findFirst("name = ? and type = ?", typeName, Type.UNKNOWN)
    }

    protected String determineTypeName(String name) {
        log.info "Determining Type Name, starting with $name"
        String specific = file.getImports()*.getName().find { it.endsWith(name) }
        String general = null // file.getImports()*.getName().find { it.endsWith("*") }

        String typeName
        if (specific) {
            typeName = specific
        } else if (general) {
            typeName = general.replace("*", name)
        } else {
//            if (name.count(".") < 1)
//                typeName = "java.lang." + name
//            else
            typeName = name
        }

        log.info "Type name determined to be: $typeName"
        typeName
    }

    protected Type findTypeInDefaultNamespace(String name) {
        log.info "Finding type in default namespace"
        Type type = proj.getDefaultNamespace().getTypeByName(name)
        if (!type) type = findTypeByQualifiedName("java.lang." + name)

        return type
    }

    protected Type findTypeUsingGeneralImports(String name) {
        log.info "Finding type as general import"
        List<String> general = file.getImports()*.getName().findAll { it.endsWith("*") }
        Type candidate = null
        for (String gen : general) {
            String imp = gen.replace("*", name)

            candidate = findTypeByQualifiedName(imp)

            if (candidate != null)
                break
        }

        candidate
    }

    protected Type findTypeUsingSpecificImports(String name) {
        log.info "Finding type as specific import"
        String specific = file.getImports()*.getName().find { it.endsWith(name) }
        Type candidate = null

        if (specific) {
            candidate = findTypeByQualifiedName(specific)
        }

        candidate
    }

    protected Type findTypeInNamespace(String name) {
        log.info "Finding type in Namespace"
        Type candidate = null

        if (namespace != null) {
            // candidate = typeMap["${namespace.getFullName()}.${name}"]
            candidate = namespace.getTypeByName(name);
            if (!candidate) candidate = findTypeByQualifiedName(namespace.getName() + "." + name)
        }

        candidate
    }

    /**
     * A heuristic to determine if the provided type name is fully defined. That is whether the type name has more than one "."
     * @param name Name to evaluate
     * @return true if the name has more than one ".", false otherwise.
     */
    boolean notFullySpecified(String name) {
        return name.count(".") < 1
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

    void reconcileUnknownTypes() {
        proj.getUnknownTypes().each {
            String typeName = it.name
            if (typeName.count(".") < 1) {
                typeName = "java.lang." + typeName
                it.setName(typeName)
                it.updateKey()
            }
        }
    }

    void incrementMethodVariableCount() {
        if (variableCounts) {
            variableCounts.push(variableCounts.pop() + 1)
        }
    }

    void incrementMethodDecisionCount() {
        if (decisionCounts) {
            decisionCounts.push(decisionCounts.pop() + 1)
        }
    }

    void incrementMethodReturnCount() {
        if (returnCounts) {
            returnCounts.push(returnCounts.pop() + 1)
        }
    }

    void incrementMethodStatementCount() {
        if (stmtCounts) {
            stmtCounts.push(stmtCounts.pop() + 1)
        }
    }
}
