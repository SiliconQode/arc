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
package dev.siliconcode.arc.quality.quamoco.processor.extents;

import dev.siliconcode.arc.datamodel.Measurable;
import dev.siliconcode.arc.datamodel.Measure;
import dev.siliconcode.arc.datamodel.Type;
import dev.siliconcode.arc.quality.quamoco.distiller.QuamocoContext;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;

import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MethodExtentDecorator extends AbstractExtentDecorator {

    public MethodExtentDecorator(Measurable node) {
        super(node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NormalizationRange findRange(String metric) {
        Measurable p = decorated.getParent();
        if (Measure.hasMetric(decorated, metric) || Measure.hasMetric(decorated, QuamocoContext.instance().getMetricRepoKey() + ":" + metric)) {
            return NormalizationRange.METHOD;
        } else if (Measure.hasMetric(p, metric) || Measure.hasMetric(p, QuamocoContext.instance().getMetricRepoKey() + ":" + metric)) {
            return NormalizationRange.CLASS;
        } else {
            return NormalizationRange.FILE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double findFileExtent(String metric) {
        Measurable type = decorated.getParent();
        Measurable file = type.getParent();

        Measure meas = Measure.retrieve(file, metric);
        if (meas == null)
            meas = Measure.retrieve(file, QuamocoContext.instance().getMetricRepoKey() + ":" + metric);
        if (meas == null) {
            return 0.0;
        }
        return Objects.requireNonNull(meas).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double findMethodExtent(String metric) {
        Measure meas = Measure.retrieve(decorated, metric);
        if (meas == null)
            meas = Measure.retrieve(decorated, QuamocoContext.instance().getMetricRepoKey() + ":" + metric);
        if (meas == null) {
            return 0.0;
        }
        return Objects.requireNonNull(meas).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double findClassExtent(String metric) {
        Measurable p = decorated.getParent();

        if (p instanceof Type) {
            Measure meas = Measure.retrieve(p, metric);
            if (meas == null)
                meas = Measure.retrieve(p, QuamocoContext.instance().getMetricRepoKey() + ":" + metric);
            if (meas == null) {
                return 0.0;
            }
            return Objects.requireNonNull(meas).getValue();
        }

        return 0.0;
    }
}
