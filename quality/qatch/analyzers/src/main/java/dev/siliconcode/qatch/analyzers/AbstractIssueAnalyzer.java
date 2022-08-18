package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.analyzers.java.PMDAnalyzer;
import dev.siliconcode.qatch.core.model.Finding;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Base class containing the common behavior for all IssueAnalyzers.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public abstract class AbstractIssueAnalyzer implements IssuesAnalyzer {

  /** path to the associated tool */
  protected String toolPath;
  /** path to the ruleset for this tool */
  protected String rulesetPath;
  /** path to the results directory */
  protected String resultsPath;

  /**
   * Constructs a new AbstractIssueAnalyzer
   *
   * @param path path to the tool used by this analyzer, cannot be null
   * @param resultsPath path to the results directory, cannot be null
   * @param rulesetPath path to the rulesets, cannot be null
   */
  public AbstractIssueAnalyzer(@NonNull String path, @NonNull String resultsPath, @NonNull String rulesetPath) {
    this.toolPath = path;
    this.resultsPath = resultsPath;
    this.rulesetPath = rulesetPath;
  }

  /**
   * This method is used in order to analyze a single project against a certain ruleset (property)
   * by calling the tool through the command line with the appropriate configuration.
   *
   * @param src The path to the project to be analyzed, cannot be null
   * @param dest The path to the destination folder where the results will be placed, cannot be null
   * @param ruleset The ruleset for use by the tool for analysis, cannot be null
   * @param filename Name of the file to which analysis results are to be stored, cannot be null
   */
  @Override
  public void analyze(
          @NotNull String src,
          @NotNull String dest,
          @NotNull String ruleset,
          @NotNull String filename) {

    Path path = Paths.get(dest);
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }

    CommandLine cmdLine = constructCommandLine(src, dest, ruleset, filename);

    try {
      DefaultExecutor executor = new DefaultExecutor();
      executor.setExitValues(getExitValues());
      executor.setWorkingDirectory(new File(src));
      int exitValue = executor.execute(cmdLine);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * This method is responsible for analyzing a single project against a set of properties (i.e.
   * rulesets) by using the associated tool.
   *
   * <p>Typically this method does the following:
   *
   * <ol>
   *   <li>Iterates through the PropertySet
   *   <li>For each Property object the method calls the analyze() method in order to analyze the
   *       project against this single property.
   * </ol>
   *
   * @param src The path of the folder that contains the sources of the project.
   * @param dest The path where the XML files with the results will be placed.
   * @param properties The set of properties against which the project will be analyzed.
   */
  @Override
  public void analyze(@NotNull String src, @NotNull String dest, @NotNull PropertySet properties) {
    properties.stream().forEach(p -> {
      if (checkIsTool(p)) {
        String ruleSet =
                Paths.get(rulesetPath, ((Finding) p.getMeasure()).getRulesetPath())
                        .toAbsolutePath()
                        .normalize()
                        .toString();
        analyze(src, dest, ruleSet, p.getName());
      }
    });
  }

  /**
   * Determines if the provided property is measured by the associated issue tool
   * and if the provided property is measured by a finding.
   *
   * @param p The property to evaluate
   * @return true if the provided property's measure is evaluated by a tool with
   *         the same name as the associated tool and this measure is a Finding.
   */
  protected boolean checkIsTool(Property p) {
    return p.getMeasure().getTool().equalsIgnoreCase(getToolName()) && p.getMeasure().isFinding();
  }

  /**
   * Constructs the command line used to execute the associated analysis tool.
   *
   * @param src The directory in which the tool is to execute, cannot be null
   * @param dest The directory to which the tool results should be stored, cannot be null
   * @param ruleset the path to the ruleset to be used in the analysis, cannot be null
   * @param filename the path of the file used to store the results, cannot be null
   * @return the CommandLine object that will be used to execute the tool
   */
  protected abstract CommandLine constructCommandLine(@NonNull String src, @NonNull String dest, @NonNull String ruleset, @NonNull String filename);

  /** @return an array of all the expected exit values for the provided tool */
  protected abstract int[] getExitValues();
}
