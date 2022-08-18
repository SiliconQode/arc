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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="file" type="{http://pmd.sourceforge.net/report/2.0.0}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="error" type="{http://pmd.sourceforge.net/report/2.0.0}error" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="suppressedviolation" type="{http://pmd.sourceforge.net/report/2.0.0}suppressedviolation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="configerror" type="{http://pmd.sourceforge.net/report/2.0.0}configerror" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timestamp" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "file",
    "error",
    "suppressedviolation",
    "configerror"
})
@XmlRootElement(name = "pmd")
public class Pmd {

    protected List<File> file;
    protected List<Error> error;
    protected List<Suppressedviolation> suppressedviolation;
    protected List<Configerror> configerror;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "timestamp", required = true)
    protected String timestamp;

    /**
     * Gets the value of the file property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the file property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFile().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link File }
     *
     *
     */
    public List<File> getFile() {
        if (file == null) {
            file = new ArrayList<File>();
        }
        return this.file;
    }

    /**
     * Gets the value of the error property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the error property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getError().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Error }
     *
     *
     */
    public List<Error> getError() {
        if (error == null) {
            error = new ArrayList<Error>();
        }
        return this.error;
    }

    /**
     * Gets the value of the suppressedviolation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suppressedviolation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuppressedviolation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Suppressedviolation }
     *
     *
     */
    public List<Suppressedviolation> getSuppressedviolation() {
        if (suppressedviolation == null) {
            suppressedviolation = new ArrayList<Suppressedviolation>();
        }
        return this.suppressedviolation;
    }

    /**
     * Gets the value of the configerror property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configerror property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigerror().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Configerror }
     *
     *
     */
    public List<Configerror> getConfigerror() {
        if (configerror == null) {
            configerror = new ArrayList<Configerror>();
        }
        return this.configerror;
    }

    /**
     * Gets the value of the version property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the timestamp property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

}
