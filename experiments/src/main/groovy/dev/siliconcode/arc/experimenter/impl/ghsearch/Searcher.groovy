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
package dev.siliconcode.arc.experimenter.impl.ghsearch

import dev.siliconcode.arc.datamodel.System
import org.apache.commons.cli.*
import org.javalite.activejdbc.Base

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.javalite.app_config.AppConfig.p

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Searcher implements CLIProcessor {

    static Options options = new Options()

    static {
        Option help = Option.builder("h")
                .desc("Display this message.")
                .longOpt("help")
                .optionalArg(true)
                .build()
        Option minSize = Option.builder("m")
                .desc("The minimum size for a project")
                .hasArg()
                .numberOfArgs(1)
                .argName("SIZE")
                .type(Integer.class)
                .optionalArg(true)
                .build()
        Option maxSize = Option.builder("n")
                .desc("The maximum size for a project")
                .hasArg()
                .numberOfArgs(1)
                .argName("SIZE")
                .type(Integer.class)
                .optionalArg(true)
                .build()
        Option minStars = Option.builder("s")
                .desc("The minimum number of stars for a project")
                .hasArg()
                .numberOfArgs(1)
                .argName("STARS")
                .type(Integer.class)
                .optionalArg(true)
                .build()
        Option maxStars = Option.builder("t")
                .desc("The minimum number of tags for a project")
                .hasArg()
                .numberOfArgs(1)
                .argName("TAGS")
                .type(Integer.class)
                .optionalArg(true)
                .build()
        Option output = Option.builder("o")
                .desc("The name of the file to output")
                .hasArg()
                .numberOfArgs(1)
                .argName("FILE")
                .required()
                .build()
        Option maxProj = Option.builder("p")
                .desc("The maximum number of projects to select")
                .hasArg()
                .numberOfArgs(1)
                .argName("NUM")
                .type(Integer.class)
                .optionalArg(true)
                .build()
        options.addOption(help)
        options.addOption(minSize)
        options.addOption(maxSize)
        options.addOption(minStars)
        options.addOption(maxStars)
        options.addOption(output)
        options.addOption(maxProj)
    }

    @Override
    void process(String[] args) {
        CommandLineParser parser = new DefaultParser()
        try {
            CommandLine cmd = parser.parse(options, args)

            int minSize = 5000
            int maxSize = 1000000
            int minStars = 1000
            int minTags = 10
            int maxProj = 10
            String file = ""

            if (cmd.hasOption("m")) {
                minSize = (Integer) cmd.getParsedOptionValue("m")
            } else if (cmd.hasOption("n")) {
                maxSize = (Integer) cmd.getParsedOptionValue("n")
            } else if (cmd.hasOption("s")) {
                minStars = (Integer) cmd.getParsedOptionValue("s")
            } else if (cmd.hasOption("t")) {
                minTags = (Integer) cmd.getParsedOptionValue("t")
            } else if (cmd.hasOption("o")) {
                file = cmd.getOptionValue("o")
            } else if (cmd.hasOption("p")) {
                maxProj = (Integer) cmd.getParsedOptionValue("p")
            } else if (cmd.hasOption("h")) {
                printHelp()
            }

            String db = p("db.file")
            String user = p("db.user")
            String pass = p("db.pass")
            Base.open("org.sqlite.JDBC", "jdbc:sqlite:" + db, user, pass)

            findProjects(minSize, maxSize, minStars, minTags, maxProj, file)
        } catch(ParseException ex)
        {
            ex.printStackTrace()
            printHelp()
        }

    }

    @Override
    String getCmdName() {
        return "search"
    }

    @Override
    String getCmdDescription() {
        return getCmdName() + "\tSearches GitHub for viable projects for analysis"
    }

    private void printHelp() {
        String header = "Generates a list of projects extracted from GitHub\n\n"
        String footer = "\nCopyright (c) 2020 Idaho State University Empirical Software Engineering Lab."

        HelpFormatter formatter = new HelpFormatter()
        formatter.printHelp("ghsearch", header, options, footer, true)
    }

    void findProjects(int minSize, int maxSize, int minStars, int minTags, int maxProj, String file) {
        GitHubSearchCommand ghs = new GitHubSearchCommand(p("gh.user"), p("gh.token"))
        ghs.authenticate()

        List<System> systems = ghs.findProjects(maxProj, minSize, maxSize, minStars, minTags)

        Path p = Paths.get(file)
        try {
            Files.deleteIfExists(p)
            Files.createFile(p)
        } catch (IOException e) {
            e.printStackTrace()
        }

        String json = "{\n"
        for (System s : systems) {
            json += s.toJson(true, "")
            json += ",\n"
        }
        json = json.substring(0, json.length() - 1)
        json += "}"

        p.toFile().withWriter { pw ->
            pw.println(json)
        }
    }
}
