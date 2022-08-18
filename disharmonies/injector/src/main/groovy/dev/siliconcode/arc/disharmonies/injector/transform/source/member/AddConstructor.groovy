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

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.transform.source.AddMember
import groovy.transform.builder.Builder

/**
 * Transform to inject a constructor into a given Type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddConstructor extends AddMember {

    /**
     * Type which will contain the new constructor
     */
    Type type
    /**
     * Node representing the new constructor
     */
    Constructor cnst
    /**
     * List of potential imports to be added to the file
     */
    private List<TypeRef> imports
    /**
     * The parameterizable body content
     */
    private String bodyContent

    StringBuilder builder = new StringBuilder()

    /**
     * Constructs a new AddConstructor transform
     * @param file File which is to be modified
     * @param type Type in which the constructor is to be injected
     * @param cnst The constructor
     */
    @Builder(buildMethodName = "create")
    AddConstructor(File file, Type type, Constructor cnst, String bodyContent) {
        super(file)
        this.type = type
        this.cnst = cnst
        this.bodyContent = bodyContent
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()
        builder = new StringBuilder()

        start = findConstructorInsertionPoint(type) - 1
    }

    @Override
    void buildContent() {
        builder << "    ${accessibility()} ${name()}(${paramList()})"
        body(builder)
    }

    @Override
    void injectContent() {
        int original = lines.size()
        lines.add(start, builder.toString())
        lines = lines.join("\n").split("\n")
        int current = lines.size()
        end = current - original
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        updateContainingAndAllFollowing(start, end)
        type.addMember(this.cnst)
        this.cnst.start = start + 1
        this.cnst.end = start + end + 1

        // 6. for return type check if primitive, if not check if an import is needed
//        updateImports(method.type)
        // 7. for each parameter check if primitive, if not check if an import is needed
        updateImports()
    }

    /**
     * @return The name of the constructor
     */
    def name() {
        cnst.getName()
    }

    /**
     * @return String representation of the constructor's accessibility
     */
    def accessibility() {
        if (cnst.accessibility != Accessibility.DEFAULT)
            cnst.accessibility.toString().toLowerCase()
        else
            ""
    }

    /**
     * Constructs the the method body
     * @param builder StringBuilder to which the method contents will be added
     * @param bodyContent Parameterized string containing the body content
     */
    void body(StringBuilder builder) {
        if (cnst.isAbstract())
            builder << ";\n\n"
        else {
            builder << " {"
            builder << "\n"
            builder << "    ${generateBodyContent()}"
            builder << "\n"
            builder << "    }\n"
        }
    }

    /**
     * @param bodyContent Parameterized string containing the body content
     * @return String representing the contents of the body of the method
     */
    def generateBodyContent() {
        int count = 1
        String content = bodyContent

        cnst.getParams().each {
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
        cnst.getParams().each {
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
