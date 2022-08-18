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
package com.empirilytics.arc.parsers.java;

import com.google.common.collect.Lists;
//import com.empirilytics.arc.datamodel.CodeTree;
//import com.empirilytics.arc.datamodel.DefaultCodeTree;
//import com.empirilytics.arc.datamodel.node.structural.File;
//import com.empirilytics.arc.parsers.java.java8.Java8Lexer;
//import com.empirilytics.arc.parsers.java.java8.Java8Parser;
//import com.empirilytics.arc.datamodel.DataModelMediator;
//import com.empirilytics.arc.datamodel.structural.File;
import com.empirilytics.arc.parsers.java.java8.Java8Lexer;
import com.empirilytics.arc.parsers.java.java8.Java8Parser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

/**
 * ParserTest -
 *
 * @author Isaac Griffith
 */
public class JavaParserTest {

//    private static final Logger LOG = LoggerFactory.getLogger(JavaParserTest.class);
//
//    private JavaParserTest()
//    {
//    }
//
//    private static Java8Parser loadFile(final String file) throws IOException
//    {
//        final Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream(file));
//        final CommonTokenStream tokens = new CommonTokenStream(lexer);
//        Java8Parser p = new Java8Parser(tokens);
//        p.setTrimParseTree(true);
//        // p.setBuildParseTree(false);
//        return new Java8Parser(tokens);
//    }
//
//    public static List<String> fileList(final String root)
//    {
//        final List<String> fileNames = Lists.newArrayList();
//        final Stack<String> directories = new Stack<>();
//        directories.push(root);
//
//        final DirectoryStream.Filter<Path> filter = (final Path f) -> {
//            return Files.isDirectory(f)
//                    || (f.getFileName().toString().endsWith(".java") && !f.getFileName().toString().contains("Parser"));
//        };
//
//        while (!directories.isEmpty())
//        {
//            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directories.pop()), filter))
//            {
//                for (final Path p : directoryStream)
//                {
//                    if (Files.isDirectory(p))
//                    {
//                        directories.add(p.toAbsolutePath().toString());
//                    }
//                    else
//                    {
//                        fileNames.add(p.toAbsolutePath().toString());
//                    }
//                }
//            }
//            catch (final IOException ex)
//            {
//                JavaParserTest.LOG.warn(ex.getMessage());
//            }
//        }
//        return fileNames;
//    }
//
//    public static void main(final String[] args)
//    {
//        final List<String> fileNames = JavaParserTest
//                .fileList("/home/git/research/TrueRefactor/source/main/java/truerefactor");
//
//        long time[] = new long[10];
//        long avg[] = new long[10];
//
//        // Project.out.println(fileNames.size());
//
//        //
//        // final ExecutorService executor = Executors
//        // .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        // final List<Future<?>> futures = Lists.newArrayList();
//        // for (final String file : fileNames) {
//        // futures.add(executor.submit(() -> {
//        for (int i = 0; i < 10; i++)
//        {
//            final DataModelMediator tree = new DefaultCodeTree();
//
//            final long start = System.currentTimeMillis();
//            fileNames.forEach((file) -> {
//                try
//                {
//                    System.out.println("Parsing: " + file);
//                    utilizeParser(file, tree);
//                }
//                catch (final RecognitionException e)
//                {
//                    JavaParserTest.LOG.warn(e.getMessage(), e);
//                }
//            });
//            final long end = System.currentTimeMillis();
//
//            time[i] = end - start;
//            avg[i] = time[i] / fileNames.size();
//
//        }
//
//        for (int i = 0; i < 10; i++)
//        {
//            System.out.println(time[i] + " " + avg[i]);
//        }
//
//        long total = 0;
//        for (int i = 0; i < 10; i++)
//            total += time[i];
//        long totalA = 0;
//        for (int i = 0; i < 10; i++)
//            totalA += avg[i];
//
//        System.out.println("Average time: " + (total / 10));
//        System.out.println("Average individual time: " + (totalA / 10));
//
//        // for (final Future<?> f : futures) {
//        // try {
//        // f.get();
//        // }
//        // catch (InterruptedException | ExecutionException e) {
//        // JavaParserTest.LOG.warn(e.getMessage(), e);
//        // continue;
//        // }
//        // }
//        // executor.shutdown();
//
//        // model.printTree();
//
//        // Project.out.println("Took: " + (end - start) + " ms");
//        // Project.out.println("Avg: " + ((end - start) / fileNames.size()) + "
//        // ms");
//    }
//
//    public static void utilizeParser(final String file, final DataModelMediator tree)
//    {
//        try
//        {
//            // TODO Make this code specific to subclasses
//            final File node = File.builder().key(file).create();
//            tree.getSystem().addChild(node);
//
//            final Java8Parser parser = JavaParserTest.loadFile(file);
//            final Java8Parser.CompilationUnitContext cuContext = parser.compilationUnit();
//            final ParseTreeWalker walker = new ParseTreeWalker();
//            tree.getSystem().addChild(node);
////            final JavaModelBuilder listener = new JavaModelBuilder(node);
////            walker.walk(listener, cuContext);
////            walker.walk(listener, cuContext);
//        }
//        catch (final IOException e)
//        {
//            LOG.warn(e.getMessage(), e);
//        }
//    }
}
