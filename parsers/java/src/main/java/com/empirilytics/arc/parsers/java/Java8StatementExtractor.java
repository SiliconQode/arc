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

import dev.siliconcode.arc.datamodel.Initializer;
import dev.siliconcode.arc.datamodel.Method;
import dev.siliconcode.arc.datamodel.cfg.CFGBuilder;
import dev.siliconcode.arc.datamodel.cfg.JumpTo;
import dev.siliconcode.arc.datamodel.cfg.StatementType;
import dev.siliconcode.arc.datamodel.util.DBCredentials;
import dev.siliconcode.arc.parsers.java.java2.JavaParser;
import dev.siliconcode.arc.parsers.*;

import java.util.Stack;

public class Java8StatementExtractor extends Java8AbstractExtractor {

    private Stack<CFGBuilder> builderStack = new Stack<>();
    CFGBuilder builder;
    DBCredentials credentials;

    public Java8StatementExtractor(BaseModelBuilder builder) {
        super(builder);
        credentials = builder.getCredentials();
    }

    private void startMethod() {
        CFGBuilder newBuilder = new CFGBuilder(credentials);
        builderStack.push(newBuilder);
        builder = newBuilder;
        builder.startMethod();
    }

    private void endMethod() {
        if (!treeBuilder.getMethods().isEmpty()) {
            if (treeBuilder.getMethods().peek() instanceof Method)
                builder.endMethod((Method) treeBuilder.getMethods().peek());
            else if (treeBuilder.getMethods().peek() instanceof Initializer) {
                builder.endMethod((Initializer) treeBuilder.getMethods().peek());
            }
        }

        builderStack.pop();
        if (builderStack.empty())
            builder = null;
        else
            builder = builderStack.peek();
    }

    ///////////////
    // Types
    ///////////////
    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        if (ctx.block() != null) {
            startMethod();
            handleInitializer(ctx);
        }

        super.enterClassBodyDeclaration(ctx);
    }

    @Override
    public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        if (ctx.block() != null) {
            endMethod();
            treeBuilder.finishMethod();
        }

        super.exitClassBodyDeclaration(ctx);
    }

    ///////////////
    // Methods
    ///////////////
    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        startMethod();
        super.enterMethodDeclaration(ctx);
    }

    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        endMethod();
        super.exitMethodDeclaration(ctx);
    }

    @Override
    public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
        startMethod();
        super.enterInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
        endMethod();
        super.exitInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        startMethod();
        super.enterConstructorDeclaration(ctx);
    }

    @Override
    public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        endMethod();
        super.exitConstructorDeclaration(ctx);
    }

    /////////////////
    // Statements
    /////////////////
    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        if (ctx.ASSERT() != null) {
            builder.createStatement(StatementType.ASSERT);
        } else if (ctx.IF() != null) {
            builder.startDecision(StatementType.IF);
        } else if (ctx.FOR() != null) {
            builder.startLoop(StatementType.FOR);
        } else if (ctx.DO() != null) {
            builder.startLoop(StatementType.DO, true);
        } else if (ctx.WHILE() != null) {
            builder.startLoop(StatementType.WHILE);
        } else if (ctx.TRY() != null) {
            builder.startBlock(StatementType.TRY);
        } else if (ctx.SWITCH() != null) {
            builder.startDecision(StatementType.SWITCH);
        } else if (ctx.SYNCHRONIZED() != null) {
            builder.startBlock(StatementType.SYNCHRONIZED);
        } else if (ctx.RETURN() != null) {
            builder.createReturnStatement();
        } else if (ctx.BREAK() != null) {
            String identifier = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : null;
            if (identifier != null)
                builder.createStatement(StatementType.BREAK, identifier, true, JumpTo.LABEL);
            else
                builder.createStatement(StatementType.BREAK, null, true, JumpTo.LOOP_END);
        } else if (ctx.CONTINUE() != null) {
            String identifier = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : null;
            builder.createStatement(StatementType.CONTINUE, identifier, true, JumpTo.LOOP_START);
        } else if (ctx.THROW() != null) {
            builder.createStatement(StatementType.THROW, null, true, JumpTo.METHOD_END);
        } else if (ctx.SEMI() != null) {
            builder.createStatement(StatementType.EMPTY);
        } else if (ctx.identifierLabel != null) {
            builder.createStatement(null, ctx.IDENTIFIER().getText());
        } else if (ctx.expression() != null) {
            builder.createStatement(StatementType.EXPRESSION);
        }

        super.enterStatement(ctx);
    }

    @Override
    public void exitStatement(JavaParser.StatementContext ctx) {
        if (ctx.IF() != null) {
            builder.endDecision();
        } else if (ctx.FOR() != null) {
            builder.endLoop();
        } else if (ctx.DO() != null) {
            builder.endLoop(true);
        } else if (ctx.WHILE() != null) {
            builder.endLoop();
        } else if (ctx.TRY() != null) {
            builder.endBlock();
        } else if (ctx.SWITCH() != null) {
            boolean def = true;
            for (final JavaParser.SwitchBlockStatementGroupContext x : ctx.switchBlockStatementGroup()) {
                if (x.switchLabel(0).DEFAULT() == null) {
                    def = false;
                    break;
                }
            }
            builder.endDecision(def);
        } else if (ctx.SYNCHRONIZED() != null) {
            builder.endBlock();
        }

        super.exitStatement(ctx);
    }

    @Override
    public void enterCatchClause(JavaParser.CatchClauseContext ctx) {
        builder.startBlock(StatementType.CATCH);

        super.enterCatchClause(ctx);
    }

    @Override
    public void exitCatchClause(JavaParser.CatchClauseContext ctx) {
        builder.endBlock();

        super.exitCatchClause(ctx);
    }

    @Override
    public void enterFinallyBlock(JavaParser.FinallyBlockContext ctx) {
        builder.startBlock(StatementType.FINALLY);

        super.enterFinallyBlock(ctx);
    }

    @Override
    public void exitFinallyBlock(JavaParser.FinallyBlockContext ctx) {
        builder.endBlock();

        super.exitFinallyBlock(ctx);
    }

    @Override
    public void enterSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
        builder.exitSwitchBlock();

        super.enterSwitchBlockStatementGroup(ctx);
    }

    public void enterLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
        builder.createStatement(StatementType.VARDECL);

        super.enterLocalVariableDeclaration(ctx);
    }
}
