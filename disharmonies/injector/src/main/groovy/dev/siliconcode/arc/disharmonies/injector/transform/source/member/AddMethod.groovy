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
package dev.siliconcode.arc.disharmonies.injector.transform.source.member

import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.AddMember
import groovy.transform.builder.Builder

/**
 * Transform which adds a method to a given type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMethod extends AddMember {

    /**
     * Type to which a method will be added
     */
    Type type
    /**
     * The method to be added
     */
    Method method
    /**
     * The parameterizable body content
     */
    private String bodyContent

    StringBuilder builder

    /**
     * Constructs a new AddMethod transform
     * @param file the file to be modified
     * @param type the type to which a method will be added
     * @param method the method to add
     */
    @Builder(buildMethodName = "create")
    AddMethod(File file, Type type, Method method, String bodyContent) {
        super(file)
        this.type = type
        this.method = method
        this.bodyContent = bodyContent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        file.refresh()
        type.refresh()
        method.refresh()
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()
        builder = new StringBuilder()

        // 1. find line of last method in type
        start = findMethodInsertionPoint(type)

//        println "\nAdd Method"
//        lines.eachWithIndex { String str, int ndx ->
//            println "${ndx + 1}: $str"
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        // 2. construct method header
        builder << "\n    ${accessibility()} ${modifiers()}${type()} ${name()}(${paramList()})"
        // 3. construct method body
        body(builder)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        // 4. Conduct Injection
//        if (start >= original) {
////            lines.add(start, "\n")
//            lines.add(start, builder.toString())
//        } else {
//        lines.add(start, builder.toString())
//        }
        List<String> content = builder.toString().split("\n")
        lines.addAll(start, content)
        end = content.size()
        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        // 5. update all following items with size of insert
        updateContainingAndAllFollowing(start, end)

        type.addMember(this.method)
        this.method.start = start + 2
        this.method.end = start + end
        this.method.updateKey()

        updateImports()

        if (this.method.isAbstract()) {
            if (type.getType() == Type.INTERFACE) {
                type.getRealizedBy().each {
                    implementAbstractMethod(it, type, method)
                }
            } else {
                type.getGeneralizes().each {
                    implementAbstractMethod(it, type, method)
                }
            }
        }
    }

    /**
     * @return method name
     */
    def name() {
        this.method.getName()
    }

    def type() {
        this.method.type.getTypeName()
    }

    def modifiers() {
        StringBuilder builder = new StringBuilder()
        this.method.getModifiers().each {
            builder << "${it.getName()} "
        }
        builder.toString()
    }

    /**
     * @return String representation of the method accessibility
     */
    def accessibility() {
        if (this.method.accessibility != Accessibility.DEFAULT)
            this.method.accessibility.toString().toLowerCase()
        else
            ""
    }

    /**
     * Constructs the the method body
     * @param builder StringBuilder to which the method contents will be added
     * @param bodyContent Parameterized string containing the body content
     */
    void body(StringBuilder builder) {
        if (method.isAbstract())
            builder << ";\n\n"
        else {
            builder << " {"
            builder << "\n"
            builder << "    ${generateBodyContent()}"
            builder << "\n"
            builder << "    }"
        }
    }

    /**
     * @param bodyContent Parameterized string containing the body content
     * @return String representing the contents of the body of the method
     */
    def generateBodyContent() {
        int count = 1
        String content = bodyContent

        method.getParams().each {
            content = content.replaceAll(/\[param${count}\]/, it.name)
            count += 1
        }

        content
    }

    /**
     * @return String representation of the method parameter list
     */
    def paramList() {
        StringBuilder builder = new StringBuilder()
        boolean first = true
        method.getParams().each {
            if (!first)
                builder << ", "
            builder << it.type.getTypeName()
            builder << " "
            builder << it.name
            if (first)
                first = !first
        }
        builder.toString()
    }
}
