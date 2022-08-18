/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.patterns.gen.cue.Cue
import dev.siliconcode.arc.patterns.gen.generators.*
import dev.siliconcode.arc.patterns.gen.generators.pb.*
import dev.siliconcode.arc.patterns.gen.generators.pb.tree.RampedHalfHalf
import dev.siliconcode.arc.patterns.gen.generators.pb.tree.TreeGenerator

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class GeneratorContext {

    // Configuration Data
    File base
    String output
    LanguageProvider plugin
    PatternManager loader
    List<String> patterns
    int numInstances = 1
    int maxBreadth = 1
    int maxDepth = 1
    int maxMethods = 5
    int maxFields = 5
    String version
    Map<String, String> license
    Map<String, String> db
    Map<String, String> build
    List<Integer> arities = [1]
    String srcPath
    String testPath
    String binPath
    String srcExt
    Map<String, RBML2DataModelManager> projRbmlMap = [:]
    Map<String, Cue> projCueMap = [:]
    Table<String, String, String> results = HashBasedTable.create()
    String javaBinary

    int num = 0

    // director flags
    boolean generateOnly = false
    boolean dataOnly = false
    boolean resetDb = false
    boolean resetOnly = false

    // Data Model Builders
    RBML2DataModelManager rbmlManager
    ClassifierBuilder     clsBuilder
    FieldBuilder          fldBuilder
    FileBuilder           fileBuilder
    GenHierBuilder        ghBuilder
    MethodBuilder         methBuilder
    ModuleBuilder         modBuilder
    NamespaceBuilder      nsBuilder
    PatternBuilder        patternBuilder
    ProjectBuilder        projBuilder
    RelationshipBuilder   relationBuilder
    SystemBuilder         sysBuilder
    ParamBuilder          paramBuilder

    // Component Generators
    LicenseGenerator      licGen
    ReadmeGenerator       readmeGen
    GitIgnoreGenerator    ignoreGen
    NamespaceGenerator    nsGen
    BuildFileGenerator    buildGen
    CodeGenerator         codeGen
    DirectoryGenerator    dirGen
    FileGenerator         fileGen
    FieldGenerator        fieldGen
    LiteralGenerator      literalGen
    MethodGenerator       methodGen
    ModuleGenerator       modGen
    ProjectGenerator      projGen
    SystemGenerator       sysGen
    TypeGenerator         typeGen

    // Tree Generator
    TreeGenerator treeGenerator

    void resetPatternBuilderComponents() {
        treeGenerator   = new RampedHalfHalf()
        loader          = PatternManager.instance

        rbmlManager     = new RBML2DataModelManager()
        sysBuilder      = new SystemBuilder(ctx: this)
        projBuilder     = new ProjectBuilder(ctx: this)
        patternBuilder  = new PatternBuilder(ctx: this)
        nsBuilder       = new NamespaceBuilder(ctx: this)
        modBuilder      = new ModuleBuilder(ctx: this)
        methBuilder     = new MethodBuilder(ctx: this)
        ghBuilder       = new GenHierBuilder(ctx: this)
        fileBuilder     = new FileBuilder(ctx: this)
        fldBuilder      = new FieldBuilder(ctx: this)
        clsBuilder      = new ClassifierBuilder(ctx: this)
        relationBuilder = new RelationshipBuilder(ctx: this)
        paramBuilder    = new ParamBuilder(ctx: this)
    }

    void resetComponentGenerators() {
        licGen     = plugin.createLicenseGenerator(this)
        readmeGen  = plugin.createReadmeGenerator(this)
        ignoreGen  = plugin.createGitIgnoreGenerator(this)
        nsGen      = plugin.createNamespaceGenerator(this)
        buildGen   = plugin.createBuildFileGenerator(this)
        codeGen    = plugin.createCodeGenerator(this)
        dirGen     = plugin.createDirectoryGenerator(this)
        fileGen    = plugin.createFileGenerator(this)
        fieldGen   = plugin.createFieldGenerator(this)
        literalGen = plugin.createLiteralGenerator(this)
        methodGen  = plugin.createMethodGenerator(this)
        modGen     = plugin.createModuleGenerator(this)
        projGen    = plugin.createProjectGenerator(this)
        sysGen     = plugin.createSystemGenerator(this)
        typeGen    = plugin.createTypeGenerator(this)
    }

    @Override
    String toString() {
        """\
        base            => ${base.toString()}
        output          => ${output}
        patterns        => ${patterns}
        numInstances    => ${numInstances}
        maxBreadth      => ${maxBreadth}
        maxDepth        => ${maxDepth}
        version         => ${version}
        arities         => ${arities}
        srcPath         => ${srcPath}
        binPath         => ${binPath}
        testPath        => ${testPath}
        srcExt          => ${srcExt}
        license:
            name        => ${license.name}
            year        => ${license.year}
            holder      => ${license.holder}
            project     => ${license.project}
            url         => ${license.url}
        build:
            project     => ${build.project}
            artifact    => ${build.artifact}
            description => ${build.description}
        db:
            driver      => ${db.driver}
            url         => ${db.url}
            user        => ${db.user}
            pass        => ${db.pass}
            type        => ${db.type}
        """
    }
}
