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
package edu.isu.arcmlp.bnf

import edu.isu.arcmlp.generated.grammar.BNFBaseListener
import edu.isu.arcmlp.generated.grammar.BNFLexer
import edu.isu.arcmlp.generated.grammar.BNFParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 * Parses a string representation of a BNF grammar to a programmatic form.
 */
fun parseBnf(input: String): BnfGrammar {
    val antlrInput = CharStreams.fromString(input)
    val lexer = BNFLexer(antlrInput)
    val tokens = CommonTokenStream(lexer)
    val parser = BNFParser(tokens)
    val tree = parser.rulelist()
    val walker = ParseTreeWalker()
    val extractor = BNFTreeVisitor()
    
    walker.walk(extractor, tree)
    
    return extractor.extractedGrammar
}

/**
 * A visitor we use to extract the grammar.
 */
private class BNFTreeVisitor : BNFBaseListener() {
    /**
     * The productions extracted so far.
     */
    private val productions = mutableListOf<Pair<BnfToken.Rule, List<List<BnfToken>>>>()
    
    /**
     * The rules that productions have been defined for.
     */
    private val definedRules = mutableSetOf<BnfToken.Rule>()
    
    /**
     * The grammar extracted so far.
     */
    val extractedGrammar: BnfGrammar
        get() = BnfGrammar(productions)
    
    /**
     * Used to process a rule.
     */
    override fun exitRule_(ctx: BNFParser.Rule_Context) {
        val name = ctx.lhs().ID().text
        val rule = BnfToken.Rule.fromToken(name)
        
        if (rule in definedRules) throw IllegalArgumentException("Tree contains duplicate definitions for $name")
        
        val alts = getAlternatives(ctx.rhs().alternatives())
        productions += rule to alts
        definedRules += rule
    }
    
    /**
     * Retrieve the options for a given production.
     */
    private fun getAlternatives(ctx: BNFParser.AlternativesContext): List<List<BnfToken>> {
        return ctx.alternative().map {
            it.element().map(::getElement)
        }
    }
    
    /**
     * Reads a string or rule token.
     */
    private fun getElement(element: BNFParser.ElementContext): BnfToken {
        val id = element.ID()
        val literal = element.text()?.STRING()
        
        if (id != null) return BnfToken.Rule.fromToken(id.text)
        if (literal != null) return BnfToken.Literal.fromToken(literal.text)
        
        throw IllegalArgumentException("Element does not have a legal id or string literal in it.")
    }
    
}