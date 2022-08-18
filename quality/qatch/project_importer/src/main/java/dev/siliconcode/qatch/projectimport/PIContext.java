package dev.siliconcode.qatch.projectimport;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.nio.file.Paths;

/**
 * The global configuration and exeuction context of the program
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@CommandLine.Command(
        name = "importer",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage|@:%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        header = "The Qatch Quality Evaluation Project Importer System.",
        description = "Handles working with the results from extracted submissions from the CodeSignal Db",
        footerHeading = "%n",
        footer = "Copyright (c) 2022 Empirilytics",
        version = {"Qatch Project Importer version 2.0.0", "Copyright (c) 2022 Empirilytics"},
        usageHelpAutoWidth = true,
        usageHelpWidth = 120,
        helpCommand = false,
        mixinStandardHelpOptions = false)
@Log4j2
public class PIContext {

  @Option(
          names = {"-h", "--help"},
          usageHelp = true,
          description = "Display this message and exit"
  )
  @Getter
  private boolean helpRequested = false;

  @Option(
          names = {"-v", "--version"},
          versionHelp = true,
          description = "Print version information and exit")
  @Getter
  boolean versionRequested = false;

  @Option(
          names = {"-i", "--input-file"},
          paramLabel = "PATH",
          description = "Path to the input json file",
          required = true
  )
  @Getter
  private String input;

  @Option(
          names = {"-w", "--workspace"},
          paramLabel = "PATH",
          description = "Path to the workspace")
  @Setter
  private String workspacePath = null;

  @Option(
          names = {"-r", "--results"},
          paramLabel = "PATH",
          description = "Base path to results")
  @Setter
  private String resultsPath = null;

  @Option(
          names = {"-o", "--output-file"},
          paramLabel = "FILE",
          description = "Path to the output file for combined results")
  @Setter
  private String outputFile = null;

  @Option(
          names = {"-c", "--combine-results"},
          description = "Combine results from multiple project analysis"
  )
  @Getter
  private boolean combineResults = false;

  @Option(
          names = {"-l", "--language"},
          paramLabel = "LANG",
          description = "language to extract"
  )
  @Getter
  private String language;

  /** @return the normalized absolute path of the project workspace */
  public String getWorkspacePath() {
    return Paths.get(workspacePath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path where results are stored */
  public String getResultsPath() {
    return Paths.get(resultsPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path where the output is to be written */
  public String getOutputFile() {
    return Paths.get(outputFile).toAbsolutePath().normalize().toString();
  }
}
