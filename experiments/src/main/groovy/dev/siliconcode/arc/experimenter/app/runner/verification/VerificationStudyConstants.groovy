/*
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
package dev.siliconcode.arc.experimenter.app.runner.verification

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
interface VerificationStudyConstants {

    String ID = "Identifier"
    String KEY = "Key"
    String LOCATION = "Location"
    String SYSTEM_KEY = "SystemKey"
    String BASE_KEY = "BaseKey"
    String INFECTED_KEY = "InfectedKey"
    String INJECTED_KEY = "InjectedKey"
    String SYSTEM_LOCATION = "SystemLocation"
    String BASE_LOCATION = "BaseLocation"
    String INFECTED_LOCATION = "InfectedLocation"
    String INJECTED_LOCATION = "InjectedLocation"
    String CONTROL_FILE = "ControlFile"
    String[] HEADERS = [ID, SYSTEM_KEY, SYSTEM_LOCATION, BASE_KEY, INFECTED_KEY, INJECTED_KEY,
                        BASE_LOCATION, INFECTED_LOCATION, INJECTED_LOCATION, CONTROL_FILE]
}
