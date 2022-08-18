/*
 * The MIT License (MIT)
 *
 * Empirilytics Command Line Interface Library
 * Copyright (c) 2020-2021 Empirilytics
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
package dev.siliconcode.arc.cli

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
    CliParams params

    void initialize(CliParams params) {
        this.params = params

        log.info "Initializing CLI"

        cli = new CliBuilder(
                usage:  params.usage,
                header: params.header,
                footer: params.footer
        )

        cli.width = params.width

        log.inof "CLI Initialized"
    }

    void execute(String[] args, Object instance) {
        cli.parseFromInstance(instance, args)
    }
}
