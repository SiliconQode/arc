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
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.datamodel.TypeRef
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.rbml.model.Classifier
import dev.siliconcode.arc.patterns.rbml.model.Multiplicity
import dev.siliconcode.arc.patterns.rbml.model.StructuralFeature
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class FieldBuilderTest extends DBSpec {

    GeneratorContext ctx
    Classifier role
    Type type

    @Before
    void setup() {
        ctx = GeneratorContext.getInstance()
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
    void teardown() {

    }

    @Test
    void "createTypeRef with known type"() {
        // given
        type

        // when
        TypeRef tr = ctx.fldBuilder.createTypeRef(type)

        // then
        the(tr.getTypeName()).shouldEqual("Class")
    }

    @Test
    void "createTypeRef with null type"() {
        // given
        type = null

        // when
        TypeRef tr = ctx.fldBuilder.createTypeRef(type)

        // then
        the(tr.getTypeName()).shouldEqual("String")
    }

    @Test
    void "test createField"() {
        // given:
        StructuralFeature feature = StructuralFeature.builder()
                .name("feature")
                .isStatic(false)
                .mult(Multiplicity.fromString("1..1"))
                .type(role)
                .create()

        // when:
        ctx.fldBuilder.init(feature: feature, fieldName: "test", owner: type)
        Field field = ctx.fldBuilder.create()

        // then:
        the(field.type.typeName).shouldEqual("Class")
        the(field.name).shouldBeEqual("test")
    }

    @Test
    void "test create static field"() {
        // given:
        StructuralFeature feature = StructuralFeature.builder()
                .name("feature")
                .isStatic(true)
                .mult(Multiplicity.fromString("1..1"))
                .type(role)
                .create()

        // when:
        ctx.fldBuilder.init(feature: feature, fieldName: "test", owner: type)
        Field field = ctx.fldBuilder.create()

        // then:
        the(field.hasModifier("STATIC")).shouldBeTrue()
        the(field.name).shouldBeEqual("test")
    }

    @Test
    void "test create field with empty type"() {
        // given:
        StructuralFeature feature = StructuralFeature.builder()
                .name("feature")
                .isStatic(false)
                .mult(Multiplicity.fromString("1..1"))
                .type()
                .create()

        // when:
        ctx.fldBuilder.init(feature: feature, fieldName: "test", owner: type)
        Field field = ctx.fldBuilder.create()

        // then:
        the(field.type.typeName).shouldEqual("String")
        the(field.name).shouldBeEqual("test")
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create a null field"() {
        // given:
        StructuralFeature feature = null

        // when:
        ctx.fldBuilder.init(feature: feature)
        Field field = ctx.fldBuilder.create()
    }

    @Test
    void createDefaultTypeRef() {
        // when
        TypeRef tr = ctx.fldBuilder.createDefaultTypeRef()

        // then
        the(tr.getTypeName()).shouldEqual("String")
    }
}
