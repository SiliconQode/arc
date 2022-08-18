/**
 * The MIT License (MIT)
 *
 * MSUSEL C# Parser
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package dev.siliconcode.arc.datamodel.parsers;

import java.util.List;
import java.util.Stack;

import dev.siliconcode.arc.datamodel.node.FileNode;
import dev.siliconcode.arc.datamodel.node.TypeNode;
import dev.siliconcode.arc.datamodel.node.StatementNode;
import dev.siliconcode.arc.datamodel.parsers.csharp.CSharp6Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.siliconcode.arc.datamodel.INode;
import dev.siliconcode.arc.datamodel.metrics.loc.LoCCounter;
import dev.siliconcode.arc.datamodel.node.FieldNode;
import dev.siliconcode.arc.datamodel.node.MethodNode;
import dev.siliconcode.arc.datamodel.node.StatementType;
import dev.siliconcode.arc.datamodel.parsers.csharp.CSharp6BaseListener;

/**
 * Using the parser, this class incrementally builds a CodeTree one file at a
 * time.
 *
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class CSharpCodeTreeBuilder extends CSharp6BaseListener {

    /**
     * Logger to log the process of the builder
     */
    private static final Logger               LOG = LoggerFactory.getLogger(CSharpCodeTreeBuilder.class);
    /**
     * Stack used for creating types
     */
    transient private final Stack<TypeNode>   types;
    /**
     * Stack used for creating methods
     */
    transient private final Stack<MethodNode> methods;
    /**
     * Stack used to buildup namespaces
     */
    transient private final Stack<String>     namespaces;
    /**
     * FileNode being built
     */
    private final FileNode file;
    /**
     * Line of Code Counter
     */
    private final LoCCounter                  locCounter;

    /**
     * Construct a new JavaCodeTreeBuilder for the provided FileNode
     *
     * @param file
     */
    public CSharpCodeTreeBuilder(final FileNode file)
    {
        types = new Stack<>();
        methods = new Stack<>();
        namespaces = new Stack<>();
        this.file = file;
        locCounter = new LoCCounter("//", "/*", "*/", "\r\n");
    }

    /**
     * Adds the LOC name measurement to the given INode based on the text from
     * the given ParserRuleContext
     *
     * @param ctx
     *            ParserRuleContext
     * @param ent
     *            INode
     */
    private void addLoCMetric(final @NonNull ParserRuleContext ctx, final @NonNull INode ent)
    {
        locCounter.reset();
        locCounter.count(ctx.getText());
        ent.addMetric("LOC", new Double(locCounter.getSloc()));
    }

    /**
     * Creates an appropriate StatementNode for the given type and context.
     *
     * @param type
     *            StatementType
     * @param ctx
     *            Context
     */
    private void createStatement(@NonNull StatementType type, @NonNull ParserRuleContext ctx)
    {
        if (!methods.isEmpty())
        {
            MethodNode mn = methods.peek();
            int start = ctx.getStart().getLine();
            int end = ctx.getStop().getLine();

            StatementNode sn = null;
            if (start > end)
            {
                sn = StatementNode.builder(type).range(start).create();
            }
            else
            {
                sn = StatementNode.builder(type).range(start, end).create();
            }
            mn.addStatement(sn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterAccessor_declarations(CSharp6Parser.Accessor_declarationsContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterAccessor_declarations(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterAdd_accessor_declaration(CSharp6Parser.Add_accessor_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterAdd_accessor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterBreak_statement(CSharp6Parser.Break_statementContext ctx)
    {
        createStatement(StatementType.Break, ctx);
        super.enterBreak_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterChecked_statement(CSharp6Parser.Checked_statementContext ctx)
    {
        createStatement(StatementType.Checked, ctx);
        super.enterChecked_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterClass_definition(final CSharp6Parser.Class_definitionContext ctx)
    {
        final CSharp6Parser.IdentifierContext itx = ctx.identifier();
        String name = itx.getText();
        if (name == null || name.isEmpty())
            name = "UNKNOWN";
        final String fullName = namespaces.isEmpty() ? name : namespaces.peek() + "." + name;
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        final TypeNode ent = TypeNode.builder(fullName == null ? name : fullName, name).range(start, end).create();
        file.addType(ent);
        types.push(ent);

        addLoCMetric(ctx, ent);

        super.enterClass_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterCompilation_unit(CSharp6Parser.Compilation_unitContext ctx)
    {
        super.enterCompilation_unit(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterConstructor_declaration2(final CSharp6Parser.Constructor_declaration2Context ctx)
    {
        final CSharp6Parser.IdentifierContext ictx = ctx.identifier();
        String name = ictx.getText();

        name += "(" + getParams(ctx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        final MethodNode ent = MethodNode.builder(fullName, name).constructor().range(start, end).create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);

        super.enterConstructor_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterContinue_statement(CSharp6Parser.Continue_statementContext ctx)
    {
        createStatement(StatementType.Continue, ctx);
        super.enterContinue_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDeclaration_statement(CSharp6Parser.Declaration_statementContext ctx)
    {
        createStatement(StatementType.Declaration, ctx);
        super.enterDeclaration_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDelegate_declaration(CSharp6Parser.Delegate_declarationContext ctx)
    {
        final CSharp6Parser.IdentifierContext mmctx = ctx.identifier();
        String name = mmctx == null ? "<DELEGATE>" : mmctx.getText();

        name += "(" + getParams(ctx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);
        super.enterDelegate_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDelegate_definition(final CSharp6Parser.Delegate_definitionContext ctx)
    {
        // final String name = ctx.identifier() == null ? "" :
        // ctx.identifier().getText() ;
        // int start = 1; if (ctx.getStart() != null) =
        // ctx.getStart().getLine();
        // int end = start; if (ctx.getStop() != null) ctx.getStop().getLine();
        // final String fullName = namespaces.isEmpty() ? name :
        // namespaces.peek() + "." + name;
        // final TypeNode ent = new TypeNode(fullName == null ? name : fullName,
        // name, start, end);
        // file.addType(ent);
        // stack.push(ent);

        super.enterDelegate_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDelegate_type(CSharp6Parser.Delegate_typeContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterDelegate_type(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDestructor_declaration(CSharp6Parser.Destructor_declarationContext ctx)
    {
        String name = "<DESTRUCTOR>";

        name += "(" + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);
        super.enterDestructor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterDo_statement(CSharp6Parser.Do_statementContext ctx)
    {
        createStatement(StatementType.Do, ctx);
        super.enterDo_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEmbedded_statement(CSharp6Parser.Embedded_statementContext ctx)
    {
        createStatement(StatementType.Embedded, ctx);
        super.enterEmbedded_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEmbedded_statement_unsafe(CSharp6Parser.Embedded_statement_unsafeContext ctx)
    {
        createStatement(StatementType.UnsafeEmbedded, ctx);
        super.enterEmbedded_statement_unsafe(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEmpty_statement(CSharp6Parser.Empty_statementContext ctx)
    {
        createStatement(StatementType.Empty, ctx);
        super.enterEmpty_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEnum_definition(final CSharp6Parser.Enum_definitionContext ctx)
    {
        final String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        final String fullName = namespaces.isEmpty() ? name : namespaces.peek() + "." + name;
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();
        final TypeNode ent = TypeNode.builder(fullName, name).range(start, end).create();

        file.addType(ent);
        types.push(ent);

        addLoCMetric(ctx, ent);

        super.enterEnum_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEnum_member_declaration(CSharp6Parser.Enum_member_declarationContext ctx)
    {
        String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        TypeNode type = types.peek();
        if (name != null)
        {
            type.addField(FieldNode.builder(name, type.getQIdentifier() + "#" + name).range(start, end).create());
        }
        super.enterEnum_member_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterExpression_statement(CSharp6Parser.Expression_statementContext ctx)
    {
        createStatement(StatementType.Expression, ctx);
        super.enterExpression_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterFixed_statement(CSharp6Parser.Fixed_statementContext ctx)
    {
        createStatement(StatementType.Fixed, ctx);
        super.enterFixed_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterFor_statement(CSharp6Parser.For_statementContext ctx)
    {
        createStatement(StatementType.For, ctx);
        super.enterFor_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterForeach_statement(CSharp6Parser.Foreach_statementContext ctx)
    {
        createStatement(StatementType.Foreach, ctx);
        super.enterForeach_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterGoto_statement(CSharp6Parser.Goto_statementContext ctx)
    {
        createStatement(StatementType.Goto, ctx);
        super.enterGoto_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterIf_statement(CSharp6Parser.If_statementContext ctx)
    {
        createStatement(StatementType.If, ctx);
        super.enterIf_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterIndexer_declaration(CSharp6Parser.Indexer_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterIndexer_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterIndexer_declaration2(CSharp6Parser.Indexer_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.enterIndexer_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_definition(final CSharp6Parser.Interface_definitionContext ctx)
    {
        final String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        final String fullName = namespaces.isEmpty() ? name : namespaces.peek() + "." + name;
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        final TypeNode ent = TypeNode.builder(fullName, name).range(start, end).isInterface().create();

        types.push(ent);
        file.addType(ent);

        addLoCMetric(ctx, ent);

        super.enterInterface_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_definition(final CSharp6Parser.Interface_definitionContext ctx)
    {

        super.exitInterface_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_method_declaration2(final CSharp6Parser.Interface_method_declaration2Context ctx)
    {
        String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        name = name + " (" + getParams(ctx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();
        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();

        types.peek().addMethod(ent);

        super.enterInterface_method_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterLabeled_statement(CSharp6Parser.Labeled_statementContext ctx)
    {
        createStatement(StatementType.Labeled, ctx);
        super.enterLabeled_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterLock_statement(CSharp6Parser.Lock_statementContext ctx)
    {
        createStatement(StatementType.Lock, ctx);
        super.enterLock_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterMember_declarator(CSharp6Parser.Member_declaratorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterMember_declarator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterMethod_declaration2(final CSharp6Parser.Method_declaration2Context ctx)
    {
        final CSharp6Parser.Method_member_nameContext mmctx = ctx.method_member_name();
        String name = mmctx == null ? "<METHOD>" : mmctx.getText();

        name += "(" + getParams(ctx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);

        super.enterMethod_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterNamespace_declaration(final CSharp6Parser.Namespace_declarationContext ctx)
    {
        final List<CSharp6Parser.IdentifierContext> ids = ctx.qualified_identifier().identifier();
        final StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (final CSharp6Parser.IdentifierContext idx : ids)
        {
            if (!first)
            {
                builder.append(".");
            }
            builder.append(idx.IDENTIFIER().getText());
            first = false;
        }
        String name = builder.toString();
        if (!namespaces.isEmpty())
        {
            name = namespaces.peek() + "." + name;
        }
        namespaces.push(name);

        super.enterNamespace_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterNamespace_member_declaration(CSharp6Parser.Namespace_member_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterNamespace_member_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterNamespace_name(CSharp6Parser.Namespace_nameContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterNamespace_name(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterOperator_declaration(CSharp6Parser.Operator_declarationContext ctx)
    {
        String name = "<OPERATOR>";
        if (ctx.operator_declarator() != null)
        {
            if (ctx.operator_declarator().binary_operator_declarator() != null)
            {
                if (ctx.operator_declarator().binary_operator_declarator().overloadable_binary_operator() != null)
                {
                    CSharp6Parser.Overloadable_binary_operatorContext oboc = ctx.operator_declarator()
                            .binary_operator_declarator()
                            .overloadable_binary_operator();
                    if (oboc.OP_EQ() != null)
                    {
                        name = oboc.OP_EQ().getText();
                    }
                    else if (oboc.OP_GE() != null)
                    {
                        name = oboc.OP_GE().getText();
                    }
                    else if (oboc.OP_LE() != null)
                    {
                        name = oboc.OP_LE().getText();
                    }
                    else if (oboc.OP_LEFT_SHIFT() != null)
                    {
                        name = oboc.OP_LEFT_SHIFT().getText();
                    }
                    else if (oboc.OP_NE() != null)
                    {
                        name = oboc.OP_NE().getText();
                    }
                    else if (oboc.AMP() != null)
                    {
                        name = oboc.AMP().getText();
                    }
                    else if (oboc.DIV() != null)
                    {
                        name = oboc.DIV().getText();
                    }
                    else if (oboc.BITWISE_OR() != null)
                    {
                        name = oboc.BITWISE_OR().getText();
                    }
                    else if (oboc.CARET() != null)
                    {
                        name = oboc.CARET().getText();
                    }
                    else if (oboc.GT() != null)
                    {
                        name = oboc.GT().getText();
                    }
                    else if (oboc.LT() != null)
                    {
                        name = oboc.LT().getText();
                    }
                    else if (oboc.MINUS() != null)
                    {
                        name = oboc.MINUS().getText();
                    }
                    else if (oboc.PERCENT() != null)
                    {
                        name = oboc.PERCENT().getText();
                    }
                    else if (oboc.PLUS() != null)
                    {
                        name = oboc.PLUS().getText();
                    }
                    else if (oboc.STAR() != null)
                    {
                        name = oboc.STAR().getText();
                    }
                    else if (oboc.right_shift() != null)
                    {
                        name = oboc.right_shift().getText();
                    }
                }
            }
            else if (ctx.operator_declarator().conversion_operator_declarator() != null)
            {
                if (ctx.operator_declarator().conversion_operator_declarator().identifier() != null)
                    name = ctx.operator_declarator().conversion_operator_declarator().identifier().getText();
                else if (ctx.operator_declarator().conversion_operator_declarator().OPERATOR() != null)
                    name = ctx.operator_declarator().conversion_operator_declarator().OPERATOR().getText();
            }
            else if (ctx.operator_declarator().unary_operator_declarator() != null)
            {
                if (ctx.operator_declarator().unary_operator_declarator().identifier() != null)
                {
                    name = ctx.operator_declarator().unary_operator_declarator().identifier().getText();
                }
                else if (ctx.operator_declarator().unary_operator_declarator().overloadable_unary_operator() != null)
                {
                    CSharp6Parser.Overloadable_unary_operatorContext ouoc = ctx.operator_declarator()
                            .unary_operator_declarator()
                            .overloadable_unary_operator();
                    if (ouoc.OP_DEC() != null)
                    {
                        name = ouoc.OP_DEC().getText();
                    }
                    else if (ouoc.OP_INC() != null)
                    {
                        name = ouoc.OP_INC().getText();
                    }
                    else if (ouoc.BANG() != null)
                    {
                        name = ouoc.BANG().getText();
                    }
                    else if (ouoc.FALSE() != null)
                    {
                        name = ouoc.FALSE().getText();
                    }
                    else if (ouoc.MINUS() != null)
                    {
                        name = ouoc.MINUS().getText();
                    }
                    else if (ouoc.PLUS() != null)
                    {
                        name = ouoc.PLUS().getText();
                    }
                    else if (ouoc.TILDE() != null)
                    {
                        name = ouoc.TILDE().getText();
                    }
                    else if (ouoc.TRUE() != null)
                    {
                        name = ouoc.TRUE().getText();
                    }
                }
            }
        }
        final String op = name;
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();
        final String fullName = types.peek().getQIdentifier() + "#" + op;
        final MethodNode ent = MethodNode.builder(fullName, op).range(start, end).create();

        methods.push(ent);
        types.peek().addMethod(ent);

        addLoCMetric(ctx, ent);
        super.enterOperator_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterOperator_declaration2(final CSharp6Parser.Operator_declaration2Context ctx)
    {
        String op = "<OPERATOR>";
        if (ctx.OPERATOR() != null)
        {
            op = ctx.OPERATOR().getText();
        }
        else if (ctx.overloadable_operator() != null)
        {
            op = ctx.overloadable_operator().getText();
        }

        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();
        final String fullName = types.peek().getQIdentifier() + "#" + op;
        final MethodNode ent = MethodNode.builder(fullName, op).range(start, end).create();

        methods.push(ent);
        types.peek().addMethod(ent);

        addLoCMetric(ctx, ent);

        super.enterOperator_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterOverloadable_binary_operator(CSharp6Parser.Overloadable_binary_operatorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterOverloadable_binary_operator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterOverloadable_operator(CSharp6Parser.Overloadable_operatorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterOverloadable_operator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterOverloadable_unary_operator(CSharp6Parser.Overloadable_unary_operatorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterOverloadable_unary_operator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterLocal_constant_declaration(CSharp6Parser.Local_constant_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterLocal_constant_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitMethod_declaration(CSharp6Parser.Method_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitMethod_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitConstructor_declaration(CSharp6Parser.Constructor_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitConstructor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitConstructor_body(CSharp6Parser.Constructor_bodyContext ctx)
    {
        super.exitConstructor_body(ctx);
        if (!methods.isEmpty())
            methods.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterStatic_constructor_declaration(CSharp6Parser.Static_constructor_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterStatic_constructor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitDestructor_declaration(CSharp6Parser.Destructor_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitDestructor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public void exitDestructor_body(CSharp6Parser.Destructor_bodyContext ctx)
    {
        super.exitDestructor_body(ctx);
        if (!methods.isEmpty())
            methods.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_method_declaration(CSharp6Parser.Interface_method_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitInterface_method_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_property_declaration(CSharp6Parser.Interface_property_declarationContext ctx)
    {
        String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        TypeNode type = types.peek();
        if (name != null)
        {
            type.addField(FieldNode.builder(name, type.getQIdentifier() + "#" + name).range(start, end).create());
        }
        super.enterInterface_property_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_property_declaration(CSharp6Parser.Interface_property_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitInterface_property_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitDelegate_declaration(CSharp6Parser.Delegate_declarationContext ctx)
    {
        super.exitDelegate_declaration(ctx);
        if (!methods.isEmpty())
            methods.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterField_declaration2(CSharp6Parser.Field_declaration2Context ctx)
    {
        super.enterField_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitConstructor_declaration2(CSharp6Parser.Constructor_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.exitConstructor_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitMethod_declaration2(CSharp6Parser.Method_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.exitMethod_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_method_declaration2(CSharp6Parser.Interface_method_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.exitInterface_method_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_property_declaration2(CSharp6Parser.Interface_property_declaration2Context ctx)
    {
        String name = ctx.identifier() == null ? "" : ctx.identifier().getText();
        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        TypeNode type = types.peek();
        if (name != null)
        {
            type.addField(FieldNode.builder(name, type.getQIdentifier() + "#" + name).range(start, end).create());
        }
        super.enterInterface_property_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_property_declaration2(CSharp6Parser.Interface_property_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.exitInterface_property_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterProperty_declaration(CSharp6Parser.Property_declarationContext ctx)
    {
        String name = ctx.member_name().toString();
        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        TypeNode type = types.peek();
        if (name != null)
        {
            type.addField(FieldNode.builder(name, type.getQIdentifier() + "#" + name).range(start, end).create());
        }

        super.enterProperty_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterMethod_declaration(CSharp6Parser.Method_declarationContext ctx)
    {
        final CSharp6Parser.Method_headerContext mhcx = ctx.method_header();
        final CSharp6Parser.Member_nameContext mmctx = mhcx.member_name();
        String name = mmctx == null ? "<METHOD>" : mmctx.getText();

        name += "(" + getParams(mhcx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);

        super.enterMethod_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEvent_declaration(CSharp6Parser.Event_declarationContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterEvent_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterIndexer_declarator(CSharp6Parser.Indexer_declaratorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterIndexer_declarator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterConstructor_declaration(CSharp6Parser.Constructor_declarationContext ctx)
    {
        final CSharp6Parser.Constructor_declaratorContext mhcx = ctx.constructor_declarator();
        String name = "<INIT>";

        name += "(" + getParams(mhcx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).constructor().create();
        types.peek().addMethod(ent);
        methods.push(ent);

        addLoCMetric(ctx, ent);
        super.enterConstructor_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_method_declaration(CSharp6Parser.Interface_method_declarationContext ctx)
    {
        String name = ctx.identifier() == null ? "<METHOD>"
                : ctx.identifier() == null ? "" : ctx.identifier().getText();

        name += "(" + getParams(ctx.formal_parameter_list()) + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);

        addLoCMetric(ctx, ent);
        super.enterInterface_method_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_event_declaration(CSharp6Parser.Interface_event_declarationContext ctx)
    {
        final CSharp6Parser.IdentifierContext ix = ctx.identifier();
        String name = ix == null ? "<METHOD>" : ix.getText();

        name += "(" + ")";
        final String fullName = types.peek().getQIdentifier() + "#" + name;

        int start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() == null)
            end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        final MethodNode ent = MethodNode.builder(fullName, name).range(start, end).create();
        types.peek().addMethod(ent);

        addLoCMetric(ctx, ent);
        super.enterInterface_event_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_indexer_declaration(CSharp6Parser.Interface_indexer_declarationContext ctx)
    {
        super.enterInterface_indexer_declaration(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterEvent_declaration2(CSharp6Parser.Event_declaration2Context ctx)
    {

        super.enterEvent_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_event_declaration2(CSharp6Parser.Interface_event_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.enterInterface_event_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterInterface_indexer_declaration2(CSharp6Parser.Interface_indexer_declaration2Context ctx)
    {
        // TODO Auto-generated method stub
        super.enterInterface_indexer_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterProperty_declaration2(CSharp6Parser.Property_declaration2Context ctx)
    {
        String name = "<PROPERTY>";
        if (ctx.member_name() != null)
            name = ctx.member_name().getText();
        if (ctx.right_arrow() != null)
            name = ctx.right_arrow().first.getText();

        int start = ctx.getStart().getLine();

        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        if (end < start)
            end = start;

        TypeNode type = types.peek();
        if (name != null)
        {
            type.addField(FieldNode.builder(name, type.getQIdentifier() + "#" + name).range(start, end).create());
        }

        super.enterProperty_declaration2(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterSelection_statement(CSharp6Parser.Selection_statementContext ctx)
    {
        createStatement(StatementType.Selection, ctx);
        super.enterSelection_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterSimple_embedded_statement(CSharp6Parser.Simple_embedded_statementContext ctx)
    {
        createStatement(StatementType.SimpleEmbedded, ctx);
        super.enterSimple_embedded_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterStruct_definition(final CSharp6Parser.Struct_definitionContext ctx)
    {
        final String name = ctx.identifier() == null ? "<STRUCT>" : ctx.identifier().getText();
        int start = 1;
        if (ctx.getStart() != null)
            start = ctx.getStart().getLine();
        int end = start;
        if (ctx.getStop() != null)
            end = ctx.getStop().getLine();

        final String fullName = namespaces.isEmpty() ? name : namespaces.peek() + "." + name;
        final TypeNode ent = TypeNode.builder(fullName, name).range(start, end).create();
        types.push(ent);
        file.addType(ent);

        addLoCMetric(ctx, ent);

        super.enterStruct_definition(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterSwitch_statement(CSharp6Parser.Switch_statementContext ctx)
    {
        createStatement(StatementType.Switch, ctx);
        super.enterSwitch_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterThrow_statement(CSharp6Parser.Throw_statementContext ctx)
    {
        createStatement(StatementType.Throw, ctx);
        super.enterThrow_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterTry_statement(CSharp6Parser.Try_statementContext ctx)
    {
        createStatement(StatementType.Try, ctx);
        super.enterTry_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterUnary_operator_declarator(CSharp6Parser.Unary_operator_declaratorContext ctx)
    {
        // TODO Auto-generated method stub
        super.enterUnary_operator_declarator(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterUnchecked_statement(CSharp6Parser.Unchecked_statementContext ctx)
    {
        createStatement(StatementType.UnChecked, ctx);
        super.enterUnchecked_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterUnsafe_statement(CSharp6Parser.Unsafe_statementContext ctx)
    {
        createStatement(StatementType.Unsafe, ctx);
        super.enterUnsafe_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterUsing_statement(CSharp6Parser.Using_statementContext ctx)
    {
        createStatement(StatementType.Using, ctx);
        super.enterUsing_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterWhile_statement(CSharp6Parser.While_statementContext ctx)
    {
        createStatement(StatementType.While, ctx);
        super.enterWhile_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterYield_statement(CSharp6Parser.Yield_statementContext ctx)
    {
        createStatement(StatementType.Yield, ctx);
        super.enterYield_statement(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitClass_body(final CSharp6Parser.Class_bodyContext ctx)
    {
        super.exitClass_body(ctx);
        if (!types.isEmpty())
            types.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitDelegate_type(CSharp6Parser.Delegate_typeContext ctx)
    {
        // TODO Auto-generated method stub
        super.exitDelegate_type(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitEnum_body(final CSharp6Parser.Enum_bodyContext ctx)
    {
        super.exitEnum_body(ctx);
        if (!types.isEmpty())
            types.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitInterface_body(final CSharp6Parser.Interface_bodyContext ctx)
    {
        super.exitInterface_body(ctx);
        if (!types.isEmpty())
            types.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitMethod_body(final CSharp6Parser.Method_bodyContext ctx)
    {
        super.exitMethod_body(ctx);
        if (!methods.isEmpty())
            methods.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitNamespace_body(final CSharp6Parser.Namespace_bodyContext ctx)
    {
        super.exitNamespace_body(ctx);
        if (!namespaces.isEmpty())
            namespaces.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitOperator_body(final CSharp6Parser.Operator_bodyContext ctx)
    {
        super.exitOperator_body(ctx);
        if (!methods.isEmpty())
            methods.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitStruct_body(final CSharp6Parser.Struct_bodyContext ctx)
    {
        super.exitStruct_body(ctx);
        if (!types.isEmpty())
            types.pop();
    }

    /**
     * Extracts a comma-delimited string of method parameters.
     *
     * @param ctx
     *            Context containing the parameter list.
     * @return Comma-delimited String of method parameters.
     */
    private String getParams(final CSharp6Parser.Formal_parameter_listContext ctx)
    {
        String retVal = "";
        if (ctx != null)
        {
            final CSharp6Parser.Fixed_parametersContext fpc = ctx.fixed_parameters();
            if (fpc != null)
            {
                final StringBuilder builder = new StringBuilder();
                String type = null;
                for (final CSharp6Parser.Fixed_parameterContext pc : fpc.fixed_parameter())
                {
                    type = pc.type().getText();
                    builder.append(type + ", ");
                }
                retVal = builder.toString();
            }
            final CSharp6Parser.Parameter_arrayContext pac = ctx.parameter_array();
            if (pac != null)
            {
                String type = "";
                if (pac.array_type() != null)
                {
                    type = pac.array_type().getText();
                }
                retVal += type;
            }
            if (retVal.endsWith(", "))
            {
                retVal = retVal.substring(0, retVal.length() - 2);
            }
        }
        return retVal;
    }
}
