/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.disharmonies.injector.grime

interface GrimeInjectorConstants {

    String repoName = "grime-rules"
    String repoKey = "isuese:grime-rules"

    Map<String, String> grimeTypes = [
            "PIG": "isuese:grime-rules:modular-grime:pig",
            "TIG": "isuese:grime-rules:modular-grime:tig",
            "PEEG": "isuese:grime-rules:modular-grime:peeg",
            "PEAG": "isuese:grime-rules:modular-grime:peag",
            "TEEG": "isuese:grime-rules:modular-grime:teeg",
            "TEAG": "isuese:grime-rules:modular-grime:teag",

            "DEPG": "isuese:grime-rules:class-grime:depg",
            "DESG": "isuese:grime-rules:class-grime:desg",
            "DIPG": "isuese:grime-rules:class-grime:dipg",
            "DISG": "isuese:grime-rules:class-grime:disg",
            "IEPG": "isuese:grime-rules:class-grime:iepg",
            "IESG": "isuese:grime-rules:class-grime:iesg",
            "IIPG": "isuese:grime-rules:class-grime:iipg",
            "IISG": "isuese:grime-rules:class-grime:iisg",

            "PICG": "isuese:grime-rules:org-grime:picg",
            "PIRG": "isuese:grime-rules:org-grime:pirg",
            "PECG": "isuese:grime-rules:org-grime:pecg",
            "PERG": "isuese:grime-rules:org-grime:perg",
            "MPICG": "isuese:grime-rules:org-grime:mpicg",
            "MPIUG": "isuese:grime-rules:org-grime:mpiug",
            "MPECG": "isuese:grime-rules:org-grime:mpecg",
            "MPEUG": "isuese:grime-rules:org-grime:mpeug",
            "MTICG": "isuese:grime-rules:org-grime:mticg",
            "MTIUG": "isuese:grime-rules:org-grime:mtiug",
            "MTECG": "isuese:grime-rules:org-grime:mtecg",
            "MTEUG": "isuese:grime-rules:org-grime:mteug",
    ]
}
