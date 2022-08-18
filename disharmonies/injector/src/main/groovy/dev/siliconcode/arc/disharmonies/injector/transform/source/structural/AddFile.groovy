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
import dev.siliconcode.arc.datamodel.Namespace
import groovy.transform.builder.Builder

/**
 * Transform which constructs a new file, including any physical artifacts it requires
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFile extends StructuralCreateTransform {

    Namespace ns
    File file

    java.io.File toWrite

    /**
     * Constructs a new CreateFile transform
     * @param file the file to be created
     */
    @Builder(buildMethodName = "create")
    AddFile(File file, Namespace ns) {
        super()
        this.file = file
        this.ns = ns
    }

    @Override
    void setup() {
        base = new java.io.File(ns.getFullPath(file.getType(), 0))
        toWrite = new java.io.File(file.getFullPath()) // FIXME
//        println "NS: ${ns.getFullName()}"
//        println "GetName: ${file.getName()}"
//        println "GetFullPath: ${file.getFullPath()}"
    }

    @Override
    void transformStructure() {

        if (!toWrite.exists()) {
            toWrite.getParentFile().mkdirs()
            toWrite.createNewFile()
        }

        String content = """\
        /**
         * The MIT License (MIT)
         *
         * MSUSEL Software Injector
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory
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

        ${packageStmt()}
        """.stripIndent()

        toWrite.text = content

        int end = content.split(/\n/).length
        int start = 1
        file.setStart(start)
        file.setEnd(end)
    }

    private String packageStmt() {
        if (ns) {
            return "package ${ns.getFullName()};"
        }
        return ""
    }
}
