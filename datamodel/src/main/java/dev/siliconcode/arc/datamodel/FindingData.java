/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("finding_data")
@BelongsTo(parent = Finding.class, foreignKeyName = "finding_id")
public class FindingData extends Model {

    public FindingData() {
    }

    public void with(String name, double value) {
        add(FindingDataPoint.builder().handle(name).value(value).create());
    }

    public Finding getParentFinding() {
        return parent(Finding.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FindingData) {
            FindingData fd = (FindingData) obj;
            if (fd.getId().equals(getId()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
