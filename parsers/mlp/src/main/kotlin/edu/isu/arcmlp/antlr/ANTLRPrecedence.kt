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

package edu.isu.arcmlp.antlr

import edu.isu.arcmlp.grammars.Concatenate
import edu.isu.arcmlp.grammars.Rule
import edu.isu.arcmlp.grammars.Union
import edu.isu.arcmlp.grammars.repr.Precedence
import edu.isu.arcmlp.grammars.repr.UnambiguousPrecedence

object ANTLRPrecedence : Precedence<Rule> {
    override fun precedenceOf(rule: Rule): Int? {
        return when (rule) {
            is Union -> 0
            is Concatenate -> 1
            is Not -> 2
            is Star -> 2
            is UnambiguousPrecedence -> 3
            else -> null
        }
    }
}