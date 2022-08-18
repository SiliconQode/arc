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

import dev.siliconcode.arc.disharmonies.injector.grime.GrimeInjector

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ResultsWriter {

    static void write(String baseStr, DataHolder holder, int reps)
    {
        Path base = Paths.get(baseStr)
        String[] quality = ["Effectiveness", "Extendibility", "Flexibility", "Functionality", "Reusability",
                "Understandability"]
        String[] patterns = ["Abstract Factory", "Adapter-Command", "Composite", "Decorator", "Facade",
                "Factory Method", "Flyweight", "Mediator", "Prototype", "Proxy", "Strategy", "Template Method"]
        String[] inject = [GrimeInjector.DEPG, GrimeInjector.DIPG, GrimeInjector.DESG, GrimeInjector.DISG,
                           GrimeInjector.IEPG, GrimeInjector.IIPG, GrimeInjector.IESG, GrimeInjector.IISG]

        for (int k = 0; k < quality.length; k++)
        {

            Path saveLoc = Paths.get(base.toString() + base.getFileSystem().getSeparator() + quality[k] + ".csv")
            try
            {
                Files.deleteIfExists(saveLoc)
                Files.createFile(saveLoc)
            }
            catch (IOException e)
            {
                e.printStackTrace()
            }

            PrintWriter pw
            try
            {
                pw = new PrintWriter(Files.newBufferedWriter(saveLoc, StandardCharsets.UTF_8,
                        StandardOpenOption.WRITE))
                for (String pattern : holder.getKeys())
                {
                    for (int l = 0; l < reps; l++)
                    {
                        for (int j = 0; j < inject.length; j++)
                        {
                            String line = holder.getDataLine(pattern, l, inject[j], quality[k])
                        }
                    }
                }
                // Map<String, Map<String, Map<String, Pair<Double>>>> data =
                // holder.getData()
                // for (String master : data.keySet())
                // {
                // System.out.println("master: " + master)
                // for (String minor : data.get(master).keySet())
                // {
                // System.out.println("minor: " + minor)
                // System.out.println("quality: " + quality[k])
                // Pair<Double> pair =
                // data.get(master).get(minor).get(quality[k])
                // System.out.println("Pair: " + pair)
                // String line = holder.getDataLine(master, minor, quality[k])
                // pw.println(line)
                // }
                // }
            }
            catch (IOException e)
            {
                e.printStackTrace()
            }
            finally {
                pw?.close()
            }
        }
    }
}
