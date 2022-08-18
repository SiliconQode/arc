package dev.siliconcode.qatch.analyzers.cpp;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.Project;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for calculating the value of the porperties that are quantified by
 * the CqMetrics static analysis tool.
 * <p>
 * Typically:
 * <ul>
 *  <li>It calculates the weighted sum of each metric against all the components
 *      of the project (component loc is used as weight)</li>
 *  <li>It keeps only the values of the properties that we are interested in</li>
 * </ul>
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CqmetricsAggregator implements Aggregator {

  /** {@inheritDoc} */
  @Override
  public void aggregate(@NonNull Project project) {

  }
}
