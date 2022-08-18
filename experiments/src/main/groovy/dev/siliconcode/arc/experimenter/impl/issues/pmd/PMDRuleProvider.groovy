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
package dev.siliconcode.arc.experimenter.impl.issues.pmd

import dev.siliconcode.arc.datamodel.Priority
import dev.siliconcode.arc.datamodel.Rule
import dev.siliconcode.arc.datamodel.RuleRepository
import dev.siliconcode.arc.datamodel.Tag
import dev.siliconcode.arc.experimenter.provider.AbstractRuleProvider
import dev.siliconcode.arc.experimenter.ArcContext
import groovy.xml.XmlSlurper

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PMDRuleProvider extends AbstractRuleProvider {

    def config

    PMDRuleProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        config = new XmlSlurper()
                .parseText(PMDRepoProvider.class.getResourceAsStream(PMDConstants.PMD_CONFIG_PATH).getText('UTF-8'))
    }

    @Override
    void updateDatabase() {
        context.open()
        RuleRepository repo = RuleRepository.findFirst("repoKey = ?", "pmd")
        context.close()

        config.rule.each { rule ->
            String ruleKey = rule.@key
            String ruleName = rule.@key
            String priorityName = rule.priority
            Priority priority = Priority.fromValue(priorityName)
            String tag = rule.tag

            context.open()
            if (!Rule.findFirst("ruleKey = ?", (String) "${repo.repoKey}:${ruleKey}")) {
                if (rule.status != "DEPRECATED" || ruleExists(repo, ruleKey)) {
                    Rule r = Rule.builder()
                            .name(ruleName)
                            .key("${repo.repoKey}:${ruleKey}")
                            .description()
                            .priority(priority)
                            .create()
                    r.addTag(Tag.of(tag))
                    repo.addRule(r)
                }
            }
            context.close()
        }
    }

    private boolean ruleExists(RuleRepository repo, String ruleKey) {
        repo.getRules().find { Rule r -> r.getKey() == ruleKey } != null
    }

}
