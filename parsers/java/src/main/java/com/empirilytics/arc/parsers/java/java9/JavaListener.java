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
// Generated from java/Java.g4 by ANTLR 4.5.2
package dev.siliconcode.arc.parsers.java.java;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse model produced by
 * {@link JavaParser}.
 */
public interface JavaListener extends ParseTreeListener {
    /**
     * Enter a parse model produced by {@link JavaParser#compilationUnit}.
     *
     * @param ctx
     *            the parse model
     */
    void enterCompilationUnit(JavaParser.CompilationUnitContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#compilationUnit}.
     *
     * @param ctx
     *            the parse model
     */
    void exitCompilationUnit(JavaParser.CompilationUnitContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#packageDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#packageDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitPackageDeclaration(JavaParser.PackageDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#importDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#importDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#modifier}.
     *
     * @param ctx
     *            the parse model
     */
    void enterModifier(JavaParser.ModifierContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#modifier}.
     *
     * @param ctx
     *            the parse model
     */
    void exitModifier(JavaParser.ModifierContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#classOrInterfaceModifier}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#classOrInterfaceModifier}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#variableModifier}.
     *
     * @param ctx
     *            the parse model
     */
    void enterVariableModifier(JavaParser.VariableModifierContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#variableModifier}.
     *
     * @param ctx
     *            the parse model
     */
    void exitVariableModifier(JavaParser.VariableModifierContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#classDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#classDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeParameters}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeParameters(JavaParser.TypeParametersContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeParameters}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeParameters(JavaParser.TypeParametersContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeParameter(JavaParser.TypeParameterContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeParameter(JavaParser.TypeParameterContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeBound}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeBound(JavaParser.TypeBoundContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeBound}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeBound(JavaParser.TypeBoundContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enumDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enumDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enumConstants}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnumConstants(JavaParser.EnumConstantsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enumConstants}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnumConstants(JavaParser.EnumConstantsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enumConstant}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnumConstant(JavaParser.EnumConstantContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enumConstant}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnumConstant(JavaParser.EnumConstantContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enumBodyDeclarations}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enumBodyDeclarations}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#interfaceDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#interfaceDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeList}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeList(JavaParser.TypeListContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeList}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeList(JavaParser.TypeListContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#classBody}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassBody(JavaParser.ClassBodyContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#classBody}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassBody(JavaParser.ClassBodyContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#interfaceBody}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInterfaceBody(JavaParser.InterfaceBodyContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#interfaceBody}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInterfaceBody(JavaParser.InterfaceBodyContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#classBodyDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#classBodyDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#memberDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterMemberDeclaration(JavaParser.MemberDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#memberDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitMemberDeclaration(JavaParser.MemberDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#methodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#methodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#genericMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#genericMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#constructorDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#constructorDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#genericConstructorDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#genericConstructorDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#fieldDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterFieldDeclaration(JavaParser.FieldDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#fieldDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#interfaceBodyDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#interfaceBodyDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#interfaceMemberDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#interfaceMemberDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#constDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterConstDeclaration(JavaParser.ConstDeclarationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#constDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#constantDeclarator}.
     *
     * @param ctx
     *            the parse model
     */
    void enterConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#constantDeclarator}.
     *
     * @param ctx
     *            the parse model
     */
    void exitConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#interfaceMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#interfaceMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#genericInterfaceMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#genericInterfaceMethodDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#variableDeclarators}.
     *
     * @param ctx
     *            the parse model
     */
    void enterVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#variableDeclarators}.
     *
     * @param ctx
     *            the parse model
     */
    void exitVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#variableDeclarator}.
     *
     * @param ctx
     *            the parse model
     */
    void enterVariableDeclarator(JavaParser.VariableDeclaratorContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#variableDeclarator}.
     *
     * @param ctx
     *            the parse model
     */
    void exitVariableDeclarator(JavaParser.VariableDeclaratorContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#variableDeclaratorId}.
     *
     * @param ctx
     *            the parse model
     */
    void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#variableDeclaratorId}.
     *
     * @param ctx
     *            the parse model
     */
    void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#variableInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void enterVariableInitializer(JavaParser.VariableInitializerContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#variableInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void exitVariableInitializer(JavaParser.VariableInitializerContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#arrayInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void enterArrayInitializer(JavaParser.ArrayInitializerContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#arrayInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void exitArrayInitializer(JavaParser.ArrayInitializerContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enumConstantName}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnumConstantName(JavaParser.EnumConstantNameContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enumConstantName}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnumConstantName(JavaParser.EnumConstantNameContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#type}.
     *
     * @param ctx
     *            the parse model
     */
    void enterType(JavaParser.TypeContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#type}.
     *
     * @param ctx
     *            the parse model
     */
    void exitType(JavaParser.TypeContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#classOrInterfaceType}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#classOrInterfaceType}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#primitiveType}.
     *
     * @param ctx
     *            the parse model
     */
    void enterPrimitiveType(JavaParser.PrimitiveTypeContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#primitiveType}.
     *
     * @param ctx
     *            the parse model
     */
    void exitPrimitiveType(JavaParser.PrimitiveTypeContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeArguments}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeArguments(JavaParser.TypeArgumentsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeArguments}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeArguments(JavaParser.TypeArgumentsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeArgument}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeArgument(JavaParser.TypeArgumentContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeArgument}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeArgument(JavaParser.TypeArgumentContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#qualifiedNameList}.
     *
     * @param ctx
     *            the parse model
     */
    void enterQualifiedNameList(JavaParser.QualifiedNameListContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#qualifiedNameList}.
     *
     * @param ctx
     *            the parse model
     */
    void exitQualifiedNameList(JavaParser.QualifiedNameListContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#formalParameters}.
     *
     * @param ctx
     *            the parse model
     */
    void enterFormalParameters(JavaParser.FormalParametersContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#formalParameters}.
     *
     * @param ctx
     *            the parse model
     */
    void exitFormalParameters(JavaParser.FormalParametersContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#formalParameterList}.
     *
     * @param ctx
     *            the parse model
     */
    void enterFormalParameterList(JavaParser.FormalParameterListContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#formalParameterList}.
     *
     * @param ctx
     *            the parse model
     */
    void exitFormalParameterList(JavaParser.FormalParameterListContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#formalParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void enterFormalParameter(JavaParser.FormalParameterContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#formalParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void exitFormalParameter(JavaParser.FormalParameterContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#lastFormalParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void enterLastFormalParameter(JavaParser.LastFormalParameterContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#lastFormalParameter}.
     *
     * @param ctx
     *            the parse model
     */
    void exitLastFormalParameter(JavaParser.LastFormalParameterContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#methodBody}.
     *
     * @param ctx
     *            the parse model
     */
    void enterMethodBody(JavaParser.MethodBodyContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#methodBody}.
     *
     * @param ctx
     *            the parse model
     */
    void exitMethodBody(JavaParser.MethodBodyContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#constructorBody}.
     *
     * @param ctx
     *            the parse model
     */
    void enterConstructorBody(JavaParser.ConstructorBodyContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#constructorBody}.
     *
     * @param ctx
     *            the parse model
     */
    void exitConstructorBody(JavaParser.ConstructorBodyContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#qualifiedName}.
     *
     * @param ctx
     *            the parse model
     */
    void enterQualifiedName(JavaParser.QualifiedNameContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#qualifiedName}.
     *
     * @param ctx
     *            the parse model
     */
    void exitQualifiedName(JavaParser.QualifiedNameContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#literal}.
     *
     * @param ctx
     *            the parse model
     */
    void enterLiteral(JavaParser.LiteralContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#literal}.
     *
     * @param ctx
     *            the parse model
     */
    void exitLiteral(JavaParser.LiteralContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#annotation}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotation(JavaParser.AnnotationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#annotation}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotation(JavaParser.AnnotationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#annotationName}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationName(JavaParser.AnnotationNameContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#annotationName}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationName(JavaParser.AnnotationNameContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#elementValuePairs}.
     *
     * @param ctx
     *            the parse model
     */
    void enterElementValuePairs(JavaParser.ElementValuePairsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#elementValuePairs}.
     *
     * @param ctx
     *            the parse model
     */
    void exitElementValuePairs(JavaParser.ElementValuePairsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#elementValuePair}.
     *
     * @param ctx
     *            the parse model
     */
    void enterElementValuePair(JavaParser.ElementValuePairContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#elementValuePair}.
     *
     * @param ctx
     *            the parse model
     */
    void exitElementValuePair(JavaParser.ElementValuePairContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#elementValue}.
     *
     * @param ctx
     *            the parse model
     */
    void enterElementValue(JavaParser.ElementValueContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#elementValue}.
     *
     * @param ctx
     *            the parse model
     */
    void exitElementValue(JavaParser.ElementValueContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#elementValueArrayInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void enterElementValueArrayInitializer(JavaParser.ElementValueArrayInitializerContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#elementValueArrayInitializer}.
     *
     * @param ctx
     *            the parse model
     */
    void exitElementValueArrayInitializer(JavaParser.ElementValueArrayInitializerContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#annotationTypeDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#annotationTypeDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#annotationTypeBody}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#annotationTypeBody}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#annotationTypeElementDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#annotationTypeElementDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#annotationTypeElementRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationTypeElementRest(JavaParser.AnnotationTypeElementRestContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#annotationTypeElementRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationTypeElementRest(JavaParser.AnnotationTypeElementRestContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#annotationMethodOrConstantRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationMethodOrConstantRest(JavaParser.AnnotationMethodOrConstantRestContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#annotationMethodOrConstantRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationMethodOrConstantRest(JavaParser.AnnotationMethodOrConstantRestContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#annotationMethodRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#annotationMethodRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#annotationConstantRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#annotationConstantRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#defaultValue}.
     *
     * @param ctx
     *            the parse model
     */
    void enterDefaultValue(JavaParser.DefaultValueContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#defaultValue}.
     *
     * @param ctx
     *            the parse model
     */
    void exitDefaultValue(JavaParser.DefaultValueContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#block}.
     *
     * @param ctx
     *            the parse model
     */
    void enterBlock(JavaParser.BlockContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#block}.
     *
     * @param ctx
     *            the parse model
     */
    void exitBlock(JavaParser.BlockContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#blockStatement}.
     *
     * @param ctx
     *            the parse model
     */
    void enterBlockStatement(JavaParser.BlockStatementContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#blockStatement}.
     *
     * @param ctx
     *            the parse model
     */
    void exitBlockStatement(JavaParser.BlockStatementContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#localVariableDeclarationStatement}.
     *
     * @param ctx
     *            the parse model
     */
    void enterLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#localVariableDeclarationStatement}.
     *
     * @param ctx
     *            the parse model
     */
    void exitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#localVariableDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void enterLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#localVariableDeclaration}.
     *
     * @param ctx
     *            the parse model
     */
    void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#statement}.
     *
     * @param ctx
     *            the parse model
     */
    void enterStatement(JavaParser.StatementContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#statement}.
     *
     * @param ctx
     *            the parse model
     */
    void exitStatement(JavaParser.StatementContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#catchClause}.
     *
     * @param ctx
     *            the parse model
     */
    void enterCatchClause(JavaParser.CatchClauseContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#catchClause}.
     *
     * @param ctx
     *            the parse model
     */
    void exitCatchClause(JavaParser.CatchClauseContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#catchType}.
     *
     * @param ctx
     *            the parse model
     */
    void enterCatchType(JavaParser.CatchTypeContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#catchType}.
     *
     * @param ctx
     *            the parse model
     */
    void exitCatchType(JavaParser.CatchTypeContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#finallyBlock}.
     *
     * @param ctx
     *            the parse model
     */
    void enterFinallyBlock(JavaParser.FinallyBlockContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#finallyBlock}.
     *
     * @param ctx
     *            the parse model
     */
    void exitFinallyBlock(JavaParser.FinallyBlockContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#resourceSpecification}.
     *
     * @param ctx
     *            the parse model
     */
    void enterResourceSpecification(JavaParser.ResourceSpecificationContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#resourceSpecification}.
     *
     * @param ctx
     *            the parse model
     */
    void exitResourceSpecification(JavaParser.ResourceSpecificationContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#resources}.
     *
     * @param ctx
     *            the parse model
     */
    void enterResources(JavaParser.ResourcesContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#resources}.
     *
     * @param ctx
     *            the parse model
     */
    void exitResources(JavaParser.ResourcesContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#resource}.
     *
     * @param ctx
     *            the parse model
     */
    void enterResource(JavaParser.ResourceContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#resource}.
     *
     * @param ctx
     *            the parse model
     */
    void exitResource(JavaParser.ResourceContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#switchBlockStatementGroup}.
     *
     * @param ctx
     *            the parse model
     */
    void enterSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#switchBlockStatementGroup}.
     *
     * @param ctx
     *            the parse model
     */
    void exitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#switchLabel}.
     *
     * @param ctx
     *            the parse model
     */
    void enterSwitchLabel(JavaParser.SwitchLabelContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#switchLabel}.
     *
     * @param ctx
     *            the parse model
     */
    void exitSwitchLabel(JavaParser.SwitchLabelContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#forControl}.
     *
     * @param ctx
     *            the parse model
     */
    void enterForControl(JavaParser.ForControlContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#forControl}.
     *
     * @param ctx
     *            the parse model
     */
    void exitForControl(JavaParser.ForControlContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#forInit}.
     *
     * @param ctx
     *            the parse model
     */
    void enterForInit(JavaParser.ForInitContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#forInit}.
     *
     * @param ctx
     *            the parse model
     */
    void exitForInit(JavaParser.ForInitContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#enhancedForControl}.
     *
     * @param ctx
     *            the parse model
     */
    void enterEnhancedForControl(JavaParser.EnhancedForControlContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#enhancedForControl}.
     *
     * @param ctx
     *            the parse model
     */
    void exitEnhancedForControl(JavaParser.EnhancedForControlContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#forUpdate}.
     *
     * @param ctx
     *            the parse model
     */
    void enterForUpdate(JavaParser.ForUpdateContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#forUpdate}.
     *
     * @param ctx
     *            the parse model
     */
    void exitForUpdate(JavaParser.ForUpdateContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#parExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void enterParExpression(JavaParser.ParExpressionContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#parExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void exitParExpression(JavaParser.ParExpressionContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#expressionList}.
     *
     * @param ctx
     *            the parse model
     */
    void enterExpressionList(JavaParser.ExpressionListContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#expressionList}.
     *
     * @param ctx
     *            the parse model
     */
    void exitExpressionList(JavaParser.ExpressionListContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#statementExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void enterStatementExpression(JavaParser.StatementExpressionContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#statementExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void exitStatementExpression(JavaParser.StatementExpressionContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#constantExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void enterConstantExpression(JavaParser.ConstantExpressionContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#constantExpression}.
     *
     * @param ctx
     *            the parse model
     */
    void exitConstantExpression(JavaParser.ConstantExpressionContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#expression}.
     *
     * @param ctx
     *            the parse model
     */
    void enterExpression(JavaParser.ExpressionContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#expression}.
     *
     * @param ctx
     *            the parse model
     */
    void exitExpression(JavaParser.ExpressionContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#primary}.
     *
     * @param ctx
     *            the parse model
     */
    void enterPrimary(JavaParser.PrimaryContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#primary}.
     *
     * @param ctx
     *            the parse model
     */
    void exitPrimary(JavaParser.PrimaryContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#creator}.
     *
     * @param ctx
     *            the parse model
     */
    void enterCreator(JavaParser.CreatorContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#creator}.
     *
     * @param ctx
     *            the parse model
     */
    void exitCreator(JavaParser.CreatorContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#createdName}.
     *
     * @param ctx
     *            the parse model
     */
    void enterCreatedName(JavaParser.CreatedNameContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#createdName}.
     *
     * @param ctx
     *            the parse model
     */
    void exitCreatedName(JavaParser.CreatedNameContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#innerCreator}.
     *
     * @param ctx
     *            the parse model
     */
    void enterInnerCreator(JavaParser.InnerCreatorContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#innerCreator}.
     *
     * @param ctx
     *            the parse model
     */
    void exitInnerCreator(JavaParser.InnerCreatorContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#arrayCreatorRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterArrayCreatorRest(JavaParser.ArrayCreatorRestContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#arrayCreatorRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitArrayCreatorRest(JavaParser.ArrayCreatorRestContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#classCreatorRest}.
     *
     * @param ctx
     *            the parse model
     */
    void enterClassCreatorRest(JavaParser.ClassCreatorRestContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#classCreatorRest}.
     *
     * @param ctx
     *            the parse model
     */
    void exitClassCreatorRest(JavaParser.ClassCreatorRestContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#explicitGenericInvocation}.
     *
     * @param ctx
     *            the parse model
     */
    void enterExplicitGenericInvocation(JavaParser.ExplicitGenericInvocationContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#explicitGenericInvocation}.
     *
     * @param ctx
     *            the parse model
     */
    void exitExplicitGenericInvocation(JavaParser.ExplicitGenericInvocationContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#nonWildcardTypeArguments}.
     *
     * @param ctx
     *            the parse model
     */
    void enterNonWildcardTypeArguments(JavaParser.NonWildcardTypeArgumentsContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#nonWildcardTypeArguments}.
     *
     * @param ctx
     *            the parse model
     */
    void exitNonWildcardTypeArguments(JavaParser.NonWildcardTypeArgumentsContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#typeArgumentsOrDiamond}.
     *
     * @param ctx
     *            the parse model
     */
    void enterTypeArgumentsOrDiamond(JavaParser.TypeArgumentsOrDiamondContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#typeArgumentsOrDiamond}.
     *
     * @param ctx
     *            the parse model
     */
    void exitTypeArgumentsOrDiamond(JavaParser.TypeArgumentsOrDiamondContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#nonWildcardTypeArgumentsOrDiamond}.
     *
     * @param ctx
     *            the parse model
     */
    void enterNonWildcardTypeArgumentsOrDiamond(JavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#nonWildcardTypeArgumentsOrDiamond}.
     *
     * @param ctx
     *            the parse model
     */
    void exitNonWildcardTypeArgumentsOrDiamond(JavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#superSuffix}.
     *
     * @param ctx
     *            the parse model
     */
    void enterSuperSuffix(JavaParser.SuperSuffixContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#superSuffix}.
     *
     * @param ctx
     *            the parse model
     */
    void exitSuperSuffix(JavaParser.SuperSuffixContext ctx);

    /**
     * Enter a parse model produced by
     * {@link JavaParser#explicitGenericInvocationSuffix}.
     *
     * @param ctx
     *            the parse model
     */
    void enterExplicitGenericInvocationSuffix(JavaParser.ExplicitGenericInvocationSuffixContext ctx);

    /**
     * Exit a parse model produced by
     * {@link JavaParser#explicitGenericInvocationSuffix}.
     *
     * @param ctx
     *            the parse model
     */
    void exitExplicitGenericInvocationSuffix(JavaParser.ExplicitGenericInvocationSuffixContext ctx);

    /**
     * Enter a parse model produced by {@link JavaParser#arguments}.
     *
     * @param ctx
     *            the parse model
     */
    void enterArguments(JavaParser.ArgumentsContext ctx);

    /**
     * Exit a parse model produced by {@link JavaParser#arguments}.
     *
     * @param ctx
     *            the parse model
     */
    void exitArguments(JavaParser.ArgumentsContext ctx);
}
