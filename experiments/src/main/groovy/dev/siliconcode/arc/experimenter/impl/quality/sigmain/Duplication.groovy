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
package dev.siliconcode.arc.experimenter.impl.quality.sigmain

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.google.common.util.concurrent.AtomicDouble
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.experimenter.ArcContext
import dev.siliconcode.arc.quality.metrics.annotations.*
import groovy.util.logging.Log4j2
import groovyx.gpars.GParsExecutorsPool
import org.apache.commons.lang3.tuple.Pair

import java.nio.charset.MalformedInputException

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Duplication",
        primaryHandle = "sigDuplication",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
@Log4j2
class Duplication extends SigAbstractMetricEvaluator implements Rateable {

    AtomicDouble totalLines
    Set<String> dupMethods

    Duplication(ArcContext context) {
        super(context)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            context.open()
            boolean hasVal = node.hasValueFor(repo.getRepoKey() + ":sigDuplication.RAW")
            context.close()

            if (hasVal)
                return

            double dupPercent = newApproach(node as Project)

            context.open()
            Measure.of("${repo.getRepoKey()}:sigDuplication.RAW").on(node).withValue(dupPercent)
            context.close()
        }
    }

    def oldMeasureValue(Measurable node) {
        if (node instanceof Project) {
            dupMethods = Sets.newConcurrentHashSet()
            context.open()
            boolean hasVal = node.hasValueFor(repo.getRepoKey() + ":" + "sigDuplication.RAW")
            context.close()
            if (hasVal)
                return

            totalLines = new AtomicDouble(0)
            Project proj = node as Project

            context.open()
            List<File> srcFiles = Lists.newArrayList(proj.getFilesByType(FileType.SOURCE))
            context.close()

            AtomicDouble dupLines = new AtomicDouble(0)

            int f = 1
            int numFiles = srcFiles.size()
//            GParsExecutorsPool.withPool(8) {
//                srcFiles.eachParallel { File source ->
            srcFiles.each { File source ->
                log.info "ScanSelf File ${f++} / ${numFiles}"
                context.open()
                dupLines.addAndGet(scanSelf(source))
                context.close()
            }
//            }

            List<Pair<Integer, Integer>> pairs = []
            for (int i = 0; i < srcFiles.size() - 1; i++) {
                for (int j = i + 1; j < srcFiles.size(); j++) {
                    pairs << Pair.of(i, j)
                }
            }

//            GParsExecutorsPool.withPool(8) {
//                int p = 1
//                int numPairs = pairs.size()
//                pairs.eachParallel { Pair<Integer, Integer> pair ->
//                    log.info "Scanning pair ${p++} / ${numPairs}"
//                    File source = srcFiles.get(pair.getLeft())
//                    File target = srcFiles.get(pair.getRight())
//                    context.open()
//                    dupLines.addAndGet(scanOther(source, target))
//                    context.close()
//                }
//            }

//            context.open()
//            dupMethods.each { String key ->
//                Method m = Method.findFirst("compKey = ?", key)
//                if (m != null) {
//                    dupLines += m.getValueFor(MetricsConstants.METRICS_REPO_KEY + ":SLOC")
//                }
//            }
//            totalLines = node.getValueFor(MetricsConstants.METRICS_REPO_KEY + ":SLOC")
//            context.close()

            double dupPercent = (dupLines.get() / totalLines.get()) * 100

            context.open()
            Measure.of("${repo.getRepoKey()}:sigDuplication.RAW").on(node).withValue(dupPercent)
            context.close()
        }
    }

    double scanSelf(File file) {
        java.io.File f1 = new java.io.File(file.getFullPath())

        if (!f1.exists())
            return 0.0

        int dup = 0

        totalLines.addAndGet(sanitize(f1.text).split("\n").size())

        List<Method> methods = file.getAllMethods()
        for (int i = 0; i < methods.size(); i++) {
            int before
            int after
            String mod

            int size = f1.text.split("\n").size()

            if (size <= methods[i].getStart() || size <= methods[i].getEnd() || methods[i].getEnd() - methods[i].getStart() < 6)
                continue

            String m1Text = sanitize(f1.text.split("\n").toList().subList(methods[i].getStart(), methods[i].getEnd()).join("\n"))
            totalLines.addAndGet(m1Text.split("\n").size())

            if (dupMethods.contains(methods[i].getCompKey()))
                continue


            try {

                before = m1Text.split("\n").size()
                totalLines.addAndGet(before)
                mod = new String(m1Text)
                mod = processText(m1Text.split("\n").toList(), mod)
                after = mod.split("\n").size()

                dup += before - after
                if (dup > 0)
                    dupMethods << methods[i].getCompKey()
            } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                dup += 0
            }

            for (int j = i + 1; j < methods.size(); j++) {
                try {
                    size = f1.text.split("\n").size()
                    if (size <= methods[j].getStart() || size <= methods[j].getEnd() || methods[i].getEnd() - methods[i].getStart() < 6 || dupMethods.contains(methods[i].getCompKey()))
                        continue

                    String m2Text = f1.text.split("\n").toList().subList(methods[i].getStart(), methods[i].getEnd()).join("\n")
                    before = m2Text.split("\n").size()
//                    totalLines.addAndGet(before)

                    if (dupMethods.contains(methods[j].getCompKey()))
                        continue

                    mod = new String(m2Text)
                    mod = processText(m2Text.split("\n").toList(), mod)
                    after = mod.split("\n").size()

                    dup += before - after

                    if (dup > 0)
                        dupMethods.add(methods[j].getCompKey())
                } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                    dup += 0
                }
            }
        }

        return dup
    }

    String processText(List<String> lines, String mod) {
        int i = 0
        for (; lines.size() >= 6 && i < lines.size() - 5; i++) {
            String subText = lines.subList(i, i + 6).join("\n")
            mod = mod.replace(subText, "")
        }
//        if (i < lines.size()) {
//            String subText = lines.subList(i, lines.size()).join("\n")
//            mod = mod.replace(subText, "")
//        }
        sanitize(mod)
    }

    double scanOther(File file1, File file2) {
        java.io.File f1 = new java.io.File(file1.getFullPath())
        java.io.File f2 = new java.io.File(file2.getFullPath())

        if (!f1.exists() || !f2.exists())
            return 0.0

        int dup = 0

        List<Method> methods = file1.getAllMethods()
        List<Method> other = file2.getAllMethods()

        methods.each { m1 ->
            try {
                int size = f1.text.split("\n").size()
                if (size > m1.getStart() && size > m1.getEnd() && m1.getEnd() - m1.getStart() >= 6 && !dupMethods.contains(m1.getCompKey())) {
                    String m1Text = sanitize(f1.text.split("\n").toList().subList(m1.getStart(), m1.getEnd()).join("\n"))

                    other.each { m2 ->
                        try {
                            size = f2.text.split("\n").size()
                            if (size > m2.getStart() && size > m2.getEnd() && m2.getEnd() - m2.getStart() >= 6 && !dupMethods.contains(m2.getCompKey())) {
                                String m2Text = sanitize(f2.text.split("\n").toList().subList(m2.getStart(), m2.getEnd()).join("\n"))
                                int before = m2Text.split("\n").size()
//                                totalLines.addAndGet(before)
                                String mod = processText(m1Text.split("\n").toList(), m2Text)
                                int after = mod.split("\n").size()

                                dup += before - after

                                if (dup > 0) {
                                    dupMethods.add(m2.getCompKey())
                                }
                            }
                        } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                            dup += 0
                        }
                    }
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                dup += 0
            }
        }

        return dup
    }

    String sanitize(String text) {
        text = text.replaceAll(/(?ms)\/\*.*?\*\\//, "")
        text = text.replaceAll(/\/\/.*/, "")
        text = text.replaceAll(/(?ms)^.*?\{/, "")
        List<String> lines = text.split("\n")
        for (int i = 0; i < lines.size(); i++)
            lines[i] = lines[i].trim()
        lines.removeAll("")
        for (int i = 0; i < lines.size(); i++)
            lines[i] = lines[i].replaceAll(/\s\s+/, " ")
        return lines.join("\n")
    }

    Metric toMetric(MetricRepository repository) {
        this.toMetric(repository, ["RAW", "RATING"])

        null
    }

    double newApproach(Project proj) {
        List<Integer> sizes = []
        List<Integer> newSizes = []

        context.open()
        List<File> srcFiles = Lists.newArrayList(proj.getFilesByType(FileType.SOURCE))
        int size = srcFiles.size() - 1
        java.io.File temp = createTempFiles(srcFiles, sizes)
        context.close()

        for (int j = 0; j < size; j++) {
            log.info "Processing file ${j + 1} / ${size}"
            processSelf(temp, j)

            int k = j + 1
            GParsExecutorsPool.withPool(8) {
                (k..size).eachParallel { Integer ndx ->
                    processOther(temp, j, ndx)
                }
            }
        }

        for (int j = 0; j <= size; j++) {
            java.io.File file = new java.io.File(temp, "${j}.java")
            newSizes << file.text.split("\n").size()
        }

        deleteTempFiles(temp)

        double total = sizes.sum()
        double dup = total - newSizes.sum()
        double dupPercent = (dup / total) * 100
        return dupPercent
    }

    private java.io.File createTempFiles(ArrayList<File> srcFiles, List<Integer> sizes) {
        try {
            java.io.File temp = new java.io.File("." + java.io.File.separator + ".tmp" + java.io.File.separator)
            if (temp.exists() && !temp.isDirectory())
                temp.delete()
            if (!temp.exists())
                temp.mkdirs()

            srcFiles.eachWithIndex { File file, int index ->
                java.io.File write = new java.io.File(temp, "${index}.java")
                write.text = sanitize(new java.io.File(file.getName()).text)
                sizes << write.text.split("\n").size()
            }

            return temp
        } catch (IOException ex) {
        }

        return null
    }

    private def deleteTempFiles(java.io.File temp) {
        temp.deleteDir()
    }

    def processOther(java.io.File temp, int index, int other) {
        java.io.File file = new java.io.File(temp, "${index}.java")
        java.io.File oFile = new java.io.File(temp, "${other}.java")

        List<String> lines = file.text.split("\n")
        String otherText = oFile.text

        if (lines.size() >= 6) {
            for (int i = 0; i < lines.size() - 5; i++) {
                String toReplace = lines.subList(i, i + 5).join("\n")
                otherText = otherText.replace(toReplace, "")
            }

            oFile.text = sanitize(otherText)
        }
    }

    def processSelf(java.io.File temp, int index) {
        java.io.File file = new java.io.File(temp, "${index}.java")
        List<String> lines = file.text.split("\n")
        String findIn = new String(file.text)

        for (int i = 0; i < lines.size() - 6; i++) {
            String toFind = lines.subList(i, i + 6).join("\n")
            int first = findIn.indexOf(toFind) + toFind.length()
            String tempString = findIn.substring(first).replace(toFind, "\n")
            findIn = findIn.substring(0, first) + tempString
        }

        file.text = sanitize(findIn)
    }

    MetricRater getMetricRater() {
        return new SingleValueRater("sigDuplication")
    }
}
