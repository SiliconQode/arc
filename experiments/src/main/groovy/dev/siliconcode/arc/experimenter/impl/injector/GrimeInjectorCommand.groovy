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
package dev.siliconcode.arc.experimenter.impl.injector


import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.command.SecondaryAnalysisCommand
import dev.siliconcode.arc.disharmonies.injector.Director
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class GrimeInjectorCommand extends SecondaryAnalysisCommand {

    GrimeInjectorCommand() {
        super(InjectorConstants.GRIME_INJECT_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        log.info("Starting Grime Injection")

        log.info("Setting up Grime Injection Configuration")
        String configString = """\
            where {
                systemKey = '${context.getProject().getParentSystem().getKey()}'
                projetKey = '${context.getProject().getProjectKey()}'
                patternKey = '${context.getArcProperty(InjectorProperties.GRIME_INJECT_KEY)}'
                patternInst = '${context.getArcProperty(InjectorProperties.GRIME_INJECT_INST)}'
            }

            what {
                type = '${context.getArcProperty(InjectorProperties.GRIME_INJECT_TYPE)}'
                form = '${context.getArcProperty(InjectorProperties.GRIME_INJECT_FORM)}'
                max = ${context.getArcProperty(InjectorProperties.GRIME_INJECT_MAX)}
                min = ${context.getArcProperty(InjectorProperties.GRIME_INJECT_MIN)}
            }
            """
        ConfigSlurper slurper = new ConfigSlurper()
        def config = slurper.parse(configString)

        log.info("Grime Injection Configuration Complete")

        log.info("Injecting grime")
        Director.instance.inject(config)
        log.info("Grime Inject Complete")
    }
}
