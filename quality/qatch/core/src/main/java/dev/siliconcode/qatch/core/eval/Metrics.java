package dev.siliconcode.qatch.core.eval;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Equivalent to issues. Representation of CKJM results in the java program.
 *
 * <p>Typically, this class holds all the metrics of a certain class of the whole project.
 *
 * <p>This metrics can be found inside ckjmResult.xml between the tags &lt;class&gt; and
 * &lt;/class&gt;.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class Metrics {

  @Getter @Setter private String className;
  @Getter @Setter private List<MethodLevelAttributes> methods;
  @Getter private final Map<String, Double> metrics;

  /**
   * Constructs a new metrics object for an evaluation over the named class
   *
   * @param className Name of class that was evaluated, cannot be null
   */
  public Metrics(@NonNull String className) {
    this.className = className;
    this.metrics = Maps.newHashMap();
    this.methods = Lists.newArrayList();
  }

  /**
   * Retrieves the value of the named metric
   *
   * @param metric Name of metric, cannot be null
   * @return Value of named metric
   * @throws IllegalArgumentException if the provided metric is unknown
   */
  public double get(@NonNull String metric) throws IllegalArgumentException {
    if (!metrics.containsKey(metric.toLowerCase()))
      throw new IllegalArgumentException("Unknown metric");
    return metrics.get(metric.toLowerCase());
  }

  /**
   * Updates the named metric with the given value
   *
   * @param metric Name of metric, cannot be null or empty
   * @param value Value
   * @throws IllegalArgumentException if the provided metric name is empty
   */
  public void set(@NonNull String metric, double value) throws IllegalArgumentException {
    if (metric.isEmpty()) throw new IllegalArgumentException("Metric name cannot be empty");
    metrics.put(metric.toLowerCase(), value);
  }
}
