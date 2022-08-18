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
import dev.siliconcode.arc.disharmonies.injector.transform.model.MemberModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.RenameMember

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RenameMemberModelTransform extends MemberModelTransform {

    String name

    RenameMemberModelTransform(Member member, String name) {
        super(member)
        this.name = name
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. name is the same
        if (member.name == name)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Store previous state
        String oldName = member.name
        // Execute Transform
        member.setName(name)
        member.updateKey()
        // Generate Source Transform
        new RenameMember(member.getParentFile(), member.getParentType(), member, oldName).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. member name is name
        assert(member.name == name)
    }
}
