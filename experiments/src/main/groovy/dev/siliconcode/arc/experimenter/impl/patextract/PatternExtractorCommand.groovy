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
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.experimenter.command.SecondaryAnalysisCommand
import groovy.util.logging.Log4j2
import groovy.xml.XmlSlurper

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentMap

@Log4j2
class PatternExtractorCommand extends SecondaryAnalysisCommand {


    PatternExtractorCommand() {
        super(PatternExtractorConstants.CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        String resultsFile = context.getArcProperty(PatternExtractorConstants.RESULTS_FILE)
        String base = context.getArcProperty(PatternExtractorConstants.BASE_DIR)
        File baseDir

        log.info("arc.base.dir: " + context.getArcProperty("arc.base.dir"))
        if (context.getArcProperty("arc.base.dir").endsWith(".") || context.getArcProperty("arc.base.dir").endsWith("." + File.separator))
            baseDir = new File(base)
        else
            baseDir = new File(Paths.get(context.getArcProperty("arc.base.dir")).toAbsolutePath().toFile(), base)

        def data = new XmlSlurper().parseText(new File(resultsFile).text)

        context.open()
        // for each pattern instance's roles we need to modify the key to be consistent with the current keying scheme
        // GParsExecutorsPool.withPool(8) {
        int totalInstCount = 0
        List<String> instances = []
        data.pattern.each { pattern ->
            String patternName = pattern.@name
            int instNum = 1

            pattern.instance.each { instance ->
                Set<String> typeNames = Sets.newHashSet()

                instance.role.each { role ->
                    String element = role.@element
                    typeNames << extractTypeName(element)
                }

                Set<Type> types = Sets.newHashSet()
                typeNames.each {
                    Type t = getType(it, context.getProject())
                    if (t != null)
                        types.add(t)
                }
                Set<Type> related = findRelatedTypes(types)

                PlantUMLExtractor pumlXtract = new PlantUMLExtractor(types, related)
                String puml = pumlXtract.generateClassDiagram()
                types += related
                ConcurrentMap<String, Set<String>> pkgFiles = extractPackageFileMap(types)

                instances << "$patternName-$instNum"

                createDirectoryStructure(baseDir, patternName, instNum, puml)
                copyFiles(baseDir, pkgFiles, patternName, instNum)

                instNum++
                totalInstCount++
            }
        }

        writeAnalysisConfig(baseDir, instances, context.getProject(), totalInstCount)
        context.close()
    }

    private writeAnalysisConfig(File baseDir, data, Project proj, totalInstCount) {
        File analysisConfig = new File(baseDir, "analysis.conf")
        Files.deleteIfExists(analysisConfig.toPath())
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(analysisConfig.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))
        {
            for (int id = 1; id <= totalInstCount; id++) {
                String projKey = proj.getProjectKey()
                String projName = proj.getName()
                String projVersion = proj.getVersion()
                String patternName = data[id - 1]
                String patternLoc = new File(baseDir,  patternName).toPath().toAbsolutePath().toString()
                pw.printf("%d,\"%s_%s\",\"%s\"\n", id, projKey, patternName, patternLoc)
            }
        } catch (IOException ex) {

        }
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

    private void createDirectoryStructure(File baseDir, String patternName, int instNum, String puml) {
        def tree = new FileTreeBuilder(baseDir)
        tree."$patternName" {
            "inst-${instNum}" {
                src {
                    main {
                        java {
                        }
                    }
                }
                docs {
                    "classdiagram.puml"(puml)
                }
            }
        }
    }

    private void copyFiles(File baseDir, ConcurrentMap<String, Set<String>> pkgFiles, String patternName, int instNum) {
        pkgFiles.each { pkg, Set<String> files ->
            String pkgPath = convertPkgToPath(pkg)
            files.each { file ->
                Path srcPath = Paths.get(file)
                String fileName = srcPath.getFileName().toString()
                Path targetPath = Paths.get(baseDir.toPath().toAbsolutePath().toString(), patternName, "inst-$instNum", "src", "main", "java", pkgPath, fileName)
                log.info("Base Dir: " + baseDir.toPath().toAbsolutePath().toString())
                log.info("Pattern Name: " + patternName)
                log.info("Inst: " + "inst-$instNum")
                log.info("PkgPath: " + pkgPath)
                log.info("FileName: " + fileName)
                log.info("Source Path: " + srcPath)
                log.info("Target Path: " + targetPath.toString())
                Files.createDirectories(Paths.get(baseDir.toPath().toAbsolutePath().toString(), patternName, "inst-$instNum", "src", "main", "java", pkgPath))
                Files.copy(srcPath, targetPath)
            }
        }
    }

    private String convertPkgToPath(String pkg) {
        return pkg.replace('.', File.separator)
    }

    String extractTypeName(String element) {
        if (!element || element.isBlank())
            return ""
        else if (element.contains("::")) {
            return element.split("::")[0]
        } else {
            return element
        }
    }

    Type getType(String typeName, Project proj) {
        return proj.findTypeByQualifiedName(typeName)
    }
}
