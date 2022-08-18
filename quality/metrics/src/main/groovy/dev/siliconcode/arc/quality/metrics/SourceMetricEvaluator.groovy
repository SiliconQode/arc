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
package dev.siliconcode.arc.quality.metrics

import dev.siliconcode.arc.datamodel.Component
import dev.siliconcode.arc.datamodel.Measurable
import dev.siliconcode.arc.datamodel.File

import java.nio.file.Files
import java.nio.file.Paths
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class SourceMetricEvaluator extends MetricEvaluator {

    List<String> getLines(Measurable node) {
        if (node instanceof Component) {
            File f = node.getParentFile()

            def x = Paths.get(f.getName()).toFile().readLines()
            int start = node.start - 1
            int end = node.end - 1

            try {
                x[start..end]
            } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                throw ex
            }
        } else if (node instanceof File) {
            Paths.get(node.getName()).toFile().readLines()
        } else {
            []
        }
    }
}
