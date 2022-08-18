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
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import org.apache.commons.lang3.tuple.Pair

/**
 * Base class for those transforms which construct relationships between two types
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AddRelation extends BasicSourceTransform {

    /**
     * Constructs a new AddRelation transform
     * @param file the file to be modified
     */
    AddRelation(File file) {
        super(file)
    }

    /**
     * finds the line into which a statement may be injected within a method
     * @param method Method into which the statement is to be injected
     * @return the line number for the injection
     */
    int findStatementInsertionPoint(Method method) {
        FileOperations ops = FileOperations.getOps(file)
        List<String> content = ops.contentRegion(method)

        int index = 0
        if (!content.isEmpty()) {
            for (int i = 0; i < content.size(); i++) {
                if (content[i].trim().endsWith("{")) {
                    index = i + 1
                    break
                }
            }
        }
        return index
    }

    /**
     * Selects a field from the given first type that has the given type
     * @param owner The type to select the field from
     * @param calleeOwner Type
     * @return the selected field with the given type, null if no such field can be found
     */
    Field selectField(Type callerOwner, Type calleeOwner) {
        callerOwner.getFields().find { it.type.typeName == calleeOwner.name }
    }

    /**
     * Checks whether the given first type has a field with the given type
     * @param owner The type to check
     * @param calleeOwner Type
     * @return true if owner contains a field with the given type, false otherwise
     */
    boolean hasField(Type owner, Type calleeOwner) {
        owner.getFields().find { it.type.typeName == calleeOwner.name } != null
    }

    /**
     * Selects a parameter from the given method that has the given type
     * @param caller Method to select a parameter from
     * @param calleeOwner Type
     * @return the selected parameter or null if no parameter meets the criteria
     */
    Parameter selectParameter(Method caller, Type calleeOwner) {
        caller.getParams().find { it.type.typeName == calleeOwner.name }
    }

    /**
     * Checks whether the given method has a parameter with the given type
     * @param method Method to check
     * @param Type Type
     * @return true if the method contains a parameter with the given type, false otherwise
     */
    boolean hasParam(Method method, Type type) {
        method.getParams().find { it.type.typeName == type.name } != null
    }

    /**
     * Searches the given method for a local variable defined with the given type
     * @param method Method to search
     * @param type type
     * @return a string representation of the local variable meeting the criteria if one is found, otherwise null
     */
    String selectVariable(Method method, Type type) {
        if (method.end - method.start > 1) {
            FileOperations ops = FileOperations.getOps(file)
            List<String> lines = ops.contentRegion(method)
            def pairs = []
            lines.each { line ->
                def group = (line =~ /^\s*([\w_$]+)(<.*>)?(\[])?\s+([\w_$]+)(\[])?/)
                if (group.find()) {
                    def t = group[0][1]
                    def var = group[0][4]
                    pairs << Pair.of(t, var)
                }
            }

            return pairs.find { pair ->
                pair.key == type.name
            }.value
        }

        null
    }

    /**
     * Checks whether a local variable with the given type exists in the given method
     * @param method Method to search
     * @param type Type being looked for
     * @return true if such a local variable has been found
     */
    boolean hasLocalVar(Method method, Type type) {
        if (method.end - method.start > 1) {
            FileOperations ops = FileOperations.getOps(file)
            List<String> lines = ops.contentRegion(method)
            def pairs = []
            lines.each { line ->
                def header = line.trim() =~ /${method.getName()}\s*\(/
                if (!header.find()) {
                    def group = line.trim() =~ /^\s*([\w_$]+)(<.*>)?(\[])?\s+([\w_$]+)(\[])?/
                    if (group.find()) {
                        def t = group[0][1]
                        def var = group[0][4]
                        pairs << Pair.of(t, var)
                    }
                }
            }

            return pairs.find { pair -> pair.key == type.name } != null
        }

        false
    }

    /**
     * Checks whether the two provided types are the same
     * @param type1 First type
     * @param type2 Second type
     * @return true if the provided types are the same, false otherwise
     */
    boolean sameContainingType(Type type1, Type type2) {
        type1 == type2
    }

    /**
     * Retrieves the owning type of the provided method
     * @param method Method whose type is requested
     * @return The type containing the provided method
     */
    Type getMethodOwner(Method method) {
        if (!method.getParentTypes().isEmpty())
            return method.getParentTypes().first()
    }

    /**
     * Creates a new use dependency between the two provided types
     * @param src source side of the dependency
     * @param dest destination side of the dependency
     */
    void addUseDep(Type src, Type dest) {
       src.useTo(dest)
    }
}
