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
import com.google.common.collect.Sets
import com.google.common.util.concurrent.AtomicDouble
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.Measure
import dev.siliconcode.arc.datamodel.Namespace
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants
import dev.siliconcode.arc.quality.metrics.annotations.*
import groovy.sql.Sql
import groovy.util.logging.Log4j2
import groovyx.gpars.GParsExecutorsPool

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Component Independence",
        primaryHandle = "sigComponentIndependence",
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
@Log4j2
class ComponentIndependence extends SigMainComponentMetricEvaluator {

    ComponentIndependence(ArcContext context) {
        super(context)
    }

    @Override
    protected double evaluate(Project proj) {
        context.open()
        Set<Namespace> namespaces = Sets.newHashSet(proj.getNamespaces())
        context.close()

        AtomicDouble hiddenSize = new AtomicDouble(0)
        AtomicInteger index = new AtomicInteger(1)

//        GParsExecutorsPool.withPool(8) {
//            namespaces.eachParallel { Namespace ns ->
            namespaces.each { Namespace ns ->
                int ndx = index.getAndIncrement()
                log.info "processing namespace ${ndx} / ${namespaces.size()}"
                context.open()
                int nsid = ns.getId()
                double ca = ns.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:Ca")
                String name = ns.getName()
                context.close()

                if (name != null && ca > 0.0) {
                    Set<Type> hiddenTypes = Sets.newHashSet(hiddenTypesFromNamespace(nsid, ndx))
//                dropView(ndx)

//                GParsExecutorsPool.withPool(8) {
                    int j = 1
                    hiddenTypes.each { Type type ->
                        log.info "processing type ${j++} / ${hiddenTypes.size()}"
                        context.open()
                        double size = type.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:SLOC")
                        context.close()
                        hiddenSize.addAndGet(size)
                    }
                }
            }
//        }
//        }

        context.open()
        double projSize = proj.getValueFor("${MetricsConstants.METRICS_REPO_NAME}:SLOC")
        context.close()

        return (1 - (projSize - hiddenSize.get()) / projSize) * 100
    }

    @Override
    protected String getMetricName() {
        "sigComponentIndependence"
    }

    List<Type> hiddenTypesFromNamespace(int nsid, int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view hidden${index} as
select distinct t.compKey as toKey, y.refKey as fromKey
from namespaces as ns
    inner join types as t on t.namespace_id = ${nsid}
    inner join refs as r on t.compKey = r.refKey
    inner join relations on r.id = relations.to_id
    inner join refs as y on y.id = relations.from_id and y.refKey not like CONCAT(ns.nsKey + ':%');
""")
        sql.close()

        context.open()
        List<Type> types = Lists.newArrayList(Type.findBySQL("""\
select * from types
where namespace_id = ${nsid} and (compKey not in (select toKey from hidden${index}));
"""))
        context.close()
        return types
    }

    void dropView(int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("drop view hidden${index}")
        sql.close()
    }


}
