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
package dev.siliconcode.arc.quality.quamoco.processor.lineardist;

import dev.siliconcode.arc.quality.quamoco.processor.LinearDistribution;

/**
 * Base implementation of a LinearDistribution
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class AbstractLinearDistribution implements LinearDistribution {

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculate(final double proportion, final double lowerBound, final double lowerResult,
            final double upperBound, final double upperResult)
    {
        if (Double.compare(proportion, lowerResult) < 0)
        {
            return lowerBound;
        }
        else if (Double.compare(proportion, upperResult) > 0)
        {
            return upperBound;
        }
        else if (Double.compare(upperResult, lowerResult) == 0)
        {
            return upperBound;
        }
        else
        {
            double slopeDen = upperResult - lowerResult;
            double slopeNum = upperBound - lowerBound;
            final double slope = slopeNum / slopeDen;
            return slope * (proportion - lowerResult) + lowerBound;
        }
    }
}
