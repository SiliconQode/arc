/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.quality.quamoco.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException;

/**
 * The class <code>RootNotFoundExceptionTest</code> contains tests for the class
 * <code>{@link RootNotFoundException}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RootNotFoundExceptionTest {

	/**
	 * Run the RootNotFoundException() constructor test.
	 */
	@Test
	public void testRootNotFoundException_1() {

		final RootNotFoundException result = new RootNotFoundException();

		// TODO: add additional test code here
		Assert.assertNotNull(result);
		Assert.assertNull(result.getCause());
		Assert.assertEquals("dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException", result.toString());
		Assert.assertNull(result.getMessage());
		Assert.assertNull(result.getLocalizedMessage());
	}

	/**
	 * Run the RootNotFoundException(String) constructor test.
	 */
	@Test
	public void testRootNotFoundException_2() {
		final String message = "";

		final RootNotFoundException result = new RootNotFoundException(message);

		// TODO: add additional test code here
		Assert.assertNotNull(result);
		Assert.assertNull(result.getCause());
		Assert.assertEquals("dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException: ", result.toString());
		Assert.assertEquals("", result.getMessage());
		Assert.assertEquals("", result.getLocalizedMessage());
	}

	/**
	 * Run the RootNotFoundException(Throwable) constructor test.
	 */
	@Test
	public void testRootNotFoundException_3() {
		final Throwable cause = new Throwable();

		final RootNotFoundException result = new RootNotFoundException(cause);

		// TODO: add additional test code here
		Assert.assertNotNull(result);
		Assert.assertEquals("dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException: java.lang.Throwable", result.toString());
		Assert.assertEquals("java.lang.Throwable", result.getMessage());
		Assert.assertEquals("java.lang.Throwable", result.getLocalizedMessage());
	}

	/**
	 * Run the RootNotFoundException(String,Throwable) constructor test.
	 */
	@Test
	public void testRootNotFoundException_4() {
		final String message = "";
		final Throwable cause = new Throwable();

		final RootNotFoundException result = new RootNotFoundException(message, cause);

		// TODO: add additional test code here
		Assert.assertNotNull(result);
		Assert.assertEquals("dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException: ", result.toString());
		Assert.assertEquals("", result.getMessage());
		Assert.assertEquals("", result.getLocalizedMessage());
	}

	/**
	 * Run the RootNotFoundException(String,Throwable,boolean,boolean)
	 * constructor test.
	 */
	@Test
	public void testRootNotFoundException_5() {
		final String message = "";
		final Throwable cause = new Throwable();
		final boolean enableSuppression = true;
		final boolean writableStackTrace = true;

		final RootNotFoundException result = new RootNotFoundException(message, cause, enableSuppression,
				writableStackTrace);

		// TODO: add additional test code here
		Assert.assertNotNull(result);
		Assert.assertEquals("dev.siliconcode.arc.quality.quamoco.io.RootNotFoundException: ", result.toString());
		Assert.assertEquals("", result.getMessage());
		Assert.assertEquals("", result.getLocalizedMessage());
	}
}
