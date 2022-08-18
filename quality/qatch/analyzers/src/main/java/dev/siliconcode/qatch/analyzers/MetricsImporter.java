package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.core.eval.MetricSet;
import lombok.NonNull;

/**
 * Base interface for metrics data importers
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface MetricsImporter {

  /**
   * The method that is used to parse the calculated metrics from the results file containing the
   * results of the metrics tool for the desired project.
   *
   * @param path The exact path to the results file, cannot be null
   * @return the set of metrics imported
   */
  MetricSet parseMetrics(@NonNull String path);

  /**
   * @return The file name from which measures are to be imported
   */
  String getFileName();
}
