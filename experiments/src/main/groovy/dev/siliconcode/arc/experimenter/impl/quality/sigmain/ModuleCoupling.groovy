/*
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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain

import com.google.common.collect.Lists
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.quality.metrics.annotations.*
import groovyx.gpars.GParsExecutorsPool
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Module Coupling",
        primaryHandle = "sigModuleCoupling",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
class ModuleCoupling extends SigMainMetricEvaluator {

    ModuleCoupling(ArcContext context) {
        super(context)
        riskMap[RiskCategory.LOW] = Pair.of(0.0, 10.0)
        riskMap[RiskCategory.MODERATE] = Pair.of(10.0, 20.0)
        riskMap[RiskCategory.HIGH] = Pair.of(20.0, 50.0)
    }

    def evaluate(Project proj) {
        List<Type> types = []
        context.open()
        types = Lists.newArrayList(proj.getAllTypes())
        context.close()

//        GParsExecutorsPool.withPool(8) {
//            types.eachParallel { Type type ->
        types.each { Type type ->
            context.open()
            categorize(type, "Ca")
            context.close()
        }
//        }
    }

    @Override
    protected String getMetricName() {
        return "sigModuleCoupling"
    }
}
