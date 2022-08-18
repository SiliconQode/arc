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
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.MoveNamespace

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveNamespaceModelTransform extends ModuleModelTransform {

    Namespace ns
    def newParent

    MoveNamespaceModelTransform(Module mod, Namespace ns, newParent) {
        super(mod)
        this.ns = ns
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. ns is not null
        if (!ns)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. ns is contained in mod
        if (!mod.namespaces.contains(ns))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain an equivalent namespace
        if (newParent.namespaces.find { it.name == ns.name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // execute transform
        mod.removeNamespace(ns)
        newParent.addNamespace(ns)
        ns.updateKey()
        // create source transform
        new MoveNamespace(ns, mod, newParent).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns no longer is contained by mod
        assert(!mod.namespaces.contains(ns))
        // 2. newParent now contains ns
        assert(newParent.namespaces.contains(ns))
        // 3. ns' is newParent
        assert(ns.getParentModule() == newParent)
    }
}
