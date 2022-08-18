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
import dev.siliconcode.arc.disharmonies.injector.transform.source.relation.DeleteRealization

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteRealizationModelTransform extends TypeModelTransform {

    Type real

    DeleteRealizationModelTransform(Type type, Type real) {
        super(type)
        this.real = real
    }

    @Override
    void verifyPreconditions() {
        // 1. real is not null
        if (!real)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type is a class and real is an interface
        if (!(type.getType() == Type.CLASS) || !(real.getType() == Type.INTERFACE))
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type realizes real
        if (!type.getRealizes().contains(real))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.removeRealizes(real)
        // Generate Source Transform
        new DeleteRealization(type.getParentFile(), type, real).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. type no longer realizes real
        assert(!type.getRealizes().contains(real))
        // 2. real no longer realized by type
        assert(!real.getRealizedBy().contains(type))
    }
}
