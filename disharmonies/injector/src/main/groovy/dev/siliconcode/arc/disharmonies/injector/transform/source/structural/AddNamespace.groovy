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
package dev.siliconcode.arc.disharmonies.injector.transform.source.structural

import dev.siliconcode.arc.datamodel.FileType
import dev.siliconcode.arc.datamodel.Module
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import groovy.transform.builder.Builder

/**
 * Transform which constructs new namespace, and any physical artifacts it requires
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddNamespace extends StructuralCreateTransform {

    /**
     * Namespace to be constructed
     */
    Namespace namespace
    def to

    /**
     * Constructs a new CreateNamespace transform
    * @param namespace the namespace to be created
     */
    @Builder(buildMethodName = "create")
    AddNamespace(Namespace namespace, to) {
        super()
        this.namespace = namespace
        this.to = to
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        if (to instanceof Module) {
            base = getModuleFile((Module) to)
        } else if (to instanceof Namespace) {
            base = getNamespaceFile((Namespace) to)
        } else if (to instanceof Project) {
            base = getProjectFile((Project) to)
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void transformStructure() {
        if (base) {
            File nsPath = new File(base, namespace.getRelPath().replace(".", File.separator))
            nsPath.mkdirs()
        }
    }

    private File getModuleFile(Module mod) {
        File dir = new File(mod.getFullPath())
        File file = new File(dir, mod.getSrcPath())
        return file
    }

    private File getNamespaceFile(Namespace other) {
        File dir = new File(other.getFullPath(FileType.SOURCE, 0))
        return dir
    }

    private File getProjectFile(Project proj) {
        File dir = new File(proj.getSrcPath())
        return dir
    }
}
