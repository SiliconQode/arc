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
package dev.siliconcode.arc.experimenter.impl.quality.td.param;

/**
 * SonarParams -
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface SonarParams {

    String COST_TO_SPLIT_A_CLASS = "costToSplitAClass";
    String COST_TO_SPLIT_A_METHOD = "costToSplitAMethod";
    String COST_TO_CUT_AN_EDGE_BETWEEN_TWO_FILES = "costToCutAnEdgeBetweenTwoFiles";
    String COST_TO_COVER_ONE_OF_COMPLEXITY = "costToCoverOneOfComplexity";
    String COST_TO_COMMENT_ONE_API = "costToCommentOneAPI";
    String COST_TO_FIX_ONE_VIOLATION = "costToFixOneViolation";
    String COST_TO_FIX_ONE_BLOCK = "costToFixOneBlock";
}
