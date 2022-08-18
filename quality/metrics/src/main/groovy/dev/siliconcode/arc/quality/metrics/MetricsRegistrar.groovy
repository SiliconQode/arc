/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
package dev.siliconcode.arc.quality.metrics

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Lists
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.quality.metrics.annotations.MetricDefinition

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MetricsRegistrar {

    public static Map<String, MetricsRegistrar> instances = [:]

    private static final String PRIMARY = "primary"
    private static final String SECONDARY = "secondary"

    Table<String, String, MetricEvaluator> registry = HashBasedTable.create()

    Map<String, String> handles = [:]
    MetricRepository repo

    MetricsRegistrar(MetricRepository repo) {
        this.repo = repo
        instances[repo.getRepoKey()] = this
    }

    void registerPrimary(MetricEvaluator evaluator) {
        register(evaluator, PRIMARY)
    }

    void registerSecondary(MetricEvaluator evaluator) {
        register(evaluator, SECONDARY)
    }

    void register(MetricEvaluator evaluator, String category) {
        Class<? extends MetricEvaluator> clazz = evaluator.getClass()
        MetricDefinition mdef = clazz.getAnnotation(MetricDefinition.class)
        if ((mdef.name() != null && !mdef.name().isEmpty()) && (mdef.primaryHandle() != null && !mdef.primaryHandle().isEmpty())) {
            registry.put(category, mdef.primaryHandle(), evaluator)
            handles[mdef.primaryHandle()] = mdef.primaryHandle()
            mdef.otherHandles().each {
                handles[it] = mdef.primaryHandle()
            }
        }

        evaluator.setRepo(this.repo)
    }

    MetricEvaluator getMetric(String primaryHandle, String category = null) {
        if (!category) {
            if (registry.get(PRIMARY, primaryHandle))
                registry.get(PRIMARY, primaryHandle)
            else if (registry.get(SECONDARY, primaryHandle))
                registry.get(SECONDARY, primaryHandle)
            else
                null
        } else {
            registry.get(category, primaryHandle)
        }
    }

    List<MetricEvaluator> getPrimaryEvaluators() {
        getCategoryEvaluators(PRIMARY)
    }

    List<MetricEvaluator> getSecondaryEvaluators() {
        getCategoryEvaluators(SECONDARY)
    }

    List<MetricEvaluator> getCategoryEvaluators(String category) {
        if (registry.containsRow(category)) {
            return registry.row(category).values().asList()
        } else {
            return Lists.newArrayList()
        }
    }

    String getHandle(String handle) {
        handles[handle]
    }

    MetricEvaluator getEvaluator(String handle, String category = null) {
        String eval = getHandle(handle)
        getMetric(eval, category)
    }
}
