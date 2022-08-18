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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2018.01.18 at 07:08:06 PM MST
//


package dev.siliconcode.arc.experimenter.impl.issues.pmd.resultsdm;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for violation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="violation">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="beginline" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="endline" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="begincolumn" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="endcolumn" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="rule" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ruleset" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="variable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="externalInfoUrl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "violation", propOrder = {
    "value"
})
public class Violation {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "beginline", required = true)
    protected BigInteger beginline;
    @XmlAttribute(name = "endline", required = true)
    protected BigInteger endline;
    @XmlAttribute(name = "begincolumn", required = true)
    protected BigInteger begincolumn;
    @XmlAttribute(name = "endcolumn", required = true)
    protected BigInteger endcolumn;
    @XmlAttribute(name = "rule", required = true)
    protected String rule;
    @XmlAttribute(name = "ruleset", required = true)
    protected String ruleset;
    @XmlAttribute(name = "package")
    protected String _package;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "variable")
    protected String variable;
    @XmlAttribute(name = "externalInfoUrl")
    protected String externalInfoUrl;
    @XmlAttribute(name = "priority", required = true)
    protected String priority;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the beginline property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getBeginline() {
        return beginline;
    }

    /**
     * Sets the value of the beginline property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setBeginline(BigInteger value) {
        this.beginline = value;
    }

    /**
     * Gets the value of the endline property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getEndline() {
        return endline;
    }

    /**
     * Sets the value of the endline property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setEndline(BigInteger value) {
        this.endline = value;
    }

    /**
     * Gets the value of the begincolumn property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getBegincolumn() {
        return begincolumn;
    }

    /**
     * Sets the value of the begincolumn property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setBegincolumn(BigInteger value) {
        this.begincolumn = value;
    }

    /**
     * Gets the value of the endcolumn property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getEndcolumn() {
        return endcolumn;
    }

    /**
     * Sets the value of the endcolumn property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setEndcolumn(BigInteger value) {
        this.endcolumn = value;
    }

    /**
     * Gets the value of the rule property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRule(String value) {
        this.rule = value;
    }

    /**
     * Gets the value of the ruleset property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRuleset() {
        return ruleset;
    }

    /**
     * Sets the value of the ruleset property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRuleset(String value) {
        this.ruleset = value;
    }

    /**
     * Gets the value of the package property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Sets the value of the package property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPackage(String value) {
        this._package = value;
    }

    /**
     * Gets the value of the clazz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the method property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Gets the value of the variable property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVariable(String value) {
        this.variable = value;
    }

    /**
     * Gets the value of the externalInfoUrl property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }

    /**
     * Sets the value of the externalInfoUrl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExternalInfoUrl(String value) {
        this.externalInfoUrl = value;
    }

    /**
     * Gets the value of the priority property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPriority(String value) {
        this.priority = value;
    }

}