package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data class representing the concept of a measure
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class Measure {

  /** Tool used to evaluate the measure */
  @XStreamAlias("tool")
  @XStreamAsAttribute
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  @ToString.Include
  protected String tool;
  //	private int normalizer;

  /** construct a new measure with a null tool */
  public Measure() {
    this(null);
  }

  /**
   * Consructs a new measure object with the given tool
   *
   * @param tool Name of the tool use to evaluate the measure
   */
  public Measure(String tool) {
    this.tool = tool;
  }

  /**
   * @return true if this measure is a metric, false otherwise
   */
  public abstract boolean isMetric();

  /**
   * @return true if this measure is a finding, false othewise
   */
  public abstract boolean isFinding();
}
