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
package dev.siliconcode.arc.patterns.gen.generators.java

import dev.siliconcode.arc.patterns.gen.GeneratorContext
import dev.siliconcode.arc.patterns.gen.generators.*
import dev.siliconcode.arc.patterns.gen.plugin.LanguageDescriptor
import groovy.util.logging.Log
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaLanguageProvider extends LanguageProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    LanguageDescriptor languageDescriptor()
    {
        new LanguageDescriptor(name: "Java", fileExt: ".java", description: "Java Language Provider", cliArgs: ["java", "Java"])
    }

    @Override
    DirectoryGenerator createDirectoryGenerator(GeneratorContext ctx) {
        return new JavaDirectoryGenerator(ctx: ctx)
    }

    @Override
    BuildFileGenerator createBuildFileGenerator(GeneratorContext ctx) {
        return JavaBuildFileGenerator.getInstance("gradle", ctx)
    }

    @Override
    GitIgnoreGenerator createGitIgnoreGenerator(GeneratorContext ctx) {
        return new JavaGitIgnoreGenerator(ctx: ctx)
    }

    @Override
    CodeGenerator createCodeGenerator(GeneratorContext ctx) {
        return new JavaCodeGenerator(ctx: ctx)
    }

    @Override
    TypeGenerator createTypeGenerator(GeneratorContext ctx) {
        return new JavaTypeGenerator(ctx: ctx)
    }

    @Override
    FieldGenerator createFieldGenerator(GeneratorContext ctx) {
        return new JavaFieldGenerator(ctx: ctx)
    }

    @Override
    LiteralGenerator createLiteralGenerator(GeneratorContext ctx) {
        return new JavaLiteralGenerator(ctx: ctx)
    }

    @Override
    MethodGenerator createMethodGenerator(GeneratorContext ctx) {
        return new JavaMethodGenerator(ctx: ctx)
    }

    @Override
    FileGenerator createFileGenerator(GeneratorContext ctx) {
        return new JavaFileGenerator(ctx: ctx)
    }

    @Override
    ModuleGenerator createModuleGenerator(GeneratorContext ctx) {
        return new JavaModuleGenerator(ctx: ctx)
    }

    @Override
    ProjectGenerator createProjectGenerator(GeneratorContext ctx) {
        return new JavaProjectGenerator(ctx: ctx)
    }

    @Override
    NamespaceGenerator createNamespaceGenerator(GeneratorContext ctx) {
        return new JavaNamespaceGenerator(ctx: ctx)
    }

    @Override
    SystemGenerator createSystemGenerator(GeneratorContext ctx) {
        return new JavaSystemGenerator(ctx: ctx)
    }
}
