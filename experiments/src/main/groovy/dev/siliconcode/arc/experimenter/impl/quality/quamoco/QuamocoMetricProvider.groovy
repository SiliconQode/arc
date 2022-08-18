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
package dev.siliconcode.arc.experimenter.impl.quality.quamoco

import dev.siliconcode.arc.datamodel.Metric
import dev.siliconcode.arc.datamodel.MetricRepository
import dev.siliconcode.arc.experimenter.ArcProperties
import dev.siliconcode.arc.experimenter.provider.AbstractMetricProvider
import dev.siliconcode.arc.experimenter.ArcContext
import groovy.yaml.YamlSlurper

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QuamocoMetricProvider extends AbstractMetricProvider {

    def config

    QuamocoMetricProvider(ArcContext context) {
        super(context)
    }

    void loadData() {
        File baseDir = new File(context.getArcProperty(ArcProperties.ARC_HOME_DIR))
        File configDir = new File(baseDir.getAbsoluteFile(), QuamocoConstants.QUAMOCO_CONFIG_DIR)
        File file = new File(configDir.getAbsoluteFile(), "quamoco_metrics.yml")
        config = new YamlSlurper().parseText(file.text)
    }

    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", QuamocoConstants.QUAMOCO_REPO_KEY)
        context.close()
    }

    void updateDatabase() {
        String root = config.metrics.root
        context.addArcProperty(QuamocoProperties.QUAMOCO_METRICS_ROOT, root)

        config.metrics.each { Map<String, String> map ->
            context.open()
            Metric metric = Metric.findFirst("metricKey = ?", (String) "${repository.getRepoKey()}:${map.name}")
            if (!metric) {
                metric = Metric.builder()
                        .key("${repository.getRepoKey()}:$map.name")
                        .handle(map.handle)
                        .name(map.name)
                        .description(map.description)
                        .evaluator()
                        .create()
                repository.addMetric(metric)
            }
            context.close()
        }
    }
}
