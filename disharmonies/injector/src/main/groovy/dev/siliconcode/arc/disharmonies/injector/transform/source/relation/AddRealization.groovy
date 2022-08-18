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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.TypeHeaderTransform
import groovy.transform.builder.Builder

/**
 * Transform which modifies a type's header information to contain a new interface to realize
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddRealization extends TypeHeaderTransform {

    /**
     * The interface to realize
     */
    Type real

    /**
     * Constructs a new AddRealization transform
     * @param file the file to be modified
     * @param node the Type whose header is to be modified
     * @param real the new Type to be realized
     */
    @Builder(buildMethodName = "create")
    AddRealization(File file, Type node, Type real) {
        super(file, node)
        this.real = real
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        // 1. find realization list for the type to add to
        // 2. parse the realization list and append to end
        String header = getTypeHeader()
        String content
        if (header.contains("implements")) {
            String implList = header.split("implements")[1]
            def newList = implList.replaceAll(/\s+\{/, ", ${real.name} {")
            content = header.replace(implList, newList)
        } else {
            String allButBrace = header.split(/\s*\{/)[0]
            String replacement = "${allButBrace} implements ${real.name}"
            content = header.replace(allButBrace, replacement)
        }

        // 3. Add realization relationship
        type.realizes(real)

        // 4. Inject content
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.text = actual.text.replace(header, content)

        // 5. check the package to determine if an import is needed
        // 6. if an import is needed, check if it already exists, if not add the type to the import list
        updateImports()
        // 7. check new generalization type for any needed abstract methods (if not abstract), if any are missing add to list of things to do
        // 8. if abstract, and methods not implemented, then add them to all concrete subclasses
        implementAbstractMethods(type, real)
    }
}
