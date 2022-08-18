/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen

import dev.siliconcode.arc.datamodel.Pattern
import dev.siliconcode.arc.patterns.gen.cue.CueManager
import dev.siliconcode.arc.patterns.rbml.PatternLoader
import dev.siliconcode.arc.patterns.rbml.model.SPS

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class PatternManager {

    PatternLoader patternLoader = PatternLoader.getInstance()

    void loadPatternCues(String pattern, language) {
        CueManager.instance.loadCues(pattern, language)
//        selectCue()
    }

    void selectCue() {
        CueManager.instance.selectCue()
    }

    SPS loadPattern(String pattern) {
        patternLoader.loadPattern(getPatternName(pattern))
    }

    String getPatternName(String pattern) {
        return pattern.toLowerCase().replaceAll(/_/, ' ')
    }

    Pattern findPatternForName(String pattern) {
        pattern = getPatternName(pattern).capitalize()
        Pattern.findFirst("name = ?", pattern)
    }
}
