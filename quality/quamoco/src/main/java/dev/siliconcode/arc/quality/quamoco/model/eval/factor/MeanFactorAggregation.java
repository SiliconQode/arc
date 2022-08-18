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
package dev.siliconcode.arc.quality.quamoco.model.eval.factor;

import dev.siliconcode.arc.quality.quamoco.io.factories.EvaluationType;
import dev.siliconcode.arc.quality.quamoco.model.Annotation;
import dev.siliconcode.arc.quality.quamoco.model.Source;
import dev.siliconcode.arc.quality.quamoco.model.Tag;
import dev.siliconcode.arc.quality.quamoco.model.eval.FactorAggregation;
import dev.siliconcode.arc.quality.quamoco.model.factor.Factor;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * Indicates that the factor should aggregate incoming factors using
 * the mean operator
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeanFactorAggregation extends FactorAggregation {

    /**
     *
     */
    public MeanFactorAggregation()
    {
        super();
    }

    /**
     * @param identifier
     */
    public MeanFactorAggregation(String identifier)
    {
        super(identifier);
    }

    @Builder(buildMethodName = "create")
    protected MeanFactorAggregation(String name, Double completeness, Double maximumPoints, String title, String description, Factor evaluates,
                                    String identifier, Source originatesFrom, @Singular List<Tag> tags, @Singular List<Annotation> annotations) {
        super(name, completeness, maximumPoints, title, description, evaluates, identifier, originatesFrom, tags, annotations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double evaluate()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String xmlTag()
    {
        return generateXMLTag(EvaluationType.MEAN_FACTOR_AGGREGATION.type());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toScript()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
