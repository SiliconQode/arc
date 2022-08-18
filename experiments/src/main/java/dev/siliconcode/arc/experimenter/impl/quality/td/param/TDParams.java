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
package dev.siliconcode.arc.experimenter.impl.quality.td.param;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * TDParam -
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class TDParams {

    public Map<String, Double> numericParams;
    public Map<String, String> stringParams;

    /**
     *
     */
    private TDParams()
    {
        numericParams = Maps.newHashMap();
        stringParams = Maps.newHashMap();
    }

    /**
     * @param key
     * @param value
     */
    public void setParam(String key, double value)
    {
        if (key == null || key.isEmpty())
            return;

        numericParams.put(key, value);
    }

    public void setParam(String key, String value)
    {
        if (key == null || key.isEmpty() || value == null || value.isEmpty())
            return;

        stringParams.put(key, value);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * @param key
     * @return
     */
    public double getNumericParam(String key)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Parameter key cannot be null or empty");
        if (!numericParams.containsKey(key))
            throw new IllegalArgumentException("Unknown Parameter: " + key);

        return numericParams.get(key);
    }

    /**
     * @param key
     * @return
     */
    public String getStringParam(String key)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Parameter key cannot be null or empty");
        if (!numericParams.containsKey(key))
            throw new IllegalArgumentException("Unknown Parameter: " + key);

        return stringParams.get(key);
    }

    /**
     * Builder -
     *
     * @author Isaac Griffith
     */
    public static class Builder {

        /**
         *
         */
        private TDParams params;

        /**
         *
         */
        public Builder()
        {
            params = new TDParams();
        }

        /**
         * @return
         */
        public TDParams create()
        {
            return params;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder param(String key, double value)
        {
            params.setParam(key, value);

            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder param(String key, String value)
        {
            params.setParam(key, value);

            return this;
        }

        public Builder param(String key, int value)
        {
            params.setParam(key, value);

            return this;
        }
    }
}
