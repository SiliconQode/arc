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
package dev.siliconcode.arc.experimenter.impl.patterns;

import com.google.common.collect.ImmutableList;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.Provider;
import dev.siliconcode.arc.experimenter.provider.RepoProvider;
import dev.siliconcode.arc.experimenter.tool.MetricOnlyTool;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ArcPatternTool extends MetricOnlyTool {

    public ArcPatternTool(ArcContext context) {
        super(context);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return new PatternSizeRepoProvider(context);
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(new PatternSizeMetricProvider(context));
    }

    @Override
    public void init() {
        context.registerCommand(new PatternSizeCommand());
        context.registerCommand(new PatternChainingCommand());
        context.registerCommand(new PatternCoalescenceCommand());
    }
}
