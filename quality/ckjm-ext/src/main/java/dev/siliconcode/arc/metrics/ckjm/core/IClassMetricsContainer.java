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

/**
 * @author mariam
 * @version 2.5.0
 */
public interface IClassMetricsContainer{
    public ClassMetrics getMetrics(String name);
    public void printMetrics(CkjmOutputHandler handler);
}
