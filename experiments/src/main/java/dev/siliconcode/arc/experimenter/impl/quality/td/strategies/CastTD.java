/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
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
package dev.siliconcode.arc.experimenter.impl.quality.td.strategies;

import dev.siliconcode.arc.experimenter.impl.quality.td.param.CastParams;
import dev.siliconcode.arc.experimenter.impl.quality.td.param.TDParams;

import java.io.IOException;
import java.util.Properties;

/**
 * CastTD - Calculates Technical Debt using the Cast Method defined by Sappidi
 * et al.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class CastTD extends TechnicalDebtCalcStrategy {

    /**
     * {@inheritDoc}
     */
    public double calculatePrinciple(TDParams params)
    {
        double countHighSeverity = params.getNumericParam(CastParams.COUNT_HIGH_SEVERITY);
        double countMedSeverity = params.getNumericParam(CastParams.COUNT_MED_SEVERITY);
        double countLowSeverity = params.getNumericParam(CastParams.COUNT_LOW_SEVERITY);

        double timeToFixHighSeverity = params.getNumericParam(CastParams.TIME_TO_FIX_HIGH_SEVERITY);
        double timeToFixMedSeverity = params.getNumericParam(CastParams.TIME_TO_FIX_MED_SEVERITY);
        double timeToFixLowSeverity = params.getNumericParam(CastParams.TIME_TO_FIX_LOW_SEVERITY);

        double percentHighSeverity = params.getNumericParam(CastParams.PERCENT_HIGH_SEVERITY);
        double percentMedSeverity = params.getNumericParam(CastParams.PERCENT_MED_SEVERITY);
        double percentLowSeverity = params.getNumericParam(CastParams.PERCENT_LOW_SEVERITY);

        double costHighSeverity = params.getNumericParam(CastParams.COST_HIGH_SEVERITY);
        double costMedSeverity = params.getNumericParam(CastParams.COST_MED_SEVERITY);
        double costLowSeverity = params.getNumericParam(CastParams.COST_LOW_SEVERITY);

        return (countHighSeverity * percentHighSeverity * timeToFixHighSeverity * costHighSeverity)
                + (countMedSeverity * percentMedSeverity * timeToFixMedSeverity * costMedSeverity)
                + (countLowSeverity * percentLowSeverity * timeToFixLowSeverity * costLowSeverity);
    }

    public double calculateInterest(TDParams params) {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TDParams generateParams()
    {
        Properties props = new Properties();
        TDParams.Builder builder = new TDParams.Builder();

        try {
            props.load(CastTD.class.getResourceAsStream("cast.properties"));
            props.forEach((key, value) -> {
                try {
                    int v = Integer.parseInt(value.toString());
                    builder.param(key.toString(), v);
                } catch (NumberFormatException ex) {
                    try {
                        double v = Double.parseDouble(value.toString());
                        builder.param(key.toString(), v);
                    } catch (NumberFormatException x) {
                        String v = value.toString();
                        builder.param(key.toString(), v);
                    }
                }
            });
        } catch (IOException ex) {

        }

        return builder.create();
    }
}
