package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public class WriteJsonResults implements CkjmOutputHandler {

   private PrintWriter pw;
   private static String endl = System.getProperty("line.separator");

   public WriteJsonResults(PrintWriter pw) {
     this.pw = pw;
   }

   public void printHeader() {
     pw.println("\"classes\": [");
   }

   public void printFooter() {
     pw.println("]");
   }

   /**
    * {
    *   classes: [
    *     {
    *       name: <name>,
    *       wmc:  <wmc>,
    *     }
    *   ]
    * }
    */
   public void handleClass(String name, ClassMetrics c) {
     pw.println("  {");
     pw.println("    \"name\": "  + name         + ",");
     pw.println("    \"wmc\": "   + c.getWmc()   + ",");
     pw.println("    \"dit\": "   + c.getDit()   + ",");
     pw.println("    \"noc\": "   + c.getNoc()   + ",");
     pw.println("    \"cbo\": "   + c.getCbo()   + ",");
     pw.println("    \"rfc\": "   + c.getRfc()   + ",");
     pw.println("    \"lcom\": "  + c.getLcom()  + ",");
     pw.println("    \"ca\": "    + c.getCa()    + ",");
     pw.println("    \"ce\": "    + c.getCe()    + ",");
     pw.println("    \"npm\": "   + c.getNpm()   + ",");
     pw.println("    \"lcom3\": " + c.getLcom3() + ",");
     pw.println("    \"loc\": "   + c.getLoc()   + ",");
     pw.println("    \"dam\": "   + c.getDam()   + ",");
     pw.println("    \"moa\": "   + c.getMoa()   + ",");
     pw.println("    \"mfa\": "   + c.getMfa()   + ",");
     pw.println("    \"cam\": "   + c.getCam()   + ",");
     pw.println("    \"ic\": "    + c.getIc()    + ",");
     pw.println("    \"cbm\": "   + c.getCbm()   + ",");
     pw.println("    \"amc\": "   + c.getAmc()   + ",");
     pw.println(printJsonCC(c));
     pw.println("  },");
   }

   private String printJsonCC(ClassMetrics cm) {
       StringBuilder jsonCC = new StringBuilder();
       List<String> methodNames = cm.getMethodNames();
       Iterator<String> itr = methodNames.iterator();
       String name;

       jsonCC.append("    \"cc\": [").append(endl);
       // <method name="private static java.util.List readData()">1</method>
       while (itr.hasNext()) {
           name = itr.next();
           jsonCC.append(String.format("      { \"method_name\": \"%s\", ", name.replaceAll("<|>", "_")));
           jsonCC.append("\"cc\": " + cm.getCC(name));
           jsonCC.append(" },").append(endl);
       }
       jsonCC.append("    ]");

       return jsonCC.toString();
   }

   public void close() {
     printFooter();
     if (pw != null) {
       pw.close();
     }
   }
}
