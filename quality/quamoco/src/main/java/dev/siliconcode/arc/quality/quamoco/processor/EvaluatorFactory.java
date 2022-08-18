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
package dev.siliconcode.arc.quality.quamoco.processor;

import dev.siliconcode.arc.quality.quamoco.graph.INode;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.ManualEvaluator;
import dev.siliconcode.arc.quality.quamoco.graph.node.FactorMethod;
import dev.siliconcode.arc.quality.quamoco.graph.node.FactorNode;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.MeanEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.SingleMeasureEvaluator;
import dev.siliconcode.arc.quality.quamoco.processor.evaluators.WeightedSumEvaluator;

/**
 * Factory used to generate the appropriate evaluators for a FactorNode given
 * the node's current state.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class EvaluatorFactory extends ProcessorFactory {

    /**
     * Constructs a new EvaluatorFactory
     */
    private EvaluatorFactory()
    {
    }

    /**
     * Private static inner class used to hold the singleton instance of
     * EvaluatorFactory
     *
     * @author Isaac Griffith
     * @version 1.1.1
     */
    private static class FactoryHelper {

        /**
         * The singleton instance
         */
        private static final EvaluatorFactory INSTANCE = new EvaluatorFactory();
    }

    /**
     * @return The singleton instance of this factory
     */
    public static ProcessorFactory getInstance()
    {
        return FactoryHelper.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Processor createProcessor(final INode node)
    {
        if (node instanceof FactorNode)
        {
            final FactorNode factorNode = (FactorNode) node;

            switch (factorNode.getMethod()) {
                case FactorMethod.ONE:
                    return new SingleMeasureEvaluator(factorNode);
                case FactorMethod.MEAN:
                    return new MeanEvaluator(factorNode);
                case FactorMethod.MANUAL:
                    return new ManualEvaluator(factorNode);
                default:
                    return new WeightedSumEvaluator(factorNode);
            }
        }

        return null;
    }
}
