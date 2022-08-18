package dev.siliconcode.qatch.projectimport;

import dev.siliconcode.qatch.projectimport.generators.JavaProjectGenerator;
import dev.siliconcode.qatch.projectimport.generators.PythonProjectGenerator;
import picocli.CommandLine;

import java.util.List;
import java.util.Objects;

/**
 * Main class of the program
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ProjectImporter {

  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_RESET = "\u001B[0m";

  /**
   * Command line entry point of the program
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    PIContext context = new PIContext();
    CommandLine.populateCommand(context, args);

    if (context.isHelpRequested() || context.isVersionRequested()) {
      if (context.isHelpRequested())
        CommandLine.usage(context, System.out);
      System.exit(0);
    }

    ProjectImporter importer = new ProjectImporter();
    importer.exec(context);
  }

  /**
   * Executes the tool
   *
   * @param context the global configuration and execution context for the program
   */
  public void exec(PIContext context) {
    System.out.println(ANSI_BLUE + "  ___          _        _   ___                     _" + ANSI_RESET + "\n" +
                       ANSI_BLUE + " | _ \\_ _ ___ (_)___ __| |_|_ _|_ __  _ __  ___ _ _| |_ ___ _ _" + ANSI_RESET + "\n" +
                       ANSI_BLUE + " |  _/ '_/ _ \\| / -_) _|  _|| || '  \\| '_ \\/ _ \\ '_|  _/ -_) '_|" + ANSI_RESET + "\n" +
                       ANSI_BLUE + " |_| |_| \\___// \\___\\__|\\__|___|_|_|_| .__/\\___/_|  \\__\\___|_|" + ANSI_RESET + "\n" +
                       ANSI_BLUE + "            |__/                     |_|" + ANSI_RESET + "\n" +
                       "" +
                       ANSI_YELLOW + "         http://empirilytics.com/qatch/project_importer" + ANSI_RESET + "\n\n");

    if (context.isCombineResults()) {
      DataCombiner combiner = new DataCombiner();
      combiner.combineData(context.getInput(), context.getResultsPath(), context.getOutputFile());
    } else {
      DataReader reader = new DataReader();
      List<Submission> submissions = reader.readData(context.getInput(), context.getLanguage());
      ProjectGenerator generator = Objects.equals(context.getLanguage(), "python") ? new PythonProjectGenerator() : new JavaProjectGenerator();
      generator.generate(context.getWorkspacePath(), submissions);
    }
  }
}
