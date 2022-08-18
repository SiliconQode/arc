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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain;

import com.google.common.collect.ImmutableList;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.Provider;
import dev.siliconcode.arc.experimenter.provider.RepoProvider;
import dev.siliconcode.arc.experimenter.tool.MetricOnlyTool;

import java.util.List;

public class SigRatingTool extends MetricOnlyTool {

    SigRatingMetricProvider metricProvider;
    SigCalibrationRepoProvider repoProvider;

    public SigRatingTool(ArcContext context) {
        super(context);
        repoProvider = new SigCalibrationRepoProvider(context);
        metricProvider = new SigRatingMetricProvider(context);
    }

    @Override
    public RepoProvider getRepoProvider() { return repoProvider; }

    @Override
    public List<Provider> getOtherProviders() { return ImmutableList.of(metricProvider); }

    @Override
    public void init() { context.registerCommand(new SigRatingCommand(metricProvider)); }
}