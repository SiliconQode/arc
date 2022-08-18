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

/**
 * Simple plain text output formatter
 * @author Julien Rentrop
 * @version 2.5.0
 */
public class PrintPlainResults implements CkjmOutputHandler {
    private PrintStream p;

    public PrintPlainResults (PrintStream p) {
        this.p = p;
    }

    public void handleClass(String name, ClassMetrics c) {
        p.println(name + " " + c.toString());
    }

    public void close() {
      if (p != null) {
        p.close();
      }
    }
}
