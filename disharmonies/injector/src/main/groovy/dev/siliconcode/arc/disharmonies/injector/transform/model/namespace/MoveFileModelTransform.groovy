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
package dev.siliconcode.arc.disharmonies.injector.transform.model.namespace

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.NamespaceModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.MoveFile
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveFileModelTransform extends NamespaceModelTransform {

    File file
    Namespace newParent

    @Builder(buildMethodName = "create")
    MoveFileModelTransform(Namespace ns, File file, Namespace newParent) {
        super(ns)
        this.file = file
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. file is not null
        if (!file)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. Namespace has this file as a child
        if (!ns.getFiles().contains(file))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. a file with the same name is not already in newParent
        if (newParent.getFiles().find { it.name == file.name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        ns.removeFile(file)
        newParent.addFile(file)
        List<Type> types = Lists.newArrayList(ns.getAllTypes())
        types.findAll {it.getParentFile() == file }.each {
            newParent.addType(it)
            it.refresh()
            it.updateKey()
        }
        // file.updateKey()
        // Generate Source Transform
        new MoveFile(file, ns, newParent).execute()
        file.refresh()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns no longer contains file
        assert(!ns.getFiles().contains(file))
        // 2. newParent now contains file
        assert(newParent.getFiles().contains(file))
        // 3. file's parent is newParent
        assert(file.getParentNamespace() == newParent)
        // 4. file's path is correct
    }
}
