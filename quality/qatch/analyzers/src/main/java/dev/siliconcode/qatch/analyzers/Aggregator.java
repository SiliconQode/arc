package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.core.eval.Project;
import lombok.NonNull;

/**
 * Base interface for tool results aggregation
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface Aggregator {

  /**
   * Aggregates data from a tool for the provided project
   *
   * @param project Project to which the aggregated data belongs, cannot be null
   */
  void aggregate(@NonNull Project project);
}
