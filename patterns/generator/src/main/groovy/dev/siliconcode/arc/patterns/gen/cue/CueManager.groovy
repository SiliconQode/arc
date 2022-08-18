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
package dev.siliconcode.arc.patterns.gen.cue


import groovy.util.logging.Log4j2

@Singleton
@Log4j2
class CueManager {

    Cue current
    String pattern
    String language

    Map<String, Cue> cues

    def loadCues(String pattern, String language) {
        log.info "Loading Cues for Pattern: $pattern"
        this.pattern = pattern.toLowerCase().replaceAll(/\s/, "_")
        this.language = language

        cues = CueReader.instance.read("/cues/${language}/${this.pattern}.tp")
    }

    def selectCue() {
        List<String> names = cues.keySet().asList()
        Random rand = new Random()
        String name = names.get(rand.nextInt(names.size()))
        current = cues[name]
    }
}
