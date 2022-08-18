/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
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
package dev.siliconcode.arc.disharmonies.injector

import dev.siliconcode.arc.datamodel.util.DBManager
import groovy.cli.picocli.CliBuilder
import groovy.cli.picocli.OptionAccessor

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class App {

    static final String VERSION = "1.3.0"

    static void main(String[] args) {
        // Setup CLI
        CommandLineInterface cli = CommandLineInterface.instance
        cli.initialize()
        ConfigLoader loader = ConfigLoader.instance

        // Process Command Line Args
        String conf
        String base
        (conf, base) = cli.parse(args)
        File fBase = new File(base)
        File fConfig = new File(fBase, conf)

        // Load Configuration
        ConfigObject config = loader.loadConfiguration(fConfig)

        // Run the Program
        DBManager.instance.open(config.db.driver, config.db.url, config.db.user, config.db.pass)
        Director.instance.inject(config)
        DBManager.instance.close()
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class CommandLineInterface {

    CliBuilder cliBuilder

    void initialize() {
        cliBuilder = new CliBuilder(
                usage: "si [options] <base>",
                header: "\nOptions:",
                footer: "\nCopyright (c) 2018-2020 Isaac Griffith and Montana State University",
        )

        // set the amount of columns the usage message will be in width
        cliBuilder.width = 80 // default is 74

        // add options
        cliBuilder.with {
            h longOpt: 'help', 'Print this help text and exit'
            c longOpt: 'config-file', args: 1, argName: 'file', 'Name of config file found in the base directory'
            v longOpt: 'version', 'Print the version information'
        }
    }

    def parse(args) {
        OptionAccessor options = cliBuilder.parse(args)

        if (!options) {
            System.err << 'Error while parsing command-line options.\n\n'
            cliBuilder.usage()
            System.exit 1
        }

        if (options.v) {
            println "software injector version ${App.VERSION}"
            System.exit 0
        }

        if (options.h) {
            cliBuilder.usage()
            System.exit 0
        }

        String config
        if (options.c) {
            config = options.c
        } else {
            config = "InjectorConfig"
        }

        String base
        if (options.arguments()) {
            base = options.arguments()[0]
        } else {
            base = "."
        }

        return [config, base]
    }
}
