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

import groovy.util.logging.Log4j2

/**
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
@Log4j2
class App {

    static void main(String[] args) {
        CommandLineInterface cli = CommandLineInterface.instance
        cli.initialize()

        ArgsHolder holder = new ArgsHolder()
        cli.execute(args, holder)

        if (holder.help) {
            cli.cli.usage()
            System.exit(0)
        }

        String output = "chains.dat"
        if (holder.output) {
            output = holder.output
        }

        String base = holder.getBase()
        File baseDir = new File(base)

        if (baseDir.exists() && baseDir.isDirectory()) {
            ChainBuilder builder = new ChainBuilder(baseDir)
            builder.findPatternChains()
            ChainWriter writer = new ChainWriter()
            writer.writeChains(builder, baseDir, output)
        } else {
            System.err.println("Error: either base directory is unreadable, does not exist, or is not a directory")
            System.err.println("")
            cli.cli.usage()
        }
    }
}
