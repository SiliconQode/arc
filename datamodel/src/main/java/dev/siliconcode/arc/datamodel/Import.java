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

import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Many2Many(other = File.class, join = "files_imports", sourceFKName = "import_id", targetFKName = "file_id")
public class Import extends Model {

    public Import() {}

    @Builder(buildMethodName = "create")
    public Import(String name, int start, int end) {
        setName(name);
        setStart(start);
        setEnd(end);
    }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public int getStart() { return getInteger("start"); }

    public void setStart(int start) { setInteger("start", start); save(); }

    public int getEnd() { return getInteger("end"); }

    public void setEnd(int end) { setInteger("end", end); save(); }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Import) {
            Import comp = (Import) o;
            return comp.getName().equals(this.getName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
