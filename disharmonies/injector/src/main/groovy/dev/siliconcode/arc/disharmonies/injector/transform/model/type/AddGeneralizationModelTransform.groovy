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
import dev.siliconcode.arc.disharmonies.injector.transform.source.relation.AddGeneralization

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddGeneralizationModelTransform extends TypeModelTransform {

    Type gen

    AddGeneralizationModelTransform(Type type, Type gen) {
        super(type)
        this.gen = gen
    }

    @Override
    void verifyPreconditions() {
        // 1. gen is not null
        if (!gen)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. either both type and gen are classes or both are interfaces
        if (!(type.getType() == Type.CLASS && gen.getType() == Type.CLASS) && !(type.getType() == Type.INTERFACE && gen.getType() == Type.INTERFACE))
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type does not already extend something
        if (!type.getGeneralizedBy().isEmpty())
            throw new ModelTransformPreconditionsNotMetException()
        // 4. type does not already extend gen
        if (type.getGeneralizedBy().contains(gen))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.generalizedBy(gen)
        // Generate Source Transform
        new AddGeneralization(type.getParentFile(), type, gen).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. type generalized by gen
        assert(type.getGeneralizedBy().contains(gen))
        // 2. gen generalizes type
        assert(gen.getGeneralizes().contains(type))
    }
}
