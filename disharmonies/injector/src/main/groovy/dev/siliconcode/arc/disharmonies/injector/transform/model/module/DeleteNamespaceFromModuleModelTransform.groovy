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
package dev.siliconcode.arc.disharmonies.injector.transform.model.module

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModuleModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.DeleteNamespace
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteNamespaceFromModuleModelTransform extends ModuleModelTransform {

    Namespace ns

    @Builder(buildMethodName = "create")
    DeleteNamespaceFromModuleModelTransform(Module mod, Namespace ns) {
        super(mod)
        this.ns = ns
    }

    @Override
    void verifyPreconditions() {
        // 1. ns is not null
        if (!ns)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. ns does not exist in mod
        if (!mod.namespaces.contains(ns))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // execute transform
        mod.removeNamespace(ns)
        ns.thaw()
        mod.refresh()
        // create source transform
        new DeleteNamespace(ns).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns is not contained in mod
        assert(!mod.namespaces.contains(ns))
    }
}
