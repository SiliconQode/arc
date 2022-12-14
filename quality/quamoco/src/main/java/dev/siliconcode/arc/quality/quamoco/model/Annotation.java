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
package dev.siliconcode.arc.quality.quamoco.model;

import dev.siliconcode.arc.quality.quamoco.io.XMLSerializable;
import lombok.*;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.UUID;

/**
 * All model elements can be labeled with any number of annotations. An
 * annotation is a pair of a key with a corresponding value.
 * <br>
 * General Rules
 * <ul>
 * <li>Use annotations only if there is no suitable attribute available for this
 * purpose.</li>
 * </ul>
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@EqualsAndHashCode
@ToString
public class Annotation implements XMLSerializable {

    /**
     * The key has to be unique for the current model element.
     */
    @Getter
    @Setter
    private String key;
    /**
     * The value can be any text describing the facts for the particular key.
     */
    @Getter
    @Setter
    private String value;
    /**
     * A unique identifier for this annotation
     */
    @Getter
    private String identifier = UUID.randomUUID().toString();

    public Annotation() {
        this(null, null);
    }

    public Annotation(String identifier) {
        this(identifier, null, null);
    }

    public Annotation(String key, String value) {
        identifier = UUID.randomUUID().toString();
        this.key = key;
        this.value = value;
    }

    @Builder(buildMethodName = "create")
    public Annotation(String identifier, String key, String value) {
        this.identifier = identifier;
        this.key = key;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String xmlTag() {
        return String.format(
                "<annotations xmi:id=\"%s\" key=\"%s\" value=\"%s\" />\n", getIdentifier(),
                StringEscapeUtils.escapeXml10(getKey()), StringEscapeUtils.escapeXml10(getValue()));
    }
}
