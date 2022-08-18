/*
 * MIT License
 *
 * Copyright (c) 2018-2019, Idaho State University, Empirical Software Engineering
 * Laboratory and Isaac Griffith
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
 *
 */

package edu.isu.arcmlp.grammars.transformations.similarity

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class LongestCommonSubsequenceKtTest : StringSpec() {

    init {
        "Should merge equivalent terms" {
            val t = listOf("a", "b", "c")
            val (size, subSeq) = LongestCommonSubsequence(t, t).asPair()
            size shouldBe 3
            subSeq shouldBe t.map { LongestCommonSubsequenceElement.CommonElement(it) }
        }

        "Should ignore leading terms" {
            val t1 = listOf("a", "b", "c", "d")
            val t2 = listOf("c", "d")
            val (size, subSeq) = LongestCommonSubsequence(t1, t2).asPair()
            size shouldBe 2
            subSeq shouldBe listOf(
                    LongestCommonSubsequenceElement.DifferentElements(listOf("a", "b"), emptyList()),
                    LongestCommonSubsequenceElement.CommonElement("c"),
                    LongestCommonSubsequenceElement.CommonElement("d")
            )
        }

        "Should ignore trailing terms" {
            val t1 = listOf("a", "b", "c", "d")
            val t2 = listOf("a", "b")
            val (size, subSeq) = LongestCommonSubsequence(t1, t2).asPair()
            size shouldBe 2
            subSeq shouldBe listOf(
                    LongestCommonSubsequenceElement.CommonElement("a"),
                    LongestCommonSubsequenceElement.CommonElement("b"),
                    LongestCommonSubsequenceElement.DifferentElements(listOf("c", "d"), emptyList())
            )
        }

        "Should ignore both leading and trailing terms" {
            val t1 = listOf("a", "b", "c", "d", "e", "f")
            val t2 = listOf("c", "d")
            val (size, subSeq) = LongestCommonSubsequence(t1, t2).asPair()
            size shouldBe 2
            subSeq shouldBe listOf(
                    LongestCommonSubsequenceElement.DifferentElements(listOf("a", "b"), emptyList()),
                    LongestCommonSubsequenceElement.CommonElement("c"),
                    LongestCommonSubsequenceElement.CommonElement("d"),
                    LongestCommonSubsequenceElement.DifferentElements(listOf("e", "f"), emptyList())
            )
        }

        "Should ignore gaps" {
            val t1 = listOf("a", "b", "c", "d")
            val t2 = listOf("a", "b", "d")
            val (size, subSeq) = LongestCommonSubsequence(t1, t2).asPair()
            size shouldBe 3
            subSeq shouldBe listOf(
                    LongestCommonSubsequenceElement.CommonElement("a"),
                    LongestCommonSubsequenceElement.CommonElement("b"),
                    LongestCommonSubsequenceElement.DifferentElements(listOf("c"), emptyList()),
                    LongestCommonSubsequenceElement.CommonElement("d")
            )
        }

        "Should allow extra stuff inserted" {

            val t1 = listOf(
                    "a",
                    "stuff",
                    "b",
                    "c"
            )
            val t2 = listOf(
                    "a",
                    "b",
                    "other stuff",
                    "c"
            )
            val (size, subseq) = LongestCommonSubsequence(t1, t2).asPair()
            size shouldBe 3
            subseq shouldBe listOf(
                    LongestCommonSubsequenceElement.CommonElement("a"),
                    LongestCommonSubsequenceElement.DifferentElements(listOf("stuff"), emptyList()),
                    LongestCommonSubsequenceElement.CommonElement("b"),
                    LongestCommonSubsequenceElement.DifferentElements(emptyList(), listOf("other stuff")),
                    LongestCommonSubsequenceElement.CommonElement("c")
            )
        }
    }

}