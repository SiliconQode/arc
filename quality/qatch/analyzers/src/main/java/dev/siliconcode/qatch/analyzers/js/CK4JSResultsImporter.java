package dev.siliconcode.qatch.analyzers.js;

import dev.siliconcode.qatch.analyzers.MetricsImporter;
import dev.siliconcode.qatch.core.eval.MetricSet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for importing all the metrics that the ck4js tool calculates for a
 * given project, into a MetricSet object.
 *
 * <p>Each MetricSet object contains all the metrics of a certain component of the
 * Project.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CK4JSResultsImporter implements MetricsImporter {

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
