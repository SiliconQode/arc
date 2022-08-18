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

import com.google.common.collect.Lists;
import dev.siliconcode.arc.datamodel.Measure;
import dev.siliconcode.arc.datamodel.Rule;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.impl.quality.td.param.CastParams;
import dev.siliconcode.arc.experimenter.impl.quality.td.param.TDParams;
import dev.siliconcode.arc.experimenter.impl.quality.td.strategies.CastTD;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class CastTDCommand extends TechDebtCommand {

    public CastTDCommand() {
        super(TechDebtConstants.CAST_CMD_NAME, new CastTD());
    }

    @Override
    public void execute(ArcContext context) {
        log.info("Executing Cast TD Analysis");

        context.open();
        List<Rule> rules = Rule.findAll();

        List<Rule> high = Lists.newArrayList();
        List<Rule> med = Lists.newArrayList();
        List<Rule> low = Lists.newArrayList();

        for (Rule r : rules) {
            switch(r.getPriority()) {
                case VERY_LOW:
                case LOW:
                    low.add(r);
                    break;
                case MODERATE:
                    med.add(r);
                    break;
                case HIGH:
                case VERY_HIGH:
                    high.add(r);
                    break;
            }
        }

        TDParams params = strategy.generateParams();
        params.setParam(CastParams.COUNT_HIGH_SEVERITY, high.size());
        params.setParam(CastParams.COUNT_MED_SEVERITY, med.size());
        params.setParam(CastParams.COUNT_LOW_SEVERITY, low.size());

        double value = strategy.calculatePrinciple(params);

        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.CAST_MEASURE_NAME)
                .on(context.getProject())
                .withValue(value);
        context.close();

        log.info("Finished Cast TD Analysis");
    }
}
