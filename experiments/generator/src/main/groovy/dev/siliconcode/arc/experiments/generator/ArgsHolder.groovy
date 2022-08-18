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

import groovy.cli.Option
import groovy.cli.Unparsed

class ArgsHolder {

    @Option(shortName='h', description='display usage')
    Boolean help

    String config
    @Option(shortName='c', description='config file name')
    void setConfig(String config) {
        if (config) this.config = config
        else config = "exgen.conf"
    }

    String getConfig() { config }

    String base

    @Unparsed(description = 'directory to be processed')
    void setBase(String[] dirs) {
        if (dirs) base = dirs[0]
        else base = "."
    }

    String getBase() { base }
}
