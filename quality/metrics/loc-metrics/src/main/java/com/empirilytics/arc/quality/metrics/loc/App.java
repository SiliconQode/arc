/**
 * The MIT License (MIT)
 *
 * Empirilytics Lines of Code Metrics Tool
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
package com.empirilytics.arc.quality.metrics.loc;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.empirilytics.arc.quality.metrics.LoCProfile;
import com.empirilytics.arc.quality.metrics.LoCProfileManager;
import com.empirilytics.arc.quality.metrics.UnknownProfileException;
import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.List;
import java.util.Queue;

/**
 * Command line interface for the SparQLine Analytics LoC Counter tool.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class App {

    /**
     * Logger associated with this class
     */
    private static final Logger  LOG = LoggerFactory.getLogger(App.class);
    /**
     * Command line options object associated with this class.
     */
    private static final Options options;

    /**
     * Initializes the command line options object.
     */
    static
    {
        final Option help = Option.builder("h")
                .required(false)
                .longOpt("help")
                .desc("prints this message")
                .hasArg(false)
                .build();
        final Option output = Option.builder("o")
                .required(false)
                .longOpt("output")
                .argName("FOLDER")
                .desc("Name of the folder to output revised quality models to")
                .hasArg(true)
                .numberOfArgs(1)
                .build();
        final Option print = Option.builder("p")
                .required(false)
                .longOpt("print")
                .desc("Prints the results to the console")
                .hasArg(false)
                .build();
        options = new Options();
        App.options.addOption(help);
        App.options.addOption(output);
        App.options.addOption(print);
    }

    /**
     * Controls the execution of the LoCCounter given the command line object.
     *
     * @param line
     *            The parsed command line arguments.
     */
    public static void execute(final CommandLine line)
    {
        if (line.getOptions().length == 0 || line.hasOption('h'))
        {
            printHelp();
            return;
        }

        List<String> args = line.getArgList();
        List<Path> files = Lists.newArrayList();
        for (String arg : args)
        {
            files.addAll(getFilesList(arg));
        }

        LoCCounter counter = new LoCCounter();
        measure(counter, files);

        if (line.hasOption('o'))
        {
            final String out = line.getOptionValue('o');
            outputCSV(counter, out);
        }

        if (line.hasOption('p'))
        {
            printMetrics(counter);
        }
    }

    /**
     * Measures the provided list of files with the provided LoCCounter.
     *
     * @param counter
     *            LoCCounter used to measure
     * @param files
     *            List of Paths of the files to be measured
     */
    private static void measure(LoCCounter counter, List<Path> files)
    {
        LOG.info("Analyzing collected files and measuring LoC metrics...");
        LoCProfileManager manager = LoCProfileManager.getInstance();

        LoCProfileManager.loadProfiles();
        files.forEach((path) -> {
            String ext = getExtension(path.getFileName());
            try
            {
                LoCProfile profile = manager.getProfileByExtension(ext);
                counter.setProfile(profile);
                List<String> lines = Files.readAllLines(path);
                counter.updateCounts(lines);
            }
            catch (UnknownProfileException | IOException e)
            {

            }
        });
    }

    /**
     * Extracts the extension of a filename
     *
     * @param fileName
     *            Filename path to process
     * @return The extension of the file or the empty string if no such
     *         extension exists.
     */
    private static String getExtension(Path fileName)
    {
        if (fileName == null)
            return "";

        String name = fileName.toString();
        String[] split = name.split("\\.");
        if (split.length > 1)
        {
            return split[split.length - 1];
        }

        return "";
    }

    /**
     * Extracts a list of files from given a path string.
     *
     * @param arg
     *            Path string from the command line arguments.
     */
    private static List<Path> getFilesList(String arg)
    {
        LOG.info("Collecting files for processing...");
        List<Path> files = Lists.newArrayList();
        Queue<Path> directories = Queues.newArrayDeque();

        Path p = Paths.get(arg);
        if (Files.exists(p))
        {
            if (Files.isDirectory(p))
            {
                directories.offer(p);
                while (!directories.isEmpty())
                {
                    Path qp = directories.poll();
                    try (DirectoryStream<Path> ds = Files.newDirectoryStream(qp))
                    {
                        ds.forEach((path) -> {
                            if (Files.isDirectory(path))
                            {
                                directories.offer(path);
                            }
                            else
                            {
                                files.add(path);
                            }
                        });
                    }
                    catch (IOException e)
                    {

                    }
                }
            }
            else
            {
                files.add(p);
            }
        }

        return files;
    }

    /**
     * Starting point of execution.
     *
     * @param args
     *            Raw command line arguments.
     */
    public static void main(final String... args)
    {
        final CommandLineParser parser = new DefaultParser();
        try
        {
            final CommandLine line = parser.parse(App.options, args);
            execute(line);
        }
        catch (final ParseException exp)
        {
            printHelp();
        }
    }

    /**
     * Prints the help message
     */
    private static void printHelp()
    {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "sqloc", "Count the lines in source files.", App.options, "\nSparQLine Analytics, LLC (C) 2017",
                true);
    }

    /**
     * Output the results of the LoC analysis from the given LoCCounter to a
     * file with path provided by the path string.
     *
     * @param counter
     *            Counter holding the results.
     * @param out
     *            Path of the output file.
     */
    private static void outputCSV(LoCCounter counter, String out)
    {
        LOG.info("Writing measures to file: " + out + " ...");
        Path path = Paths.get(out);

        try
        {
            Files.deleteIfExists(path);
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try (CSVPrinter printer = new CSVPrinter(
                new PrintWriter(Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)),
                CSVFormat.DEFAULT))
        {
            // Print Headers
            List<String> headers = counter.getHeaders();
            printer.printRecord("Language", headers);

            // Print Language records
            for (String lang : counter.getLanguages())
            {
                Object[] record = new Object[headers.size() + 1];
                record[0] = lang;
                int index = 1;
                for (String head : headers)
                {
                    record[index] = counter.valueAt(lang, head);
                    index++;
                }

                printer.printRecord(record);
            }

            // Print totals
            Object[] record = new Object[headers.size() + 1];
            record[0] = "Total";
            int index = 1;
            for (String header : headers)
            {
                record[index] = counter.totalAt(header);
                index++;
            }
            printer.printRecord(record);
        }
        catch (IOException e)
        {

        }
    }

    /**
     * Prints the metrics collected to the console
     *
     * @param counter
     *            LoCCounter holding name values
     */
    private static void printMetrics(LoCCounter counter)
    {
        System.out.println();
        // Print Headers
        List<String> headers = counter.getHeaders();
        System.out.print("Language ");
        for (String head : headers)
        {
            System.out.printf("%-9.9s", head);
        }
        System.out.print("\n");
        System.out.println("================================================================================");

        // Print Language records
        for (String lang : counter.getLanguages())
        {
            System.out.printf("%-9.9s", lang);
            for (String head : headers)
            {
                System.out.printf("%-9.9s", counter.valueAt(lang, head));
            }
            System.out.print("\n");
        }
        System.out.println("================================================================================");

        // Print totals
        System.out.printf("%-9.9s", "Total");
        for (String header : headers)
        {
            System.out.printf("%-9.9s", counter.totalAt(header));
        }
        System.out.print("\n");
    }
}
