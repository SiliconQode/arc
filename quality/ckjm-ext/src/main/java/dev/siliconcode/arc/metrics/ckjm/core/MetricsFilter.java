/*
 * (C) Copyright 2005 Diomidis Spinellis
 *
 * Permission to use, copy, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in
 * supporting documentation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package dev.siliconcode.arc.metrics.ckjm.core;

import dev.siliconcode.arc.metrics.ckjm.core.io.CkjmOutputHandler;
import dev.siliconcode.arc.metrics.ckjm.core.io.PrintXmlResults;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * Convert a list of classes into their metrics.
 * Process standard input lines or command line arguments
 * containing a class file name or a jar file name,
 * followed by a space and a class file name.
 * Display on the standard output the name of each class, followed by its
 * six Chidamber Kemerer metrics:
 * TODO: The list of metrics is constantly being changed...
 * WMC, DIT, NOC, CBO, RFC, LCOM, Ca, *Ce*,  NPM, LCOM3, CC
 *
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 * @version 2.5.0
 * @see ClassMetrics
 */
@Log4j2
public class MetricsFilter implements ICountingProperities {
    /**
     * True if the measurements should include calls to the Java JDK into account
     */
    private boolean mIncludeJdk = false;

    /**
     * True if the reports should only include public classes
     */
    private boolean mOnlyPublic = false;
    /**
     * The same instance of MoaClassVisitor must be used to process all class, so it must be a class field.
     */
    private MoaClassVisitor mMoaVisitor;
    /**
     *
     */
    private IClassMetricsContainer mMetricsContainer;

    public MetricsFilter(boolean includeJdk, boolean onlyPublic) {
        this.mIncludeJdk = includeJdk;
        this.mOnlyPublic = onlyPublic;
        mMetricsContainer = new ClassMetricsContainer(this);
        mMoaVisitor = new MoaClassVisitor(mMetricsContainer);
    }

    /**
     * Return true if the measurements should include calls to the Java JDK into account
     */
    public boolean isJdkIncluded() {
        return mIncludeJdk;
    }

    /**
     * Return true if the measurements should include all classes
     */
    public boolean includeAll() {
        return !mOnlyPublic;
    }

    /**
     * Load and parse the specified class.
     * The class specification can be either a class file name, or
     * a jarfile, followed by space, followed by a class file name.
     */
    void processClass(Path clspec) {
        int spc;
        JavaClass jc = null;

        if (clspec.toAbsolutePath().toString().toLowerCase().endsWith(".jar")) {
            JarFile jf;
            try {
                jf = new JarFile(clspec.toAbsolutePath().toString());
                Enumeration<JarEntry> entries = jf.entries();

                while (entries.hasMoreElements()) {
                    String cl = entries.nextElement().getName();
                    if (cl.toLowerCase().endsWith(".class")) {
                        try {
                            jc = new ClassParser(clspec.toAbsolutePath().toString(), cl).parse();
                            processClass(jc);
                        } catch (IOException e) {
                            log.error("Error loading " + cl + " from " + clspec + ": " + e);
                        }
                    }
                }
            } catch (IOException ex) {
                log.error("Unable to load jar file " + clspec, ex);
            }

        } else {
            try {
                jc = new ClassParser(clspec.toAbsolutePath().toString()).parse();
                processClass(jc);
            } catch (IOException e) {
                log.error("Error loading " + clspec + ": " + e);
            }
        }
    }

    private void processClass(JavaClass jc) {
        if (jc != null) {
            ClassVisitor visitor = new ClassVisitor(jc, mMetricsContainer, this);
            visitor.start();
            visitor.end();
            LocClassVisitor locVisitor = new LocClassVisitor(mMetricsContainer);
            locVisitor.visitJavaClass(jc);
            DamClassVisitor damVisitor = new DamClassVisitor(jc, mMetricsContainer);
            damVisitor.visitJavaClass(jc);
            mMoaVisitor.visitJavaClass(jc);
            MfaClassVisitor mfaVisitor = new MfaClassVisitor(mMetricsContainer);
            mfaVisitor.visitJavaClass(jc);
            CamClassVisitor camVisitor = new CamClassVisitor(mMetricsContainer);
            camVisitor.visitJavaClass(jc);
            IcAndCbmClassVisitor icVisitor = new IcAndCbmClassVisitor(mMetricsContainer);
            icVisitor.visitJavaClass(jc);
            AmcClassVisitor amcVisitor = new AmcClassVisitor(mMetricsContainer);
            amcVisitor.visitJavaClass(jc);
        }
    }

    /**
     * The interface for other Java based applications.
     * Implement the outputhandler to catch the results
     *
     * @param files         Class files to be analyzed
     * @param outputHandler An implementation of the CkjmOutputHandler interface
     */
    public void runMetricsInternal(List<Path> files, CkjmOutputHandler consoleHandler, CkjmOutputHandler fileHandler) {

        files.forEach(this::processClass);

        mMoaVisitor.end();
        if (consoleHandler != null)
          mMetricsContainer.printMetrics(consoleHandler);
        if (fileHandler != null)
          mMetricsContainer.printMetrics(fileHandler);
    }


}
