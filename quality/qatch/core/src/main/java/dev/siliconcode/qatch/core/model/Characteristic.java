package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

/**
 * This class represents a quality characteristic of the Quality Model that is used in order to
 * evaluate a project or a set of projects.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class Characteristic extends AbstractWeightedObject {

  /** The name of the characteristic */
  @XStreamAlias("name")
  @XStreamAsAttribute
  @Getter
  @Setter
  private String name;

  /** The standard from which this characteristic derives */
  @XStreamAlias("standard")
  @XStreamAsAttribute
  @Getter
  @Setter
  private String standard;

  /** A brief description of the characteristic */
  @XStreamAlias("description")
  @XStreamAsAttribute
  @Getter
  @Setter
  private String description;

  /** Constructs a new Characteristic object with all fields initialized to empty values */
  public Characteristic() {
    this("", "", "");
  }

  /**
   * Constructs a new Charactersitic object with the given name, standard, description, and an empty
   * set of weights
   *
   * @param name The name of the characteristic
   * @param standard The standard from which it derives
   * @param description The description of the characteristic
   */
  public Characteristic(String name, String standard, String description) {
    this(name, standard, description, new Weights());
  }

  /**
   * Constructs a new Characteristic object with the given name, standard, description, and set of
   * weights
   *
   * @param name The name of the characteristic
   * @param standard The standard from which it derives
   * @param description The description of this characteristic
   * @param weights The set of weights of this characteristic to the set of properties in the model
   */
  public Characteristic(String name, String standard, String description, Weights weights) {
    this.name = name;
    this.description = description;
    this.standard = standard;
    this.weights = weights;
  }

  /**
   * This method is used in order to calculate the eval field of this Characteristic object, based
   * 1. on the eval fields of the properties that have an impact on this characteristic and 2. the
   * weights that quantify those impacts.
   *
   * <p>Typically, it calculates the weighted average of the values of the eval fields of the
   * project properties and stores it to the eval field of this characteristic.
   *
   * <p>ATTENTION: - The order in which the weights are placed inside the weight vector corresponds
   * to the order of the Properties of the Quality Model.
   *
   * @param properties The set of properties to evaluate, cannot be null
   * @param propEvals The current set of property evaluations, cannot be null
   * @return The value of the evaluation
   */
  public double evaluate(@NonNull PropertySet properties, @NonNull Map<Property, Double> propEvals) {
    double sum = 0;
    for (int i = 0; i < weights.size(); i++) {
      sum += propEvals.get(properties.get(i)) * weights.get(i);
    }
    return sum;
  }
}
