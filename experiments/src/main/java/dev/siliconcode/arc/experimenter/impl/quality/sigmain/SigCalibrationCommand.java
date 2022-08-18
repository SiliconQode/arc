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

import dev.siliconcode.arc.datamodel.FileType;
import dev.siliconcode.arc.datamodel.Namespace;
import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.datamodel.Type;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.command.SecondaryAnalysisCommand;
import dev.siliconcode.arc.quality.metrics.MetricEvaluator;
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class SigCalibrationCommand extends SecondaryAnalysisCommand {

    SigCalibrationMetricProvider provider;

    public SigCalibrationCommand(SigCalibrationMetricProvider provider) {
        super(SigMainConstants.SIGCAL_CMD_NAME);
        this.provider = provider;
    }

    @Override
    public void execute(ArcContext context) {
        log.info("Starting Sig Maintainability Calibration Analysis");

//        context.open();

        provider.getRegistrar().getSecondaryEvaluators().forEach(metricEvaluator -> {
            MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
            log.info("Measuring project with: " + mdef.primaryHandle());
            metricEvaluator.measure(context.getProject());
        });
//        context.close();

        log.info("Finished Sig Maintainability Calibration Analysis");
    }
}