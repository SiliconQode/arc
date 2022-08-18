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
public interface MetricsAnalyzer {

  /**
   * This method is responsible for the execution of the desired static analysis on the desired
   * project.
   *
   * @param src The path to the project to be analyzed, cannot be null
   * @param dest The path to the destination folder where the results will be placed, cannot be null
   */
  void analyze(@NonNull String src, @NonNull String dest);

  /**
   * This method is responsible for the execution of the desired static analysis on the desired
   * project, for the provided PropertySet
   *
   * @param src The path to the project to be analyzed, cannot be null
   * @param dest The path to the destination folder where the results will be placed, cannot be null
   * @param properties The properties to be evaluated, cannot be null
   */
  void analyze(@NonNull String src, @NonNull String dest, @NonNull PropertySet properties);

  /** @return the name of the metrics tool as used in the quality model definition */
  String getToolName();
}
