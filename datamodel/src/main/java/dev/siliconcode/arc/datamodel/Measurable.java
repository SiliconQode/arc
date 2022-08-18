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

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface Measurable {

    /**
     * @return A key based on the id of this Measurable which will be used when constructing the reference held by a Measure
     */
    String getRefKey();

    /**
     * @return The parent Measurable of this Measurable
     */
    Measurable getParent();

    /**
     * @return The parent project of this Measurable
     */
    Project getParentProject();

    /**
     * @return The parent file of this Measurable
     */
    File getParentFile();

    void addMeasure(Measure meas);

    void removeMeasure(Measure meas);

    List<Measure> getMeasures();

    double getValueFor(String metricKey);

    boolean hasValueFor(String metricKey);
}
