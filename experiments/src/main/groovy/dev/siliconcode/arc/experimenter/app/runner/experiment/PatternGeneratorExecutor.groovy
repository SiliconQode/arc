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
package dev.siliconcode.arc.experimenter.app.runner.experiment

import com.google.common.collect.Table
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.app.runner.WorkFlow
import dev.siliconcode.arc.patterns.gen.ConfigLoader
import dev.siliconcode.arc.patterns.gen.Director
import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.gen.PluginLoader

class PatternGeneratorExecutor extends WorkFlow {

    String base
    String lang
    int NUM
    List<String> patterns

    PatternGeneratorExecutor(ArcContext context) {
        super("Design Pattern Generator", "Generates Design Pattern Instances", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
        patterns = (List<String>) runnerConfig.pattern_types
        base = (String) runnerConfig.base
        lang = (String) runnerConfig.lang
        NUM = num
    }

    void executeStudy() {
        PluginLoader plugins = PluginLoader.instance
        ConfigLoader loader = new ConfigLoader()

        File fBase = new File(base)
        fBase.mkdirs()

        plugins.loadBuiltInLanguageProviders()
        loader.buildContext(createConfig(), fBase)
        plugins.loadLanguage(lang)

        Director director = new Director()
        director.initialize()
        GeneratorContext context = GeneratorContext.instance
        context.resetDb = false
        context.dataOnly = false
        context.results = results
        director.execute()
    }

    ConfigObject createConfig() {
        String jb = System.getenv('JAVA_HOME')
        if (jb.endsWith(File.separator))
            jb += "bin" + File.separator + "java"
        else
            jb += File.separator + "bin" + File.separator + "java"

        String config = """\
        output = '$base'
        language = 'java'
        patterns = ['${patterns.join("','")}']
        numInstances = ${(int) Math.ceil((double)NUM / patterns.size())}
        maxBreadth = 3
        maxDepth = 2
        version = "1.0.0"
        arities = [1, 2, 3]
        srcPath = "src/main/java"
        testPath = "src/test/java"
        binPath = "build/classes/java/main"
        srcExt = ".java"
        java_binary = '${jb.replace('\\', '\\\\')}'

        license {
            name = 'MIT'
            year = 2021
            holder = 'Isaac D. Griffith'
            project = 'Design Pattern Generaotr'
            url = 'isu-ese.github.io/patterngen'
        }

        db {
            driver = '${context.getDBCreds().driver}'
            url = '${context.getDBCreds().url}'
            user = '${context.getDBCreds().user}'
            pass = '${context.getDBCreds().pass}'
            type = '${context.getDBCreds().type}'
        }

        build {
            project = 'griffith-experiment-one'
            artifact = 'piolot-experiment'
            description = 'Pilot Experiment Generated Pattern Instance'
        }
        """

        ConfigSlurper slurper = new ConfigSlurper()
        slurper.parse(config)
    }
}
