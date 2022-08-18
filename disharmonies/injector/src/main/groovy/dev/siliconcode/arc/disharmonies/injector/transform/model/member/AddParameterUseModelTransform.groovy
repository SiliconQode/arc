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
package dev.siliconcode.arc.disharmonies.injector.transform.model.member

import dev.siliconcode.arc.datamodel.Member
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Parameter
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.model.MemberModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.AddMethodParameter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddParameterUseModelTransform extends MemberModelTransform {

    Type type
    static int generated = 0

    AddParameterUseModelTransform(Member member, Type type) {
        super(member)
        this.type = type
    }

    @Override
    void verifyPreconditions() {
        if (!member)
            throw new ModelTransformPreconditionsNotMetException()
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        if (!(member instanceof Method))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        Parameter param = Parameter.builder()
                .type(type.createTypeRef())
                .name(type.getName().uncapitalize() + generated++)
                .create()

        ((Method) member).addParameter(param)

        AddMethodParameter.builder()
                .file(member.getParentFile())
                .type(member.getParentType())
                .method((Method) member)
                .param(param)
                .create()
                .execute()

        member.getParentType().useTo(type)
    }

    @Override
    void verifyPostconditions() {
        assert(member.getParentType().hasUseTo(type))
    }
}
