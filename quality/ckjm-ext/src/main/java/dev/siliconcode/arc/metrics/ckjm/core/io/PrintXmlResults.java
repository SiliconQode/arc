/*
 * (C) Copyright 2005 Diomidis Spinellis, Julien Rentrop
 *
 * Permission to use, copy, and distribute this software and its documentation
 * for any purpose and without fee is hereby granted, provided that the above
 * copyright notice appear in all copies and that both that copyright notice and
 * this permission notice appear in supporting documentation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * XML output formatter
 *
 * @author Julien Rentrop
 * @version 2.5.0
 */
public class PrintXmlResults implements CkjmOutputHandler {
    private PrintStream p;
    private static String endl = System.getProperty("line.separator");

    public PrintXmlResults(PrintStream p) {
        this.p = p;
        printHeader();
    }

    public void printHeader() {
        p.println("<?xml version=\"1.0\"?>");
        p.println("<ckjm>");
    }

    public void handleClass(String name, ClassMetrics c) {
        p.println("  <class>");
        p.println("    <name>" + name + "</name>");
        p.println("    <wmc>" + c.getWmc() + "</wmc>");
        p.println("    <dit>" + c.getDit() + "</dit>");
        p.println("    <noc>" + c.getNoc() + "</noc>");
        p.println("    <cbo>" + c.getCbo() + "</cbo>");
        p.println("    <rfc>" + c.getRfc() + "</rfc>");
        p.println("    <lcom>" + c.getLcom() + "</lcom>");
        p.println("    <ca>" + c.getCa() + "</ca>");
        p.println("    <ce>" + c.getCe() + "</ce>");
        p.println("    <npm>" + c.getNpm() + "</npm>");
        p.println("    <lcom3>" + c.getLcom3() + "</lcom3>");
        p.println("    <loc>" + c.getLoc() + "</loc>");
        p.println("    <dam>" + c.getDam() + "</dam>");
        p.println("    <moa>" + c.getMoa() + "</moa>");
        p.println("    <mfa>" + c.getMfa() + "</mfa>");
        p.println("    <cam>" + c.getCam() + "</cam>");
        p.println("    <ic>" + c.getIc() + "</ic>");
        p.println("    <cbm>" + c.getCbm() + "</cbm>");
        p.println("    <amc>" + c.getAmc() + "</amc>");
        p.println(printXmlCC(c));
        p.println("  </class>");
    }

    public void printFooter() {
        p.println("</ckjm>");
    }

    private String printXmlCC(ClassMetrics cm) {
        StringBuilder xmlCC = new StringBuilder();
        List<String> methodNames = cm.getMethodNames();
        Iterator<String> itr = methodNames.iterator();
        String name;

        xmlCC.append("    <cc>").append(endl);
        while (itr.hasNext()) {
            name = itr.next();
            xmlCC.append(String.format("      <method name=\"%s\">", name.replaceAll("<|>", "_")));
            xmlCC.append(cm.getCC(name));
            xmlCC.append("</method>").append(endl);
        }
        xmlCC.append("    </cc>");

        return xmlCC.toString();
    }

    public void close() {
      printFooter();
      if (p != null) {
        p.close();
      }
    }
}
