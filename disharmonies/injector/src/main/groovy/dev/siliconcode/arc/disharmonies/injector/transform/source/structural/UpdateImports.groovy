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

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder

class UpdateImports extends BasicSourceTransform {

    int delta = 0
    boolean pkgOnly = false
    boolean existingImps = false
    boolean neither = false

    /**
     * Constructs a new AddImport transform
     * @param file The file to be modified
     * @param imp The import to be added
     */
    @Builder(buildMethodName = "create")
    UpdateImports(File file) {
        super(file)
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
        List<String> imps = Lists.newArrayList()
        delta = 0

        if (pkgOnly) {
            imps << ""
            delta += 1
        }

        file.refresh()
        file.getImports().each {
            if (!it.getName().endsWith("String")) {
                String str = "import ${it.name};"
                if (!imps.contains(str))
                    imps << str
            }
        }

        if (neither)
            imps << ""

        text = imps.join("\n")

        delta += imps.size() - ((end + 1) - start)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        if (existingImps) {
            for (int i = end; i >= start; i--)
                lines.remove(i)
        }

        lines.add(start, text)

        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateAllFollowing(start, delta)
    }

    /**
     * Selects the line into which the import will be injected
     * @return Line at which the import should be injected
     */
    int findImportInsertionPoint(java.io.File actual) {
        start = -1
        end = -1
        actual.readLines().eachWithIndex { String str, int ndx ->
            if (str.startsWith("import ") && start == -1) {
                start = ndx
                end = ndx
                existingImps = true
            } else if (str.startsWith("import ") && start >= 0) {
                end = ndx
            }
        }

        if (start == -1 || file.getImports().isEmpty()) {
            actual.readLines().eachWithIndex { String str, int ndx ->
                if (str.startsWith("package ")) {
                    pkgOnly = true
                    start = end = ndx + 1
                }
            }
        }

        if (start == -1 && !pkgOnly) {
            if (actual.readLines().get(0).startsWith("/*")) {
                int endComment = 0
                actual.readLines().eachWithIndex { String str, int ndx ->
                    if (str.contains("*/") && endComment == 0) {
                        endComment = ndx
                        start = endComment + 1
                    }
                }
            }
        }

        if (start == -1) {
            start = end = 0
            neither = true
        }

        if (end == -1)
            end = start

        start
    }
}
