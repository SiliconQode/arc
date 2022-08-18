package dev.siliconcode.arc.metrics.ckjm.app;

import dev.siliconcode.arc.metrics.ckjm.core.MetricsFilter;
import dev.siliconcode.arc.metrics.ckjm.core.io.CkjmOutputHandler;
import dev.siliconcode.arc.metrics.ckjm.core.io.OutputHandlerFactory;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author IsaacG
 * @version 2.5
 */
@Log4j2
public class Main {

  private static List<Path> classes;

  /**
   * The filter's main body.
   * Process command line arguments and the standard input.
   */
  public static void main(String[] args) throws IOException {
    CLI cli = CLI.INST;
    classes = Lists.newArrayList();

    cli.init();

    cli.parseArgs(args);

    CkjmOutputHandler consoleHandler = OutputHandlerFactory.instance().getConsoleHandler(cli.isUseStandardIO(), cli.getOutputType());
    ;
    CkjmOutputHandler fileHandler = OutputHandlerFactory.instance().getFileHandler(cli.getOutputFile(), cli.getOutputType());
    ;

    if (cli.getSourceDirectory() != null) {
      List<Path> paths = findClassesAndJarFiles(cli.getSourceDirectory());
      paths.forEach((path) -> {
        try {
          addClass(path.toAbsolutePath().toString());
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      });
    }
    if (cli.getOtherArgs() != null) {
      cli.getOtherArgs().forEach((arg) -> {
        try {
          addClass(arg);
        } catch (IOException e) {

        }
      });
    }
    if (cli.getSourceDirectory() == null && cli.getOtherArgs() == null) {
      readClassesFromStdio();
    }

    runMetrics(classes, consoleHandler, fileHandler, cli.isIncludeJDK(), cli.isOnlyPublic());

    if (consoleHandler != null) consoleHandler.close();
    if (fileHandler != null) fileHandler.close();
  }

  /**
   * The interface for other Java based applications.
   * Implement the outputconsoleHandler to catch the results
   *
   * @param files                Class files to be analyzed
   * @param outputconsoleHandler An implementation of the CkjmOutputconsoleHandler interface
   */
  public static void runMetrics(List<Path> files, CkjmOutputHandler consoleHandler, CkjmOutputHandler fileHandler, boolean includeJDK, boolean onlyPublic) {
    MetricsFilter mf = new MetricsFilter(includeJDK, onlyPublic);
    mf.runMetricsInternal(files, consoleHandler, fileHandler);
  }

  private static List<Path> findClassesAndJarFiles(String pathStr) throws IOException {
    List<Path> list = Lists.newArrayList();
    for (String str : pathStr.split(";")) {
      Path path = Paths.get(str);
      if (!Files.isDirectory(path)) {
        throw new IllegalArgumentException("Source Directory must be a directory!");
      }

      list.addAll(Files.find(path, Integer.MAX_VALUE, (p, attrs) -> (p.getFileName().toString().endsWith(".class"))).toList());
    }

    return list;
  }

  private static void addClass(String colonSeparatedNames) throws IOException {
    List<String> names = Arrays.asList(colonSeparatedNames.split(";"));
    names.forEach(p -> {
      Path path = Paths.get(p);
      if (Files.isRegularFile(path)) classes.add(path);
      else {
        try {
          classes.addAll(findClassesAndJarFiles(path.toString()));
        } catch (IOException ex) {
          System.out.println(ex.getMessage());
        }
      }
    });
  }

  public static List<Path> getClassNames() {
    return classes;
  }

  private static void readClassesFromStdio() throws IOException {
    System.out.println("Please enter fully qualified names of the java classes to analyse.");
    System.out.println("Each class should be entered in separate line.");
    System.out.println("After the last class press enter to continue.");
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    try {
      String s;
      while ((s = in.readLine()) != null && s.length() > 0) addClass(s);
    } catch (Exception e) {
      log.error("Error reading line: " + e);
      System.exit(1);
    }
  }
}
