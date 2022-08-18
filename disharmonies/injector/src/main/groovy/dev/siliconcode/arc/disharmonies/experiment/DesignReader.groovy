/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
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
package dev.siliconcode.arc.disharmonies.experiment

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DesignReader {

    static ExperimentDesign read(String loc)
    {
        ExperimentDesign design = null
        Path path = Paths.get(loc)
        if (Files.exists(path) && !Files.isDirectory(path))
        {
            design = new ExperimentDesign()

            try
            {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8)
                for (int i = 1; i < lines.size(); i++)
                {
                    String line = lines.get(i)
                    StringTokenizer tok = new StringTokenizer(line, ",")
                    String patternType = tok.nextToken()
                    int rep = Integer.parseInt(tok.nextToken())
                    String instanceLoc = tok.nextToken()
                    String injectType = tok.nextToken()
                    String system = instanceLoc.substring(instanceLoc.lastIndexOf("\\") + 1)

                    if (system.contains("-") && system.contains("_"))
                        system = system.substring(system.indexOf("-") + 1, system.lastIndexOf("_"))
                    else if (system.contains("-"))
                        system = system.substring(system.indexOf("-") + 1)

                    design.addNode(patternType, rep, instanceLoc, injectType, system)
                }
            }
            catch (IOException e)
            {
                e.printStackTrace()
            }
        }

        return design
    }
}
