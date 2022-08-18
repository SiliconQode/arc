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
package dev.siliconcode.arc.disharmonies.injector.transform.source

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.cond.Condition
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.AddMethod
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.UpdateImports

/**
 * Base class on which all transforms are built
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AbstractSourceTransform implements SourceTransform {

    /**
     * File containing the component on which this transform operates
     */
    protected File file
    /**
     * List of pre-conditions which must be true prior to this transform executing
     */
    List<Condition> conditions

    /**
     * Constructs a new AbstractSourceTransform
     * @param context The current InjectorContext
     * @param file The filenode to be transformed
     */
    AbstractSourceTransform(File file) {
        this.file = file
    }

    /**
     * Updates all items following the item at the provided line, by shifting their start and end values by length
     * @param line The line after which items need to be shifted
     * @param length The offset to update following items by
     */
    void updateAllFollowing(File file = this.file, int line, int length) {
        file.refresh()
        file.following(line).each { c ->
            if (c instanceof Component) {
                c.refresh()
                c.setStart(c.getStart() + length)
                c.setEnd(c.getEnd() + length)
                c.refresh()
            }
        }

        file.refresh()
        file.setEnd(file.getEnd() + length)
        file.refresh()
    }

    void updateImports(File file = this.file) {
        Set<String> imports = readKnownImports()
        getTypeImports(imports)

        file.refresh()

        imports.each { String name ->
            Import imp = Import.findFirst("name = ?", name)
            if (!imp) {
                imp = Import.builder().name(name).create()
            }

            file.addImport(imp)
        }

        if (imports)
            UpdateImports.builder().file(file).create().execute()
    }

    private Set<String> readKnownImports() {
        Set<String> imports = Sets.newHashSet()
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.readLines().each {
            if (it.startsWith("import ")) {
                String impOnly = it.split(/ /)[1]
                impOnly = impOnly.substring(0, impOnly.indexOf(";"))
                imports << impOnly
            }
        }
        imports
    }

    private void getTypeImports(Set<String> imports) {
        file.getAllTypes().each {type ->
            type.getAllMethods().each { method ->
                if (method.getType() && method.getType().getType() == TypeRefType.Type)
                    if (!this.shareSameNamespace(type, method.getType().getTypeFullName()))
                        imports << method.getType().getTypeFullName()

                method.getParams().each { param ->
                    if (param.getType() && param.getType().getType() == TypeRefType.Type)
                        if (!this.shareSameNamespace(type, param.getType().getTypeFullName()))
                            imports << param.getType().getTypeFullName()
                }
            }

            type.getFields().each {field ->
                if (field.getType() && field.getType().getType() == TypeRefType.Type)
                    if (!this.shareSameNamespace(type, field.getType().getTypeFullName()))
                        imports << field.getType().getTypeFullName()
            }

            type.getUseTo().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }

            type.getGeneralizedBy().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }

            type.getRealizes().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }

            type.getAssociatedTo().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }

            type.getAggregatedTo().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }

            type.getComposedTo().each {
                if (!this.shareSameNamespace(type, it))
                    imports << it.getFullName()
            }
        }
    }

    boolean shareSameNamespace(Type one, Type two) {
        String nsOne = one.getFullName()
        String nsTwo = two.getFullName()

        if (nsOne.contains("."))
            nsOne = nsOne.substring(0, nsOne.lastIndexOf("."))
        if (nsTwo.contains("."))
            nsTwo = nsTwo.substring(0, nsTwo.lastIndexOf("."))

        return nsOne == nsTwo
    }

    boolean shareSameNamespace(Type one, String typeName) {
        String nsOne = one.getFullName()

        if (nsOne.contains("."))
            nsOne = nsOne.substring(0, nsOne.lastIndexOf("."))
        if (typeName.contains(".")) {
            typeName = typeName.substring(0, typeName.lastIndexOf("."))

            return nsOne == typeName
        }

        return false
    }

    /**
     * Adds to the file's list of imports with the provided string representations of imports to add
     * @param imports List of strings representing imports to add
     */
    void addImports(List<String> imports) {
        file.imports.each {
            imports.removeAll(it.getName())
        }
        imports.removeAll {
            if (it.contains(".")) {
                String pkg = it.substring(0, it.lastIndexOf("."))
                pkg == file.getParentNamespace().getFullName()
            }
        }

        imports.each { String name ->
            Import imp = Import.findFirst("name = ?", name)
            if (!imp) {
                imp = Import.builder().name(name).create()
            }

            file.addImport(imp)
        }

        if (imports)
            UpdateImports.builder().file(file).create().execute()
    }

    /**
     * Updates the node containing the given line by shifting its end value by the provided length. Then it updates all
     * items following the given line by shifting their start and end values by the given length.
     * @param line Line where the insertion occurred
     * @param length Length of the change
     */
    void updateContainingAndAllFollowing(int line, int length, file = this.file) {
        file.refresh()
        file.containing(line).each {
            it.refresh()
            it.setEnd(it.getEnd() + length)
            it.refresh()
        }
        updateAllFollowing(line, length)
    }

    def implementAbstractMethods(Type type, Type parent) {
        if (type.isAbstract() && type.getType() != Type.INTERFACE) {
            if (parent.getType() == Type.INTERFACE) {
                parent.getAllMethods().each { m ->
                    Random rand = new Random()
                    if (rand.nextDouble() <= 0.25d)
                        copyAndImplement(type, m)
                    else
                        pushToSubclasses(type, m)
                }
            } else {
                parent.getAllMethods().each { m ->
                    if (m.isAbstract()) {
                        Random rand = new Random()
                        if (rand.nextDouble() <= 0.25d)
                            copyAndImplement(type, m)
                        else
                            pushToSubclasses(type, m)
                    }
                }
            }
        } else if (type.getType() == Type.INTERFACE) {
            parent.getAllMethods().each { m ->
                if (m.isAbstract()) {
                    pushToSubclasses(type, m)
                }
            }
        } else if (!type.isAbstract()) {
            parent.getAllMethods().each { m ->
                if (m.isAbstract()) {
                    Random rand = new Random()
                    if (rand.nextDouble() <=> 0.25d && !type.getGeneralizes().isEmpty()) {
                        overrideInChildren(type, m)
                    }
                    copyAndImplement(type, m)
                }
            }
        }
    }

    def implementAbstractMethod(Type type, Type parent, Method m) {
        if (type.isAbstract() && type.getType() != Type.INTERFACE) {
            if (parent.getType() == Type.INTERFACE) {
                Random rand = new Random()
                if (rand.nextDouble() <= 0.25d)
                    copyAndImplement(type, m)
                else
                    pushToSubclasses(type, m)
            } else {
                if (m.isAbstract()) {
                    Random rand = new Random()
                    if (rand.nextDouble() <= 0.25d)
                        copyAndImplement(type, m)
                    else
                        pushToSubclasses(type, m)
                }
            }
        } else if (type.getType() == Type.INTERFACE) {
            if (m.isAbstract()) {
                pushToSubclasses(type, m)
            }
        } else if (!type.isAbstract()) {
            if (m.isAbstract()) {
                Random rand = new Random()
                if (rand.nextDouble() <=> 0.25d && !type.getGeneralizes().isEmpty()) {
                    overrideInChildren(type, m)
                }
                copyAndImplement(type, m)
            }
        }
    }

    def copyAndImplement(Type type, Method m) {
        if (!m.modifiers.contains(Modifier.forName("static")) && !m.modifiers.contains(Modifier.forName("final"))) {
            if (!type.hasMethodWithNameAndNumParams(m.name, m.getParams().size())) {
                Method copy = Method.builder().name(m.name).compKey(m.name).type(m.type).accessibility(m.accessibility).create()
                m.getParams().each {
                    copy.addParameter(Parameter.builder().name(it.name).type(it.getType()).create())
                }
                m.getModifiers().each {
                    if (it.getName() != "abstract")
                        copy.addModifier(it)
                }
                AddMethod.builder().file(file).type(type).method(copy).bodyContent("    throw new UnsupportedOperationException();").create().execute()
            }
        }
    }

    def pushToSubclasses(Type type, Method m) {
        if (type.getType() == Type.INTERFACE) {
            type.getRealizedBy().each {
                copyAndImplement(it, m)
            }
        } else {
            type.getGeneralizes().each {
                copyAndImplement(it, m)
            }
        }
    }

    def overrideInChildren(type, m) {
        type.getGeneralizes().each {
            copyAndImplement(it, m)
        }
    }
}
