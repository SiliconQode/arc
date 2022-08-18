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
package dev.siliconcode.arc.disharmonies.injector.transform.source.relation

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.FileOperations
import groovy.transform.builder.Builder

/**
 * Transform that injects a method call into an existing method's body
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMethodCall extends AddRelation {

    Method caller
    Method callee

    Type calleeOwner
    Type callerOwner

    int delta

    /**
     * Constructs a new AddMethodCall transform
     * @param file the file to be modified
     * @param caller the method making the call
     * @param callee the method being called
     */
    @Builder(buildMethodName = "create")
    AddMethodCall(File file, Method caller, Method callee) {
        super(file)
        this.caller = caller
        this.callee = callee
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        calleeOwner = getMethodOwner(callee)
        callerOwner = getMethodOwner(caller)
        start = findStatementInsertionPoint(caller)
        text
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        if (callee.hasModifier("static")) {
            text = "        ${calleeOwner.name}.${callee.name}(${params(callee)});"
        } else if (sameContainingType(callerOwner, calleeOwner)) {
            text = "        this.${callee.name}(${params(callee)});"
        } else {
            StringBuilder builder = new StringBuilder()
            builder << "        ${calleeOwner.name} ${calleeOwner.name.toLowerCase()} = new ${calleeOwner.name}();\n"
            builder << "        ${calleeOwner.name.toLowerCase()}.${callee.name}(${params(callee)});"
            text = builder.toString()
        }
        println "Text:"
        println text
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        java.io.File ops = new java.io.File(file.getFullPath())
        def lines = ops.readLines()

        callee.refresh()
        caller.refresh()
        file.refresh()

        List<String> oldContent = lines[(caller.getStart() - 1)..(caller.getEnd() - 1)]
        String oc = oldContent.join("\n")

        def pattern = ~/(?s).*\{(?<content>.*)}/
        def matcher = oc =~ pattern
        if (matcher.matches()) {
            String actualContent = matcher.group("content")
            List<String> oldLines = actualContent.split("\n")
            oldLines.add(0, text)
            oldLines.add(0,"\n")
            String nc = oc.replace(actualContent, oldLines.join("\n"))
            List<String> newContent = nc.split("\n")
            delta = newContent.size() - oldContent.size()
            ops.text = lines.join("\n").replace(oc, nc)
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateContainingAndAllFollowing(caller.getStart() + 1, delta)

        caller.callsMethod(callee)
        addUseDep(callerOwner, calleeOwner)

        int oldEnd = caller.getEnd()

        caller.refresh()
        if (caller.getEnd() - oldEnd != delta)
            caller.setEnd(caller.getEnd() + delta)
        caller.refresh()

        updateImports()
    }

    /**
     * Generates the string for the actual parameters of the method call
     * @param methodNode Method to be called
     * @return A string of the actual parameters to the method call, uses null for object, 0 or 0.0 for numbers, '' for char, and false for boolean
     */
    String params(Method methodNode) {
        List<String> builder = []

        methodNode.params.each {
            if (it.getType()) {
                if (it.getType().getType() == TypeRefType.Primitive) {
                    switch (it.getType().typeName) {
                        case "int":
                        case "byte":
                        case "short":
                        case "long":
                            builder << "0"
                            break
                        case "float":
                        case "double":
                            builder << "0.0"
                            break
                        case "char":
                            builder << "''"
                            break
                        case 'boolean':
                            builder << "true"
                            break
                    }
                } else {
                    builder << "null"
                }
            } else {
                builder << "null"
            }
        }

        println "Params: ${builder.join(', ')}"
        return builder.join(", ")
    }
}
