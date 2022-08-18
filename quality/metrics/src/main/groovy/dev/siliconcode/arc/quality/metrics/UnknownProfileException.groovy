/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
package dev.siliconcode.arc.quality.metrics

/**
 * An exception that occurs when attempting to process a file or selection of
 * text when no known profile has been loaded.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class UnknownProfileException extends Exception {

    /**
     * Constructs a new UnknownProfileException with no message.
     */
    UnknownProfileException()
    {
        super()
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructs a new UnknownProfileException with the given message, cause,
     * capability for suppression, and whether or not to write to the stacktrace
     *
     * @param message
     *            The message
     * @param cause
     *            The originating cause
     * @param enableSuppression
     *            Flag enabling/disabling suppression
     * @param writableStackTrace
     *            Flag enabling stack trace
     */
    UnknownProfileException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace)
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructs a new UnknownProfileException with the given message and
     * cause.
     *
     * @param message
     *            The message
     * @param cause
     *            The originating cause
     */
    UnknownProfileException(String message, Throwable cause)
    {
        super(message, cause)
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructs a new UnknownProfileException with the given message
     *
     * @param message
     *            The message
     */
    UnknownProfileException(String message)
    {
        super(message)
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructs a new UnknownProfileException with the given cause.
     *
     * @param cause
     *            The originating cause
     */
    UnknownProfileException(Throwable cause)
    {
        super(cause)
        // TODO Auto-generated constructor stub
    }

}
