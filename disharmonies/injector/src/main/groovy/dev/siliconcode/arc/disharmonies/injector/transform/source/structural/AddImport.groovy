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
package dev.siliconcode.arc.disharmonies.injector.transform.source.structural

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Import
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder

/**
 * Transform to add an import to a File
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddImport extends BasicSourceTransform {

    /**
     * The import to be added
     */
    Import imp

    /**
     * Constructs a new AddImport transform
     * @param file The file to be modified
     * @param imp The import to be added
     */
    @Builder(buildMethodName = "create")
    AddImport(File file, Import imp) {
        super(file)
        this.imp = imp
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        ops = new java.io.File(file.fullPath)
        lines = ops.readLines()
        start = findImportInsertionPoint(ops)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        text = "import ${imp.name};"
        lines.add(start, text)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        file.addImport(imp)
        updateAllFollowing(start, 1)
    }

    /**
     * Selects the line into which the import will be injected
     * @return Line at which the import should be injected
     */
    int findImportInsertionPoint(java.io.File actual) {
        int line = 0

        if (!file.getImports().isEmpty()) {
            line = file.getImports().max { it.getStart() }.start
        } else {
            actual.readLines().eachWithIndex { String str, int ndx ->
                if (str.startsWith("package")) {
                    line = ndx + 1
                }
            }
        }

        line
    }
}
