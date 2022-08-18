/*
 * The MIT License (MIT)
 *
 * Empirilytics Configuration Library
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
package dev.siliconcode.arc.config

import com.google.common.collect.Lists

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * The default config provider implementation
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DefaultConfigProvider implements ConfigProvider {

    /**
     * The environment variable pointing to the home directory of the app
     */
    String envvarname
    /**
     * Current local directory
     */
    String localDirectory
    /**
     * Home directory of the app using this config provider
     */
    String homeDirectory
    /**
     * Expected configuration file name
     */
    String configFilename
    /**
     * List of command line arguments to process
     */
    def cmdLineArgs
    /**
     * map of the configuration
     */
    private def config
    /**
     * List of required keys, any of which are missing will cause a MissingRequiredConfigurationKeyException to be thrown
     */
    List<String> requiredKeys

    /**
     * Constructs a new DefaultConfigProvider
     * @param envvarname the environment variable name for the app home, if none provided defaults to ""
     * @param localDirectory current local directory
     * @param homeDirectory home directory of the application using this config provider
     * @param requiredKeys list of those keys that are required to be present
     * @param configFileName expected name of the config file, default value is msusel.config
     */
    DefaultConfigProvider(String envvarname, String localDirectory, String homeDirectory, List<String> requiredKeys, String configFileName = "msusel.config") {
        this.envvarname = envvarname
        this.localDirectory = localDirectory
        this.homeDirectory = homeDirectory
        this.configFilename = configFileName
        this.requiredKeys = requiredKeys
        config = [:]
    }

    @Override
    void loadConfig(String... cmdLineArgs) throws MissingRequiredConfigKeyException {
        this.cmdLineArgs = cmdLineArgs

        ConfigObject home = null
        if (envvarname)
            home = loadHome()
        ConfigObject local = loadLocal()
        Map<String, String> cmd = loadCmdLine()

        unify(home, local, cmd)

        checkForMissingRequiredKeys()
    }

    @Override
    List<String> configKeys() {
        Lists.newArrayList(config.keySet())
    }

    @Override
    String value(String key) {
        if (config[key])
            config[key]
        else
            null
    }
/**
     * Checks for any missing required keys
     * @throws MissingRequiredConfigKeyException if any required keys are found to be missing in the unified configuration
     */
    private void checkForMissingRequiredKeys() throws MissingRequiredConfigKeyException {
        def missingKeys = []
        requiredKeys.each { key ->
            if (!config[key])
                missingKeys << key
        }

        if (missingKeys) {
            StringBuilder builder = new StringBuilder()
            builder << 'Missing configuration keys detected, the following keys were not found in the application configuration, local\n'
            builder << 'configuration, or command line overridden configuration:\n'
            missingKeys.each { builder << "\t${it}\n"}

            throw new MissingRequiredConfigKeyException(builder.toString())
        }
    }

    /**
     * Loads the configuration information from the home directory (in "/config")
     */
    private ConfigObject loadHome() {
        def env = System.getenv()
        String homedir = envvarname ? env[envvarname] : "./"
        if (configFilename.endsWith(".properties"))
            processProperties(Paths.get(homedir, "config", configFilename))
        else
            new ConfigSlurper().parse(Files.readAllLines(Paths.get(homedir, "config", configFilename)).join("\n"))
    }

    /**
     * Loads the configuration information from the local directory
     */
    private ConfigObject loadLocal() {
        if (configFilename.endsWith(".properties"))
            processProperties(Paths.get("./", configFilename))
        else
            new ConfigSlurper().parse(Files.readAllLines(Paths.get("./", localDirectory, configFilename)).join("\n"))
    }

    private processProperties(Path path) {
        Properties props = new Properties()
        BufferedReader input
        try {
            input = Files.newBufferedReader(path)
            props.load(input)
        } catch (IOException ex) {

        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (IOException e) {

                }
            }
        }
        new ConfigSlurper().parse(props)
    }

    /**
     * Parses configuration information from the command line arguments (those starting with -D)
     */
    private def loadCmdLine() {
        def cmdline = [:]
        cmdLineArgs.each { cmd ->
            if (cmd =~ /^-D(.+)=(.+)/) {
                def group = (cmd =~ /^-D(.+)=(.+)/)
                def key = group[0][1].trim()
                def val = group[0][2].trim()
                cmdline[key] = val
            }
        }
        cmdline
    }

    /**
     * Unifies the results of the three configurations into a single configuration
     * @param home results of loading the home config
     * @param local results of loading the local config
     * @param cmd results of loading config info from the command line
     */
    private void unify(ConfigObject home, ConfigObject local, Map<String, String> cmd) {
        if (home)
            config = home.merge(local)
        else config = local.flatten()
        config += cmd
    }

    static void main(String[] args) {
        DefaultConfigProvider prov = new DefaultConfigProvider("", "msusel-config/data", "", ["test.test"], "test.properties")
        prov.loadConfig("-Dtest.other=other", "-Dtest.test=another")
        println prov.config
    }
}
