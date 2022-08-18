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

import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypedMember
import dev.siliconcode.arc.disharmonies.injector.transform.model.MemberModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.ChangeMemberType
import groovy.transform.builder.Builder

class ChangeMemberTypeModelTransform extends MemberModelTransform {

    Type type

    @Builder(buildMethodName = "create")
    ChangeMemberTypeModelTransform(TypedMember member, Type type) {
        super(member)
        this.type = type
    }

    @Override
    void verifyPreconditions() {
        // 1. type is not null
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. member does not already have this accessibility
        if (((TypedMember) member).getType().getType(type.getParentProject().getProjectKey()) == type)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Generate Source Transform
        new ChangeMemberType(member.getParentFile(), (TypedMember) member, type.createTypeRef()).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. member has access of access
        assert(((TypedMember) member).getType().getType(type.getParentProject().getProjectKey()) == type)
    }
}
