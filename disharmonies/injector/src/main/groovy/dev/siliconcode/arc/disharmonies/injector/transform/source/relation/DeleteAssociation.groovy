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
package dev.siliconcode.arc.disharmonies.injector.transform.source.relation

import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.DeleteField

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteAssociation extends BasicSourceTransform {

    Type from
    Type to
    Field fromToField
    Field toFromField
    DeleteField dftf
    DeleteField dtff
    File toFile

    /**
     * Constructs a new BasicSourceTransform
     * @param context the current InjectorContext
     * @param file the file to be modified
     */
    DeleteAssociation(File file, Type from, File toFile, Type to) {
        super(file)
        this.from = from
        this.to = to
        this.toFile = toFile
    }

    @Override
    void setup() {
        fromToField = findField(from, to)
        toFromField = findField(to, from)
    }

    @Override
    void buildContent() {
        if (fromToField)
            dftf = new DeleteField(file, from, fromToField)
        if (toFromField)
            dtff = new DeleteField(toFile, to, toFromField)
    }

    @Override
    void injectContent() {
        if (dftf)
            dftf.execute()
        if (dtff)
            dtff.execute()
    }

    @Override
    void updateModel() {
        if (dftf) {
            from.removeAssociatedTo(to)
            updateImports()
        }
        if (dtff) {
            to.removeAssociatedTo(from)
            updateImports(toFile)
        }
    }

    Field findField(Type from, Type to) {
        from.getFields().find {
            (it.getType().getTypeFullName() == to.getFullName())
        }
    }
}