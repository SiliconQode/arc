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
package dev.siliconcode.arc.disharmonies.injector.transform.model.type

import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.model.TypeModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.relation.AddAssociation

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddAssociationModelTransform extends TypeModelTransform {

    Type assoc
    String fromName
    String toName
    boolean bidirect

    AddAssociationModelTransform(Type type, Type assoc, String fromName, String toName, boolean bidirect) {
        super(type)
        this.assoc = assoc
        this.fromName = fromName
        this.toName = toName
        this.bidirect = bidirect
    }

    @Override
    void verifyPreconditions() {
        // 1. type is not null
        if (!assoc)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. from name is not null or empty
        if (!fromName)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. to name is not null or empty
        if (!toName)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
//        type.associatedTo(assoc)
        // Generate Source Transform
        new AddAssociation(type.getParentFile(), type, assoc.getParentFile(), assoc, toName, fromName, bidirect).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. type associated to assoc
        assert(type.getAssociatedTo().contains(assoc))
        // 2. assoc associated from type
        assert(assoc.getAssociatedFrom().contains(type))
    }
}
