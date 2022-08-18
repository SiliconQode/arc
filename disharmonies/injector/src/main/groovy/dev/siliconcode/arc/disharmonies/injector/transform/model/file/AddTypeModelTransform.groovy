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
package dev.siliconcode.arc.disharmonies.injector.transform.model.file


import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Modifier
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.FileModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.type.AddType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddTypeModelTransform extends FileModelTransform {

    String name
    Accessibility access
    String typeName
    Modifier[] mods
    Type type

    AddTypeModelTransform(File file, String name, Accessibility access, String typeName, Modifier... mods) {
        super(file)
        this.name = name
        this.access = access
        this.typeName = typeName
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. access is not null or empty
        if (!access)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type is not null or empty
        if (!typeName)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. file does not already contain a type with name
        if (file.getAllTypes().find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        switch (typeName) {
            case "class":
                type = Type.builder()
                        .type(Type.CLASS)
                        .name(name)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .accessibility(access)
                        .create()
                break
            case "interface":
                type = Type.builder()
                        .type(Type.INTERFACE)
                        .name(name)
                        .accessibility(access)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .create()
                break
            case "enum":
                type = Type.builder()
                        .type(Type.ENUM)
                        .name(name)
                        .accessibility(access)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .create()
                break
        }
        mods.each {
            type.addModifier(it)
        }
        // Generate Source Transform
        new AddType(file, type).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. A new type with the given name and features exists in file
        assert (file.getAllTypes().contains(type))
        // 2. The new type's parent is file
        assert (type.getParentFile() == file)
    }
}
