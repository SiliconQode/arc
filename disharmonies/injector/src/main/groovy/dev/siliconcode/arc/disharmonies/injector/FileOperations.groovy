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
package dev.siliconcode.arc.disharmonies.injector

import dev.siliconcode.arc.datamodel.Component
import dev.siliconcode.arc.datamodel.File

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Container for contents of files currently under modification. This class acts as the receiver
 * of the transforms.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FileOperations {

    /**
     * The string representation of the path for the file to be modified
     */
    String file
    java.io.File actual
    /**
     * List containing the string contents of the file under modification
     */
    List<String> lines

    private FileOperations() {}

    private static class Holder { private static final FileOperations INSTANCE = new FileOperations() }

    static FileOperations getOps(File file) {
        FileOperations ops = Holder.INSTANCE
        synchronized (FileOperations) {
            if (ops.file == file.getFullPath())
                return ops
            else {
                ops.file = file.getFullPath()
                ops.actual = new java.io.File(ops.file)
                ops.lines = ops.actual.readLines()
            }
            return ops
        }
    }

    void clear() {
        lines = []
        actual = null
        file = null
    }

    /**
     * Injects the contents of the provided string at the provided location
     * @param line Location for the injection
     * @param content Content to be injected
     * @return the length of the injected content, in lines
     * @throws IllegalArgumentException if line is outside the bounds of the file or if the content is null
     */
    int inject(int line, String content) {
        if (line - 1 < 0 || line - 1 >= lines.size())
            throw new IllegalArgumentException("No such line as ${line} in file ${file}")
        if (content == null)
            throw new IllegalArgumentException("Cannot inject null content")

        String[] newLines = content.split(/\n/)

        lines.addAll(line - 1, newLines.toList())

        return newLines.size()
    }

    def replace(int line, String oldContent, String content) {
        if (line -1 < 0 || line -1 >= lines.size())
            throw new IllegalArgumentException("No such line as ${line} in file ${file}")
        if (content == null)
            throw new IllegalArgumentException("Cannot replace with null content")
        if (oldContent)
            throw new IllegalArgumentException("Cannot replace nothing")

        //String[] newLines = content.split(/\n/)

        //String substring = lines[line].substring(range.getFrom(), range.getTo())
        lines[line].replace(oldContent, content)
    }

    def replaceRange(int start, int end, String content) {
        if (start < 0 || end >= lines.size())
            throw new IllegalArgumentException("Invalid range")
        if (content == null)
            throw new IllegalArgumentException("content cannot be null")

        String[] newLines = content.split(/\n/)

        for (int i = 0; i < (end - start); i++)
            lines.remove(start)

        int ndx = start
        newLines.each {
            lines.add(ndx, it)
            ndx += 1
        }
    }

    /**
     * Retrieves the content at the line provided
     * @param line index of the line of content (assumed to start at 1)
     * @return The actual content of the at that line in the file
     * @throws IllegalArgumentException if the line is outside the bounds of the file
     */
    String contentAt(int line) {
        if (line - 1 < 0 || line - 1 >= lines.size())
            throw new IllegalArgumentException("No such line as ${line} in file ${file}")
        return lines[line - 1]
    }

    /**
     * Retrieves the content in the file between the start and end lines of the provided code node (inclusive)
     * @param node Node whose content is requested
     * @return List of strings covering the lines in the region requested
     * @throws IllegalArgumentException if the provide node is null
     */
    List<String> contentRegion(Component node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot retrieve content region of a null Component")
        }
        try {
            lines[node.start - 1..node.end - 1]
        } catch (IndexOutOfBoundsException e) {
            []
        }
    }

    /**
     * Injects the provided string at the very end of the file
     * @param s The content to be injected
     * @return The length of the injected content in lines
     * @throws IllegalArgumentException if the content provided is null
     */
    int injectAtEnd(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Cannot inject null content")
        }
        int start = lines.size()

        def newLines = s.split(/\n/)
        lines.addAll(newLines)

        newLines.size()
    }

    void injectInLineAtLocation(int line, int column, String content) {
        lines[line] = lines[line].substring(0, column) + content + lines[line].substring(column)
    }
}
