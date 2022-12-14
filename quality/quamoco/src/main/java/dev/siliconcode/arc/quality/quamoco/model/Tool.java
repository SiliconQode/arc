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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.Map;

/**
 * A tool is a software program, which is used to collect measurement data. This
 * can be, for example, a static analysis tool.
 * <br>
 * General Rules
 * Describe the used tool as precisely as possible.
 * You can create different tools for different tool versions, if
 * necessary.
 *
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Tool extends QMElement {

    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    /**
     * Optional title providing a more detailed name than the simple name field
     */
    @Getter @Setter
    private String title;

    /**
     * Constructs a new instance of Tool
     *
     * @param name
     *            name of the new Tool instance
     */
    public Tool(String name)
    {
        super();
        this.name = name;
    }

    /**
     * Constructs a new instance of Tool
     *
     * @param name
     *            name of the new Tool instance
     * @param identifier
     *            unique identifier of this tool
     */
    public Tool(String name, String identifier)
    {
        super(identifier);
        this.name = name;
    }

    @Builder(buildMethodName = "create")
    protected Tool(String name, String description, String title, String identifier, Source originatesFrom, @Singular List<Tag> tags, @Singular List<Annotation> annotations)
    {
        super(identifier, originatesFrom, tags, annotations);
        this.name = name;
        this.description = description;
        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String xmlTag()
    {
        Map<String, String> attrs = Maps.newHashMap();

        attrs.put("name", StringEscapeUtils.escapeXml10(getName()));
        attrs.put("description", StringEscapeUtils.escapeXml10(getDescription()));
        attrs.put("title", StringEscapeUtils.escapeXml10(getTitle()));

        return generateXMLTag("tools", null, attrs, Lists.newArrayList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toScript()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
