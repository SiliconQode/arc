package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * XML output formatter
 *
 * @author Isaac Griffith
 * @version 2.5.0
 */
public class WriteXmlResults implements CkjmOutputHandler {
    private PrintWriter pw;
    private static String endl = System.getProperty("line.separator");

    public WriteXmlResults(PrintWriter pw) {
        this.pw = pw;
        printHeader();
    }

    public void printHeader() {
        pw.println("<?xml version=\"1.0\"?>");
        pw.println("<ckjm>");
    }

    public void handleClass(String name, ClassMetrics c) {
        pw.println("  <class>");
        pw.println("    <name>" + name + "</name>");
        pw.println("    <wmc>" + c.getWmc() + "</wmc>");
        pw.println("    <dit>" + c.getDit() + "</dit>");
        pw.println("    <noc>" + c.getNoc() + "</noc>");
        pw.println("    <cbo>" + c.getCbo() + "</cbo>");
        pw.println("    <rfc>" + c.getRfc() + "</rfc>");
        pw.println("    <lcom>" + c.getLcom() + "</lcom>");
        pw.println("    <ca>" + c.getCa() + "</ca>");
        pw.println("    <ce>" + c.getCe() + "</ce>");
        pw.println("    <npm>" + c.getNpm() + "</npm>");
        pw.println("    <lcom3>" + c.getLcom3() + "</lcom3>");
        pw.println("    <loc>" + c.getLoc() + "</loc>");
        pw.println("    <dam>" + c.getDam() + "</dam>");
        pw.println("    <moa>" + c.getMoa() + "</moa>");
        pw.println("    <mfa>" + c.getMfa() + "</mfa>");
        pw.println("    <cam>" + c.getCam() + "</cam>");
        pw.println("    <ic>" + c.getIc() + "</ic>");
        pw.println("    <cbm>" + c.getCbm() + "</cbm>");
        pw.println("    <amc>" + c.getAmc() + "</amc>");
        pw.println(printXmlCC(c));
        pw.println("  </class>");
    }

    public void printFooter() {
        pw.println("</ckjm>");
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
      if (pw != null) {
        pw.close();
      }
    }
}
