package dev.siliconcode.arc.metrics.ckjm.app;

import lombok.Getter;
import org.apache.commons.cli.*;
import java.util.List;
import dev.siliconcode.arc.metrics.ckjm.core.io.OutputType;

/**
 * TODO Shift this to the picocli implementation
 *
 * @author Isaac Griffith
 * @version 2.5
 */
public enum CLI {
  INST;

  private Options options = new Options();
  private CommandLineParser parser = new DefaultParser();
  private HelpFormatter formatter = new HelpFormatter();

  @Getter
  private boolean useStandardIO = true;
  @Getter
  private String sourceDirectory = null;
  @Getter
  private List<String> otherArgs = null;
  private boolean useXMLOutput = false;
  private boolean useJsonOutput = false;
  @Getter
  private boolean includeJDK = false;
  @Getter
  private boolean onlyPublic = false;
  @Getter
  private String outputFile = null;

  public void init() {
    options.addOption("s", "include-jdk", false, "Include JDK");
    options.addOption("p", "only-public", false, "Only analyze public methods");
    options.addOption("x", "xml-output", false, "Use xml output.");
    options.addOption("d", "src-dir", true, "Source directory containing class files or jar files to evaluate");
    options.addOption("o", "output", true, "Name of the file to output to");
    options.addOption("h", "help", false, "display this message");
    options.addOption("n", "no-console-output", false, "Do not use console output (default enabled)");
    options.addOption("j", "json-output", false, "Use json output.");

    formatter.setWidth(120);
  }

  public void parseArgs(String[] args) {
    try {
      CommandLine cmd = parser.parse(options, args);
      if (cmd.hasOption("j")) {
        useJsonOutput = true;
      }
      if (cmd.hasOption("x")) {
        useXMLOutput = true;
      }
      if (cmd.hasOption("s")) {
        sourceDirectory = cmd.getOptionValue("s");
      }
      if (cmd.hasOption("o")) {
        outputFile = cmd.getOptionValue("o");
      }
      if (cmd.hasOption("h")) {
        displayHelp();
        System.exit(0);
      }
      if (cmd.hasOption("p")) {
        onlyPublic = true;
      }
      if (cmd.hasOption("n")) {
        useStandardIO = false;
      }
      if (cmd.hasOption("d")) {
        sourceDirectory = cmd.getOptionValue("d");
      }

      otherArgs = cmd.getArgList();

    } catch (Exception e) {
      displayHelp();
      System.exit(0);
    }
  }

  public void displayHelp() {
    formatter.printHelp("ckjm-ext", "\nEvaluates a Java project using the CK metrics suite (plus some additional metrics)\n\n", options, "\nCopyright (c) 2022, SiliconCode, LLC", true);
  }

  public OutputType getOutputType() {
    if (useXMLOutput)
      return OutputType.XML;
    else if (useJsonOutput)
      return OutputType.JSON;
    else
      return OutputType.PLAIN;
  }
}
