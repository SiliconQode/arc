/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc.datamodel.pattern

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import dev.siliconcode.arc.datamodel.Pattern
import dev.siliconcode.arc.datamodel.PatternChain
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.System
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ChainIdentifier {

    MutableGraph<PatternInstance> graph

    ChainIdentifier() {
        graph = GraphBuilder.directed().build()
    }

    def findChains(System system) {
        if (!system)
            throw new IllegalArgumentException("findChains: System cannot be null.")

        def projects = system.getProjects()
        projects.sort {a, b -> a.version <=> b.version }

        for (int i = 0; i < projects.size() - 1; i++) {
            Project current = projects[i]
            Project next = projects[i + 1]

            log.info "Current Version: ${current.getVersion()}"
            log.info "Next Version: ${next.getVersion()}"

            Pattern.findAll().each { pattern ->
                log.info ""
                log.info "Chaining for Pattern: ${(pattern as Pattern).getName()}"
                log.info ""
                List<PatternInstance> currInsts = PatternInstance.find("project_id = ? AND parent_pattern_id = ?", current.getId(), pattern.getId())
                List<PatternInstance> nextInsts = PatternInstance.find("project_id = ? AND parent_pattern_id = ?", next.getId(), pattern.getId())

                for (int currNdx = 0; currNdx < currInsts.size(); currNdx++) {
                    log.info "Current Instance ID: ${currInsts[currNdx].getId()}"
                    for (int nextNdx = 0; nextNdx < nextInsts.size(); nextNdx++) {
                        log.info "Next Instance ID: ${nextInsts[nextNdx].getId()}"
                        if (nextInsts[nextNdx].isMatched())
                            continue
                        if (checkMatchingInstances(currInsts[currNdx], nextInsts[nextNdx])) {
                            log.info "Match Found"
                            createGraphEntry(currInsts[currNdx], nextInsts[nextNdx])
                            break
                        }
                    }
                }
            }
        }
    }

    def constructChains(System sys) {
        List<PatternInstance> chainStarts = graph.nodes().findAll { graph.inDegree(it) == 0 }.toList()

        chainStarts.each { start ->
            PatternChain chain = PatternChain.builder()
                    .chainKey(start.getInstKey() + "-chain")
                    .create()

            Stack<PatternInstance> stack = new Stack<>()
            stack.push(start)
            while (!stack.isEmpty()) {
                PatternInstance inst = stack.pop()
                chain.addInstance(inst)
                for (PatternInstance succ : graph.successors(inst))
                    stack.push(succ)
            }
            sys.addPatternChain(chain)
        }
    }

    boolean checkMatchingInstances(PatternInstance first, PatternInstance second) {
        List<String> firstNames = extractRoleNames(first)
        List<String> secondNames = extractRoleNames(second)

        return secondNames.containsAll(firstNames)
    }

    List<String> extractRoleNames(PatternInstance inst) {
        List<String> names = []

        String parentProjKey = inst.getParentProject().getProjectKey()
        inst.getRoleBindings().each {binding ->
            String refKey = binding.getReference().getRefKey()
            names << refKey.replace(parentProjKey + ":", "")
        }

        return names
    }

    void createGraphEntry(PatternInstance first, PatternInstance second) {
        if (!first || !second)
            return

        first.setMatched(true)
        second.setMatched(true)

        graph.putEdge(first, second)
    }
}
