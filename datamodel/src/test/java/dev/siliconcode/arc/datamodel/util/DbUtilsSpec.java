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
package dev.siliconcode.arc.datamodel.util;

import com.google.common.collect.Lists;
import dev.siliconcode.arc.datamodel.Module;
import dev.siliconcode.arc.datamodel.System;
import dev.siliconcode.arc.datamodel.*;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

import java.util.List;

public class DbUtilsSpec extends DBSpec {

    private System system;
    private PatternRepository patternRepo;
    private MetricRepository metricRepo;
    private RuleRepository ruleRepo;
    private Type type;
    private Finding finding;
    private Measure meas;
    private Role role;
    private PatternInstance instance;
    private Project p;

    private void populateDb() {
        system = System.createIt("name", "fake name", "sysKey", "fake key");
        p = Project.createIt("name", "fake project", "projKey", "fake key", "version", "fake version");
        SCM scm = SCM.createIt("scmKey", "scm", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        SCM scm2 = SCM.createIt("scmKey", "scm2", "tag", "1.0", "branch", "dev", "url", "git@git.somewhere.com/what");
        Language lang = Language.createIt("name", "lang");
        Module module = Module.createIt("moduleKey", "module", "name", "module");
        Module module2 = Module.createIt("moduleKey", "module2", "name", "module");
        Namespace ns = Namespace.createIt("nsKey", "ns", "name", "ns");
        Namespace ns2 = Namespace.createIt("nsKey", "ns2", "name", "ns");
        File file = File.createIt("fileKey", "fileKey", "name", "file", "pathIndex", 0);
        File file2 = File.createIt("fileKey", "fileKey2", "name", "file", "pathIndex", 0);
        Import imp = Import.createIt("name", "imp");
        Import imp2 = Import.createIt("name", "imp2");
        type = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass", "type", Type.CLASS);
        type.setAccessibility(Accessibility.PUBLIC);
        Type type2 = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass2", "type", Type.ENUM);
        type2.setAccessibility(Accessibility.PUBLIC);
        Type type3 = Type.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass3", "type", Type.INTERFACE);
        type3.setAccessibility(Accessibility.PUBLIC);
        Member member = Literal.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member.setAccessibility(Accessibility.PUBLIC);
        member.addModifier(Modifier.Values.STATIC.name());
        Member member2 = Initializer.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        member2.setAccessibility(Accessibility.PUBLIC);
        member2.addModifier(Modifier.Values.STATIC.name());
        Method method = Method.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        method.setAccessibility(Accessibility.PUBLIC);
        Method method2 = Constructor.createIt("name", "TestClass2", "start", 1, "end", 100, "compKey", "TestClass");
        method2.setAccessibility(Accessibility.PUBLIC);
        Method method3 = Destructor.createIt("name", "TestClass3", "start", 1, "end", 100, "compKey", "TestClass");
        method3.setAccessibility(Accessibility.PUBLIC);
        Parameter param = Parameter.builder().name("param").create();
        Parameter param2 = Parameter.builder().name("param").create();
        Parameter param3 = Parameter.builder().name("param").create();
        Field field = Field.createIt("name", "TestClass", "start", 1, "end", 100, "compKey", "TestClass");
        field.setAccessibility(Accessibility.PUBLIC);
        TypeRef typeRef = TypeRef.createIt("typeName", "typeRef", "dimensions", null);
        typeRef.setType(TypeRefType.Type);
        metricRepo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
        meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        ruleRepo = RuleRepository.createIt("repoKey", "key", "name", "name");
        Rule rule = Rule.createIt("ruleKey", "fake key", "name", "fake name", "description", "fake content", "rule_repository_id", 1);
        rule.setPriority(Priority.HIGH);
        Tag tag = Tag.createIt("tag", "Tag");
        finding = Finding.createIt("findingKey", "finding");
        patternRepo = PatternRepository.createIt("repoKey", "repo", "name", "repo");
        PatternChain chain = PatternChain.createIt("chainKey", "chain");
        instance = PatternInstance.createIt("instKey", "inst");
        RoleBinding binding = RoleBinding.createIt();
        Pattern pattern = Pattern.createIt("patternKey", "pattern", "name", "pattern");
        role = Role.createIt("roleKey", "role", "name", "role");
        Reference ref = Reference.createIt("refKey", "ref");
        Relation rel = Relation.createIt("relKey", "rel");
        Reference to = Reference.createIt("refKey", "to");
        to.setType(RefType.TYPE);
        Reference from = Reference.createIt("refKey", "from");
        from.setType(RefType.TYPE);

        system.addProject(p);
        p.addModule(module);
        p.addModule(module2);
        p.addSCM(scm);
        p.addSCM(scm2);
        p.addLanguage(lang);
        p.addFinding(finding);
        p.addMeasure(meas);
        p.addRelation(rel);
        module.addNamespace(ns);
        module.addNamespace(ns2);
        ns.addFile(file);
        ns.addFile(file2);
        file.addImport(imp);
        file.addImport(imp2);
        file.addType(type);
        file.addType(type2);
        file.addType(type3);
        type2.addMember(member);
        type.addMember(member2);
        type.addMember(method);
        type.addMember(method2);
        type.addMember(method3);
        method.addParameter(param);
        method2.addParameter(param2);
        method3.addParameter(param3);
        type.addMember(field);
        method.setReturnType(typeRef);
        field.setType(typeRef);
        metricRepo.addMetric(metric);
        metric.addMeasure(meas);
        ruleRepo.addRule(rule);
        rule.add(finding);
        rule.add(tag);
        patternRepo.addPattern(pattern);
        pattern.addRole(role);
        pattern.addInstance(instance);
        chain.addInstance(instance);
        instance.addRoleBinding(binding);
        binding.setRoleRefPair(role, ref);
        system.addPatternChain(chain);
        rel.setToAndFromRefs(to, from);
        p.addPatternInstance(instance);

        a(system.getPatternChains().size()).shouldNotBeEqual(0);
        a(p.getPatternInstances().size()).shouldNotBeEqual(0);
    }

    @Test
    public void testGetRolesFromPatternRepository() {
        populateDb();
        List<PatternRepository> repos = PatternRepository.findAll();
        List<Role> roles = Lists.newLinkedList();
        for (PatternRepository repo : repos) {
            roles.addAll(patternRepo.getRoles());
        }

        the(roles.size()).shouldBeEqual(2);
    }
}
