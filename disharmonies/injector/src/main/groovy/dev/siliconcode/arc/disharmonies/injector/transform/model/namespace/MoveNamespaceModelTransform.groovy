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

import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.NamespaceModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.structural.MoveNamespace

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveNamespaceModelTransform extends NamespaceModelTransform {

    Namespace child
    def newParent

    MoveNamespaceModelTransform(Namespace ns, Namespace child, newParent) {
        super(ns)
        this.child = child
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. child is not null
        if (!child)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. child is a child of ns
        if (!ns.getNamespaces().contains(child))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent is either a module or namespace
        if (!(newParent instanceof Project) && !(newParent instanceof Module) && !(newParent instanceof Namespace))
            throw new ModelTransformPreconditionsNotMetException()
        // 5. newParent does not already contain a namespace equivalent to child
        if (newParent.getNamespaces().find { it.name == child.name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        ns.removeNamespace(child)
        newParent.addNamespace(child)
        child.updateKey()
        // Generate Source Transform
        new MoveNamespace(child, ns, newParent).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns no longer contains child
        assert(!ns.getNamespaces().contains(child))
        // 2. newParent contains child
        assert(newParent.getNamespaces().contains(child))
    }
}
