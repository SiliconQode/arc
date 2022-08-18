/*
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
package dev.siliconcode.arc.parsers.java

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Project
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager
import dev.siliconcode.arc.parsers.java.java2.JavaLexer
import dev.siliconcode.arc.parsers.java.java2.JavaParser
import groovy.util.logging.Log4j2
import groovyjarjarantlr.RecognitionException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.jetbrains.annotations.NotNull
import dev.siliconcode.arc.parsers.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaDirector extends BaseDirector {

    JavaDirector(Project proj, DBCredentials credentials, boolean statements = false, boolean useSinglePass = true, boolean createCFG = false, boolean useExpressions = false) {
        super(proj, new ParallelJavaArtifactIdentifier(credentials), credentials, statements, useSinglePass, createCFG, useExpressions)
    }

    void gatherAllInfoAtOnce(File file, int current, int total) {
        DBManager.instance.open(credentials)
        log.info("Parsing File: ${file.getName()}")
        builder = new JavaModelBuilder(proj, file, credentials)
        boolean processed = file.getParseStage() < 1 || file.getAllTypes().size() > 0
//        DBManager.instance.close()

        if (processed) {
            utilizeParser(file, new Java8SinglePassExtractor(builder, createCFG, useExpressions), current, total)
//            DBManager.instance.open(credentials)
            file.setParseStage(1)
        }
        DBManager.instance.close()
    }

    @Override
    void gatherFileAndTypeInfo(File file) {
        DBManager.instance.open(credentials)
        int stage = file.getParseStage()
        DBManager.instance.close()

        if (stage >= 1)
            return

        builder = new JavaModelBuilder(proj, file, credentials)
        utilizeParser(file, new Java8FileAndTypeExtractor(builder))

        DBManager.instance.open(credentials)
        file.setParseStage(1)
        DBManager.instance.close()
    }

    @Override
    void gatherMembersAndBasicRelationInfo(File file) {
        DBManager.instance.open(credentials)
        int stage = file.getParseStage()
        DBManager.instance.close()

        if (stage >= 2)
            return

        BaseModelBuilder builder = new JavaModelBuilder(proj, file, credentials)
        utilizeParser(file, new Java8MemberAndGenRealUseRelsExtractor(builder))

        DBManager.instance.open(credentials)
        file.setParseStage(2)
        DBManager.instance.close()
    }

    @Override
    void gatherMemberUsageInfo(File file) {
        DBManager.instance.open(credentials)
        int stage = file.getParseStage()
        DBManager.instance.close()

        if (stage >= 3)
            return

        BaseModelBuilder builder = new JavaModelBuilder(proj, file, credentials)
        utilizeParser(file, new Java8MemberUseExtractor(builder))

        DBManager.instance.open(credentials)
        file.setParseStage(3)
        DBManager.instance.close()
    }

    @Override
    void gatherStatementInfo(File file) {
        DBManager.instance.open(credentials)
        int stage = file.getParseStage()
        DBManager.instance.close()

        if (stage >= 4)
            return

        BaseModelBuilder builder = new JavaModelBuilder(proj, file, credentials)
        utilizeParser(file, new Java8StatementExtractor(builder))

        DBManager.instance.open(credentials)
        file.setParseStage(4)
        DBManager.instance.close()
    }

    @Override
    boolean includeFile(File file) {
        file.getName().endsWith(".java")
    }

    void utilizeParser(File file, ParseTreeListener listener, int current = 0, int total = 0) {
//        DBManager.instance.open(credentials)
//        String path = file.getFullPath()
        String path = file.getName()
//        DBManager.instance.close()

        log.info "Parsing ($current / $total)... $path"
        try {
            final JavaParserConstructor pt = new JavaParserConstructor()
            final JavaParser parser = pt.loadFile(path)
            final JavaParser.CompilationUnitContext cuContext = parser.compilationUnit()
            final ParseTreeWalker walker = new ParseTreeWalker()
            walker.walk(listener, cuContext)
        } catch (final IOException | RecognitionException e) {
            log.atError().withThrowable(e).log(e.getMessage())
//            DBManager.instance.close()
        }
    }

    /**
     * Constructs the Java parser needed to extract code model information
     *
     * @author Isaac Griffith
     * @version 1.3.0
     */
    private class JavaParserConstructor {

        /**
         * @param file
         * @return
         * @throws IOException
         */
        @NotNull
        private synchronized JavaParser loadFile(final String file) throws IOException {
            final JavaLexer lexer = new JavaLexer(CharStreams.fromFileName(file))
            final CommonTokenStream tokens = new CommonTokenStream(lexer)
            return new JavaParser(tokens)
        }
    }
}
