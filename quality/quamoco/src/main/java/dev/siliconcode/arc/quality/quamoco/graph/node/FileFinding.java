/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
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
package dev.siliconcode.arc.quality.quamoco.graph.node;

import dev.siliconcode.arc.datamodel.File;
import lombok.Getter;

public class FileFinding extends Finding {

    /**
     * Location in the CodeTree where the Finding was found
     */
    @Getter
    private File location;

    /**
     * Constructs a new Finding for the given issue key and name at the given
     * location in the CodeTree
     *
     * @param location  Location
     * @param issueKey  Issue Key
     * @param issueName Issue Name
     */
    public FileFinding(final File location, final String issueKey, final String issueName) {
        super(issueKey, issueName);
        if (location == null) {
            throw new IllegalArgumentException();
        }

        this.location = location;
    }

    /**
     * @param location the new location of the activated issue
     */
    public void setLocation(final File location) {
        if (location == null) {
            return;
        }

        this.location = location;
    }
}
