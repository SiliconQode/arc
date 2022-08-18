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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.NamespaceModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.module.AddNamespaceToModuleModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.module.DeleteNamespaceFromModuleModelTransform
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SplitNamespaceModelTransform extends NamespaceModelTransform {

    List<File> left
    List<File> right

    Namespace ns1
    Namespace ns2
    def parent

    static int generated = 1

    @Builder(buildMethodName = "create")
    SplitNamespaceModelTransform(Namespace ns, List<File> left, List<File> right) {
        super(ns)
        this.left = left
        this.right = right
    }

    @Override
    void verifyPreconditions() {
        if (!ns)
            throw new ModelTransformPreconditionsNotMetException()
        if (!left)
            throw new ModelTransformPreconditionsNotMetException()
        if (!right)
            throw new ModelTransformPreconditionsNotMetException()
        if (!ns.getParentNamespace() && !ns.getParentModule())
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // 1. Create Two New Namespaces (ns.name 1, ns.name 2) -> Use AddNamespaceModelTransform
        ModelTransform trans1
        ModelTransform trans2
//        ModelTransform delete

        if (ns.getParentModule()) {
            parent = ns.getParentModule()
//            trans1 = AddNamespaceToModuleModelTransform.builder().mod(ns.getParentModule()).name("${ns.getName()}1").create()
            trans2 = AddNamespaceToModuleModelTransform.builder().mod(ns.getParentModule()).name("${ns.getName()}_generate_${generated++}").create()
//            delete = DeleteNamespaceFromModuleModelTransform.builder().mod(ns.getParentModule()).ns(ns).create()

//            trans1.execute()
            trans2.execute()

//            ns1 = ((AddNamespaceToModuleModelTransform) trans1).getNs()
            ns1 = ns
            ns1.saveIt()
            ns1.refresh()
            ns2 = ((AddNamespaceToModuleModelTransform) trans2).getNs()
            ns2.saveIt()
        } else {
            parent = ns.getParentNamespace()
//            trans1 = AddNamespaceToNamespaceModelTransform.builder().ns(ns.getParentNamespace()).name("${ns.getName()}_generated_1").create()
            trans2 = AddNamespaceToNamespaceModelTransform.builder().ns(ns.getParentNamespace()).name("${ns.getName()}_generated_${generated++}").create()
//            delete = DeleteNamespaceFromNamespaceModelTransform.builder().ns(ns.getParentNamespace()).child(ns).create()

//            trans1.execute()
            trans2.execute()

//            ns1 = ((AddNamespaceToNamespaceModelTransform) trans1).getChild()
            ns1 = ns
            ns1.saveIt()
            ns1.refresh()
            ns2 = ((AddNamespaceToNamespaceModelTransform) trans2).getChild()
            ns2.saveIt()
            ns2.refresh()
        }
        ns2.refresh()

        // 2. for each file in left move to ns.name1 - Use MoveFileModelTransform
//        left.each { MoveFileModelTransform.builder().ns(ns).newParent(ns1).file(it).create().execute() }

        // 3. for each file in right move to ns.name2 -> Use MoveFileModelTransform
        right.each { MoveFileModelTransform.builder().ns(ns).newParent(ns2).file(it).create().execute() }

        // 4. Delete the current Namespace
//        delete.execute() // FIXME is this the problem?
    }

    @Override
    void verifyPostconditions() {
        if (parent instanceof Module) {
            Module modParent = (Module) parent
//            assert(!modParent.getNamespaces().contains(ns))
            assert(modParent.getNamespaces().contains(ns1))
            assert(modParent.getNamespaces().contains(ns2))
        } else {
            Namespace nsParent = (Namespace) parent
//            assert(!nsParent.getNamespaces().contains(ns))
            assert(nsParent.getNamespaces().contains(ns1))
            assert(nsParent.getNamespaces().contains(ns2))
        }
    }
}
