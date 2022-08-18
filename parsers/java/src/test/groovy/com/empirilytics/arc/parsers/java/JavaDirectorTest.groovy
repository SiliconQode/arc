/*
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
package com.empirilytics.arc.parsers.java


import com.empirilytics.arc.datamodel.File
import com.empirilytics.arc.datamodel.Module
import com.empirilytics.arc.datamodel.Project
import com.empirilytics.arc.datamodel.System
import com.empirilytics.arc.datamodel.util.DBCredentials
import com.empirilytics.arc.datamodel.util.DBManager
import groovy.util.logging.Log4j2
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.empirilytics.arc.parsers.*

class JavaDirectorTest {

    JavaDirector fixture
    List<File> files = []
    String basePath = "data/example-classes"
    DBCredentials creds

    @Before
    void setUp() throws Exception {
        creds = DBCredentials.builder().type("sqlite").user("dev1").pass("passwd").url("jdbc:sqlite:data/test.db").driver("org.sqlite.JDBC").create()
        DBManager.instance.createDatabase(creds)

        DBManager.instance.open(creds)
        System  sys   = System.builder().name("Test").key("test").basePath(basePath).create()
        Project proj  = Project.builder().name("test").projKey("test").relPath("").create()
        Module  mod   = Module.builder().name("default").moduleKey("default").relPath("").create()
        proj.addModule(mod)
        sys.addProject(proj)

        files << File.builder().name("$basePath/AllInOne7.java").fileKey("$basePath/AllInOne7.java").relPath("AllInOne7.java").create()
        files << File.builder().name("$basePath/AllInOne8.java").fileKey("$basePath/AllInOne8.java").relPath("AllInOne8.java").create()
        DBManager.instance.close()

        fixture = new JavaDirector(proj, creds)
    }

    @After
    void tearDown() throws Exception {
        DBManager.instance.open(creds)
        DBManager.instance.rollback()
        DBManager.instance.close()
    }

    @Test
    void build() {

    }

    @Test
    void identify() {
    }

    @Test
    void process() {
//        DBManager.instance.open(creds)
        fixture.process(files)
//        DBManager.instance.close()
    }

    @Test
    void onFiles() {
    }

    @Test
    void gatherTypeAssociations() {
    }

    @Test
    void gatherFileAndTypeInfo() {
    }

    @Test
    void gatherMembersAndBasicRelationInfo() {
    }

    @Test
    void gatherMemberUsageInfo() {
    }

    @Test
    void gatherStatementInfo() {
    }

    @Test
    void includeFile() {
    }

    @Test
    void utilizeParser() {
    }
}
