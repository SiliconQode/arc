/**
 * MIT License
 *
 * Empirilytics PlantUML Editor
 * Copyright (c) 2018-2021 Empirilytics
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
package dev.siliconcode.arc.ui.plantuml.action.export;

import dev.siliconcode.arc.ui.plantuml.PlantUMLEditor;
import net.sourceforge.plantuml.FileFormat;

/**
 * An Exporter for Standard XMI from PlantUML Source
 *
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class ExportStdXMI extends ExportXMI {

    /**
     * Constructs a new Standard XMI Exporter Action for the given PlantUMLEditor
     *
     * @param owner Owner of this action
     */
    public ExportStdXMI(PlantUMLEditor owner) {
        super(owner, "Export Standard XMI", "Exports Standard XMI", 'N');
    }

    @Override
    protected FileFormat fileFormat() {
        return FileFormat.XMI_STANDARD;
    }
}
