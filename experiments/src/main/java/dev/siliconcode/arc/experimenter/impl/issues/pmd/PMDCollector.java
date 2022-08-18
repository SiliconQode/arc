/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
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
package dev.siliconcode.arc.experimenter.impl.issues.pmd;

import dev.siliconcode.arc.datamodel.*;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.collector.FileCollector;
import dev.siliconcode.arc.experimenter.impl.issues.pmd.resultsdm.Pmd;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class PMDCollector extends FileCollector {

    PMDTool owner;

    @Builder(buildMethodName = "create")
    public PMDCollector(PMDTool owner, String resultsFile) {
        super(PMDConstants.PMD_COLL_NAME, resultsFile);
        this.owner = owner;
    }

    @Override
    public void execute(ArcContext ctx) {
        log.info("Starting Collecting PMD Results");

        this.project = ctx.getProject();
        try {
            JAXBContext context = JAXBContext.newInstance(Pmd.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Pmd pmd = (Pmd) unmarshaller.unmarshal(new java.io.File(resultsFile));
            pmd.getFile().forEach(file ->
                file.getViolation().forEach(v -> {
                    ctx.open();
                    Namespace ns = ctx.getProject().findNamespace(v.getPackage());
                    Rule rule = Rule.findFirst("ruleKey = ?", "pmd:" + v.getRule());
                    Type t = ns.getTypeByName(v.getClazz());
                    if (t != null) {
                        if (v.getMethod() != null) {
                            Method m = t.getMethodWithName(v.getMethod());
                            Finding finding = Finding.of(rule.getKey()).on(m);
                            rule.addFinding(finding);
                        }
                        else {
                            Finding finding = Finding.of(rule.getKey()).on(t);
                            rule.addFinding(finding);
                        }
                    }
                    ctx.close();
                })
            );
        } catch (JAXBException e) {
            log.atError().withThrowable(e).log(e.getMessage());
        }

        log.info("Finished Collecting PMD Results");
    }

    public static void main(String args[]) {
//        new PMDCollector("/home/git/msusel/msusel-patterns-experimenter/data/pmdresults/pmdreport.xml").execute(new ArcContext());
    }
}
