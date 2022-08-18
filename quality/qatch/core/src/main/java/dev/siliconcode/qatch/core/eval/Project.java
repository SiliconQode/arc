package dev.siliconcode.qatch.core.eval;

import dev.siliconcode.qatch.core.model.*;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class represents a project under evaluation.
 *
 * <p>Basically, it contains all the data (metrics, violations, properties etc.) that belong to a
 * certain project.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class Project {

  @Getter @Setter private String name;
  @Getter @Setter private String path;
  @Getter @Setter private List<IssueSet> issues;
  @Getter @Setter private MetricSet metrics;
  @Getter @Setter private QualityModelInstance model;

  /**
   * The constructor methods of this class.
   *
   * @param model The quality model used to evaluate this project, cannot be null
   */
  public Project(@NonNull QualityModel model) {
    this(null, model);
  }

  /**
   * Construct a new project, with the given name, and quality model for evaluation
   *
   * @param name Name of the project
   * @param model The quality model used to evaluate the project, cannot be null
   */
  public Project(String name, @NonNull QualityModel model) {
    this.issues = Lists.newArrayList();
    this.metrics = new MetricSet();
    this.model = model.getInstance();
    this.name = name;
  }

  /**
   * @return Properties associated with the QualityModelInstance
   */
  public PropertySet getProperties() {
    return model.getProperties();
  }

  /**
   * Sets the properties associated with the Quality Model
   *
   * @param properties new quality model properties, cannot be null
   */
  public void setProperties(@NonNull PropertySet properties) {
    model.setProperties(properties);
  }

  /**
   * @return The quality model characteristics
   */
  public CharacteristicSet getCharacteristics() {
    return this.model.getCharacteristics();
  }

  /**
   * Sets the characteristics used by the quality model
   *
   * @param characteristics new set of characteristics to be used, cannot be null
   */
  public void setCharacteristics(@NonNull CharacteristicSet characteristics) {
    this.model.setCharacteristics(characteristics);
  }

  /**
   * @return Quality Model TQI
   */
  public Tqi getTqi() {
    return model.getTqi();
  }

  /**
   * Sets the current quality model's tqi
   *
   * @param tqi Updated model TQI, cannot be null
   */
  public void setTqi(@NonNull Tqi tqi) {
    model.setTqi(tqi);
  }

  /**
   * Adds an IssueSet in the issues vector.
   *
   * @param issueSet Additional issues set, cannot be null
   */
  public void addIssueSet(@NonNull IssueSet issueSet) {
    this.issues.add(issueSet);
  }

  /**
   * Checks if the issues vector is empty.
   *
   * @return true if the issues set is empty, false otherwise
   */
  public boolean isEmpty() {
    return issues.isEmpty();
  }

  /**
   * Creates an iterator for the issuesSet.
   *
   * @return an iterator over the issues set
   */
  public Iterator<IssueSet> issueSetIterator() {
    return issues.iterator();
  }

  /**
   * Creates a sream over the issuesSet
   *
   * @return a stream over the issues sets
   */
  public Stream<IssueSet> issuesSetStream() {
    return issues.stream();
  }

  /**
   * @return Size of the issues sets
   */
  public int size() {
    return issues.size();
  }

  /**
   * @return Array representation of the issues set
   */
  public IssueSet[] toArray() {
    return issues.toArray(new IssueSet[0]);
  }

  /**
   * @return String representation of the issues set
   */
  @Override
  public String toString() {
    return issues.toString();
  }

  /** A method that calculates the total evaluation (TQI) of this Project. */
  public void calculateTQI() {
    /*
     * Just call the calculateTQI() method of the Tqi object in order to
     * calculate the value of the TQI from the eval field of the model's
     * characteristics
     */
    model.calculateTQI();
  }

  /**
   * Method for freeing memory. Typically, it deletes the contents of the metrics and issues objects
   * of this project. I.e. it deletes the results of the static analysis concerning this project.
   */
  public void clearIssuesAndMetrics() {
    metrics.clear();
    issues.clear();
  }

  /** Evaluation of the project's characteristics using the quality model instance */
  public void evaluateProjectCharacteristics() {
    model.evaluateProjectCharacteristics();
  }

  /** Evaluation of the project's properties using the quality model instanc e */
  public void evaluateProjectProperties() {
    model.evaluateProjectProperties();
  }

  /**
   * Evaluation of the project's tqi using the quality model instance
   *
   * @return Project TQI
   */
  public double getTqiEval() {
    return model.getTqiEval();
  }

  /**
   * Return the evaluation values of the charactersitics of the model
   *
   * @return Map of the charactericts evaluation values, keyed by the characteristics
   */
  public Map<Characteristic, Double> getCharacteristicEvals() {
    return model.getData().getCharacteristicEvals();
  }

  /**
   * This method is responsible for sorting a group of projects according to a desired field in a
   * descending order. Typically, it is used in order to sort the projects under evaluation
   * according to their TQI.
   *
   * @param field Field on which to sort projects, cannot be null
   * @param projects The vector of projects to be sorted, cannot be null
   */
  public static void sort(final @NonNull String field, @NonNull List<Project> projects) {

    projects.sort(
        (p1, p2) -> {
          if (field.equals("eval")) {
            return Double.compare(p1.getTqiEval(), p2.getTqiEval());
          } else {
            return 1;
          }
        });
  }

  /** Normalizes all measures in the model */
  public void normalizeMeasures() {
    model.normalizeMeasures();
  }

  /**
   * Sets the normalizer for a given property to the provided total loc
   *
   * @param prop Property to be normalized, cannot be null
   * @param totalLoc Normalizer LOC value
   */
  public void setPropertyMeasureNormalizer(@NonNull Property prop, double totalLoc) {
    model.setPropertyMeasureNormalizer(prop, totalLoc);
  }

  /**
   * Sets the value of the properties measure
   *
   * @param property Property to set, cannot be null
   * @param value Value
   */
  public void setPropertyMeasureValue(@NonNull Property property, double value) {
    model.setPropertyMeasureValue(property, value);
  }

  /**
   * Normalizes the given property's measure
   *
   * @param p Property to normalize, cannot be null
   * @return The normalized measure value
   */
  public double normalizeMeasure(@NonNull Property p) {
    return model.normalizeMeasure(p);
  }

  /**
   * Evaluate the given characteristic's value
   *
   * @param characteristic Characteristic to evaluate, cannot be null
   * @return Value
   */
  public double evaluateProjectCharacteristic(@NonNull Characteristic characteristic) {
    return model.evaluateProjectCharacteristic(characteristic);
  }

  /**
   * Evaluate the given property's value
   *
   * @param property Property to evaluate, cannot be null
   * @return Value
   */
  public double evaluateProjectProperty(@NonNull Property property) {
    return model.evaluateProjectProperty(property);
  }
}
