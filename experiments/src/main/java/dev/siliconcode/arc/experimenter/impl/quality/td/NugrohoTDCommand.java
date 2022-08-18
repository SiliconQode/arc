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
package dev.siliconcode.arc.experimenter.impl.quality.td;

import dev.siliconcode.arc.datamodel.Measure;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants;
import dev.siliconcode.arc.experimenter.impl.quality.sigmain.SigMainConstants;
import dev.siliconcode.arc.experimenter.impl.quality.td.param.NugrohoParams;
import dev.siliconcode.arc.experimenter.impl.quality.td.param.TDParams;
import dev.siliconcode.arc.experimenter.impl.quality.td.strategies.NugrohoTD;
import lombok.extern.log4j.Log4j2;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class NugrohoTDCommand extends TechDebtCommand {

    public NugrohoTDCommand() {
        super(TechDebtConstants.NUGROHO_CMD_NAME, new NugrohoTD());
    }

    public void execute(ArcContext context) {
        log.info("Executing Nugroho TD Analysis");

        context.open();
        double analyzability = context.getProject().getValueFor(SigMainConstants.SIGMAIN_REPO_KEY + ":sigAnalyzability");
        double modifiability = context.getProject().getValueFor(SigMainConstants.SIGMAIN_REPO_KEY + ":sigModifiability");
        double testability = context.getProject().getValueFor(SigMainConstants.SIGMAIN_REPO_KEY + ":sigTestability");
        double modularity = context.getProject().getValueFor(SigMainConstants.SIGMAIN_REPO_KEY + ":sigModularity");
        double reusability = context.getProject().getValueFor(SigMainConstants.SIGMAIN_REPO_KEY + ":sigReusability");

        double maintainability = 0.2 * analyzability + 0.2 * modifiability + 0.2 * testability + 0.2 * modularity + 0.2 * reusability;

        double size = context.getProject().getValueFor(MetricsConstants.METRICS_REPO_KEY + ":SLOC");

        TDParams params = strategy.generateParams();
        params.setParam(NugrohoParams.MAINTAINABILITY, maintainability);
        params.setParam(NugrohoParams.REFACTORING_ADJ, 0.10);
        params.setParam(NugrohoParams.TECH_FACTOR, 0.00136d);
        params.setParam(NugrohoParams.MAINTENANCE_FRACTION, 0.15);
        params.setParam(NugrohoParams.MAINT_TEAM_COST, 100_000);
        params.setParam(NugrohoParams.SYSTEM_SIZE, size);

        double principle = strategy.calculatePrinciple(params);
        double interest = strategy.calculateInterest(params);

        double principleDollars = (principle * params.getNumericParam(NugrohoParams.MAINT_TEAM_COST)) / 12;
        double interestDollars = (interest * params.getNumericParam(NugrohoParams.MAINT_TEAM_COST)) / 12;

        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.NUGROHO_PRINCIPLE_MM)
                .on(context.getProject())
                .withValue(principle);
        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.NUGROHO_PRINCIPLE_DOLLARS)
                .on(context.getProject())
                .withValue(principleDollars);
        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.NUGROHO_INTEREST_MM)
                .on(context.getProject())
                .withValue(interest);
        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.NUGROHO_INTEREST_DOLLARS)
                .on(context.getProject())
                .withValue(interestDollars);
        context.close();

        log.info("Finished Nugroho TD Analysis");
    }
}
