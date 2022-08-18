/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.patterns.gen.generators.pb

import dev.siliconcode.arc.datamodel.Accessibility
import dev.siliconcode.arc.datamodel.Parameter
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypeRef
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.rbml.model.Classifier
import dev.siliconcode.arc.patterns.rbml.model.Multiplicity
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class ParamBuilderTest extends DBSpec {

    GeneratorContext ctx
    Classifier role
    Type type

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()

        role = Classifier.builder()
                .name("Test")
                .create()

        type = Type.builder().type(Type.CLASS)
                .name("Class")
                .compKey("Class")
                .accessibility(Accessibility.PUBLIC)
                .create()

        ctx.rbmlManager.addMapping(role, type)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test createParameter"() {
        // given:
        dev.siliconcode.arc.patterns.rbml.model.Parameter param = dev.siliconcode.arc.patterns.rbml.model.Parameter.builder()
                .var("param")
                .mult(Multiplicity.fromString("1..1"))
                .type(role)
                .create()

        // when:
        ctx.paramBuilder.init(param: param)
        Parameter p = ctx.paramBuilder.create()

        // then:
        the(p.type.typeName).shouldEqual("Class")
        the(p.name).shouldEqual("param")
    }

    @Test(expected = IllegalArgumentException.class)
    void "null param input to createParameter"() {
        // given:
        dev.siliconcode.arc.patterns.rbml.model.Parameter param = null

        // when:
        ctx.paramBuilder.init(param: param)
        Parameter p = ctx.paramBuilder.create()
    }

    @Test
    void "empty param type input to createParameter"() {
        // given:
        dev.siliconcode.arc.patterns.rbml.model.Parameter param = dev.siliconcode.arc.patterns.rbml.model.Parameter.builder()
                .var("param")
                .mult(Multiplicity.fromString("1..1"))
                .type()
                .create()

        // when:
        ctx.paramBuilder.init(param: param)
        Parameter p = ctx.paramBuilder.create()

        // then:
        the(p.type.typeName).shouldEqual("String")
        the(p.name).shouldEqual("param")
    }

    @Test
    void createDefaultTypeRef() {
        // when
        TypeRef tr = ctx.paramBuilder.createDefaultTypeRef()

        // then
        the(tr.getTypeName()).shouldEqual("String")
    }
}
