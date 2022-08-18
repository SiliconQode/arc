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

import dev.siliconcode.arc.parsers.java.java2.JavaParser;
import dev.siliconcode.arc.parsers.*;

public class Java8MemberUseExtractor extends Java8AbstractExtractor {

    boolean inMethod = false;
    boolean inField = false;
    boolean inConstant = false;
    boolean inConstructor = false;
    boolean inType = false;
    boolean inLocalVar = false;
    boolean inClass = false;
    boolean inEnum = false;
    boolean inInterface = false;
    boolean inTypeList = false;
    boolean inMethodBody = false;

    public Java8MemberUseExtractor(BaseModelBuilder builder) {
        super(builder);
    }

    ////////////////////
    //
    // Generalization and Realization
    //
    ////////////////////
    @Override
    public void enterAnnotationTypeDeclaration(final JavaParser.AnnotationTypeDeclarationContext ctx) {
        inType = true;
        inInterface = true;
        super.enterAnnotationTypeDeclaration(ctx);
    }

    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        if (ctx.block() != null) {
            inMethod = true;
            handleInitializer(ctx);
            inMethod = false;
        }

        super.enterClassBodyDeclaration(ctx);
    }

    @Override
    public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        if (ctx.block() != null) {
            treeBuilder.finishMethod();
        }

        super.exitClassBodyDeclaration(ctx);
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        inType = true;
        inClass = true;
        super.enterClassDeclaration(ctx);
    }

    @Override
    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        inType = true;
        inInterface = true;
        super.enterInterfaceDeclaration(ctx);
    }

    @Override
    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
        inType = true;
        inEnum = true;
        super.enterEnumDeclaration(ctx);
    }

    @Override
    public void enterClassBody(JavaParser.ClassBodyContext ctx) {
        inType = false;
        inClass = false;

        super.enterClassBody(ctx);
    }

    @Override
    public void enterEnumConstants(JavaParser.EnumConstantsContext ctx) {
        inType = false;
        inEnum = false;

        super.enterEnumConstants(ctx);
    }

    @Override
    public void enterEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx) {
        inType = false;
        inEnum = false;

        super.enterEnumBodyDeclarations(ctx);
    }

    @Override
    public void enterInterfaceBody(JavaParser.InterfaceBodyContext ctx) {
        inType = false;
        inInterface = false;

        super.enterInterfaceBody(ctx);
    }

    @Override
    public void enterTypeList(JavaParser.TypeListContext ctx) {
        inTypeList = true;

        super.enterTypeList(ctx);
    }

    @Override
    public void exitTypeList(JavaParser.TypeListContext ctx) {
        inTypeList = false;

        super.exitTypeList(ctx);
    }

    ////////////////////
    //
    // Use Dependency - Method Return Type, Parameter Types, and Local Variable Types
    //
    ////////////////////
    @Override
    public void enterMethodBody(JavaParser.MethodBodyContext ctx) {
        inMethodBody = true;

        super.enterMethodBody(ctx);
    }

    @Override
    public void exitMethodBody(JavaParser.MethodBodyContext ctx) {
        inMethodBody = false;

        super.exitMethodBody(ctx);
    }

    @Override
    public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        inMethodBody = true;

        super.enterConstructorDeclaration(ctx);
    }

    @Override
    public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        inMethodBody = false;

        super.exitConstructorDeclaration(ctx);
    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {


        super.enterBlock(ctx);
    }

    @Override
    public void enterConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
        inConstant = true;

        super.enterConstDeclaration(ctx);
    }

    @Override
    public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
        inConstant = false;

        super.exitConstDeclaration(ctx);
    }

    @Override
    public void enterFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        inField = true;

        super.enterFieldDeclaration(ctx);
    }

    @Override
    public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        inField = false;

        super.exitFieldDeclaration(ctx);
    }

    @Override
    public void enterLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
        inLocalVar = true;

        super.enterLocalVariableDeclaration(ctx);
    }

    @Override
    public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
        inLocalVar = false;

        super.exitLocalVariableDeclaration(ctx);
    }

    @Override
    public void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        if (inLocalVar && ctx.IDENTIFIER() != null) {
            treeBuilder.addLocalVarToScope(ctx.IDENTIFIER().getText());
        }

        super.enterVariableDeclaratorId(ctx);
    }


    ///////////////////
    //
    // Method Calls and Field Uses
    //
    //////////////////
    @Override
    public void enterExpression(JavaParser.ExpressionContext ctx) {
        if (inMethodBody && ctx.lambdaExpression() == null && !inField && ctx.getParent() instanceof JavaParser.StatementContext) {
            treeBuilder.processExpression(ctx.getText());
        }

        super.enterExpression(ctx);
    }
}
