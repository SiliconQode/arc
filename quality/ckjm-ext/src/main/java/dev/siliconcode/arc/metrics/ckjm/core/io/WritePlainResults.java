package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintWriter;

/**
 * Simple plain text output formatter
 * @author Isaac Griffith
 * @version 2.5.0
 */
public class WritePlainResults implements CkjmOutputHandler {
    private PrintWriter pw;

    public WritePlainResults (PrintWriter pw) {
        this.pw = pw;
    }

    public void handleClass(String name, ClassMetrics c) {
        pw.println(name + " " + c.toString());
    }

    public void close() {
      if (pw != null) {
        pw.close();
      }
    }
}
