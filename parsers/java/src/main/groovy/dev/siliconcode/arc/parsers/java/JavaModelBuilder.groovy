/*
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.parsers.java

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.parsers.*

/**
 * Using the parser, this class incrementally builds a DataModelMediator one file at a
 * time.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaModelBuilder extends BaseModelBuilder {

    JavaModelBuilder(Project proj, File file, DBCredentials credentials) {
        super(proj, file, credentials)
    }

    String cleanExpression(String expr) {
        expr = expr.replaceAll(/[+\-=<>?:;!*&|^]/, " ") // remove all operators
        expr = expr.replaceAll(/\b\s\([\w\d]+\)\s/, " ") // remove casts
        expr = expr.replaceAll(/((?<![\\])['"])((?:.(?!(?<![\\])\1))*.?)\1/, "String") // remove string literals
        expr = expr.replaceAll(/\btrue\b|\bfalse\b/, " ") // remove boolean literals
        expr = expr.replaceAll(/\b(0[xX][A-Fa-f\d_]+(\.[A-Fa-f\d_]+)?|0[bB][10_]+(\.[10_]+)?|[\d_]+(\.[\d_]+)?([eE][\d_]+)?)[fFdDlL]?\b/, "Number")
        // remove numeric literals
        expr = expr.replaceAll(/\s\s/, " ") // remove double spaces
        expr = expr.replaceAll(/\(\s+/, "(")
        expr = expr.replaceAll(/\s+\)/, ")")
        expr = expr.replaceAll(/,\s+/, ",")

        return expr
    }
}
