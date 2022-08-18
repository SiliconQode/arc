package dev.siliconcode.qatch.analyzers.cpp;

import dev.siliconcode.qatch.analyzers.MetricsImporter;
import dev.siliconcode.qatch.core.eval.MetricSet;
import lombok.NonNull;

/**
 * Responsible for importing all the metrics that the cqmetrics tool calculates
 * for a given project, into a MetricSet object.
 *
 * <p>
 * Each object of the MetricSet contains all the metrics of a component from the
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CqmetricsResultsImporter implements MetricsImporter {

  /** {@inheritDoc} */
  @Override
  public MetricSet parseMetrics(@NonNull String path) {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String getFileName() {
    return null;
  }
}
