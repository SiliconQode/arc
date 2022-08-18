package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.*;

/**
 * Data class representing teh concept of a property
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Property {

  /** The breakpoint of the Utility Function used for the evaluation of the Property */
  private static final double BREAKPOINT = 0.5;

  /** Maximum number of thresholds for any given property */
  public static final int THRESHOLDS_NUM = 3;

  /** The name of the property */
  @XStreamAlias("name")
  @XStreamAsAttribute
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  @ToString.Include
  private String name;

  /** A brief description of the property (optional) */
  @XStreamAlias("description")
  @XStreamAsAttribute
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  private String description;

  /** If this field is true then the metric has a positive impact on the property */
  @XStreamAlias("positiveImpact")
  @XStreamAsAttribute
  @Getter
  @Setter
  private boolean positive;

  /** The measure used to evaluate this property */
  @Getter
  @Setter
  @XStreamAlias("measure")
  private Measure measure;

  /** The three thresholds of the property metric, needed for the evaluation */
  @Getter @Setter private Thresholds thresholds;

  /**
   * The profile of this Property (SIG Model for metrics - just violation counter per severity
   * category for PMD)
   */
  @Getter @Setter @XStreamOmitField private double[] profile;

  /** Constructs a new property with a null measure and empty threshold set */
  public Property() {
    this(null);
  }

  /**
   * Constructs a new Property with the given measure for evaluation and an empty name, empty
   * description, and an empty set of thresholds
   *
   * @param measure Measure used to evaluate this property
   */
  public Property(Measure measure) {
    this("", "", measure);
  }

  /**
   * Constructs a new Property with the given name, description, measure and a new empty set of
   * thresholds
   *
   * @param name Name of the property
   * @param description Description of the property
   * @param measure Measure of the property
   */
  public Property(String name, String description, Measure measure) {
    this.name = name;
    this.description = description;
    this.measure = measure;
    thresholds = new Thresholds(THRESHOLDS_NUM);
  }

  /**
   * Checks whether this property is evaluated by a finding or not
   *
   * @return true if the associated measure is a Finding, false otherwise
   */
  public boolean isFinding() {
    return measure.isFinding();
  }

  /**
   * Retrieves the threshold at the provided index, if the index is in range (0, size] where size is
   * the number of thresholds
   *
   * @param index index of the threshold
   * @return threshold at the given index
   */
  public double getThreshold(int index) {
    return thresholds.get(index);
  }

  /**
   * Adds the given value as a threshold
   *
   * @param weight new threshold to add, cannot be null
   */
  public void add(@NonNull Double weight) {
    thresholds.add(weight);
  }

  /**
   * Add all the provided values as thresholds
   *
   * @param values Threshold values, cannot be null
   */
  public void addAll(@NonNull Double... values) {
    thresholds.addAll(values);
  }

  /** Remove all thresholds */
  public void clearThresholds() {
    thresholds.clear();
  }

  /**
   * Number of current thresholds
   *
   * @return Number of current thresholds
   */
  public int thresholdsSize() {
    return thresholds.size();
  }
}
