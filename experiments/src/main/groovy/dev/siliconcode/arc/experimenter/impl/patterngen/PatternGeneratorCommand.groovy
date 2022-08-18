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
package dev.siliconcode.arc.experimenter.impl.patterngen

import dev.siliconcode.arc.datamodel.SCMType
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.command.PrimaryAnalysisCommand
import dev.siliconcode.arc.experimenter.db.DbProperties
import dev.siliconcode.arc.patterns.gen.Director
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.gen.PatternManager
import dev.siliconcode.arc.patterns.gen.PluginLoader
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class PatternGeneratorCommand extends PrimaryAnalysisCommand {

    PatternGeneratorCommand() {
        super(GeneratorConstants.GENERATOR_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        PluginLoader plugins = PluginLoader.instance

        log.info("PatternGenerator: Creating the base directory")
        File fBase = new File(context.getArcProperty(GeneratorProperties.GEN_BASE_DIR))
        fBase.mkdirs()

        log.info("PatternGenerator: Setting up the GeneratorContext and loading generator plugins")
        createGeneratorContext(context)
        plugins.loadBuiltInLanguageProviders()
        plugins.loadLanguage(context.getArcProperty(GeneratorProperties.GEN_LANG_PROP))
        log.info("PatternGenerator: Completed GeneratorContext setups and loading generator plugins")

        // Run the program
        log.info("PatternGenerator: Initializing the director")
        Director director = new Director()
        director.initialize()
        log.info("PatternGenerator: Director initialized")

        log.info("PatternGenerator: Executing...")
        director.execute()
        log.info("PatternGenerator: Execution complete")
    }

    String createGeneratorContext(ArcContext context) {
        GeneratorContext generatorContext = GeneratorContext.getInstance()
        generatorContext.base         = new File(context.getArcProperty(GeneratorProperties.GEN_BASE_DIR))
        generatorContext.output       = context.getArcProperty(GeneratorProperties.GEN_OUTPUT_DIR)
        generatorContext.loader       = PatternManager.getInstance()
        generatorContext.patterns     = createPatternsList(context)
        generatorContext.numInstances = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_NUM_INSTANCES))
        generatorContext.maxBreadth   = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_MAX_BREADTH))
        generatorContext.maxDepth     = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_MAX_DEPTH))
        generatorContext.version      = context.getArcProperty(GeneratorProperties.GEN_VERSION)
        generatorContext.license      = createLicenseMap(context)
        generatorContext.db           = createDbMap(context)
        generatorContext.build        = createBuildMap(context)
        generatorContext.arities      = createAritiesList(context)
        generatorContext.srcPath      = context.getArcProperty(GeneratorProperties.GEN_SOURCE_PATH)
        generatorContext.srcExt       = context.getArcProperty(GeneratorProperties.GEN_SOURCE_EXT)
        generatorContext.resetDb      = false
        generatorContext.resetOnly    = false
        generatorContext.generateOnly = false
        generatorContext.dataOnly     = false

        log.info("PatternGenerator: GeneratorContext created with the following values:")
        log.info(generatorContext.toString())
    }

    private Map<String, String> createLicenseMap(ArcContext context) {
        [
                "name": context.getArcProperty(GeneratorProperties.GEN_LICENSE_NAME),
                "year": context.getArcProperty(GeneratorProperties.GEN_LICENSE_YEAR),
                "holder": context.getArcProperty(GeneratorProperties.GEN_LICENSE_HOLDER),
                "project": context.getProject().getName(),
                "url": context.getProject().getSCM(SCMType.GIT).getURL()
        ]
    }

    private Map<String, String> createDbMap(ArcContext context) {
        [
                "driver": context.getArcProperty(DbProperties.DB_DRIVER),
                "url": context.getArcProperty(DbProperties.DB_URL),
                "user": context.getArcProperty(DbProperties.DB_USER),
                "pass": context.getArcProperty(DbProperties.DB_PASS),
                "type": context.getArcProperty(DbProperties.DB_TYPE)
        ]
    }

    private Map<String, String> createBuildMap(ArcContext context) {
        [
                "project": context.getArcProperty(GeneratorProperties.GEN_BUILD_PROJECT),
                "artifact": context.getArcProperty(GeneratorProperties.GEN_BUILD_ARTIFACT),
                "description": context.getArcProperty(GeneratorProperties.GEN_BUILD_DESC)
        ]
    }

    private List<Integer> createAritiesList(ArcContext context) {
        String a = context.getArcProperty(GeneratorProperties.GEN_ARITIES)
        String[] vals = a.split(/,\w*/)
        List<Integer> arities = []
        vals.each {
            arities << Integer.parseInt(it.trim())
        }

        arities
    }

    private List<String> createPatternsList(ArcContext context) {
        String[] patterns = context.getArcProperty(GeneratorProperties.GEN_PATTERNS).split(/,\w*/)
        patterns.toList()
    }
}
