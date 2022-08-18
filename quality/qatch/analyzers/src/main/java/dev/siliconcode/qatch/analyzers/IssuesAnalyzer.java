package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;

/**
 * This is an abstract class that describes the minimum functionality that an Analyzer should have
 * in order to work properly.
 *
 * <p>e.g. PMDAnalyzer, CKJMAnalyzer etc.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface IssuesAnalyzer {

  /**
   * This method is responsible for the execution of the desired static analysis on the desired
   * project.
   *
   * @param src The path to the project to be analyzed, cannot be null
   * @param dest The path to the destination folder where the results will be placed, cannot be null
   * @param ruleset The ruleset for use by the tool for analysis, cannot be null
   * @param filename Name of the file to which analysis results are to be stored, cannot be null
   */
  void analyze(
      @NonNull String src, @NonNull String dest, @NonNull String ruleset, @NonNull String filename);

  /**
   * This method is responsisble for the execution of the desired static analysis on the desired
   * project
   *
   * @param src The path to the project to be analyzed, cannot be null
   * @param dest The path to the destination folder where the results will be placed, cannot be null
   * @param properties The set of properties to be analyzed, cannot be null
   */
  void analyze(@NonNull String src, @NonNull String dest, @NonNull PropertySet properties);

  /** @return The name of the tool, as used in model definition files */
  String getToolName();
}
