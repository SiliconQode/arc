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
package dev.siliconcode.arc.quality.quamoco.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import dev.siliconcode.arc.quality.quamoco.model.QualityModel;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class AbstractWriter {

    /**
     *
     */
    public AbstractWriter()
    {
        // TODO Auto-generated constructor stub
    }

    public void write(List<QualityModel> models)
    {
        models.forEach((qm) -> {
            Path path = Paths.get(qm.getFileName());

            try (PrintWriter pw = new PrintWriter(
                    Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)))
            {
                writeData(qm, pw);
                pw.flush();
            }

            catch (IOException e)
            {

            }
        });
    }

    /**
     * @param qm
     * @param pw
     */
    protected abstract void writeData(QualityModel qm, PrintWriter pw);
}
