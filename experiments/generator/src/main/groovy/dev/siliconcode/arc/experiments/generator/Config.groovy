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

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table

@Singleton
class Config {

    int machines = 1
    int replications = 1
    int severityLevels
    String javaHome
    String gradleHome
    String detectorHome
    List<String> patternTypes
    List<String> injectionTypes
    Map<Integer, Table<Integer, String, String>> configurationTables // indexed by replication number
    Map<Integer, List<Integer>> configurationOrders // indexed by replication number
    Table<Integer, Integer, List<Integer>> machineOrderList // row is replication, col is machine

    void load(String file) {
        ConfigSlurper slurper = new ConfigSlurper()
        File config = new File(file)
        ConfigObject co = slurper.parse(config.text)

        machines = co.machines
        replications = co.replications
        javaHome = co.javaHome
        gradleHome = co.gradleHome
        detectorHome = co.detectorHome
        patternTypes = co.patternTypes
        injectionTypes = co.injectionTypes
        severityLevels = co.severityLevels
        configurationTables = [:]
        configurationOrders = [:]
        machineOrderList = HashBasedTable.create()
    }
}
