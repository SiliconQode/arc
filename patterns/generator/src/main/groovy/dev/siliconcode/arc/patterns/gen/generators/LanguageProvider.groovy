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
package dev.siliconcode.arc.patterns.gen.generators

import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.gen.plugin.LanguageDescriptor

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class LanguageProvider {

    abstract DirectoryGenerator createDirectoryGenerator(GeneratorContext ctx)

    abstract LanguageDescriptor languageDescriptor()

    LicenseGenerator createLicenseGenerator(GeneratorContext ctx) {
        return new LicenseGenerator(ctx: ctx)
    }

    ReadmeGenerator createReadmeGenerator(GeneratorContext ctx) {
        return new ReadmeGenerator(ctx: ctx)
    }

    abstract BuildFileGenerator createBuildFileGenerator(GeneratorContext ctx)

    abstract GitIgnoreGenerator createGitIgnoreGenerator(GeneratorContext ctx)

    abstract CodeGenerator createCodeGenerator(GeneratorContext ctx)

    abstract TypeGenerator createTypeGenerator(GeneratorContext ctx)

    abstract FieldGenerator createFieldGenerator(GeneratorContext ctx)

    abstract LiteralGenerator createLiteralGenerator(GeneratorContext ctx)

    abstract MethodGenerator createMethodGenerator(GeneratorContext ctx)

    abstract FileGenerator createFileGenerator(GeneratorContext ctx)

    abstract ModuleGenerator createModuleGenerator(GeneratorContext ctx)

    abstract ProjectGenerator createProjectGenerator(GeneratorContext ctx)

    abstract NamespaceGenerator createNamespaceGenerator(GeneratorContext ctx)

    abstract SystemGenerator createSystemGenerator(GeneratorContext ctx)
}
