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
import dev.siliconcode.arc.parsers.java.java2.JavaParser;
import dev.siliconcode.arc.parsers.*;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Java8MemberAndGenRealUseRelsExtractor extends Java8AbstractExtractor {

    List<String> modifiers = Lists.newArrayList();
    List<String> fieldNames = Lists.newArrayList();
    String primFieldType = null;
    String fieldType = null;
    boolean inMember = false;
    boolean inMethod = false;
    boolean inParameters = false;
    boolean inTypeParams = false;
    boolean inField = false;
    boolean inExceptions = false;
    boolean inConstant = false;
    boolean inConstructor = false;
    boolean inType = false;
    boolean inLocalVar = false;
    boolean inClass = false;
    boolean inEnum = false;
    boolean inInterface = false;
    boolean inTypeList = false;

    public Java8MemberAndGenRealUseRelsExtractor(BaseModelBuilder builder) {
        super(builder);
    }

    ////////////////////
    //
    // Generalization and Realization
    //
    ////////////////////
    ////////////////////
    // Type Declarations
    ////////////////////
    @Override
    public void enterAnnotationTypeDeclaration(final JavaParser.AnnotationTypeDeclarationContext ctx) {
        inType = true;
        inInterface = true;
        super.enterAnnotationTypeDeclaration(ctx);
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

    ////////////////////////
    // Type bodies
    ////////////////////////
    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        modifiers.clear();
        if (ctx.block() != null) {
            inMethod = true;
            initializerCount.push(initializerCount.pop() + 1);
            treeBuilder.createInitializer(String.format("<init-%d>", initializerCount.peek()), initializerCount.peek(), ctx.STATIC() != null, ctx.getStart().getLine(), ctx.getStop().getLine());
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
    public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
        modifiers.clear();

        super.enterInterfaceBodyDeclaration(ctx);
    }

    @Override
    public void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
        modifiers.clear();

        super.enterAnnotationTypeElementDeclaration(ctx);
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

    //////////////////////
    //
    // Handle body setup
    //
    //////////////////////
    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
        inMember = false;
        inMethod = false;
        inField = false;
        inParameters = false;
        inTypeParams = false;
        inExceptions = false;

        super.enterBlock(ctx);
    }

    //////////////////////
    //
    // Handle Methods and Constructors
    //
    //////////////////////
    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        inMember = true;
        inMethod = true;
        treeBuilder.createMethod(ctx.IDENTIFIER().getText(), ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setMethodModifiers(modifiers);
        modifiers.clear();

        //super.enterMethodDeclaration(ctx);
    }

    @Override
    public void enterGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx) {
        inMember = true;
        inMethod = true;

        super.enterGenericMethodDeclaration(ctx);
    }

    @Override
    public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
        inMember = true;
        inMethod = true;
        treeBuilder.createMethod(ctx.IDENTIFIER().getText(), ctx.getStart().getLine(), ctx.getStop().getLine());
        treeBuilder.setMethodModifiers(modifiers);
        modifiers.clear();

        //super.enterInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void enterGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx) {
        inMember = true;
        inMethod = true;

        super.enterGenericInterfaceMethodDeclaration(ctx);
    }

    @Override
    public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        inMember = true;
        inMethod = true;
        if (ctx.IDENTIFIER() != null) {
            treeBuilder.createConstructor(ctx.IDENTIFIER().getText(), ctx.getStart().getLine(), ctx.getStop().getLine());
            treeBuilder.setMethodModifiers(modifiers);
        }
        modifiers.clear();

        //super.enterConstructorDeclaration(ctx);
    }

    @Override
    public void enterGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx) {
        inMember = true;
        inMethod = true;

        super.enterGenericConstructorDeclaration(ctx);
    }

    //////////////////////
    //
    // Handle Parameters
    //
    //////////////////////
    @Override
    public void enterFormalParameters(JavaParser.FormalParametersContext ctx) {
        inParameters = true;

        super.enterFormalParameters(ctx);
    }

    @Override
    public void exitFormalParameters(JavaParser.FormalParametersContext ctx) {
        inParameters = false;

        super.exitFormalParameters(ctx);
    }

    @Override
    public void enterFormalParameter(JavaParser.FormalParameterContext ctx) {
        treeBuilder.createMethodParameter();

        super.enterFormalParameter(ctx);
    }

    @Override
    public void enterLastFormalParameter(JavaParser.LastFormalParameterContext ctx) {
        treeBuilder.createMethodParameter();
        treeBuilder.setVariableParameter(ctx.ELLIPSIS() != null);

        super.enterLastFormalParameter(ctx);
    }

    //////////////////////
    //
    // Handle Fields, Constants, and Enum Literals
    //
    //////////////////////
    @Override
    public void enterFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        inField = true;

        super.enterFieldDeclaration(ctx);
    }

    @Override
    public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        createFields(ctx.getStart(), ctx.getStop());
        inField = false;

        super.exitFieldDeclaration(ctx);
    }

    @Override
    public void enterConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
        inField = true;
        inConstant = true;
        fieldNames.clear();

        super.enterConstDeclaration(ctx);
    }

    @Override
    public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
        createFields(ctx.getStart(), ctx.getStop());
        inConstant = false;

        super.exitConstDeclaration(ctx);
    }

    private void createFields(Token start, Token stop) {
        fieldNames.forEach(name -> {
            if (fieldType != null) {
                treeBuilder.createField(name, fieldType, false, start.getLine(), stop.getLine(), modifiers);
            } else if (primFieldType != null) {
                treeBuilder.createField(name, primFieldType, true, start.getLine(), stop.getLine(), modifiers);
            }
        });

        inField = false;
    }

    @Override
    public void enterConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx) {
        fieldNames.add(ctx.IDENTIFIER().getText());

        super.enterConstantDeclarator(ctx);
    }

    @Override
    public void enterEnumConstant(JavaParser.EnumConstantContext ctx) {
        if (ctx.IDENTIFIER() != null)
            treeBuilder.createEnumLiteral(ctx.IDENTIFIER().getText(), ctx.getStart().getLine(), ctx.getStop().getLine());

        super.enterEnumConstant(ctx);
    }

    @Override
    public void enterVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
        fieldNames.clear();

        super.enterVariableDeclarators(ctx);
    }

    @Override
    public void exitVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
        super.exitVariableDeclarators(ctx);
    }

    @Override
    public void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        if (inField)
            fieldNames.add(ctx.IDENTIFIER().getText());
        else if (inLocalVar && ctx.IDENTIFIER() != null) {
            treeBuilder.addLocalVarToScope(ctx.IDENTIFIER().getText());
        }
        // TODO add array knowledge here
//        if (ctx.RBRACK() != null) {
//            treeBuilder.setCurrentMethodParameterAsArray();
//        }

        super.enterVariableDeclaratorId(ctx);
    }

    ///////////////////
    //
    // Handle Modifiers
    //
    ///////////////////
    @Override
    public void enterModifier(JavaParser.ModifierContext ctx) {
        if (inMember && ctx.classOrInterfaceModifier() == null) {
            modifiers.add(ctx.getText());
        }

        super.enterModifier(ctx);
    }

    @Override
    public void enterClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx) {
        if (inMember && ctx.annotation() == null) {
            modifiers.add(ctx.getText());
        }

        super.enterClassOrInterfaceModifier(ctx);
    }

    @Override
    public void enterInterfaceMethodModifier(JavaParser.InterfaceMethodModifierContext ctx) {
        if (inMember && ctx.annotation() == null) {
            modifiers.add(ctx.getText());
        }

        super.enterInterfaceMethodModifier(ctx);
    }

    @Override
    public void enterVariableModifier(JavaParser.VariableModifierContext ctx) {
        if (ctx.FINAL() != null)
            treeBuilder.addParameterModifier(ctx.getText());
    }

    ////////////////////
    //
    // Type Parameters
    //
    ////////////////////
    @Override
    public void enterTypeParameters(JavaParser.TypeParametersContext ctx) {
        inTypeParams = true;

        super.enterTypeParameters(ctx);
    }

    @Override
    public void exitTypeParameters(JavaParser.TypeParametersContext ctx) {
        inTypeParams = false;

        super.exitTypeParameters(ctx);
    }

    @Override
    public void enterTypeParameter(JavaParser.TypeParameterContext ctx) {
        treeBuilder.createMethodTypeParameter(ctx.IDENTIFIER().getText());

        super.enterTypeParameter(ctx);
    }

    ////////////////////
    //
    // Types
    //
    ////////////////////
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

    @Override
    public void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {
        if (inTypeList) {
            if (inClass || inEnum) {
                treeBuilder.addRealization(ctx.getText().replaceAll("<.*>", ""));
            } else if (inInterface) {
                treeBuilder.addGeneralization(ctx.getText().replaceAll("<.*>", ""));
            }
        } else if (!inTypeList && inClass) {
            treeBuilder.addGeneralization(ctx.getText().replaceAll("<.*>", ""));
        } else if (inMethod) {
            treeBuilder.setMethodReturnType(ctx.getText());
            handleMethodType(ctx);
        } else if (inConstructor) {
            handleMethodType(ctx);
        }
//        else if (inTypeParams) {
//            treeBuilder.addTypeParamBoundType(ctx.getText());
//        }
        else if (inField) {
            fieldType = ctx.getText();
            primFieldType = null;
        }

        super.enterClassOrInterfaceType(ctx);
    }

    private void handleMethodType(JavaParser.ClassOrInterfaceTypeContext ctx) {
        if (inParameters) {
            treeBuilder.setParameterType(ctx.getText());
            treeBuilder.addUseDependency(ctx.getText().replaceAll("<.*>", ""));
        } else if (inLocalVar) {
            treeBuilder.addUseDependency(ctx.getText().replaceAll("<.*>", ""));
        } else {
            treeBuilder.addUseDependency(ctx.getText().replaceAll("<.*>", ""));
        }
    }

    @Override
    public void enterTypeTypeOrVoid(JavaParser.TypeTypeOrVoidContext ctx) {
        if (ctx.VOID() != null)
            treeBuilder.setMethodReturnTypeVoid();

        super.enterTypeTypeOrVoid(ctx);
    }

    @Override
    public void enterTypeType(JavaParser.TypeTypeContext ctx) {
        super.enterTypeType(ctx);
    }

    @Override
    public void enterPrimitiveType(JavaParser.PrimitiveTypeContext ctx) {
        if (inMethod && inParameters) {
            treeBuilder.setParameterPrimitiveType(ctx.getText());
        } else if (inMethod) {
            treeBuilder.setMethodReturnPrimitiveType(ctx.getText());
        } else if (inField) {
            primFieldType = ctx.getText();
            fieldType = null;
        }

        super.enterPrimitiveType(ctx);
    }

    /////////////////
    //
    // Exceptions
    //
    /////////////////
    @Override
    public void enterQualifiedNameList(JavaParser.QualifiedNameListContext ctx) {
        if (inMethod)
            inExceptions = true;

        super.enterQualifiedNameList(ctx);
    }

    @Override
    public void exitQualifiedNameList(JavaParser.QualifiedNameListContext ctx) {
        inExceptions = false;

        super.exitQualifiedNameList(ctx);
    }

    @Override
    public void enterQualifiedName(JavaParser.QualifiedNameContext ctx) {
        if (inExceptions) {
            treeBuilder.addMethodException(ctx.getText());
        }

        super.enterQualifiedName(ctx);
    }

    /////////////////////////
    //
    // Local Variables
    //
    /////////////////////////
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
}
