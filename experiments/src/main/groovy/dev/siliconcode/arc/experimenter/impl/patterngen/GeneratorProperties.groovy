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
package dev.siliconcode.arc.experimenter.impl.patterngen

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface GeneratorProperties {

    String GEN_LANG_PROP      = "arc.generator.language"
    String GEN_OUTPUT_DIR     = "arc.generator.output.dir"
    String GEN_BASE_DIR       = "arc.generator.base.dir"
    String GEN_NUM_INSTANCES  = "arc.generator.num.inst"
    String GEN_MAX_BREADTH    = "arc.generator.max.breadth"
    String GEN_MAX_DEPTH      = "arc.generator.max.depth"
    String GEN_VERSION        = "arc.generator.version"
    String GEN_ARITIES        = "arc.generator.arities"
    String GEN_PATTERNS       = "arc.generator.patterns"
    String GEN_LICENSE_NAME   = "arc.generator.license.name"
    String GEN_LICENSE_YEAR   = "arc.generator.license.year"
    String GEN_LICENSE_HOLDER = "arc.generator.license.holder"
    String GEN_BUILD_PROJECT  = "arc.generator.build.project"
    String GEN_BUILD_ARTIFACT = "arc.generator.build.artifact"
    String GEN_BUILD_DESC     = "arc.generator.build.desc"
    String GEN_SOURCE_PATH    = "arc.generator.source.path"
    String GEN_SOURCE_EXT     = "arc.generator.source.ext"
}
