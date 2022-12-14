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

import dev.siliconcode.arc.quality.quamoco.graph.node.FindingNode;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.model.QMElement;
import dev.siliconcode.arc.quality.quamoco.model.eval.Evaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.ManualEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.factor.MeanFactorAggregation;
import dev.siliconcode.arc.quality.quamoco.model.eval.factor.WeightedSumFactorAggregation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.MeanMultiMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.SingleMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.eval.measure.WeightedSumMultiMeasureEvaluation;
import dev.siliconcode.arc.quality.quamoco.model.measurement.MeasurementMethod;
import dev.siliconcode.arc.quality.quamoco.model.measurement.aggregation.*;
import dev.siliconcode.arc.quality.quamoco.processor.Processor;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.*;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.ManualEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.MeanEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.SingleMeasureEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.WeightedSumEvaluator;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ProcessorFactory {

    private ProcessorFactory() {

    }

    private static final class Holder {
        public static final ProcessorFactory INSTANCE = new ProcessorFactory();
    }

    public static ProcessorFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Processor createProcessor(QMElement element, DistillerData data) {
        if (element instanceof Evaluation) {
            return createFactorProcessor((Evaluation) element, data);
        } else if (element instanceof MeasureAggregation) {
            return createMeasureProcessor((MeasureAggregation) element, data);
        } else if (element instanceof MeasurementMethod) {
            return createMeasurementMethodProcessor((MeasurementMethod) element, data);
        } else
            return null;
    }

    private Processor createMeasurementMethodProcessor(MeasurementMethod element, DistillerData data) {
        Node node = data.getValue(element);
        if (node != null) {
            if (node instanceof FindingNode) {
                return new FindingsUnionAggregator(node);
            } else {
                return new MeanEvaluator(node);
            }
        } else {
            node = data.getUnion(element);
            if (node != null) {
                return new FindingsUnionAggregator(node);
            }
        }

        return null;
    }

    public Processor createMeasureProcessor(MeasurementMethod method, DistillerData data) {
        if (method instanceof NumberMeanMeasureAggregation) {
            return createNumberMeanAggregator(method, data);
        } else if (method instanceof NumberMedianMeasureAggregation) {
            return createNumberMedianAggregator(method, data);
        } else if (method instanceof NumberMaxMeasureAggregation) {
            return createNumberMaxAggregator(method, data);
        } else if (method instanceof NumberMinMeasureAggregation) {
            return createNumberMinAggregator(method, data);
        } else if (method instanceof FindingsUnionMeasureAggregation) {
            return createFindingsUnionAggregator(method, data);
        } else if (method instanceof FindingsIntersectionMeasureAggregation) {
            return createFindingsIntersectAggregator(method, data);
        } else if (method instanceof TextAggregation) {
            if (method.getDetermines().getType() == MeasureType.FINDINGS) {
                return createFindingsUnionAggregator(method, data);
            } else {
                return createNumberMeanAggregator(method, data);
            }
        } else {
            return createNumberMeanAggregator(method, data);
        }
    }

    private Processor createNumberMedianAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new NumberMedianAggregator(n);
    }

    private Processor createNumberMaxAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new NumberMaxAggregator(n);
    }

    private Processor createNumberMinAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new NumberMinAggregator(n);
    }

    private Processor createFindingsIntersectAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new FindingsIntersectAggregator(n);
    }

    private Processor createFindingsUnionAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new FindingsUnionAggregator(n);
    }

    private Processor createNumberMeanAggregator(MeasurementMethod method, DistillerData data) {
        Node n = data.getMeasure(method.getDetermines());
        return new NumberMeanAggregator(n);
    }

    public Processor createFactorProcessor(Evaluation eval, DistillerData data) {
        if (eval instanceof WeightedSumMultiMeasureEvaluation || eval instanceof WeightedSumFactorAggregation) {
            return createWeightedSumEvaluator(eval, data);
        } else if (eval instanceof MeanFactorAggregation || eval instanceof MeanMultiMeasureEvaluation) {
            return createMeanEvaluator(eval, data);
        } else if (eval instanceof SingleMeasureEvaluation) {
            return SingleMeasureEvaluator(eval, data);
        } else if (eval instanceof ManualEvaluation) {
            return createManualEvaluator(eval, data);
        } else {
            return createMeanEvaluator(eval, data);
        }
    }

    private Processor createManualEvaluator(Evaluation eval, DistillerData data) {
        Node n = data.getFactor(eval.getEvaluates());
        return new ManualEvaluator(n);
    }

    private Processor SingleMeasureEvaluator(Evaluation eval, DistillerData data) {
        Node n = data.getFactor(eval.getEvaluates());
        return new SingleMeasureEvaluator(n);
    }

    private Processor createMeanEvaluator(Evaluation eval, DistillerData data) {
        Node n = data.getFactor(eval.getEvaluates());
        return new MeanEvaluator(n);
    }

    private Processor createWeightedSumEvaluator(Evaluation eval, DistillerData data) {
        Node n = data.getFactor(eval.getEvaluates());
        return new WeightedSumEvaluator(n);
    }
}
