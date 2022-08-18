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
package dev.siliconcode.arc.experimenter.impl.issues.findbugs;

import com.google.common.collect.Lists;
import dev.siliconcode.arc.datamodel.*;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.collector.FileCollector;
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.resultsdm.BugCollection;
import dev.siliconcode.arc.experimenter.impl.issues.findbugs.resultsdm.SourceLine;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class FindBugsCollector extends FileCollector {

    FindBugsTool owner;

    @Builder(buildMethodName = "create")
    public FindBugsCollector(FindBugsTool owner, String resultsFile) {
        super(FindBugsConstants.FB_COLL_NAME, resultsFile);
        this.owner = owner;
    }

    @Override
    public void execute(ArcContext ctx) {
        log.info("Started collecting FindBugs Results");

        this.project = ctx.getProject();
        try {
            JAXBContext context = JAXBContext.newInstance(BugCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            BugCollection bugColl = (BugCollection) unmarshaller.unmarshal(new File(resultsFile));
            log.info("Instances Found: " + bugColl.getBugInstance().size());

            List<Finding> findings = Lists.newArrayList();
            bugColl.getBugInstance().forEach(inst -> {
                ctx.open();
                Rule rule = Rule.findFirst("name = ?", inst.getType());
                ctx.close();
                if (rule != null) {
                    ctx.open();
                    log.info("Rule: " + rule.getKey());
                                        ctx.close();

                    inst.getClazzOrTypeOrMethod().forEach(obj -> {
                        Finding finding = null;
                        if (obj instanceof BugCollection.BugInstance.Class) {
                            BugCollection.BugInstance.Class clazz = (BugCollection.BugInstance.Class) obj;
                            ctx.open();
                            Type type = project.findTypeByQualifiedName(clazz.getClassname());
                            if (!rule.hasFindingOn(type))
                                finding = Finding.of(rule.getKey());
                            ctx.close();
                            setReferenceAndLineInfo(ctx, finding, type, clazz.getSourceLine());
                            if (finding != null)
                                findings.add(finding);
                        } else if (obj instanceof BugCollection.BugInstance.Method) {
                            BugCollection.BugInstance.Method meth = (BugCollection.BugInstance.Method) obj;
                            ctx.open();
                            Type type = project.findTypeByQualifiedName(meth.getClassname()); // TODO Verify it finds the component, also <init> maps to constructor
                            if (type != null) {
                                Method method = type.getMethodWithName(meth.getName());
                                Member member = type.findMemberInRange(meth.getSourceLine().getStart(), meth.getSourceLine().getEnd());
                                if (!rule.hasFindingOn(method))
                                    finding = Finding.of(rule.getKey());
                                ctx.close();
                                setReferenceAndLineInfo(ctx, finding, method, meth.getSourceLine());
                                if (finding != null)
                                    findings.add(finding);
                            }
                        }
                    });
                }
            });

            log.info("Findings Created: " + findings.size());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        log.info("Finished collecting FindBugs Results");
    }

    public void setReferenceAndLineInfo(ArcContext ctx, Finding finding, Component comp, SourceLine line) {
        if (finding != null && comp != null) {
            ctx.open();
            finding.on(comp);
            ctx.close();
            setStartAndEnd(ctx, finding, line);
        }
    }

    public void setStartAndEnd(ArcContext ctx, Finding finding, SourceLine line) {
        if (line != null) {
            if (line.getStart() != null) {
                ctx.open();
                finding.setStart(line.getStart());
                ctx.close();
            }
            if (line.getEnd() != null) {
                ctx.open();
                finding.setEnd(line.getEnd());
                ctx.close();
            }
        }
    }

    public static void main(String args[]) {
        //new FindBugsCollector("/home/git/msusel/msusel-patterns-experimenter/data/fbresults/fbresults.xml").execute(null);
    }
}
