/**
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
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
package dev.siliconcode.arc.parsers.java;

import com.google.common.collect.Lists;
import dev.siliconcode.arc.datamodel.Type;
import dev.siliconcode.arc.parsers.java.java2.JavaParser;
import dev.siliconcode.arc.parsers.java.java2.JavaParserBaseListener;
import dev.siliconcode.arc.parsers.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class Java8FileAndTypeExtractor extends JavaParserBaseListener {

    BaseModelBuilder treeBuilder;
    List<String> modifiers = Lists.newArrayList();
    boolean inPackage;
    boolean inImport;
    boolean onDemand;
    boolean inTypeDecl;
    boolean inTypeParams;

    public Java8FileAndTypeExtractor(BaseModelBuilder builder) {
        treeBuilder = builder;
        inPackage = false;
        inImport = false;
        onDemand = false;
        inTypeDecl = false;
        inTypeParams = false;
    }

    ////////////////////////////
    //
    // Handle package declarations
    //
    ////////////////////////////
    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Package");
        inPackage = true;

        super.enterPackageDeclaration(ctx);
    }

    @Override
    public void exitPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Leaving Package");
        inPackage = false;

        super.exitPackageDeclaration(ctx);
    }

    ////////////////////////////
    //
    // Handle import declarations
    //
    ////////////////////////////
    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Import");
        inImport = true;

        if (ctx.MUL() != null)
            onDemand = true;

        super.enterImportDeclaration(ctx);
    }

    @Override
    public void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Exiting Import");
        inImport = false;
        onDemand = false;

        super.exitImportDeclaration(ctx);
    }

    @Override
    public void enterQualifiedName(JavaParser.QualifiedNameContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Qualified Name");
        if (inPackage) {
            treeBuilder.createNamespace(ctx.getText());
        } else if (inImport) {
            String name = ctx.getText();
            if (onDemand)
                name += ".*";
            treeBuilder.createImport(name, ctx.getStart().getLine(), ctx.getStop().getLine());
        }

        super.enterQualifiedName(ctx);
    }

    ////////////////////////////
    //
    // Handle type declarations
    //
    ////////////////////////////
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Type Declaration");
        modifiers.clear();
        inTypeDecl = true;

        super.enterTypeDeclaration(ctx);
    }

    public void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Exiting Type Declaration");
        treeBuilder.endType();

        super.exitTypeDeclaration(ctx);
    }

    public void enterLocalTypeDeclaration(JavaParser.LocalTypeDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Local Type Declaration");
        modifiers.clear();
        inTypeDecl = true;

        super.enterLocalTypeDeclaration(ctx);
    }

    public void exitLocalTypeDeclaration(JavaParser.LocalTypeDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Exiting Local Type Declaration");
        treeBuilder.endType();

        super.exitLocalTypeDeclaration(ctx);
    }

    ////////////////////////////
    //
    // Specific types
    //
    ////////////////////////////
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Class Declaration");
        treeBuilder.createType(ctx.IDENTIFIER().getText(), Type.CLASS, ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setTypeModifiers(modifiers);

        super.enterClassDeclaration(ctx);
    }

    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Enum Declaration");
        treeBuilder.createType(ctx.IDENTIFIER().getText(), Type.ENUM, ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setTypeModifiers(modifiers);

        super.enterEnumDeclaration(ctx);
    }

    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Interface Declaration");
        treeBuilder.createType(ctx.IDENTIFIER().getText(), Type.INTERFACE, ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setTypeModifiers(modifiers);

        super.enterInterfaceDeclaration(ctx);
    }

    public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Annotation Type Declaration");
        treeBuilder.createType(ctx.IDENTIFIER().getText(), Type.ANNOTATION, ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setTypeModifiers(modifiers);

        super.enterAnnotationTypeDeclaration(ctx);
    }

    ////////////////////////////
    //
    // Modifiers
    //
    ////////////////////////////
    public void enterClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Class or Interface Modifier");
        if (ctx.annotation() == null)
            modifiers.add(ctx.getText());

        super.enterClassOrInterfaceModifier(ctx);
    }

    ////////////////////////////
    //
    // Type Parameters
    //
    ////////////////////////////
    public void enterTypeParameters(JavaParser.TypeParametersContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Type Parameters");
        inTypeParams = true;

        super.enterTypeParameters(ctx);
    }

    public void exitTypeParameters(JavaParser.TypeParametersContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Exiting Type Parameters");
        inTypeParams = false;

        super.exitTypeParameters(ctx);
    }

    public void enterTypeParameter(JavaParser.TypeParameterContext ctx) {
        treeBuilder.createTypeTypeParameter(ctx.IDENTIFIER().getText());

        super.enterTypeParameter(ctx);
    }

    ////////////////////////////
    //
    // Handle types
    //
    ////////////////////////////
    public void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Class or Interface Type");
        if (inTypeParams && inTypeDecl) {
            //treeBuilder.addTypeParamBoundType(ctx.getText());
        }

        super.enterClassOrInterfaceType(ctx);
    }

    ////////////////////////////
    //
    // Bodies
    //
    ////////////////////////////
    public void enterClassBody(JavaParser.ClassBodyContext ctx) {
//        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Class Body");
        inTypeDecl = false;

        super.enterClassBody(ctx);
    }

    public void enterInterfaceBody(JavaParser.InterfaceBodyContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Interface Body");
        inTypeDecl = false;

        super.enterInterfaceBody(ctx);
    }

    public void enterEnumConstants(JavaParser.EnumConstantsContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Enum Constants");
        inTypeDecl = false;

        super.enterEnumConstants(ctx);
    }

    public void enterEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Enum Body Declarations");
        inTypeDecl = false;

        super.enterEnumBodyDeclarations(ctx);
    }

    public void enterAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx) {
        log.atInfo().log(treeBuilder.getFile().getName() + " Entering Annotation Type Body");
        inTypeDecl = false;

        super.enterAnnotationTypeBody(ctx);
    }
}
