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
package dev.siliconcode.arc.quality.quamoco.io.factories;

import dev.siliconcode.arc.quality.quamoco.distiller.ModelManager;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measure.NormalizationMeasure;
import org.w3c.dom.Element;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MeasureFactory extends AbstractQMElementFactory {

    /**
     *
     */
    private MeasureFactory() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @author Isaac Griffith
     */
    private static class Holder {

        /**
         *
         */
        private static final MeasureFactory INSTANCE = new MeasureFactory();
    }

    /**
     * @return
     */
    public static MeasureFactory instance() {
        return Holder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measure create(Element e, ModelManager manager) {
        this.manager = manager;
        Measure meas = null;
        MeasuresType type = null;

        if (e.hasAttribute("type")) {
            type = MeasuresType.fromType(e.getAttribute("type"));
        }

        if (type != null) {
            switch (type) {
                case FINDING_MEASURE:
                    meas = createFindingsMeasure(e);
                    break;
                case NUMBER_MEASURE:
                    meas = createNumberMeasure(e);
                    break;
            }
        }

        return meas;
    }

    /**
     * @param e
     * @return
     */
    private Measure createNormMeasure(Element e, MeasureType type) {
        return NormalizationMeasure.normBuilder()
                .name(e.getAttribute("name"))
                .identifier(e.getAttribute("xmi:id"))
                .description(e.hasAttribute("description") ? e.getAttribute("description") : "")
                .title(e.hasAttribute("title") ? e.getAttribute("title") : "")
                .type(type)
                .create();
    }

    /**
     * @param e
     * @return
     */
    private Measure createNumberMeasure(Element e) {
        if (e.hasAttribute("xsi:type") && e.getAttribute("xsi:type").equals("qm:NormalizationMeasure")) {
            return createNormMeasure(e, MeasureType.NUMBER);
        }

        return Measure.builder()
                .name(e.getAttribute("name"))
                .identifier(e.getAttribute("xmi:id"))
                .description(e.hasAttribute("description") ? e.getAttribute("description") : "")
                .title(e.hasAttribute("title") ? e.getAttribute("title") : "")
                .type(MeasureType.NUMBER)
                .create();
    }

    /**
     * @param e
     * @return
     */
    private Measure createFindingsMeasure(Element e) {
        if (e.hasAttribute("xsi:type") && e.getAttribute("xsi:type").equals("qm:NormalizationMeasure")) {
            return createNormMeasure(e, MeasureType.FINDINGS);
        }

        return Measure.builder()
                .name(e.getAttribute("name"))
                .identifier(e.getAttribute("xmi:id"))
                .description(e.hasAttribute("description") ? e.getAttribute("description") : "")
                .title(e.hasAttribute("title") ? e.getAttribute("title") : "")
                .type(MeasureType.FINDINGS)
                .create();
    }
}
