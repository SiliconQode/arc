/**
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
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
package com.empirilytics.arc.parsers.java;

import com.empirilytics.arc.datamodel.*;
import com.empirilytics.arc.datamodel.Module;
import com.empirilytics.arc.datamodel.System;
import com.empirilytics.arc.datamodel.util.DBCredentials;
import com.empirilytics.arc.datamodel.util.DBManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PackagesTest extends BaseTestClass {

    public String getBasePath() {
        return "data/java-example-project/Packages";
    }

    @Test
    public void testPackages() {
        DBManager.getInstance().open(credentials);
        List<Namespace> namespaces = proj.getNamespaces();

        assertEquals(3, namespaces.size());
        for (Namespace n : namespaces) {
            if (n.getName().isEmpty())
                continue;
            assertEquals(2, n.getAllTypes().size());
            assertEquals(2, n.getFiles().size());
        }
        DBManager.getInstance().close();
    }

    @Test
    public void testPackageOuter() {
        DBManager.getInstance().open(credentials);
        Namespace outer = proj.findNamespace("outer");
        assertNotNull(outer);
        assertEquals(1, outer.getNamespaces().size());
        DBManager.getInstance().close();
    }

    @Test
    public void testPackageInner() {

    }
}
