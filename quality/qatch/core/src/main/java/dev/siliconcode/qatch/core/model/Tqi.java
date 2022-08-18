package dev.siliconcode.qatch.core.model;

import lombok.NonNull;

import java.util.Map;

/**
 * This class represents the total quality index of a project. It is used for the evaluation
 * (quality assessment - estimation) of a certain project or a benchmark of projects.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class Tqi extends AbstractWeightedObject {

  /** Constructs a new TQI object with an empty set of weights */
  public Tqi() {
    super(new Weights());
  }

  /**
   * Constructs a new TQI object with the given set of weights
   *
   * @param weights Weights for the TQI to Characteristics
   */
  public Tqi(Weights weights) {
    super(weights);
  }

  /**
   * This method calculates the Total Quality Index (TQI), i.e. the total eval (quality score), of
   * the project based on: - The values of "eval" fields of the characteristics of the model - The
   * values of the weights that the model assigns to each characteristic
   *
   * <p>ATTENTION: - The order of the Characteristic objects that are placed in the
   * CharacteristicSet of the quality model matters, because the weights are placed in the weights
   * Vector depending on this order! - Do not change the auto-generated order neither in the Quality
   * Model xml file nor elsewhere inside the program.
   *
   * @param characteristics The CharacteristicSet object with the eval fields calculated received
   *     from the Project object., cannot be null
   * @param charEvals Map of the charateristic evaluations used in calculating TQI, cannot be null
   * @return The TQI value
   */
  public double calculateTQI(
      @NonNull CharacteristicSet characteristics, @NonNull Map<Characteristic, Double> charEvals) {
    double sum = 0;
    for (int i = 0; i < characteristics.size(); i++) {
      // The number of weights is equal to the number of the model's characteristics
      // The sequence of the characteristics matters!!!!!!
      sum += charEvals.get(characteristics.get(i)) * this.weights.get(i);
    }
    return sum;
  }
}
