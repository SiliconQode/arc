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
package dev.siliconcode.arc.experimenter.app

import dev.siliconcode.arc.datamodel.util.DBManager
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.ArcProperties
import dev.siliconcode.arc.experimenter.app.runner.EmpiricalStudy
import dev.siliconcode.arc.experimenter.db.ConfigLoader
import dev.siliconcode.arc.experimenter.app.runner.StudyManager
import groovy.cli.Option
import groovy.cli.Unparsed
import groovy.cli.picocli.CliBuilder
import groovy.util.logging.Log4j2

import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class CLI {

    static final String VERSION = "1.3.0"

    static void main(String[] args) {
        ArcContext context = new ArcContext(log)
        StudyManager studyManager = new StudyManager(context)
        EmpiricalStudy empiricalStudy = null

        // Setup CLI
        CommandLineInterface cli = CommandLineInterface.instance
        cli.context = context
        cli.manager = studyManager

        String base = ""
        ConfigLoader loader = ConfigLoader.instance

        // Load Configuration
        log.info("Loading Configuration")

        (base, empiricalStudy) = cli.initializeAndParse(args)

        File fBase
        if (System.getenv("ARC_HOME") == null)
            fBase = new File(base)
        else fBase = new File((String) System.getenv("ARC_HOME"))
        File fConfig = new File(fBase, ArcConstants.PROPERTIES_FILE)
        Properties config = loader.loadConfiguration(fConfig)
        context.setArcProperties(config)
        if (System.getenv("ARC_HOME") == null)
            context.addArcProperty(ArcProperties.ARC_HOME_DIR, "")
        else context.addArcProperty(ArcProperties.ARC_HOME_DIR, System.getenv("ARC_HOME"))

        log.info("Configuration loaded")

        // Process Command Line Args
        log.info("Processing Command Line Arguments")
        context.addArcProperty(ArcProperties.BASE_DIRECTORY, base)
        log.info("Command Line Arguments Processed")

        log.info("Verifying Database and Creating if missing")
        DBManager.instance.checkDatabaseAndCreateIfMissing(context.getDBCreds())

        empiricalStudy?.run()

        // Select experiment
//        log.info(String.format("Selecting experiment: %s", empiricalStudy.getName()))
//
//        log.info("Experiment loaded and ready to execute")
//
//        // Run the experiment
//        empiricalStudy.execute()
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
@Log4j2
class CommandLineInterface {

    CliBuilder cli
    ArcContext context
    StudyManager manager

    def initializeAndParse(String[] args) {
        log.info("Initializing CLI")

        cli = new CliBuilder(
                usage: "arc [options] <base>",
                header: "\nOptions:",
                footer: "\nCopyright (c) 2017-2020 Isaac Griffith and Montana State University",
        )

        // set the amount of columns the usage message will be in width
        cli.width = 80 // default is 74

        log.info("Completed CLI Initialization")

        log.info("started parsing command line arguments")

        ArcCli arc = cli.parseFromInstance(new ArcCli(), args)

        if (arc.version) {
            println "arc experimenter version ${CLI.VERSION}"
            System.exit 0
        }

        if (arc.help) {
            System.out.println()
            cli.usage()
            System.exit 0
        }

        String base
        if (arc.base) {
            if ((arc.base.first() as String).contains("."))
                (arc.base.first() as String).replace(".", "")
            base = Paths.get(arc.base.first()).toAbsolutePath().toString()
        } else {
            base = Paths.get("").toAbsolutePath().toString()
        }

//        if (arc.properties) {
//            List<String> values = arc.properties
//            for (int i = 0; i < values.size(); i+=2)
//                context.updateProperty(values[i], values[i + 1])
//        }

//        if (options.l) {
//            String logfile = options.l
//
//            Appender console = ConsoleAppender.
//            Appender file = new FileAppender("", (String) System.getenv(ArcConstants.HOME_ENV_VAR) + "/logs/" + logfile)
//            Logger logger = LogManager.getRootLogger()
//            logger.addAppender(console)
//            logger.addAppender(file)
//        } else {
//            Appender console = new ConsoleAppender()
//            Appender file = new FileAppender("", (String) System.getenv(ArcConstants.HOME_ENV_VAR) + "/logs/arc.log")
//            Logger logger = LogManager.getRootLogger()
//            logger.addAppender(console)
//            logger.addAppender(file)
//        }

        EmpiricalStudy empiricalStudy = null
        if (arc.study) {
            String studyName = arc.study
            if (manager.studies.keySet().contains(studyName))
                empiricalStudy = manager.studies[studyName]
            else
            {
                log.error(String.format("Empirical Study, %s, is unknown. Failed to execute study runner.", studyName))
                System.exit(1)
            }
        }
        else {
            log.error("No empirical study specified. Failed to execute study runner.")
            System.exit(1)
        }

        log.info("Completed parsing command line arguments")

        return [base, empiricalStudy]
    }
}

class ArcCli {
    @Option(shortName = 'h', description = 'Print this help text and exit', longName = 'help')
    Boolean help

    @Option(shortName = 'v', longName = 'version', description = 'Print the version information')
    Boolean version

//    @Option(shortName = 'D', numberOfArguments = 2, valueSeparator = '=', numberOfArgumentsString = 'property=value', description = 'use value for given property')
//    List<String> properties

    private String study
    @Option(shortName = 'e', longName = 'empirical-study', description = 'Name of the study to execute')
    void setStudy(String study) {
        this.study = study
    }
    String getStudy() { study }

    @Unparsed(description = 'base directory')
    List base
}
