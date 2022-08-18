package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public class PrintJsonResults implements CkjmOutputHandler {

   private PrintStream p;
   private static String endl = System.getProperty("line.separator");

   public PrintJsonResults(PrintStream p) {
     this.p = p;
   }

   public void printHeader() {
     p.println("\"classes\": [");
   }

   public void printFooter() {
     p.println("]");
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
     p.println("  {");
     p.println("    \"name\": "  + name         + ",");
     p.println("    \"wmc\": "   + c.getWmc()   + ",");
     p.println("    \"dit\": "   + c.getDit()   + ",");
     p.println("    \"noc\": "   + c.getNoc()   + ",");
     p.println("    \"cbo\": "   + c.getCbo()   + ",");
     p.println("    \"rfc\": "   + c.getRfc()   + ",");
     p.println("    \"lcom\": "  + c.getLcom()  + ",");
     p.println("    \"ca\": "    + c.getCa()    + ",");
     p.println("    \"ce\": "    + c.getCe()    + ",");
     p.println("    \"npm\": "   + c.getNpm()   + ",");
     p.println("    \"lcom3\": " + c.getLcom3() + ",");
     p.println("    \"loc\": "   + c.getLoc()   + ",");
     p.println("    \"dam\": "   + c.getDam()   + ",");
     p.println("    \"moa\": "   + c.getMoa()   + ",");
     p.println("    \"mfa\": "   + c.getMfa()   + ",");
     p.println("    \"cam\": "   + c.getCam()   + ",");
     p.println("    \"ic\": "    + c.getIc()    + ",");
     p.println("    \"cbm\": "   + c.getCbm()   + ",");
     p.println("    \"amc\": "   + c.getAmc()   + ",");
     p.println(printJsonCC(c));
     p.println("  },");
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
     if (p != null) {
       p.close();
     }
   }
}
