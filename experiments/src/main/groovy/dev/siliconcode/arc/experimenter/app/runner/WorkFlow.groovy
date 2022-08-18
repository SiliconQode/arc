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
package dev.siliconcode.arc.experimenter.app.runner

import com.google.common.collect.Table
import dev.siliconcode.arc.experimenter.ArcContext
import groovy.transform.ToString
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
@ToString(includes = ["name"])
abstract class WorkFlow {

    String name
    String description
    Table<String, String, String> results
    ArcContext context

    WorkFlow(String name, String description, ArcContext context) {
        this.name = name
        this.description = description
        this.context = context
    }

    void execute(ConfigObject config, int num) {
        log.info("Running Empirical Study: $name")
        log.info("Initializing study workflow")
        initWorkflow(config, num)

        log.info("Starting empirical study workflow")
        executeStudy()
        log.info("Empirical study workflow complete")
    }

    abstract void initWorkflow(ConfigObject runnerConfig, int num)

    abstract void executeStudy()

    String normalizePath(String path) {
        while (path.contains("/"))
            path = path.replace("/", "[sep]")
        while (path.contains("\\"))
            path = path.replace("\\", "[sep]")
        while (path.contains("[sep]"))
            path = path.replace("[sep]", File.separator)
        path
    }
}
