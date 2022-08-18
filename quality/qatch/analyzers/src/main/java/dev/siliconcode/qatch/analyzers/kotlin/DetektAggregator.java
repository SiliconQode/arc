package dev.siliconcode.qatch.analyzers.kotlin;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.Project;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Aggregates the results of a Detekt analysis into a project.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class DetektAggregator implements Aggregator {

  /** {@inheritDoc} */
  @Override
  public void aggregate(@NonNull Project project) {

  }
}
