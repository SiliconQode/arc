/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
package dev.siliconcode.arc.quality.metrics

import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Builder(buildMethodName = "create")
class LoCProfile {

    /**
     * Name of the profile
     */
    String name
    /**
     * Name of the language associated with this profile
     */
    String language
    /**
     * List of file extensions associated with this profile
     */
    List<String> extensions
    /**
     * String indicating the start of a single line comment
     */
    String lineCommentStart
    /**
     * String indicating the start of a multi-line, or block, comment
     */
    String blockCommentStart
    /**
     * String indicating the end of a multi-line, or block, comment
     */
    String blockCommentEnd
    /**
     * Exceptions to comment start rules
     */
    List<String> commentStartExceptions

    /**
     *
     */
    LoCProfile()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param extension
     *            the extension to add
     */
    void addExtension(String extension)
    {
        if (extension == null || extension.isEmpty())
            throw new IllegalArgumentException("Extension cannot be null or empty")
        if (extensions.contains(extension))
            return

        this.extensions.add(extension)
    }
}
