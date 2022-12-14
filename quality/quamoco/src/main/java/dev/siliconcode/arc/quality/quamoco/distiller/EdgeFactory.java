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
package dev.siliconcode.arc.quality.quamoco.distiller;

import dev.siliconcode.arc.quality.quamoco.graph.edge.*;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.graph.node.ValueNode;
import dev.siliconcode.arc.quality.quamoco.model.InfluenceEffect;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.QMElement;
import dev.siliconcode.arc.quality.quamoco.model.factor.Factor;
import dev.siliconcode.arc.quality.quamoco.model.measure.Measure;
import dev.siliconcode.arc.quality.quamoco.model.measurement.MeasurementMethod;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class EdgeFactory {

    private EdgeFactory() {

    }

    private static final class Holder {
        public static final EdgeFactory INSTANCE = new EdgeFactory();
    }

    public static EdgeFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Edge createEdge(QMElement source, QMElement dest, DistillerData data) {
        if (source instanceof MeasurementMethod) {
            if (dest instanceof Measure) {
                return createProviderToMeasureEdge((MeasurementMethod) source, (Measure) dest, data);
            }
        } else if (source instanceof Measure) {
            if (dest instanceof Measure) {
                return createMeasureToMeasureEdge((Measure) source, (Measure) dest, data);
            } else if (dest instanceof Factor) {
                return createMeasureToFactorEdge((Measure) source, (Factor) dest, data);
            }
        } else if (source instanceof Factor) {
            if (dest instanceof Factor) {
                return createFactorToFactorEdge((Factor) source, (Factor) dest, data);
            }
        }

        return null;
    }

    private Edge createFactorToFactorEdge(Factor source, Factor target, DistillerData data) {
        Node src = data.getFactor(source);
        Node dest = data.getFactor(target);
        if (src != null && dest != null)
            return new FactorToFactorEdge(source.getFullName() + ":" + target.getFullName(), src, dest, source.getInfluenceOn(target));
        return null;
    }

    private Edge createMeasureToFactorEdge(Measure source, Factor target, DistillerData data) {
        Node src = data.getMeasure(source);
        Node dest = data.getFactor(target);

        if (src != null && dest != null) {
            if (source.getType() == MeasureType.FINDINGS) {
                return new MeasureToFactorFindingsEdge(source.getFullName() + ":" + target.getFullName(),
                        src, dest, InfluenceEffect.POSITIVE).setRank(1);
            } else {
                return new MeasureToFactorNumberEdge(source.getFullName() + ":" + target.getFullName(),
                        src, dest, InfluenceEffect.POSITIVE).setRank(1);
            }
        }

        return null;
    }

    private Edge createMeasureToMeasureEdge(Measure source, Measure target, DistillerData data) {
        Node src = data.getMeasure(source);
        Node dest = data.getMeasure(target);

        if (src != null && dest != null) {
            if (source.getType().equals(MeasureType.FINDINGS) && target.getType().equals(MeasureType.FINDINGS))
            {
                return new MeasureToMeasureFindingsEdge(source.getFullName() + ":" + target.getFullName(), src, dest);
            }
            else if (source.getType().equals(MeasureType.NUMBER) && target.getType().equals(MeasureType.NUMBER))
            {
                new MeasureToMeasureNumberEdge(source.getFullName() + ":" + target.getFullName(), src, dest);
            }
            else
            {
                new MeasureToMeasureFindingsNumberEdge(source.getFullName() + ":" + target.getFullName(), src, dest);
            }
        }

        return null;
    }

    private Edge createProviderToMeasureEdge(MeasurementMethod source, Measure target, DistillerData data) {
        Node src = data.getValue(source);
        if (src == null)
            src = data.getUnion(source);

        if (src != null) {
            if (src instanceof ValueNode) {
                return new ValueToMeasureEdge(source.getFullName() + ":" + target.getFullName(), src, data.getMeasure(target));
            } else {
                return new FindingToMeasureEdge(source.getFullName() + ":" + target.getFullName(), src, data.getMeasure(target));
            }
        }
        return null;
    }
}
