/**
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
package dev.siliconcode.arc.experimenter;

import com.google.common.collect.Table;
import dev.siliconcode.arc.datamodel.Measurable;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class ReportGenerator {

    protected Measurable parent;
    protected Set<String> measures;
    @Singular
    protected Set<String> findings;
    protected Set<String> identifiers;
    @Setter
    @Getter
    protected ReportingLevel level;
    protected Table<String, String, Number> valueTable;

    public ReportGenerator(Set<String> measures, Set<String> findings, Set<String> identifiers, ReportingLevel level) {
        this.measures = measures;
        this.findings = findings;
        this.identifiers = identifiers;
        this.level = level;
    }

    public void addMeasure(String meas) {
        if (meas != null && !meas.isEmpty())
            measures.add(meas);
    }

    public void removeMeasure(String meas) {
        if (meas != null && !meas.isEmpty() && measures.contains(meas))
            measures.remove(meas);
    }

    public void addFinding(String find) {
        if (find != null && !find.isEmpty())
            findings.add(find);
    }

    public void removeFinding(String find) {
        if (find != null && !find.isEmpty() && findings.contains(find))
            findings.remove(find);
    }

    public void addIdentifier(String id) {
        if (id != null && !id.isEmpty())
            identifiers.add(id);
    }

    public void removeIdentifier(String id) {
        if (id != null && !id.isEmpty() && identifiers.contains(id))
            identifiers.remove(id);
    }

    public abstract void constructTable();

    public void writeCSV(String fileName) {

    }
}
