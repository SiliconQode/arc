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
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.common.graph.MutableNetwork
import com.google.common.graph.NetworkBuilder
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.detector.GraphUtils
import dev.siliconcode.arc.disharmonies.detector.impl.GraphElementFactory
import dev.siliconcode.arc.disharmonies.detector.impl.NamespaceRelation
import dev.siliconcode.arc.disharmonies.detector.impl.Node
import dev.siliconcode.arc.disharmonies.detector.impl.Relationship
import dev.siliconcode.arc.experimenter.ArcContext
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
        name = "Sig Component Entanglement",
        primaryHandle = "sigComponentEntanglement",
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
class ComponentEntanglement extends SigMainComponentMetricEvaluator {

    MutableNetwork<Node, Relationship> graph

    ComponentEntanglement(ArcContext context) {
        super(context)
    }

    @Override
    protected double evaluate(Project proj) {
        // 1. create component graph
        createGraph(proj)
        log.info "Done creating graph"

        // 2. calc communication density
        double commDensity = ((double) graph.nodes().size()) / graph.edges().size()

        // 3. calc communication violation ratio
        if (graph.edges().size() == 0) {
            return 1.0
        } else {
            GraphUtils.getInstance().markCycles2(graph)
            double numCyclic = graph.edges().findAll { it.cyclic }.size()
            double commViolationRatio = numCyclic / graph.edges().size()

            // 4. calculate component entanglement
            return commDensity * commViolationRatio
        }
    }

    @Override
    protected String getMetricName() {
        "sigComponentEntanglement"
    }

    private void createGraph(Project proj) {
        Map<String, Node> nsMap = Maps.newConcurrentMap()

        graph = NetworkBuilder.directed()
                .allowsParallelEdges(false)
                .allowsSelfLoops(false)
                .build()

        context.open()
        List<Namespace> namespaces = Lists.newArrayList(proj.getNamespaces())
        context.close()

//        GParsExecutorsPool.withPool(8) {
//            namespaces.eachParallel { Namespace ns ->
            namespaces.each { Namespace ns ->
                context.open()
                if (!ns.getName().isEmpty()) {
                    Node node = GraphElementFactory.getInstance().createNode(ns)
                    nsMap.put(ns.getNsKey(), node)
                    graph.addNode(node)
                }
                context.close()
            }
//        }

        AtomicInteger j = new AtomicInteger(1)
//        GParsExecutorsPool.withPool(8) {
//            namespaces.eachParallel { Namespace ns ->
            namespaces.each { Namespace ns ->
                int index = j.getAndIncrement()
                log.info "processing namespace ${index} / ${namespaces.size()}"
                context.open()
                Node nsNode = nsMap.get(ns.getNsKey())
                String nsName = ns.getName()
                int nsid = ns.getId()
                context.close()
                Set<String> inNs = Sets.newHashSet()
                Set<String> outNs = Sets.newHashSet()

                if (!nsName.isEmpty()) {
                    // incoming
                    context.open()
                    inNs += incomingNamespacesFromNamespace(nsid, index)

                    // outgoing
                    outNs += outgoingNamespacesFromNamespace(nsid, index)
                    context.close()
                }

                inNs.remove(ns.getNsKey())
                outNs.remove(ns.getNsKey())

                context.open()
                inNs.each { key ->
                    Node other = nsMap.get(key)
                    if (nsNode && other && !graph.hasEdgeConnecting(nsNode, other))
                        graph.addEdge(nsNode, other, new NamespaceRelation())
                }
                outNs.each { key ->
                    Node other = nsMap.get(key)
                    if (nsNode && other && !graph.hasEdgeConnecting(other, nsNode))
                        graph.addEdge(other, nsNode, new NamespaceRelation())
                }
                context.close()

//                dropViews(index)
            }
//        }
    }

    List<Type> outgoingTypesFromType(String compKey) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view outgoingRefKeys as
select distinct y.refKey
from types as t
inner join refs as r on r.refKey = ${compKey}
inner join relations on r.id = relations.to_id
inner join refs as y on y.id = relations.from_id;
""")
        List<Type> types = Type.findBySQL("select types.* from types inner join outgoingRefKeys on types.compKey = outgoingRefKeys.refKey")
        return types
    }

    List<Type> incomingTypesFromType(String compKey) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view incomingRefKeys as
select distinct y.refKey
from types as t
inner join refs as r on r.refKey = ${compKey}
inner join relations on r.id = relations.from_id
inner join refs as y on y.id = relations.to_id;
""")
        List<Type> types = Type.findBySQL("select types.* from types inner join incomingRefKeys on types.compKey = incomingRefKeys.refKey")
        return types
    }

    List<String> outgoingNamespacesFromNamespace(int nsid, int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view outgoingNsRefs${index} as
select distinct ns.id as nsID, y.refKey
from namespaces as ns
    inner join types as t on t.namespace_id = ${nsid}
    inner join refs as r on t.compKey = r.refKey
    inner join relations on r.id = relations.from_id
    inner join refs as y on y.id = relations.to_id;
""")

        sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        List<String> namespaces = sql.rows("""\
select distinct ns.nsKey
from namespaces as ns
inner join types on types.namespace_id = ns.id
inner join outgoingNsRefs${index} as onsr on types.compKey = onsr.refKey
where ns.id != ${nsid};
""").collect {it.nsKey }
        sql.close()
        return namespaces
    }

    List<String> incomingNamespacesFromNamespace(int nsid, int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view incomingNsRefs${index} as
select distinct ns.id as nsID, y.refKey
from namespaces as ns
    inner join types as t on t.namespace_id = ${nsid}
    inner join refs as r on t.compKey = r.refKey
    inner join relations on r.id = relations.to_id
    inner join refs as y on y.id = relations.from_id;
""")
        List<String> namespaces = sql.rows("""\
select distinct ns.nsKey
from namespaces as ns
inner join types on types.namespace_id = ns.id
inner join incomingNsRefs${index} as insr on types.compKey = insr.refKey
where ns.id != ${nsid};
""").collect { it.nsKey }
        sql.close()
        return namespaces
    }

    void dropViews(int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("drop view outgoingNsRefs${index};drop view incomingNsRefs${index};")
        sql.close()
    }
}
