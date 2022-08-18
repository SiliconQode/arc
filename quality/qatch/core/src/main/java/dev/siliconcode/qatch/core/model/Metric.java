package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A class representing a metric type of measure
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("metric")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Metric extends Measure {

  /** Name of the metric */
  @XStreamAlias("metricName")
  @XStreamAsAttribute
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  private String metricName;

  /** Consructs a new metric with no name or tool */
  public Metric() {
    this(null, null);
  }

  /**
   * Constructs a new Metric with the given name and tool
   *
   * @param name Name of the metric
   * @param tool Tool used
   */
  public Metric(String name, String tool) {
    super(tool);
    this.metricName = name;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isMetric() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isFinding() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("Metric: %s, Tool: %s", getMetricName(), getTool());
  }
}
