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
package dev.siliconcode.arc.quality.quamoco.io.qmr;

import dev.siliconcode.arc.quality.quamoco.distiller.ModelManager;
import dev.siliconcode.arc.quality.quamoco.io.AbstractQuamocoReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QMRXMLReader extends AbstractQuamocoReader {

    /**
     *
     */
    public QMRXMLReader(ModelManager manager)
    {
        super(manager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void firstPass(String doc) throws ParserConfigurationException, SAXException, IOException
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void secondPass(String doc) throws ParserConfigurationException, SAXException, IOException
    {
        // TODO Auto-generated method stub

    }

}
