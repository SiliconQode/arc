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

import dev.siliconcode.arc.experimenter.impl.quality.td.param.TDParams;

/**
 * SonarTD -
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class SonarTD extends TechnicalDebtCalcStrategy {

    private static final String COST_TO_FIX_ONE_BLOCK = null;
    private static final String COST_TO_FIX_ONE_VIOLATION = null;
    private static final String COST_TO_COMMENT_ONE_API = null;
    private static final String COST_TO_COVER_ONE_OF_COMPLEXITY = null;
    private static final String COST_TO_CUT_AN_EDGE_BETWEEN_TWO_FILES = null;
    private static final String COST_TO_SPLIT_A_METHOD = null;
    private static final String COST_TO_SPLIT_A_CLASS = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculatePrinciple(TDParams params)
    {
        double costToFixOneBlock = params.getNumericParam(COST_TO_FIX_ONE_BLOCK);
        double duplicatedBlocks = 0;
        double costToFixOneViolation = params.getNumericParam(COST_TO_FIX_ONE_VIOLATION);
        double mandatoryViolations = 0;
        double costToCommentOneAPI = params.getNumericParam(COST_TO_COMMENT_ONE_API);
        double publicUndocumentedAPI = 0;
        double costToCoverOneOfComplexity = params.getNumericParam(COST_TO_COVER_ONE_OF_COMPLEXITY);
        double uncoveredComplexityByTests = 0;
        double costToCutAnEdgeBetweenTwoFiles = params.getNumericParam(COST_TO_CUT_AN_EDGE_BETWEEN_TWO_FILES);
        double packageEdgesWeight = 0;
        double costToSplitAMethod = params.getNumericParam(COST_TO_SPLIT_A_METHOD);
        double functionComplexityDistribution = 0;
        double costToSplitAClass = params.getNumericParam(COST_TO_SPLIT_A_CLASS);
        double classComplexitDistribution = 0;

        double duplication = costToFixOneBlock * duplicatedBlocks;
        double violations = costToFixOneViolation * mandatoryViolations;
        double comments = costToCommentOneAPI * publicUndocumentedAPI;
        double coverage = costToCoverOneOfComplexity * uncoveredComplexityByTests;
        double design = costToCutAnEdgeBetweenTwoFiles * packageEdgesWeight;
        double complexity = (costToSplitAMethod * functionComplexityDistribution)
                + (costToSplitAClass * classComplexitDistribution);

        return duplication + violations + comments + coverage + design + complexity;
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
        // TODO Auto-generated method stub
        return null;
    }

}
