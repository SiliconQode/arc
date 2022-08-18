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

import dev.siliconcode.arc.datamodel.Type;
import dev.siliconcode.arc.parsers.java.java2.JavaParser;
import dev.siliconcode.arc.parsers.java.java2.JavaParserBaseListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;
import dev.siliconcode.arc.parsers.*;

import java.util.Stack;

public abstract class Java8AbstractExtractor extends JavaParserBaseListener {

    protected Stack<Integer> initializerCount = new Stack<>();
    protected BaseModelBuilder treeBuilder;

    public Java8AbstractExtractor(BaseModelBuilder builder) {
        this.treeBuilder = builder;
    }

    @NotNull
    protected String createSignature(TerminalNode identifier, JavaParser.FormalParametersContext formalParametersContext) {
        StringBuilder builder = new StringBuilder();
        builder.append(identifier.getText());
        builder.append("(");
        if (formalParametersContext != null) {
            if (formalParametersContext.formalParameterList() != null) {
                for (JavaParser.FormalParameterContext fpc : formalParametersContext.formalParameterList().formalParameter()) {
                    builder.append(fpc.typeType().getText());
                    builder.append(", ");
                }

                if (formalParametersContext.formalParameterList().lastFormalParameter() != null) {
                    JavaParser.LastFormalParameterContext lfpc = formalParametersContext.formalParameterList().lastFormalParameter();
                    builder.append(lfpc.typeType().getText());
                }
            }
        }
        builder.append(")");
        String signature = builder.toString();
        if (signature.endsWith(", )"))
            signature = signature.replace(", )", ")");
        return signature;
    }

    @Override
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
        initializerCount.push(0);

        super.enterTypeDeclaration(ctx);
    }

    @Override
    public void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
        treeBuilder.endType();
        initializerCount.pop();

        super.exitTypeDeclaration(ctx);
    }

    @Override
    public void enterLocalTypeDeclaration(JavaParser.LocalTypeDeclarationContext ctx) {
        initializerCount.push(0);

        super.enterLocalTypeDeclaration(ctx);
    }

    @Override
    public void exitLocalTypeDeclaration(JavaParser.LocalTypeDeclarationContext ctx) {
        treeBuilder.endType();
        initializerCount.pop();

        super.exitLocalTypeDeclaration(ctx);
    }

    protected void handleInitializer(JavaParser.ClassBodyDeclarationContext ctx) {
        initializerCount.push(initializerCount.pop() + 1);
        treeBuilder.findInitializer(String.format("<init-%d>", initializerCount.peek()), ctx.STATIC() != null);
    }

    @Override
    public void enterClassDeclaration(final JavaParser.ClassDeclarationContext ctx) {
        treeBuilder.findOrCreateType(ctx.IDENTIFIER().getText(), Type.CLASS, ctx.getStart().getLine(), ctx.getStop().getLine());

        super.enterClassDeclaration(ctx);
    }

    @Override
    public void enterInterfaceDeclaration(final JavaParser.InterfaceDeclarationContext ctx) {
        treeBuilder.findOrCreateType(ctx.IDENTIFIER().getText(), Type.INTERFACE, ctx.getStart().getLine(), ctx.getStop().getLine());

        super.enterInterfaceDeclaration(ctx);
    }

    @Override
    public void enterEnumDeclaration(final JavaParser.EnumDeclarationContext ctx) {
        treeBuilder.findOrCreateType(ctx.IDENTIFIER().getText(), Type.ENUM, ctx.getStart().getLine(), ctx.getStop().getLine());

        super.enterEnumDeclaration(ctx);
    }

    @Override
    public void enterAnnotationTypeDeclaration(final JavaParser.AnnotationTypeDeclarationContext ctx) {
        treeBuilder.findOrCreateType(ctx.IDENTIFIER().getText(), Type.ANNOTATION, ctx.getStart().getLine(), ctx.getStop().getLine());

        super.enterAnnotationTypeDeclaration(ctx);
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        String signature = createSignature(ctx.IDENTIFIER(), ctx.formalParameters());

        treeBuilder.findMethod(signature);

        super.enterMethodDeclaration(ctx);
    }

    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        treeBuilder.finishMethod();

        super.exitMethodDeclaration(ctx);
    }

    @Override
    public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
        String signature = createSignature(ctx.IDENTIFIER(), ctx.formalParameters());

        treeBuilder.findMethod(signature);

        super.enterInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
        treeBuilder.finishMethod();

        super.exitInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        String signature = createSignature(ctx.IDENTIFIER(), ctx.formalParameters());

        treeBuilder.findMethod(signature);

        super.enterConstructorDeclaration(ctx);
    }

    @Override
    public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        treeBuilder.finishMethod();

        super.exitConstructorDeclaration(ctx);
    }
}
