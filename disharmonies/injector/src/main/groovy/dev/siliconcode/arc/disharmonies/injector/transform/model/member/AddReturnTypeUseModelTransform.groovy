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
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypedMember
import dev.siliconcode.arc.disharmonies.injector.transform.model.MemberModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.AddMethod
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.ChangeMemberType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddReturnTypeUseModelTransform extends MemberModelTransform {

    Type type

    AddReturnTypeUseModelTransform(Member member, Type type) {
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
        if (((TypedMember) member).getType().getTypeName() == type.getName())
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        if (member instanceof Method) {
            Method method = (Method) member

            if (method.getType().getTypeName() == type.getName()) {
                // need to add method
                method.setType(type.createTypeRef())

                AddMethod.builder()
                        .file(method.getParentFile())
                        .type(member.getParentType())
                        .method(method)
                        .bodyContent("    throw new UnsupportedOperationException();")
                        .create()
                        .execute()
            } else {
                ChangeMemberTypeModelTransform.builder()
                        .member(method)
                        .type(type)
                        .create()
                        .execute()
            }

            member.getParentType().useTo(type)
        }
    }

    @Override
    void verifyPostconditions() {
        assert(member.getParentType().hasUseTo(type))
        assert(((Method) member).getType().getTypeName() == type.getName())
    }
}
