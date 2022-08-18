/*
 * The MIT License (MIT)
 *
 * Empirilytics Metrics
 * Copyright (c) 2015-2019 Emprilytics
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
//import dev.siliconcode.arc.datamodel.CodeTree
//import dev.siliconcode.arc.datamodel.node.structural.Project
//import dev.siliconcode.arc.datamodel.node.type.Type
//import dev.siliconcode.arc.datamodel.parsers.JavaCodeTreeBuilder
//import dev.siliconcode.arc.datamodel.measures.MeasuresTable
import dev.siliconcode.arc.quality.metrics.MetricEvaluator
import dev.siliconcode.arc.quality.metrics.MetricsRegistrar
import spock.lang.Shared
import spock.lang.Specification

class MetricTests extends Specification {

//    @Shared
//    CodeTree tree
//    @Shared
//    MeasuresTable table = MeasuresTable.instance
//
//    def setup() {
//        JavaCodeTreeBuilder builder = new JavaCodeTreeBuilder()
//        tree = builder.build("Test", "data/test/")
//
//        measureMetrics()
//    }
//
//    def measureMetrics() {
//        MetricsRegistrar reg = MetricsRegistrar.instance
//        def metrics = ["NOS"]
//
//        tree.getUtils().getMethods().each { method ->
//            MetricEvaluator metric = reg.getMetric("NOS").newInstance()
//            metric.setTree(tree)
//            metric.measure(method)
//        }
//
//        metrics += ["NOF", "NOM", "SLOC", "BLOC", "CLOC"]
//
//        tree.getUtils().getAllTypes().each { type ->
//            metrics.each { handle ->
//                MetricEvaluator metric = reg.getMetric(handle).newInstance()
//                metric.setTree(tree)
//                metric.measure(type)
//            }
//        }
//
//        Project project = tree.getProject()
//        project.files().each { File file ->
//            metrics.each { handle ->
//                MetricEvaluator metric = reg.getMetric(handle).newInstance()
//                metric.setTree(tree)
//                metric.measure(file)
//            }
//        }
//
//        metrics += ["NC"]
//        metrics.each { handle ->
//            MetricEvaluator metric = reg.getMetric(handle).newInstance()
//            metric.setTree(tree)
//            metric.measure(project)
//        }
//
//    }
//
//    def "Test Class A"() {
//        given:
//        Type type = tree.getUtils().findType("A")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Class B"() {
//        given:
//        Type type = tree.getUtils().findType("B")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Class C"() {
//        given:
//        Type type = tree.getUtils().findType("C")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Class D"() {
//        given:
//        Type type = tree.getUtils().findType("D")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Class E"() {
//        given:
//        Type type = tree.getUtils().findType("E")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Class F"() {
//        given:
//        Type type = tree.getUtils().findType("F")
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(type, "NOF")
//        def nos = table.retrieve(type, "NOS")
//        def nom = table.retrieve(type, "NOM")
//        def sloc = table.retrieve(type, "SLOC")
//        def bloc = table.retrieve(type, "BLOC")
//        def cloc = table.retrieve(type, "CLOC")
//
//        then:
//        nof == 2
//        nos == 6
//        nom == 3
//        sloc == 17
//        bloc == 4
//        cloc == 3
//    }
//
//    def "Test Project"() {
//        given:
//        Project project = tree.getProject()
//
//        when:
//        // need to actual execute metrics
//        def nof = table.retrieve(project, "NOF")
//        def nos = table.retrieve(project, "NOS")
//        def nom = table.retrieve(project, "NOM")
//        def sloc = table.retrieve(project, "SLOC")
//        def bloc = table.retrieve(project, "BLOC")
//        def cloc = table.retrieve(project, "CLOC")
//        def nc = table.retrieve(project, "NC")
//
//        then:
//        nof == 12
//        nos == 36
//        nom == 18
//        nc == 6
//        sloc == 102
//        bloc == 24
//        cloc == 18
//    }
}
