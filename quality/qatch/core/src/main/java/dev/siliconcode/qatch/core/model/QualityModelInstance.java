package dev.siliconcode.qatch.core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * An instance of a model, which is attached to a project The instance can then be evaluated without
 * concern for other projects
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class QualityModelInstance {

  @Getter @Setter private QualityModel parent;
  @Getter @Setter private InstanceData data;

  /**
   * Constructs a new QualityModelInstance object, for the provided parent quality model
   *
   * @param model Parent quality model from which this instance is derived
   */
  public QualityModelInstance(QualityModel model) {
    parent = model;
    data = new InstanceData();
    data.init(model);
  }

  /**
   * This method implements the whole functionality of this class. It iterates through the set of
   * characteristics of the certain project and calls the method evaluate() of each characteristic.
   */
  public void evaluateProjectCharacteristics() {
    getCharacteristics().stream()
        .forEach(ch -> data.addCharacteristicEval(ch, evaluateProjectCharacteristic(ch)));
  }

  /** A method for evaluating a project's properties */
  public void evaluateProjectProperties() {
    getProperties().stream()
        .forEach(prop -> data.addPropertyEval(prop, evaluateProjectProperty(prop)));
  }

  /**
   * A method for evaluating a Property object (i.e this property). In other words, this method,
   * calculates the eval field of a property based on the values of the following fields:
   *
   * <p>- positive : User defined or received by the QM.xml - normValue : Calculated by the
   * aggregator. - thresholds : Imported by ThresholdImporter or by the QM.xml file.
   *
   * <p>Typically, it simulates the Utility Function that is used for the evaluation of the
   * properties.
   *
   * @param property The property whose measure is to be evaluated
   * @return the value of the evaluation
   */
  public double evaluatePropertyMeasure(Property property) {
    Measure measure = property.getMeasure();
    double eval;
    /*
     * Check the sign of the impact that this property has on the total quality
     * and choose  the monotony of the utility function.
     */
    if (property.isPositive()) {
      return calculatePositiveEffect(measure, property);
    } else {
      return calculateNegativeEffect(measure, property);
    }
  }

  // If the metric has a positive impact on quality -> Ascending utility function
  double calculatePositiveEffect(Measure measure, Property property) {
    if (checkThreshold(measure, property, 0)) return 0; // Lower Group
    else if (checkThreshold(measure, property, 1)) {
      return (0.5 / (property.getThreshold(1) - property.getThreshold(0)))
          * (data.getNormValue(measure) - property.getThreshold(0)); // Middle Group
    } else if (checkThreshold(measure, property, 2)) {
      return 1
          - (0.5 / (property.getThreshold(2) - property.getThreshold(1)))
              * (property.getThreshold(2) - data.getNormValue(measure)); // Upper Group
    } else return 1; // Saturation
  }

  // If the metric has a negative impact on quality -> Descending utility function
  double calculateNegativeEffect(Measure measure, Property property) {
    if (roundDown4(data.getNormValue(measure)) <= property.getThreshold(0)) return 1; // Lower Group
    else if (roundDown4(data.getNormValue(measure)) <= property.getThreshold(1)) {
      return 1
          - (0.5 / (property.getThreshold(1) - property.getThreshold(0)))
              * (data.getNormValue(measure) - property.getThreshold(0)); // Middle Group
    } else if (roundDown4(data.getNormValue(measure)) <= property.getThreshold(2)) {
      return (0.5 / (property.getThreshold(2) - property.getThreshold(1)))
          * (property.getThreshold(2) - roundDown4(data.getNormValue(measure))); // Upper Group
    } else return 0; // Saturation
  }

  /**
   * Checks if the normalized value of the provided measure of the provided property is less than
   * the threshold of the property with the given index.
   *
   * @param measure the measure
   * @param property the property
   * @param threshold index of the the property's threshold
   * @return True if the normalized value of the measure is less than or equal to the indexed
   *     threshold of the given property
   */
  boolean checkThreshold(@NonNull Measure measure, @NonNull Property property, int threshold) {
    return data.getNormValue(measure) <= property.getThreshold(threshold);
  }

  /**
   * A method for keeping only the first four digits of a double number.
   *
   * <p>Used as a quick fix for precision difference spotted between the numbers calculated by Java
   * and those received from R Analysis.
   *
   * @param value to be rounded down
   * @return the rounded value
   */
  public static double roundDown4(double value) {
    return (long) (value * 1e4) / 1e4;
  }

  /** Normalizes all the associated measures in the model */
  public void normalizeMeasures() {
    parent.getProperties().stream().forEach(this::normalizeMeasure);
  }

  /** A method that calculates the total evaluation (TQI) of this Project. */
  public void calculateTQI() {
    /*
     * Just call the calculateTQI() method of the Tqi object in order to
     * calculate the value of the TQI from the eval field of the model's
     * characteristics
     */
    data.setTqiEval(getTqi().calculateTQI(getCharacteristics(), data.getCharacteristicEvals()));
  }

  /**
   * Retrieves the name of the parent quality model
   *
   * @return Parent quality model name
   */
  public String getName() {
    return parent.getName();
  }

  /**
   * Sets the name of the parent quality model
   *
   * @param name new parent quality model name, cannot be null
   */
  public void setName(@NonNull String name) {
    parent.setName(name);
  }

  /**
   * Retrieve the set of properties from the parent quality model
   *
   * @return Parent quality model propertis
   */
  public PropertySet getProperties() {
    return parent.getProperties();
  }

  /**
   * Update the parent qualitly model properties, be aware that this can affect other projects
   *
   * @param properties New property set for the parent quality model, cannot be null
   */
  public void setProperties(@NonNull PropertySet properties) {
    parent.setProperties(properties);
  }

  /**
   * Retrieve the set of characteristics from the parent quality model
   *
   * @return Parent quality model characteristics
   */
  public CharacteristicSet getCharacteristics() {
    return parent.getCharacteristics();
  }

  /**
   * Update the set of characteristics for the parent quality model. Note in a multi-project setting
   * this can affect all projects
   *
   * @param characteristics the new set of characteristics, cannot be null
   */
  public void setCharacteristics(@NonNull CharacteristicSet characteristics) {
    parent.setCharacteristics(characteristics);
  }

  /**
   * Retrieve the parent quality model TQI
   *
   * @return Parent quality model tqi
   */
  public Tqi getTqi() {
    return parent.getTqi();
  }

  /**
   * Update the parent quality model tqi, note this has an effect across all projects in a
   * multi-project setting
   *
   * @param tqi The new tqi, cannot be null
   */
  public void setTqi(@NonNull Tqi tqi) {
    parent.setTqi(tqi);
  }

  /**
   * Evaluate the tqi for this instance
   *
   * @return The tqi of this instance
   */
  public double getTqiEval() {
    return data.getTqiEval();
  }

  /**
   * Set the property measure normalizer for the given property to the provided total loc value.
   * This is only for this instance
   *
   * @param prop Property to set the measure normalizer for, cannot be null
   * @param totalLoc Total project LOC
   */
  public void setPropertyMeasureNormalizer(@NonNull Property prop, double totalLoc) {
    data.getMeasureNorms().put(prop.getMeasure(), totalLoc);
  }

  /**
   * Set the property measure value for the given property to the given value
   *
   * @param property Property for which the measure value is to be set, cannot be null
   * @param value The measure value
   */
  public void setPropertyMeasureValue(@NonNull Property property, double value) {
    data.getMeasureValues().put(property.getMeasure(), value);
  }

  /**
   * Normalize the measure value for the provided Property
   *
   * @param p Property to be normalized, cannot be null
   * @return The normalized value
   */
  public double normalizeMeasure(@NonNull Property p) {
    data.calculateNormValue(p.getMeasure());
    return data.getNormValue(p.getMeasure());
  }

  /**
   * Evaluate the provided project characteristic
   *
   * @param characteristic The characteristic to evaluate, cannot be null
   * @return Value of the characteristic
   */
  public double evaluateProjectCharacteristic(@NonNull Characteristic characteristic) {
    return characteristic.evaluate(getProperties(), data.getPropertyEvals());
  }

  /**
   * Evaluate the provided project property
   *
   * @param property The property to evaluate, cannot be null
   * @return Value of the property
   */
  public double evaluateProjectProperty(@NonNull Property property) {
    return evaluatePropertyMeasure(property);
  }

  /**
   * Retrieves the measure value for the given property
   *
   * @param property The property, cannot be null
   * @return The value of that property, or 0 if the property is not known
   */
  public double getPropertyMeasureValue(@NonNull Property property) {
    if (data.getMeasureValues().containsKey(property.getMeasure()))
      return data.getMeasureValues().get(property.getMeasure());
    return 0;
  }
}
