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
import dev.siliconcode.arc.datamodel.Modifier
import dev.siliconcode.arc.datamodel.Parameter
import dev.siliconcode.arc.datamodel.TypeRef
import dev.siliconcode.arc.disharmonies.injector.transform.model.MemberModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.model.ModelTransformPreconditionsNotMetException
import dev.siliconcode.arc.disharmonies.injector.transform.source.member.AddMethodParameter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddPrimitiveParamModelTransform extends MemberModelTransform {

    String name
    String type
    Modifier[] mods
    Parameter param

    AddPrimitiveParamModelTransform(Member member, String name, String type, Modifier ... mods) {
        super(member)
        this.name = name
        this.type = type
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. member is a method
        if (!(member instanceof Method))
            throw new ModelTransformPreconditionsNotMetException()
        // 2. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type is not null or empty
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. member does not already have a param with the given name
        if (member.params.find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        param = Parameter.builder()
                .name(name)
                .type(TypeRef.createPrimitiveTypeRef(type))
                .create()
        mods.each {
            param.addModifier(it)
        }
        ((Method) member).addParameter(param)
        member.updateKey()
        // Generate Source Transform
        new AddMethodParameter(member.getParentFile(), member.getParentType(), (Method) member, param).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. member has param
        assert(((Method) member).getParams().contains(param))
    }
}
