/*
 * The MIT License (MIT)
 *
 * Empirilytics Configuration Library
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.config

/**
 * Contract for and config provider, must provide a loadConfig method which returns a configuration item
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface ConfigProvider {

    /**
     * Loads the configuration information from the necessary directories and unifies it with the provided command line arguments
     * @param cmdLineArgs list of command line arguments
     * @throws MissingRequiredConfigKeyException if any required key is not found in any of the provided components of the configuration
     */
    void loadConfig(String... cmdLineArgs) throws MissingRequiredConfigKeyException

    /**
     * Retrieves a list of the currently known keys for the current configuration
     * @return list of current keys in the configuration
     */
    List<String> configKeys()

    /**
     * Retrieves the string value associated with the given key, or null if no such key exists
     * @param key Key for which the value is expected
     * @return value associated with the given key, or null if no such value exists
     */
    String value(String key)
}
