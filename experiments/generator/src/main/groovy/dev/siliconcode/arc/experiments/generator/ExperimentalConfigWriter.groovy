/*
 * The MIT License (MIT)
 *
 * Empirilytics Experiment Generator
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.experiments.generator

import com.google.common.collect.Table
import groovy.util.logging.Log4j2
import org.apache.commons.io.FileUtils

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Outputs the experimental configurations for the machines
 *
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class ExperimentalConfigWriter {

    Config conf
    Path outputDir

    ExperimentalConfigWriter(Config config) {
        this.conf = config
    }

    void execute(String path) {
        outputDir = Paths.get(path)
        createMachineOrderList()
        createDirectoryStructure()
    }

    void createMachineOrderList() {
        (conf.replications).each { rep ->
            int subListSize = (int) Math.ceil((double) conf.configurationOrders[rep].size() / conf.machines)
            (0..(conf.machines - 1)).each { machine ->
                int start = machine * subListSize
                int end = (machine + 1) * subListSize > conf.configurationOrders[rep].size() ? conf.configurationOrders[rep].size() : (machine + 1) * subListSize
                List<Integer> subList = conf.configurationOrders[rep].subList(start, end)
                conf.machineOrderList.put(rep, machine + 1, subList)
            }
        }
    }

    void createDirectoryStructure() {
        FileTreeBuilder builder = new FileTreeBuilder(outputDir.toFile())
        builder {
            (1..conf.machines).each {part ->
                dir("part_${part}") {
                    (1..conf.replications).each {rep ->
                        "rep_${rep}" {
                            dir('tool_output') {}
                            dir('code') {}
                            dir('config') {
                                dir('quamoco') {}
                                file("runner.conf", this.runnerConfContent(part, rep).stripIndent())
                                file("arc.properties", this.arcPropertiesContent().stripIndent())
                            }
                            file("experiment.conf", this.experimentConfContent(part, rep).stripIndent())
                            file("results_${rep}_${part}.csv", "")
                            file("data.db", "")
                        }
                        this.copyConfigComponents(part, rep)
                    }
                }
            }
        }
    }

    private void copyConfigComponents(int part, int rep) {
        File baseDir = new File(outputDir.toAbsolutePath().toString(), "part_${part}/rep_${rep}/config")
        File srcFile = new File("config", "patterns.config")
        File destFile = new File(baseDir, "patterns.config")
        File srcDir = new File("config", "quamoco")
        File destDir = new File(baseDir, "quamoco")
        FileUtils.copyFile(srcFile, destFile)
        FileUtils.copyDirectory(srcDir, destDir)
    }

    String runnerConfContent(int part, int rep) {
        """\
        base = "code"
        lang = "Java"
        measures = [
            'qmood-metrics:QMEFFECT',
            'qmood-metrics:QMEXTEND',
            'qmood-metrics:QMFLEX',
            'qmood-metrics:QMFUNC',
            'qmood-metrics:QMREUSE',
            'qmood-metrics:QMUNDER',
            'arc-quamoco:QFS',
            'arc-quamoco:QPE',
            'arc-quamoco:QCOMP',
            'arc-quamoco:QU',
            'arc-quamoco:QREL',
            'arc-quamoco:QS',
            'arc-quamoco:QMAIN',
            'arc-quamoco:QP',
            'techdebt:cast_td'
        ]
        status_file = "status.txt"
        results_file = "results_${rep}_${part}.csv"
        reporting_level = "PROJECT"
        """
    }

    private String arcPropertiesContent() {
        """\
        # Database properties
        arc.db.driver = org.sqlite.JDBC
        arc.db.url    = jdbc:sqlite:data.db
        arc.db.user   = dev
        arc.db.pass   = pass
        arc.db.type   = sqlite

        # Tools Properties
        arc.tool.findbugs.home = ${conf.detectorHome}/spotbugs-4.0.3/
        arc.tool.pattern4.home = ${conf.detectorHome}/pattern4
        arc.tool.pmd.home      = ${conf.detectorHome}/pmd-bin-6.24.0/
        arc.tool.gradle.binary = ${conf.gradleHome}/bin/gradle
        arc.tool.java.home     = ${conf.javaHome}

        # Directory properties
        arc.base.dir        = .
        arc.tool.output.dir = tool_output/
        """
    }

    String experimentConfContent(int part, int rep) {
        StringBuilder builder = new StringBuilder()
        Table<Integer, String, String> data = conf.configurationTables[rep]
        conf.machineOrderList.get(rep, part).each {
            builder.append("$it,${data.get(it, Constants.PatternType)},${data.get(it, Constants.GrimeType)},${data.get(it, Constants.GrimeSeverity)}\n")
        }
        builder.toString()
    }
}
