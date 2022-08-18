/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class ProjectSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Project proj = new Project();
        a(proj).shouldBe("valid");
//        //a(proj.errors().get("author")).shouldBeEqual("Author must be provided");
        proj.set("projKey", "proj", "name", "proj", "version", "1.0");
        a(proj).shouldBe("valid");
        proj.save();
        proj = (Project) Project.findAll().get(0);
        a(proj.getId()).shouldNotBeNull();
        a(proj.get("name")).shouldBeEqual("proj");
        a(proj.get("projKey")).shouldBeEqual("proj");
        a(proj.get("version")).shouldBeEqual("1.0");
        a(Project.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddPatternInstance() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        p.addPatternInstance(inst);

        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePatternInstance() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        p.addPatternInstance(inst);

        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removePatternInstance(inst);
        a(p.getAll(PatternInstance.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddMeasure() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        p.addMeasure(meas);

        a(p.getAll(Measure.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveMeasure() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        p.addMeasure(meas);

        a(p.getAll(Measure.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeMeasure(meas);
        a(p.getAll(Measure.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddFinding() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Finding finding = Finding.createIt("findingKey", "finding");
        p.addFinding(finding);

        a(p.getAll(Finding.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveFinding() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Finding finding = Finding.createIt("findingKey", "finding");
        p.addFinding(finding);

        a(p.getAll(Finding.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeFinding(finding);
        a(p.getAll(Finding.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddRelation() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Relation rel = Relation.createIt("relKey", "rel");
        p.addRelation(rel);

        a(p.getAll(Relation.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveRelation() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Relation rel = Relation.createIt("relKey", "rel");
        p.addRelation(rel);

        a(p.getAll(Relation.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeRelation(rel);
        a(p.getAll(Relation.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canSetSCM() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        p.addSCM(scm);

        a(p.getAll(SCM.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canUnSetSCM() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        p.addSCM(scm);

        a(p.getAll(SCM.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeSCM(scm);
        a(p.getAll(SCM.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddModule() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        p.addModule(module);

        a(p.getAll(Module.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveModule() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        p.addModule(module);

        a(p.getAll(Module.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeModule(module);
        a(p.getAll(Module.class).size()).shouldBeEqual(0);
    }

    @Test
    public void canAddLanguage() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Language lang = Language.createIt("name", "lang");
        p.addLanguage(lang);

        a(p.getAll(Language.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveLanguage() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Language lang = Language.createIt("name", "lang");
        p.addLanguage(lang);

        a(p.getAll(Language.class).size()).shouldBeEqual(1);
        p = (Project) Project.findAll().get(0);
        p.removeLanguage(lang);
        a(p.getAll(Language.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Project p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        scm.setType(SCMType.GIT);
        Relation rel = Relation.createIt("relKey", "rel");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        Finding finding = Finding.createIt("findingKey", "finding");
        Language lang = Language.createIt("name", "lang");

        p.addLanguage(lang);
        p.addModule(module);
        p.addSCM(scm);
        p.addRelation(rel);
        p.addPatternInstance(inst);
        p.addMeasure(meas);
        p.addFinding(finding);

        a(Module.count()).shouldBeEqual(1);
        a(SCM.count()).shouldBeEqual(1);
        a(Relation.count()).shouldBeEqual(1);
        a(PatternInstance.count()).shouldBeEqual(1);
        a(Measure.count()).shouldBeEqual(1);
        a(Finding.count()).shouldBeEqual(1);
        a(Language.count()).shouldBeEqual(1);
        a(ProjectsLanguages.count()).shouldBeEqual(1);
        p.delete(true);
        a(Module.count()).shouldBeEqual(0);
        a(SCM.count()).shouldBeEqual(0);
        a(Relation.count()).shouldBeEqual(0);
        a(PatternInstance.count()).shouldBeEqual(0);
        a(Measure.count()).shouldBeEqual(0);
        a(Finding.count()).shouldBeEqual(0);
        a(ProjectsLanguages.count()).shouldBeEqual(0);
        a(Language.count()).shouldBeEqual(0);
    }
}
