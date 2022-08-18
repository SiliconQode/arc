package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.io.PrintStream;

/**
 * @author Isaac Griffith
 * @version 2.5.0
 */
public class NullOutputHandler implements CkjmOutputHandler {

    public void handleClass(String name, ClassMetrics c) {
    }

    public void close() {
    }
}
