/*
 * MIT License
 *
 * Empirilytics Base-Server
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
package dev.siliconcode.arc.server.config

/**
 * Utility class which loads the liquid-gamma configuration file and
 * produces an instance of ModelContext
 *
 * @author Isaac Griffith
 * @version 1.0.0
 */
@Singleton
class ConfigLoader {

    /**
     * Loads the configuration from the provided File
     * @param path File representing the location of the configuration
     * @return A ModelContext instance containing the configuration information
     * @throws FileNotFoundException if the provided file cannot be found.
     */
    def load(File path) {
        if (!path.exists()) {
            System.err << "Config file ${path.name} does not exist\n"
            throw new FileNotFoundException("Config file ${path.name} does not exist\n")
        }

        ConfigSlurper slurper = new ConfigSlurper()
        def confObj = slurper.parse(path.text)
        buildContext(confObj)
    }

    /**
     * Constructs the ModelContext instance from the provided ConfigObject
     * @param config Object resulting from the use of ConfigSlurper to read in the configuration file
     * @return An instance of ModelContext containing all of the LiquidGamma configuration information
     * @throws IllegalArgumentException if config is null
     */
    def buildContext(ConfigObject config) {
        if (!config)
            throw new IllegalArgumentException("buildContext: config cannot be null")

        ModelContext context = new ModelContext()
        context.db     = config.db     ?: [:]
        context.server = config.server ?: [:]

        context
    }
}
