package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.MetricSet;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Metric;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Responsible for calculating the value of the properties that are quantified
 * by the CKPM static analysis tool.
 * <p>
 * Typically:
 * <ul>
 *  <li>It calculates the weighted sum of each metric against all the components
 *      of the project (component LOC is used as the weight)</li>
 *  <li>It keeps only the values of the properties we are interested in.</li>
 * </ul>
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CKPMAggregator implements Aggregator {

  /** {@inheritDoc} */
  @Override
  public void aggregate(@NotNull Project project) {
    MetricSet metricSet = project.getMetrics();

    AtomicReference<Double> totalLoc = new AtomicReference<>((double) 0);
    Map<String, Double> metricMap = Maps.newHashMap();

    metricSet.stream().forEach(metrics -> {
      double loc = metrics.get("sloc");
      if (metrics.getClassName().endsWith(".py")) {
        totalLoc.updateAndGet(v -> v + loc);
      }

      metrics.getMetrics().forEach((k, v) -> {
        if (k.equalsIgnoreCase("loc") || k.equalsIgnoreCase("bloc") || k.equalsIgnoreCase("lloc") ||
                k.equalsIgnoreCase("sloc") || k.equalsIgnoreCase("mcloc") || k.equalsIgnoreCase("scloc")) {
          if (metricMap.containsKey(k)) metricMap.put(k, metricMap.get(k) + v);
          else metricMap.put(k, v);
        } else {
          if (metricMap.containsKey(k)) metricMap.put(k, metricMap.get(k) + (v * loc));
          else metricMap.put(k, v * loc);
        }
      });
    });

    project.getProperties().stream().forEach(prop -> {
      project.setPropertyMeasureNormalizer(prop, totalLoc.get());

      if (prop.getMeasure().getTool().equalsIgnoreCase("ckpm")) {
        Metric measure = (Metric) prop.getMeasure();
        if (metricMap.containsKey(measure.getMetricName()))
          project.setPropertyMeasureValue(prop, metricMap.get(measure.getMetricName()));
      }
    });
  }
}
