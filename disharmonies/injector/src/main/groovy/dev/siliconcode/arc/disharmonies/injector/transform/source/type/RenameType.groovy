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
package dev.siliconcode.arc.disharmonies.injector.transform.source.type

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.TypeTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameType extends TypeTransform {

    String oldName

    String oldTypeHeader
    String newTypeHeader

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    RenameType(File file, Type type, String oldName) {
        super(file, type)
        this.oldName = oldName
    }

    @Override
    void setup() {
        oldTypeHeader = getTypeHeader(oldName)
        newTypeHeader = getTypeHeader(type.name)
        ops = new java.io.File(file.getFullPath())
    }

    @Override
    void buildContent() {}

    @Override
    void injectContent() {
        if (ops.text.contains(oldTypeHeader)) {
            ops.text = ops.text.replaceAll(oldTypeHeader, newTypeHeader)
        }
    }

    @Override
    void updateModel() {}

    private String getTypeHeader(String name) {
        "${kind()} ${name}"
    }
}
