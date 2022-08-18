/*
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import groovy.xml.slurpersupport.NodeChildren
import org.ccil.cowan.tagsoup.Parser

@Grapes(@Grab('org.ccil.cowan.tagsoup:tagsoup:1.2.1'))

String pmdVersion = "6.24.0"
String ENCODING = "UTF-8"
Map<String, String> pages = [
            "Java Best Practices"  : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_bestpractices.html",
            "Java Code Style"      : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_codestyle.html",
            "Java Design"          : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_design.html",
            "Java Documentation"   : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_documentation.html",
            "Java Error Prone"     : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_errorprone.html",
            "Java Multi Threading" : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_multithreading.html",
            "Java Performance"     : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_performance.html",
            "Java Security"        : "https://pmd.github.io/pmd-$pmdVersion/pmd_rules_java_security.html"
        ]

String ruleHeader = "<h2 class='clickable-header top-level-header'>" // rule name is h2 text
//String since = "<p><strong>Since:</strong>" // value is remainder of <p> tag -- need to trim
//String priority = "<p><strong>Priority:</strong>" // value is remainder of <p> tag -- need to trim
//String description = "<p></p>" // after priority and value is body of <p> tag
// skip a <p> tag
String examples = "<p><strong>Example(s):</strong>"
String exampleCode = "<div class='language-java'><div><pre><code>...</code>..." // need to strip the spans correctly
//String configKey = "<div class='language-xml'><div><pre><code>...</code>..." // need to strip the spans correctly

def ruleList = []

pages.each { key, url ->
    def parser = new XmlSlurper(new Parser())

    new URL(url).withReader(ENCODING) { reader ->
        GPathResult document = parser.parse(reader)
        document.'**'.findAll{ it.name() == "h2" }.each {
            String rule = it.text()
            def children = it.parent().children().asList()
            int index = children.indexOf(it)
            String next = children[index + 1].text()
            if (next == "Deprecated") {
                index += 1
            }
            String since = children[index + 1].text().split(":")[1].trim()
            String priority = children[index + 2].text().split(":")[1].trim()
            String description = children[index + 3].text()

            String configKey = ""
            for (int i = 1; i <= 9; i++) {
                if (children[index + i].text().trim().startsWith('<ruleref=')) {
                    configKey = children[index + i].text().split('"')[1]
                    break
                }
            }

            def map = [
                    'rule': rule,
                    'since': since,
                    'priority': priority,
                    'description': description,
                    'configKey': configKey
            ]
            ruleList << map
        }
    }
}

def writer = new StringWriter()
def rules = new MarkupBuilder(writer)
rules.rules {
    ruleList.each { item ->
        rule(key: item['rule']) {
            since item['since']
            priority convertPriority(item['priority'])
            description item['description']
            configKey item['configKey']
        }
    }
}

String convertPriority(String priority) {
    switch(priority) {
        case "High (1)": return "CRITICAL"
        case "Medium High (2)": return "BLOCKER"
        case "Medium (3)": return "MAJOR"
        case "Medium Low (4)": return "MINOR"
        case "Low (5)": return "INFO"
    }
    return null
}

File f = new File("pmd_rules.xml")
f.text = writer.toString()