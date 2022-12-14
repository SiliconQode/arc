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
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureMethod;
import dev.siliconcode.arc.quality.quamoco.graph.node.MeasureNode;
import dev.siliconcode.arc.quality.quamoco.model.MeasureType;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.FindingsIntersectAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMeanAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMinAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMedianAggregator;
import dev.siliconcode.arc.quality.quamoco.graph.node.Node;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.FindingsUnionAggregator;
import dev.siliconcode.arc.quality.quamoco.processor.aggregators.NumberMaxAggregator;

/**
 * Factory used to select the appropriate aggregator for a given MeasureNode
 * based on its current state.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class AggregatorFactory extends ProcessorFactory {

    /**
     * Constructs a new AggregatorFactory
     */
    private AggregatorFactory()
    {
    }

    /**
     * Private static internal class used to hold the singleton instance of
     * AggregatorFactory
     *
     * @author Isaac Griffith
     * @version 1.1.1
     */
    private static class FactoryHelper {

        /**
         * The singleton instance
         */
        private static final AggregatorFactory INSTANCE = new AggregatorFactory();
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
        if (node instanceof MeasureNode)
        {
            final MeasureNode measureNode = (MeasureNode) node;

            if (measureNode.getType().equals(MeasureType.FINDINGS))
            {
                if (measureNode.getMethod().equals(MeasureMethod.UNION))
                {
                    return new FindingsUnionAggregator(measureNode);
                }
                else if (measureNode.getMethod().equals(MeasureMethod.INTERSECT))
                {
                    return new FindingsIntersectAggregator(measureNode);
                }
            }
            else if (measureNode.getType().equals(MeasureType.NUMBER))
            {
                switch (measureNode.getMethod()) {
                    case MeasureMethod.MEAN:
                        return new NumberMeanAggregator(measureNode);
                    case MeasureMethod.MAX:
                        return new NumberMaxAggregator(measureNode);
                    case MeasureMethod.MIN:
                        return new NumberMinAggregator(measureNode);
                    case MeasureMethod.MEDIAN:
                        return new NumberMedianAggregator(measureNode);
                }
            }
        }

        return new NullProcessor((Node) node);
    }
}
