/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
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
package dev.siliconcode.arc.quality.quamoco.model.factor;

import dev.siliconcode.arc.quality.quamoco.io.factories.FactorType;
import dev.siliconcode.arc.quality.quamoco.model.Annotation;
import dev.siliconcode.arc.quality.quamoco.model.Impact;
import dev.siliconcode.arc.quality.quamoco.model.Source;
import dev.siliconcode.arc.quality.quamoco.model.Tag;
import dev.siliconcode.arc.quality.quamoco.model.entity.Entity;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Quality in use attributes define a way to decompose the abstract concept
 * software quality. These quality attributes are defined in the ISO 25010 as
 * description of the quality in its various forms of usage. Examples are
 * efficiency or effectiveness. To be more precise, we add the activity that is
 * characterised by the attribute as entity to the factor. Activities like
 * maintenance, program comprehension, modification, or testing, which can be
 * decomposed in their respective sub-activities, provide a means to model
 * software development cost structures.
 *
 * The entity of a quality in use attribute is an activity.
 * Characterize an activity by:
 *
 * Effectiveness
 * Efficiency
 * Safety
 * Satisfaction
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QualityInUseAttribute extends QualityAspect {

    /**
     * Constructs a new QualityInUseAttribute with the given name
     *
     * @param name
     *            The name
     */
    public QualityInUseAttribute(String name) {
        super(name);
    }

    /**
     * @param name
     * @param identifier
     */
    public QualityInUseAttribute(String name, String identifier) {
        super(name, identifier);
    }

    @Builder(buildMethodName = "create")
    protected QualityInUseAttribute(String name, String description, Entity characterizes, String title, @Singular Map<String, Impact> influences, Factor refines,
                                    String identifier, Source originatesFrom, @Singular List<Tag> tags, @Singular List<Annotation> annotations) {
        super(name, description, characterizes, title, influences, refines,
                identifier, originatesFrom, tags, annotations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String xmlTag() {
        return generateXMLTag(FactorType.QUALITY_IN_USE_ATTRIBUTE.type());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toScript() {
        // TODO Auto-generated method stub
        return null;
    }
}
