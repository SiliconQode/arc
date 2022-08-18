/*
 * The MIT License (MIT)
 *
 * Empirilytics Pattern Chain Builder
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
package dev.siliconcode.arc.patterns.chains

import com.google.common.collect.Sets
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import groovy.util.logging.Log4j2

@Log4j2
class ChainBuilder {

    MutableGraph<PatternInst> graph

    File base

    List<String> versions
    List<String> patterns

    ChainBuilder(File base) {
        this.base = base
        graph = GraphBuilder.directed().build()
        versions = []
        patterns = []

        createVersionList()
        createPatternList()
    }

    void createVersionList() {
        File instDir = new File(base, "instances")

        instDir.eachDir {
            versions << it.getName()
        }
    }

    void createPatternList() {
        File instDir = new File(base, "instances")

        Set<String> set = Sets.newHashSet()
        versions.each {
            File versDir = new File(instDir, it)
            versDir.eachDir {
                set << it.getName()
            }
        }

        patterns = set.asList()
    }

    void findPatternChains() {
        log.info "Number of Versions: ${versions.size()}"
        log.info "Number of Patterns: ${patterns.size()}"

        File instDir = new File(base, "instances")
        for (int index = 0; index < versions.size() - 1; index++) {
            File versDir = new File(instDir, versions[index])
            File nextVersDir = new File(instDir, versions[index + 1])

            log.info "Current Version: ${versions[index]}"
            log.info "Next Version: ${versions[index + 1]}"

            patterns.each { pattern ->
                log.info ""
                log.info "Pattern: $pattern"
                log.info ""
                File currentPatternDir = new File(versDir, pattern)
                File nextPatternDir = new File(nextVersDir, pattern)

                if (currentPatternDir.exists() && nextPatternDir.exists()) {
                    List<File> currInstances = getInstances(currentPatternDir)
                    List<File> nextInstances = getInstances(nextPatternDir)

                    currInstances.eachWithIndex { currInst, currNdx ->
                        log.info "Current Instance: ${currInst.getName()}"
                        File currentPuml = new File(currInst, "docs${File.separator}classdiagram.puml")
                        for (int nextNdx = currNdx; nextNdx < nextInstances.size(); nextNdx++) {
                            File nextInst = nextInstances[nextNdx]
                            log.info "Next Instance: ${nextInst.getName()}"
                            File nextPuml = new File(nextInst, "docs${File.separator}classdiagram.puml")
                            if (hasPumlChainMatch(currentPuml, nextPuml)) {
                                log.info("Found match")
                                createGraphEntry(new PatternInst(version: versions[index], patternName: pattern, instNum: currInst.getName()),
                                        new PatternInst(version: versions[index + 1], patternName: pattern, instNum: nextInst.getName()))
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    void createGraphEntry(PatternInst current, PatternInst next) {
        if (!current || !next)
            return

        graph.addNode(current)
        graph.addNode(next)
        graph.putEdge(current, next)
    }

    boolean hasPumlChainMatch(File currentPuml, File nextPuml) {
        if (!currentPuml || !nextPuml || !currentPuml.exists() || !nextPuml.exists())
            return false

        List<String> currentLines = currentPuml.text.split("\n").toList()
        List<String> nextLines = nextPuml.text.split("\n").toList()

        currentLines = currentLines.findAll { it =~ /(abstract)?\s+(class|interface|enum)\s+[\w.\u0024_]+\s+#aliceblue\s+##\[bold]blue/ }
        nextLines = nextLines.findAll { it =~ /(abstract)?\s+(class|interface|enum)\s+[\w.\u0024_]+\s+#aliceblue\s+##\[bold]blue/ }

        return nextLines.containsAll(currentLines)
    }

    List<File> getInstances(File file) {
        List<File> insts = []
        file.eachDirMatch(~/^inst-\d+/) {
            insts << it
        }

        log.info "Number of instances found: ${insts.size()}"

        return insts
    }

    List<String> getPatternChains() {
        List<String> chains = []
        List<PatternInst> chainStarts = graph.nodes().findAll {
            graph.inDegree(it) == 0
        }.toList()

        chainStarts.each {start ->
            StringBuilder builder = new StringBuilder()
            Stack<PatternInst> stack = new Stack<>()
            stack.push(start)
            while (!stack.isEmpty()) {
                PatternInst inst = stack.pop()
                builder << inst.toString()
                if (graph.outDegree(inst) > 0)
                    builder << " -> "
                for (PatternInst succ : graph.successors(inst))
                    stack.push(succ)
            }
            chains << builder.toString()
        }

        return chains
    }
}
