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
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RandomPatternSelector {

    static void main(String[] args) throws IOException
    {
        String location = "C:\\experiments\\DesignPatterns\\experiment.csv"
        List<Treatment> treats = new ArrayList<>()
        String[][] locations = [
                ["Adapter-Command", "C:\\experiments\\DesignPatterns\\instances\\Adapter-Command"],
                ["Abstract Factory", "C:\\experiments\\DesignPatterns\\instances\\Abstract Factory"],
                ["Composite", "C:\\experiments\\DesignPatterns\\instances\\Composite"],
                ["Chain of Responsibility", "C:\\experiments\\DesignPatterns\\instances\\Chain of Responsibility"],
                ["Decorator", "C:\\experiments\\DesignPatterns\\instances\\Decorator"],
                ["Facade", "C:\\experiments\\DesignPatterns\\instances\\Facade"],
                ["Factory Method", "C:\\experiments\\DesignPatterns\\instances\\Factory Method"],
                ["Flyweight", "C:\\experiments\\DesignPatterns\\instances\\Flyweight"],
                ["Mediator", "C:\\experiments\\DesignPatterns\\instances\\Mediator"],
                ["Observer", "C:\\experiments\\DesignPatterns\\instances\\Observer"],
                ["Prototype", "C:\\experiments\\DesignPatterns\\instances\\Prototype"],
                ["Proxy", "C:\\experiments\\DesignPatterns\\instances\\Proxy"],
                ["Singleton", "C:\\experiments\\DesignPatterns\\instances\\Singleton"],
                ["State", "C:\\experiments\\DesignPatterns\\instances\\Singleton"],
                ["Strategy", "C:\\experiments\\DesignPatterns\\instances\\Strategy"],
                ["Template Method", "C:\\experiments\\DesignPatterns\\instances\\Template Method"],
                ["Visitor", "C:\\experiments\\DesignPatterns\\instances\\Visitor"]]

        String[] treatments = ["DPIG", "IPIG", "DSIG", "ISIG", "DPEG", "IPEG", "DSEG", "ISEG"]

        for (int i = 0; i < locations.length; i++)
        {
            String pattern = locations[i][0]
            List<String> instances = randomInstanceSelection(locations[i][1], treatments.length)
            treats.addAll(randomAssignTreatments(pattern, instances, treatments))
        }

        Collections.shuffle(treats)

        writeTreatmentList(treats, location)
    }

    /**
     * @param string
     * @param length
     * @return
     * @throws IOException
     */
    private static List<String> randomInstanceSelection(String string, int length) throws IOException
    {
        List<String> dirContents = new ArrayList<>()

        Path path = Paths.get(string)
        DirectoryStream<Path> stream = Files.newDirectoryStream(path)
        Iterator<Path> iter = stream.iterator()
        while (iter.hasNext())
        {
            Path p = iter.next()
            if (Files.isDirectory(p))
            {
                dirContents.add(p.toString())
            }
        }

        Collections.shuffle(dirContents)

        return dirContents.subList(0, length)
    }

    /**
     * @param instances
     * @param treatments
     * @return
     */
    private static List<Treatment> randomAssignTreatments(String pattern, List<String> instances, String[] treatments)
    {
        List<Treatment> retVal = new ArrayList<>()
        List<String> treats = Arrays.asList(treatments)
        Collections.shuffle(treats)
        for (int i = 0; i < treats.size(); i++)
        {
            retVal.add(new Treatment(pattern, instances.get(i), treats.get(i)))
        }

        return retVal
    }

    /**
     * @param treats
     * @throws IOException
     */
    private static void writeTreatmentList(List<Treatment> treats, String location) throws IOException
    {
        Path path = Paths.get(location)
        Files.deleteIfExists(path)

        Files.createFile(path)
        PrintWriter pw
        try
        {
            pw = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE))
            pw.println("Pattern,InstanceLoc,Injection")
            for (Treatment t : treats)
            {
                pw.println(t.toString())
            }
        }
        catch (IOException e)
        {
            e.printStackTrace()
        } finally {
            pw?.close()
        }
    }

    static class Treatment {
        String pattern
        String instanceLoc
        String treatment

        Treatment(String pattern, String instanceLoc, String treatment)
        {
            this.pattern = pattern
            this.instanceLoc = instanceLoc
            this.treatment = treatment
        }

        String toString()
        {
            return String.format("%s,%s,%s", pattern, instanceLoc, treatment)
        }
    }
}
