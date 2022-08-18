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

import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.NamespaceModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.AddNamespace
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddNamespaceToNamespaceModelTransform extends NamespaceModelTransform {

    String name
    Namespace child

    @Builder(buildMethodName = "create")
    AddNamespaceToNamespaceModelTransform(Namespace ns, String name) {
        super(ns)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. a namespace with the given name does not already exist in ns
        if (ns.getNamespaces().find {it.name == name || (it.name.contains(".") && it.name.substring(it.name.lastIndexOf(".") + 1) == name) || it.nsKey == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        child = Namespace.builder().name(name).nsKey(name).relPath(name).create()
        ns.addNamespace(child)
        ns.getParentProject().addNamespace(child)
        child.updateKey()
        // Generate Source Transform
        new AddNamespace(child, ns).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns contains newNS
        assert(ns.getNamespaces().contains(child))
        // 2. newNS parent is ns
        assert(child.getParentNamespace() == ns)
    }
}
