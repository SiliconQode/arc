/*
 * The MIT License (MIT)
 *
 * Empirilytics Results Aggregator
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
package dev.siliconcode.arc.experiments.aggregator

import groovy.cli.picocli.CliBuilder
import groovy.util.logging.Log4j2

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Singleton
@Log4j2
class CommandLineInterface {

    CliBuilder cli

    void initialize() {
        log.info "Initializing CLI"

        cli = new CliBuilder(
                usage: "resagg [options] <base>",
                header: "\nOptions:",
                footer: "\nCopyright (c) 2017-2021 Isaac D Griffith and Idaho State University"
        )

        cli.width = 80
    }

    void execute(String[] args, Object instance) {
        cli.parseFromInstance(instance, args)
    }
}
