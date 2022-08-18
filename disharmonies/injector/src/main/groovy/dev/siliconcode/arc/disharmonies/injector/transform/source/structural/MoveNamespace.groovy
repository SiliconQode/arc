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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveNamespace extends StructuralMoveTransform {

    def to
    def from
    Namespace ns

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveNamespace(Namespace ns, from, to) {
        super()
        this.to = to
        this.from = from
        this.ns = ns
    }

    @Override
    void setup() {
        if (from instanceof Module) {
            oldFile = getModuleFile((Module) from)
        }
        else if (from instanceof Namespace) {
            oldFile = getNamespaceFile((Namespace) from)
        }
        if (to instanceof Module) {
            newFile = getModuleFile((Module) to)
        }
        else if (to instanceof Namespace) {
            newFile = getNamespaceFile((Namespace) to)
        }
    }

    @Override
    void transformStructure() {
        if (oldFile && newFile) {
            oldFile.renameTo(newFile)
            oldFile.deleteDir()
        }
    }

    private File getModuleFile(Module mod) {
        File dir = new File(mod.getFullPath())
        File file = new File(dir, mod.getSrcPath())
        new File(file, ns.getRelPath())
    }

    private File getNamespaceFile(Namespace other) {
        File dir = new File(other.getFullPath(FileType.SOURCE, 0))
        new File(dir, ns.getRelPath())
    }
}
