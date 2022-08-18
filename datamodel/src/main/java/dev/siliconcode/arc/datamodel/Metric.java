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

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Metric extends Model {

    public Metric() {
    }

    @Builder(buildMethodName = "create")
    public Metric(String key, String handle, String name, String description, String evaluator) {
        if (key != null && !key.isEmpty()) setString("metricKey", key);
        if (name != null && !name.isEmpty()) setName(name);
        if (handle != null && !handle.isEmpty()) setHandle(handle);
        if (description != null && !description.isEmpty()) setDescription(description);
        if (evaluator != null && !evaluator.isEmpty()) setEvaluator(evaluator);
        save();
    }

    public String getMetricKey() {
        return getString("metricKey");
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String desc) {
        set("description", desc);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public String getHandle() {
        return getString("handle");
    }

    public void setHandle(String name) {
        set("handle", name);
        save();
    }

    public void addMeasure(Measure measure) {
        add(measure);
        save();
    }

    public void removeMeasure(Measure measure) {
        remove(measure);
        save();
    }

    public List<Measure> getMeasures() {
        return getAll(Measure.class);
    }

    public MetricRepository getParentMetricRepository() {
        return parent(MetricRepository.class);
    }

    public void setEvaluator(String evaluator) {
        if (evaluator != null)
            set("evaluator", evaluator);
        save();
    }

    public String getEvaluator() {
        return getString("evaluator");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Metric) {
            Metric other = (Metric) obj;
            if (other.getDescription().equals(getDescription())) {
                if (other.getName().equals(getName())) {
                    if (other.getMetricKey().equals(getMetricKey())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getName(), getMetricKey());
    }
}
