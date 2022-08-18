/*
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
package dev.siliconcode.arc.experimenter.impl.pattern4

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import dev.siliconcode.arc.datamodel.Pattern
import dev.siliconcode.arc.datamodel.PatternRepository
import dev.siliconcode.arc.datamodel.Role
import dev.siliconcode.arc.datamodel.RoleType
import dev.siliconcode.arc.experimenter.provider.AbstractPatternProvider
import dev.siliconcode.arc.experimenter.ArcContext
import groovy.xml.XmlSlurper
import groovyx.gpars.GParsPool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Pattern4PatternProvider extends AbstractPatternProvider {

    def data
    Map<String, String> pattern4rbml = [:]
    Table<String, String, String> rolePattern4Rbml = HashBasedTable.create()

    Pattern4PatternProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        def slurper = new XmlSlurper()
        data = slurper.parseText(Pattern4PatternProvider.class.getResourceAsStream(Pattern4Constants.PATTERN4_CONFIG_PATH).getText("UTF-8"))
    }

    @Override
    void updateDatabase() {
        repository = findPatternRepo(data)
        createPatterns(data)
    }

    String rbmlNameFor(String pattern4Name) {
        return pattern4rbml[pattern4Name]
    }

    String rbmlRoleNameFor(String patternName, String p4RoleName) {
        return rolePattern4Rbml.get(patternName, p4RoleName)
    }

    PatternRepository findPatternRepo(data) {
        String repoName = data.@repo

        context.open()
        PatternRepository repo = PatternRepository.findFirst("repoKey = ?", repoName)
        context.close()

        return repo
    }

    void createPatterns(data) {
        data.pattern.each {
            String name = it.@gofName
            String pattern4Name = it.@pattern4Name
            String family = it.@family

            context.open()
            Pattern pattern = Pattern.findFirst("patternKey = ?", repository.getRepoKey() + ":" + name)
            if (!pattern) {
                pattern = Pattern.builder()
                        .name(name)
                        .key("${repository.getRepoKey()}:$name")
                        .family(family)
                        .create()
                repository.addPattern(pattern)
            }
            context.close()

            if (name && pattern4Name)
                pattern4rbml[pattern4Name] = name

            createRoles(pattern, it.role)
        }
    }

    void createRoles(Pattern pattern, roles) {
        roles.each {
            String rbmlName = it.@rbmlName
            String pattern4Name = it.@pattern4Name
            String type = it.@elementType
            boolean mand = Boolean.parseBoolean((String) it.@mandatory)

            context.open()
            Role role = Role.findFirst("roleKey = ?", (String) "${pattern.getPatternKey()}:$rbmlName")
            if (!role) {
                role = Role.builder()
                        .name(rbmlName)
                        .type(roleTypeFromElementType(type))
                        .roleKey("${pattern.getPatternKey()}:$rbmlName")
                        .mandatory(mand)
                        .create()
                pattern.addRole(role)
            }

            if (rbmlName && pattern4Name)
                rolePattern4Rbml.put(pattern.getName(), pattern4Name, rbmlName)

            context.close()
        }
    }

    RoleType roleTypeFromElementType(String elementType) {
        switch (elementType) {
            case "CLASS": return RoleType.CLASSIFIER
            case "FIELD": return RoleType.STRUCT_FEAT
            case "METHOD": return RoleType.BEHAVE_FEAT
        }

        null
    }
}
