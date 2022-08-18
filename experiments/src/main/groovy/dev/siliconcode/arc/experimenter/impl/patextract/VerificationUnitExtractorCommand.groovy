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
package dev.siliconcode.arc.experimenter.impl.patextract

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.PatternInstance
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.command.SecondaryAnalysisCommand
import groovy.util.logging.Log4j2
import org.apache.commons.lang3.tuple.Pair

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentMap

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class VerificationUnitExtractorCommand extends SecondaryAnalysisCommand {

    VerificationUnitExtractorCommand() {
        super(PatternExtractorConstants.UNIT_EXTRACTOR_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        log.info "Started Verification Unit Extraction"
//        String base = context.getArcProperty(PatternExtractorConstants.BASE_DIR)
        String base = context.getArcProperty("arc.base.dir")
        if (base.contains("."))
            base.replace(".", "")
        File baseDir = new File(base)

//        if (context.getArcProperty("arc.base.dir").endsWith(".") || context.getArcProperty("arc.base.dir").endsWith("." + File.separator))
//            baseDir = new File(base)
//        else
//            baseDir = new File(Paths.get(context.getArcProperty("arc.base.dir")).toAbsolutePath().toFile(), base)

        context.open()

        List<Pair<PatternInstance, PatternInstance>> pairs = []
        context.system.getPatternChains().each { chain ->
            PatternInstance first = null
            PatternInstance second = null
            chain.getInstances().sort { a, b -> a.getInstKey() <=> b.getInstKey() }.each { inst ->
                if (!first) first = inst
                else if (!second) second = inst

                if (second) {
                    if (second.shouldExtract()) {
                        pairs << Pair.of(first, second)
                    }

                    first = second
                    second = null
                }
            }
        }
        writeOutPairs(baseDir, pairs)

        context.close()
        log.info "Finished Verification Unit Extraction"
    }

    def writeOutPairs(File baseDir, List<Pair<PatternInstance, PatternInstance>> pairs) {
        int unitNum = 1
        int totalUnitCount = 0
        pairs.each {pair ->
            PatternInstance base = pair.left
            PatternInstance infected = pair.right

            String unitName = "unit-$unitNum"

            var baseTuple = collectTypeData(base)
            var infTuple = collectTypeData(infected)

            createDirectoryStructure(base, infected, baseDir, unitName, baseTuple[0] as String, infTuple[0] as String, base.getParentProject().getProjectKey(), infected.getParentProject().getProjectKey(), base.getParentPattern().getName())
            copyFiles(baseDir, unitName, baseTuple[2] as ConcurrentMap<String, Set<String>>, infTuple[2] as ConcurrentMap<String, Set<String>>)
            generateInjectionControlData(unitName, base, infected, baseDir)

            unitNum++
            totalUnitCount++
        }

        writeAnalysisConfig(baseDir, totalUnitCount)
    }

    private writeAnalysisConfig(File baseDir, int totalUnitCount) {
        log.info "Started Writing Analysis Config"
        File analysisConfig = new File(new File(baseDir, "units"), "verex.conf")
        Files.deleteIfExists(analysisConfig.toPath())
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(analysisConfig.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))
        {
            for (int id = 1; id <= totalUnitCount; id++) {
                String unitName = "unit-$id"
                String sysKey = "verification"
                String sysLoc = new File(baseDir, "units").toString().replace("./", "")
                String baseKey = "verification:${unitName}-base"
                String infKey = "verification:${unitName}-inf"
                String injKey = "${unitName}-inj"
                String baseLoc = "${unitName}${File.separator}base"
                String infLoc = "${unitName}${File.separator}infected"
                String injLoc = "${unitName}${File.separator}injected"
                String controlFileLoc = "${unitName}${File.separator}injector.control"
                pw.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", id, sysKey, sysLoc, baseKey, infKey, injKey, baseLoc, infLoc, injLoc, controlFileLoc)
            }
        } catch (IOException ex) {
            log.error "Could not write out the analysis config"
        }
        log.info "Finished Writing Analysis Config"
    }

    private void createDirectoryStructure(PatternInstance baseInst, PatternInstance infInst, File baseDir, String unitName, String basePuml, String infPuml, String baseProjKey, String infProjKey, String pattern) {
        log.info "Creating Directory Structure for unit: $unitName"
        def tree = new FileTreeBuilder(baseDir)
        String settingsBase = createGradleSettings("$unitName-base")
        String buildBase = createGradleBuild()
        String settingsInf = createGradleSettings("$unitName-infected")
        String buildInf = createGradleBuild()
        String baseInstConf = PatternInstanceWriter.instance.write(baseInst)
        String infInstConf = PatternInstanceWriter.instance.write(infInst)

        tree."units" {
            "$unitName" {
                base {
                    src {
                        main {
                            java {
                            }
                        }
                    }
                    docs {
                        "classdiagram.puml"(basePuml)
                    }
                    "build.gradle"(buildBase)
                    "settings.gradle"(settingsBase)
                    "instance.conf"(baseInstConf)
                }
                infected {
                    src {
                        main {
                            java {
                            }
                        }
                    }
                    docs {
                        "classdiagram.puml"(infPuml)
                    }
                    "build.gradle"(buildInf)
                    "settings.gradle"(settingsInf)
                    "instance.conf"(infInstConf)
                }
                "unit.properties"("""\
                    base.project = ${baseProjKey}
                    infected.project = ${infProjKey}
                    pattern.type = ${pattern}
                """.stripIndent())
                "injection.control"(generateInjectionControlFile())
            }
        }
    }

    private void copyFiles(File baseDir, String unitName, ConcurrentMap<String, Set<String>> basePkgFiles, ConcurrentMap<String, Set<String>> infPkgFiles) {
        basePkgFiles.each { pkg, Set<String> files ->
            String pkgPath = convertPkgToPath(pkg)
            files.each { file ->
                Path srcPath = Paths.get(file)
                String fileName = srcPath.getFileName().toString()
                Path targetPath = Paths.get(baseDir.toPath().toString(), "units", unitName, "base", "src", "main", "java", pkgPath, fileName)
                Files.createDirectories(Paths.get(baseDir.toPath().toString(), "units", unitName, "base", "src", "main", "java", pkgPath))
                Files.copy(srcPath, targetPath)
            }
        }

        infPkgFiles.each { pkg, Set<String> files ->
            String pkgPath = convertPkgToPath(pkg)
            files.each { file ->
                Path srcPath = Paths.get(file)
                String fileName = srcPath.getFileName().toString()
                Path targetPath = Paths.get(baseDir.toPath().toString(), "units", unitName, "infected", "src", "main", "java", pkgPath, fileName)
                Files.createDirectories(Paths.get(baseDir.toPath().toString(), "units", unitName, "infected", "src", "main", "java", pkgPath))
                Files.copy(srcPath, targetPath)
            }
        }
    }

    private String convertPkgToPath(String pkg) {
        return pkg.replace('.', File.separator)
    }

    def collectTypeData(PatternInstance inst) {
        Set<Type> types = Sets.newHashSet(inst.getTypes())
        Set<Type> related = findRelatedTypes(types)
        PlantUMLExtractor pumlXtract = new PlantUMLExtractor(types, related)
        String puml = pumlXtract.generateClassDiagram()
        types += related
        ConcurrentMap<String, Set<String>> pkgFiles = extractPackageFileMap(types)

        Tuple.of(puml, types, pkgFiles)
    }

    private Map<String, Set<String>> extractPackageFileMap(Set<Type> types) {
        Map<String, Set<String>> pkgFiles = Maps.newConcurrentMap()
        types.each {
            if (it && it.getParentNamespace() && it.getParentFile()) {
                String pkgName = it.getParentNamespace().getFullName()
                String file = it.getParentFile().getFullPath()
                if (pkgFiles[pkgName]) {
                    pkgFiles[pkgName] << file
                } else {
                    Set<String> files = Sets.newHashSet(file)
                    pkgFiles[pkgName] = files
                }
            }
        }
        pkgFiles
    }

    private Set<Type> findRelatedTypes(Set<Type> types) {
        Set<Type> related = Sets.newHashSet()
        types.each {
            related += it.getGeneralizedBy()
            related += it.getGeneralizes()
            related += it.getAssociatedFrom()
            related += it.getAssociatedTo()
            related += it.getComposedFrom()
            related += it.getComposedTo()
            related += it.getAggregatedFrom()
            related += it.getAggregatedTo()
            related += it.getUseFrom()
            related += it.getUseTo()
            related += it.getDependencyFrom()
            related += it.getDependencyTo()
            related += it.getRealizedBy()
            related += it.getRealizes()
        }
        related
    }

    private String createGradleBuild() {
        """
        plugins {
            id 'java'
        }

        repositories {
            mavenCentral()
        }

        dependencies {
            // Use JUnit Jupiter API for testing.
            testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.2'

            // Use JUnit Jupiter Engine for testing.
            testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'

            // This dependency is used by the application.
            implementation 'com.google.guava:guava:29.0-jre'
        }

        tasks.named('test') {
            // Use junit platform for unit tests.
            useJUnitPlatform()
        }
        """.stripIndent()
    }

    private String createGradleSettings(String name) {
        """\
        rootProject.name = '$name'
        """.stripIndent()
    }

    private void generateInjectionControlData(String unitName, PatternInstance base, PatternInstance inf, File baseDir) {
        File file = new File(new File(new File(baseDir, "units"), unitName), "injector.control")
        InjectorControlGenerator generator = new InjectorControlGenerator()
        generator.generate(base, inf, file)
    }
}
