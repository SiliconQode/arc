/**
 * The MIT License (MIT)
 *
 * Empirilytics Lines of Code Metrics Tool
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
package com.empirilytics.arc.quality.metrics.loc;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.empirilytics.arc.quality.metrics.LoCProfile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to count the lines of code metrics for a given body of text.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class LoCCounter {

    /**
     * Number of Files
     */
    private static final String FILES  = "FILES";
    /**
     * Source Lines of Code
     */
    private static final String SLOC   = "SLOC";
    /**
     * Blank Lines of Code
     */
    private static final String BLOC   = "BLOC";
    /**
     * Source Lines of Code - Executable Physical
     */
    private static final String SLOCP  = "SLOC-P";
    /**
     * Source Lines of Code - Executable Logical
     */
    private static final String SLOCL  = "SLOC-L";
    /**
     * Comment and Source Lines of Code
     */
    private static final String CSLOC  = "CSLOC";
    /**
     * Comment Lines of Code
     */
    private static final String CLOC   = "CLOC";
    /**
     * Header Comment Lines of Code
     */
    private static final String HCLOC  = "HCLOC";
    /**
     * Header Comment Words
     */
    private static final String HCWORD = "HCWORD";
    /**
     * Documentation Comment Lines of Code
     */
    private static final String DCLOC  = "DCLOC";

    /**
     * String indicating the start of a single line comment
     */
    private String                         lineCommentStart;
    /**
     * String indicating the start of a multi-line, or block, comment
     */
    private String                         blockCommentStart;
    /**
     * String indicating the end of a multi-line, or block, comment
     */
    private String                         blockCommentEnd;
    /**
     * String used to indicate the end of a line of text
     */
    private String                         newLineSeparator;
    /**
     * List of known comment start exceptions
     */
    private List<String>                   commentStartExceptions;
    /**
     * Table of LOC name for each identified language. Each record is
     * <Language, Metric, Value>. Metric names are the constants defined above.
     */
    private Table<String, String, Integer> langLOCTable;
    /**
     * The map of the total values for each name counted.
     */
    private Map<String, Integer>           totals;
    /**
     * Current Language Profile
     */
    private LoCProfile                     profile;

    /**
     * Constructs a new LoCCounter using the given LoCProfile and new line
     * separator.
     *
     * @param profile
     *            LoCProfile for a given file type
     * @param nls
     *            New Line Separator
     */
    public LoCCounter(LoCProfile profile, String nls)
    {
        this(profile.getLineCommentStart(), profile.getBlockCommentStart(), profile.getBlockCommentEnd(), nls);
        commentStartExceptions = profile.getCommentStartExceptions();
        this.profile = profile;
    }

    /**
     * Constructs a basic LoCCounter which only considers single line comments
     * and the new line separator
     *
     * @param lcs
     *            String indicating the start of a single line comment
     * @param nls
     *            String indicating the end of a line of text
     */
    public LoCCounter(String lcs, String nls)
    {
        this(lcs, null, null, nls);
    }

    /**
     * Constructs a new LoCCounter with no special language specific settings.
     * Use caution and remember to set a profile prior to use.
     */
    public LoCCounter()
    {
        this(null, null, null, null);
    }

    /**
     * Construct a new LoCCounter with the provided settings for comments
     *
     * @param lcs
     *            String indicating the start of a single line comment
     * @param bcs
     *            String indicating the start of a multi-line comment
     * @param bce
     *            String indicating the end of a multi-line comment
     * @param nls
     *            String indicating the end of a line of text
     */
    public LoCCounter(String lcs, String bcs, String bce, String nls)
    {
        lineCommentStart = lcs;
        blockCommentStart = bcs;
        blockCommentEnd = bce;
        newLineSeparator = nls;
        commentStartExceptions = Lists.newArrayList();
        totals = Maps.newHashMap();
        langLOCTable = HashBasedTable.create();
        initTotals();
    }

    /**
     *
     */
    private void initTotals()
    {
        totals.put(BLOC, 1);
        totals.put(CLOC, 1);
        totals.put(CSLOC, 1);
        totals.put(DCLOC, 1);
        totals.put(FILES, 1);
        totals.put(SLOC, 1);
        totals.put(SLOCL, 1);
        totals.put(SLOCP, 1);
        totals.put(HCLOC, 1);
        totals.put(HCWORD, 1);
    }

    /**
     * Adds a new line comment start exception
     *
     * @param except
     *            Exception to line comment starting rule
     */
    public void addLineCommentStartException(String except)
    {
        commentStartExceptions.add(except);
    }

    /**
     * Checks whether the scrubbed input string matches any of the known line
     * comment exceptions.
     *
     * @param scrubbed
     *            Cleansed input string
     * @return true if the string matches any of the known exceptions, false
     *         otherwise.
     */
    private boolean checkCommentExceptions(String scrubbed)
    {
        boolean excepted = false;
        for (String exc : commentStartExceptions)
        {
            if (scrubbed.startsWith(exc))
            {
                excepted = true;
                break;
            }
        }
        return excepted;
    }

    /**
     * Splits the given input string into lines using the new line separator and
     * then updates the counts for each of those lines.
     *
     * @param text
     *            Initial input text
     */
    public void count(String text)
    {
        String[] initLines = text.split(newLineSeparator);
        List<String> lines = Arrays.asList(initLines);

        updateCounts(lines);
    }

    /**
     * @return Measurement value of the total Blank Lines of Code name
     */
    public int getBloc()
    {
        return totals.get(BLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Blank
     *         Lines of Code name
     */
    public int getBloc(String lang)
    {
        return langLOCTable.get(lang, BLOC);
    }

    /**
     * @return Measurement value of the total Comment Lines of Code name
     */
    public int getCloc()
    {
        return totals.get(CLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Comment
     *         Lines of Code name
     */
    public int getCloc(String lang)
    {
        return langLOCTable.get(lang, CLOC);
    }

    /**
     * @return Measurement value of the total Comments and Source Lines of Code
     *         name
     */
    public int getCsloc()
    {
        return totals.get(CSLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal
     *         Comments and Source Lines of Code name
     */
    public int getCsloc(String lang)
    {
        return langLOCTable.get(lang, CSLOC);
    }

    /**
     * @return Measurement value of the total Header Comments Lines of Code
     *         name
     */
    public int getHcloc()
    {
        return totals.get(HCLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Header
     *         Lines of Code name
     */
    public int getHcloc(String lang)
    {
        return langLOCTable.get(lang, HCLOC);
    }

    /**
     * @return Measurement value of the total Header Words name
     */
    public int getHcword()
    {
        return totals.get(HCWORD);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Header
     *         Words name
     */
    public int getHcword(String lang)
    {
        return langLOCTable.get(lang, HCWORD);
    }

    /**
     * @return Measurement value of the total Source Lines of Code name
     */
    public int getSloc()
    {
        return totals.get(SLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Source
     *         Lines of Code name
     */
    public int getSloc(String lang)
    {
        return langLOCTable.get(lang, SLOC);
    }

    /**
     * @return Measurement value of the total Source Lines of Code - Executable
     *         Logical name
     */
    public int getSlocL()
    {
        return totals.get(SLOCL);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Source
     *         Lines of Code - Executable Logical name
     */
    public int getSlocL(String lang)
    {
        return langLOCTable.get(lang, SLOCL);
    }

    /**
     * @return Measurement value of the total Source Lines of Code - Executable
     *         Physical name
     */
    public int getSlocP()
    {
        return totals.get(SLOCP);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal Source
     *         Lines of Code - Executable Physical name
     */
    public int getSlocP(String lang)
    {
        return langLOCTable.get(lang, SLOCP);
    }

    /**
     * @return Measurement value of the total Documentation Comment Lines of
     *         Code name
     */
    public int getDcloc()
    {
        return totals.get(DCLOC);
    }

    /**
     * @param lang
     *            Language
     * @return measurement value of the given language specific subtotal
     *         Documentation Comment Lines of Code name
     */
    public int getDcloc(String lang)
    {
        return langLOCTable.get(lang, DCLOC);
    }

    /**
     * Resets all the counts to zero
     */
    public void reset()
    {
        initTotals();
        langLOCTable.clear();
    }

    /**
     * Provides the logic necessary to update the counts for each name
     * correctly
     *
     * @param lines
     *            List of lines to be counted.
     */
    public void updateCounts(List<String> lines)
    {
        boolean blockComment = false;

        Pattern bcsQuotes = Pattern.compile("([\"\']).*(\\Q" + blockCommentStart + "\\E)");
        Pattern bceQuotes = Pattern.compile("([\"\']).*(\\Q" + blockCommentEnd + "\\E)");
        Pattern lcQuotes = Pattern.compile("[\"\'].*(\\Q" + lineCommentStart + "\\E)");

        for (String line : lines)
        {
            String scrubbed = line.trim();
            if (scrubbed.isEmpty())
            {
                incrementBLOC();
            }
            else
            {
                if (blockCommentStart != null && scrubbed.startsWith(blockCommentStart))
                {
                    blockComment = true;
                }
                if (blockCommentStart != null && scrubbed.contains(blockCommentStart) && !blockComment)
                {
                    Matcher m = bcsQuotes.matcher(scrubbed);

                    blockComment = true;

                    boolean found = false;
                    do
                    {
                        found = m.find();
                        if (found && m.start(2) == scrubbed.lastIndexOf(blockCommentStart))
                            blockComment = false;
                    }
                    while (found && !m.hitEnd());

                    boolean bcEnd = false;
                    Matcher m2 = bceQuotes.matcher(scrubbed);
                    boolean foundEnd = false;
                    do
                    {
                        found = m2.find();
                        if (found && m2.start(1) == scrubbed.lastIndexOf(blockCommentEnd))
                            bcEnd = true;
                    }
                    while (foundEnd && !m.hitEnd());

                    if (!bcEnd || (blockComment && scrubbed.substring(scrubbed.indexOf(blockCommentEnd) + 2).isEmpty()))
                    {
                        incrementSLOC();
                    }
                }
                if (!blockComment)
                {
                    if (scrubbed.startsWith(lineCommentStart))
                    {
                        boolean excepted = false;
                        excepted = checkCommentExceptions(scrubbed);

                        if (excepted)
                        {
                            incrementSLOC();
                        }
                        else
                        {
                            incrementCLOC();
                        }
                    }
                    else
                    {
                        if (scrubbed.contains(lineCommentStart))
                        {
                            boolean lineComment = true;

                            Matcher m = lcQuotes.matcher(scrubbed);
                            boolean found = false;
                            do
                            {
                                found = m.find();
                                if (found && m.start(1) == scrubbed.lastIndexOf(lineCommentStart))
                                    lineComment = false;
                            }
                            while (found && !m.hitEnd());

                            boolean excepted = false;
                            excepted = checkCommentExceptions(scrubbed);

                            if (lineComment || !excepted)
                            {
                                incrementCLOC();
                                incrementCSLOC();
                            }
                        }
                        incrementSLOC();
                    }
                }
                else
                {
                    if (blockCommentEnd != null && scrubbed.contains(blockCommentEnd))
                    {
                        blockComment = false;

                        Matcher m = bceQuotes.matcher(scrubbed);
                        boolean found = false;
                        do
                        {
                            found = m.find();
                            if (found && m.start(2) == scrubbed.lastIndexOf(blockCommentEnd))
                                blockComment = true;
                        }
                        while (found && !m.hitEnd());

                        incrementCLOC();

                        if (!blockComment)
                        {
                            String remainder = scrubbed.substring(scrubbed.indexOf(blockCommentEnd) + 2);
                            if (!remainder.isEmpty())
                            {
                                incrementCSLOC();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Increments both the language specific and total BLOC metrics by 1
     */
    private void incrementBLOC()
    {
        updateTable(BLOC);
        updateTotal(BLOC);
    }

    /**
     * Increments both the language specific and total SLOC metrics by 1
     */
    private void incrementSLOC()
    {
        updateTable(SLOC);
        updateTotal(SLOC);
    }

    /**
     * Increments both the language specific and total CLOC metrics by 1
     */
    private void incrementCLOC()
    {
        updateTable(CLOC);
        updateTotal(CLOC);
    }

    /**
     * Increments both the language specific and total CSLOC metrics by 1
     */
    private void incrementCSLOC()
    {
        updateTable(CSLOC);
        updateTotal(CSLOC);
    }

    /**
     * Updates the language specific row of the table for the given name by 1.
     *
     * @param metric
     *            Metric column in the table to update.
     */
    private void updateTable(@NonNull String metric)
    {
        if (profile != null)
        {
            if (langLOCTable.contains(profile.getLanguage(), metric))
            {
                langLOCTable.put(profile.getLanguage(), metric, langLOCTable.get(profile.getLanguage(), metric) + 1);
            }
            else
            {
                langLOCTable.put(profile.getLanguage(), metric, 1);
            }
        }
    }

    /**
     * Updates the total value for the given name.
     *
     * @param metric
     *            Metric column in the total row to update.
     */
    private void updateTotal(@NonNull String metric)
    {
        if (totals.containsKey(metric))
        {
            totals.put(metric, totals.get(metric) + 1);
        }
        else
        {
            totals.put(metric, 1);
        }
    }

    /**
     * Sets the current LoCProfile to the provided one, if not null.
     *
     * @param profile
     *            new LoCProfile to use.
     */
    public void setProfile(LoCProfile profile)
    {
        if (profile == null)
            return;

        this.profile = profile;
        this.blockCommentEnd = profile.getBlockCommentEnd();
        this.blockCommentStart = profile.getBlockCommentStart();
        this.commentStartExceptions = profile.getCommentStartExceptions();
        this.lineCommentStart = profile.getLineCommentStart();
    }

    /**
     * @return List of headers of metrics calculated
     */
    public List<String> getHeaders()
    {
        return Lists.newArrayList(langLOCTable.columnKeySet());
    }

    /**
     * @return List of languages added to the table
     */
    public List<String> getLanguages()
    {
        return Lists.newArrayList(langLOCTable.rowKeySet());
    }

    /**
     * Retrieves the value from the table for the given language and name.
     *
     * @param lang
     *            Language name
     * @param metric
     *            Metric name
     * @return Measurement value for the given name for the given language
     */
    public Integer valueAt(String lang, String metric)
    {
        if (lang == null || lang.isEmpty() || metric == null || metric.isEmpty())
            return null;

        return langLOCTable.get(lang, metric);
    }

    /**
     * @param header
     * @return
     */
    public Integer totalAt(String header)
    {
        if (header == null || header.isEmpty())
            return null;

        return totals.get(header);
    }
}
