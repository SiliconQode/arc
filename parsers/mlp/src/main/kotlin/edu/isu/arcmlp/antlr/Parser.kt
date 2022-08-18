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

import edu.isu.arcmlp.generated.grammar.ANTLRv4Lexer
import edu.isu.arcmlp.generated.grammar.ANTLRv4Parser
import edu.isu.arcmlp.grammars.*
import edu.isu.arcmlp.grammars.transformations.concatProductions
import edu.isu.arcmlp.grammars.transformations.link
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.apache.commons.text.StringEscapeUtils
import java.io.File

class NoRulesException : Exception {
    constructor(filename: File, cause: NoRulesException) : super("file ${filename.absolutePath}", cause)
    constructor() : super()
}

fun parseAntlr(input: String): Grammar {
    return parseAntlr(CharStreams.fromString(input)).link()
}

fun parseAntlr(stream: CharStream): Grammar {
    val lexer = ANTLRv4Lexer(stream)
    val tokens = CommonTokenStream(lexer)
    val parser = ANTLRv4Parser(tokens)
    val tree = parser.grammarSpec()

    return tree.toAst()
}

fun parseAntlr(vararg files: File): Grammar {
    return parseAntlr(files.toList())
}

fun parseAntlrFromFilenames(vararg fileNames: String): Grammar {
    return parseAntlr(fileNames.map(::File))
}

class ParsingException(message: String) : Exception(message)

fun parseAntlr(files: List<File>): Grammar {
    val grammars = files.map { file ->
        try {
            parseAntlr(CharStreams.fromStream(file.inputStream()))
        } catch (e: NoRulesException) {
            throw NoRulesException(file, e)
        }
    }

    return grammars.reduce { g1, g2 -> g1.concatProductions(g2) }.link()
}

private fun ANTLRv4Parser.GrammarSpecContext.toAst(): Grammar {
    val preModeProductions = rules().ruleSpec()
            .map { it.toAst() }
    val modeProductions = modeSpec()
            .flatMap { modeSpec -> modeSpec.lexerRuleSpec() }
            .map { it.toAst() }

    val productions = preModeProductions + modeProductions
    val prodsWithSymbols = productions.map { it.symbol to it }
    if (prodsWithSymbols.isEmpty()) {
        throw NoRulesException()
    }
    return Grammar(prodsWithSymbols.toMap(), prodsWithSymbols[0].first)
}

private fun ANTLRv4Parser.RuleSpecContext.toAst(): Production {
    return lexerRuleSpec()?.toAst() ?: parserRuleSpec()?.toAst() ?: error("No rule")
}

private fun ANTLRv4Parser.ParserRuleSpecContext.toAst(): Production {
    val name = nonTerminalSymbol(RULE_REF().symbol.text)
    val content = ruleBlock().toAst()
    return Production(name, content)
}

private fun ANTLRv4Parser.LexerRuleSpecContext.toAst(): Production {
    val rule = nonTerminalSymbol(TOKEN_REF().symbol.text)
    val content = lexerRuleBlock().toAst()
    return Production(rule, content)
}

private fun ANTLRv4Parser.RuleBlockContext.toAst(): Rule {
    return Union(ruleAltList().labeledAlt().map { it.toAst() })
}

private fun ANTLRv4Parser.LabeledAltContext.toAst(): Rule {
    return alternative().toAst()
}

private fun ANTLRv4Parser.ElementContext.toAst(): Rule? {
    val withoutSuffix = when {
        labeledElement() != null -> labeledElement().toAst()
        atom() != null -> atom().toAst()
        ebnf() != null -> ebnf().toAst()
        else -> return null
    }
    return ebnfSuffix()?.decorate(withoutSuffix) ?: withoutSuffix
}

private fun ANTLRv4Parser.EbnfContext.toAst(): Rule {
    val block = block().toAst()
    return blockSuffix()?.ebnfSuffix()?.decorate(block) ?: block
}

private fun ANTLRv4Parser.BlockContext.toAst(): Rule {
    return altList().toAst()
}

private fun ANTLRv4Parser.AltListContext.toAst(): Rule {
    return Union(alternative().map { it.toAst() })
}

private fun ANTLRv4Parser.AlternativeContext.toAst(): Rule {
    return Concatenate(element().mapNotNull { it.toAst() })
}

private fun ANTLRv4Parser.AtomContext.toAst(): Rule {
    return when {
        terminal() != null -> terminal().toAst()
        ruleref() != null -> ruleref().toAst()
        notSet() != null -> notSet().toAst()
        DOT() != null -> Dot
        else -> error("Unexpected atom")
    }
}

private fun ANTLRv4Parser.NotSetContext.toAst(): Rule {
    return when {
        setElement() != null -> Not(setElement().toAst())
        blockSet() != null -> Not(blockSet().toAst())
        else -> error("Unknown not set")
    }
}

private fun ANTLRv4Parser.BlockSetContext.toAst(): Rule {
    return Union(setElement().map { it.toAst() })
}

private fun ANTLRv4Parser.SetElementContext.toAst(): Rule {
    return when {
        TOKEN_REF() != null -> nonTerminalSymbol(TOKEN_REF().symbol.text)
        STRING_LITERAL() != null -> parseStringLiteral(STRING_LITERAL().symbol.text)
        characterRange() != null -> characterRange().toAst()
        LEXER_CHAR_SET() != null -> parseLexerCharSet(LEXER_CHAR_SET().symbol.text)
        else -> error("Unexpected set element")
    }
}

/**
 * Extracts a unicode literal, startI should be the index of the character immediately after \u
 * Parses forms \uxxxx or \u{xxxxxx}
 *
 * Returns a pair of the new value for i and the literal
 */
fun extractUnicodeCodePoint(text: String, startI: Int): Pair<Int, Int> {
    var hex: Int
    var i = startI
    if (text[i] == '{') {
        i++
        hex = 0
        while (text[i] != '}') {
            hex = hex * 16 + text[i++].fromHexToInt()
        }
        i++
    } else {
        hex = 0
        for (j in i until i + 4) {
            hex = hex * 16 + text[j].fromHexToInt()
        }
        i += 4
    }
    return i to hex
}

fun parseLexerCharSet(text: String): Rule {
    var i = 0
    require(i in text.indices && text[i] == '[') { "Charset must start with a [" }

    val items = mutableListOf<Rule>()


    fun readEscape(): Rule {
        if (i !in text.indices) error("End of input")
        return when (val c = text[i++]) {
            'n' -> Literal("\n")
            'r' -> Literal("\r")
            'b' -> Literal("\b")
            't' -> Literal("\t")
            'f' -> Literal("\u000c")
            'u' -> {
                val (newI, unicode) = extractUnicodeCodePoint(text, i)
                i = newI
                Literal(Character.toChars(unicode)!!.joinToString(""))
            }
            'p', 'P' -> {
                require(text[i++] == '{')
                val inside = text.substring(i).substringBefore('}')
                i += inside.length + 1 // 1 added to get past '}'
                if (c == 'p') {
                    CharClass(inside)
                } else {
                    Not(CharClass(inside))
                }
            }
            else -> Literal(c.toString())
        }
    }

    i++
    loop@ while (true) {
        if (i !in text.indices) {
            throw ParsingException("Reached end of input while parsing $text")
        }
        when (val c = text[i++]) {
            ']' -> return Union(items)
            '\\' -> items += readEscape()
            '-' -> {
                if (text[i] == ']' || items.size == 0) {
                    items += Literal(c.toString())
                    continue@loop
                }
                val previous = items.removeAt(items.lastIndex)
                if (previous !is Literal || previous.literal.codePointCount(0, previous.literal.length) != 1) {
                    throw ParsingException("Invalid first term $previous in $text. Type is ${previous::class.qualifiedName}")
                }
                if (i !in text.indices) error("End of input")
                val next = when (val chr = text[i++]) {
                    '\\' -> readEscape()
                    else -> Literal(chr.toString())
                }
                if (next !is Literal || next.literal.codePointCount(0, next.literal.length) != 1) {
                    error("Invalid second term $next in $text")
                }
                items.add(UnicodeCodepointRange(previous.literal.codePointAt(0), next.literal.codePointAt(0)))
            }
            else -> items += Literal(c.toString())
        }
    }
}

private fun Char.fromHexToInt(): Int {
    return tryHexToInt() ?: error("Not a valid hexadecimal character: $this")
}

private fun Char.tryHexToInt(): Int? {
    return when (this) {
        in '0'..'9' -> this - '0'
        in 'a'..'f' -> this - 'a' + 10
        in 'A'..'F' -> this - 'A' + 10
        else -> null
    }
}

private fun ANTLRv4Parser.CharacterRangeContext.toAst(): Rule {

    val c1 = parseStringLiteral(STRING_LITERAL(0).symbol.text).literal
    val c2 = parseStringLiteral(STRING_LITERAL(1).symbol.text).literal
    if (c1.codePointCount(0, c1.length) != 1 || c2.codePointCount(0, c2.length) != 1) {
        throw ParsingException("${StringEscapeUtils.escapeJava(c1)} or ${StringEscapeUtils.escapeJava(c2)} are not single characters")
    }
    return UnicodeCodepointRange(c1.codePointAt(0), c2.codePointAt(0))
}

fun parseStringLiteral(text: String): Literal {
    require(text[0] == '\'' && text.last() == '\'') { "Must begin and end with single quotes." }

    val withoutQuotes = text.substring(1 until text.lastIndex)

    var i = 0
    val builder = StringBuilder()
    while (i in withoutQuotes.indices) {
        if (withoutQuotes[i] != '\\') {
            builder.append(withoutQuotes[i])
            i++
            continue
        }
        i++
        val c = when (val chr = withoutQuotes[i++]) {
            'b' -> '\b'.toInt()
            't' -> '\t'.toInt()
            'n' -> '\n'.toInt()
            'f' -> '\u000c'.toInt()
            'r' -> '\r'.toInt()
            'u' -> {
                val (newI, c) = extractUnicodeCodePoint(withoutQuotes, i)
                i = newI
                c
            }
            else -> chr.toInt()
        }
        builder.appendCodePoint(c)
    }

    return Literal(builder.toString())
}

private fun ANTLRv4Parser.RulerefContext.toAst(): Rule {
    return nonTerminalSymbol(RULE_REF().symbol.text)
}

private fun ANTLRv4Parser.TerminalContext.toAst(): Rule {
    return when {
        TOKEN_REF() != null -> nonTerminalSymbol(TOKEN_REF().symbol.text)
        STRING_LITERAL() != null -> parseStringLiteral(STRING_LITERAL().symbol.text)
        else -> error("Unkown terminal: ${this.text}")
    }
}

private fun ANTLRv4Parser.EbnfSuffixContext.decorate(rule: Rule): Rule {
    return when {
        STAR() != null -> Star(rule)
        PLUS() != null -> Concatenate(rule, Star(rule))
        QUESTION().isNotEmpty() -> Union(rule, Empty)
        else -> error("Unknown ebnf suffix: $text")
    }
}

private fun ANTLRv4Parser.LabeledElementContext.toAst(): Rule {
    return when {
        atom() != null -> atom().toAst()
        block() != null -> block().toAst()
        else -> error("Unknown labeled element $text")
    }
}

private fun ANTLRv4Parser.LexerRuleBlockContext.toAst(): Rule {
    return lexerAltList().toAst()
}

private fun ANTLRv4Parser.LexerAltContext.toAst(): Rule {
    val a = lexerElements() ?: return Empty
    val b = a.lexerElement()
    val c = b.mapNotNull {
        it.toAst()
    }
    return Concatenate(c)
}

private fun ANTLRv4Parser.LexerElementContext.toAst(): Rule? {
    val preSuffix = when {
        labeledLexerElement() != null -> labeledLexerElement().toAst()
        lexerAtom() != null -> lexerAtom().toAst()
        lexerBlock() != null -> lexerBlock().toAst()
        else -> return null
    }
    return if (ebnfSuffix() != null) {
        ebnfSuffix().decorate(preSuffix)
    } else {
        preSuffix
    }
}

private fun ANTLRv4Parser.LexerBlockContext.toAst(): Rule {
    return lexerAltList().toAst()
}

private fun ANTLRv4Parser.LexerAltListContext.toAst(): Rule {
    return Union(lexerAlt().map { it.toAst() })
}

private fun ANTLRv4Parser.LexerAtomContext.toAst(): Rule {
    return when {
        characterRange() != null -> characterRange().toAst()
        terminal() != null -> terminal().toAst()
        notSet() != null -> notSet().toAst()
        LEXER_CHAR_SET() != null -> parseLexerCharSet(LEXER_CHAR_SET().symbol.text)
        DOT() != null -> Dot
        else -> error("Unknown lexer atom: $text")
    }
}

private fun ANTLRv4Parser.LabeledLexerElementContext.toAst(): Rule {
    return when {
        lexerAtom() != null -> lexerAtom().toAst()
        lexerBlock() != null -> lexerBlock().toAst()
        else -> error("Uknown labeled lexer element: $text")
    }
}
