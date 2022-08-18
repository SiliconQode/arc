package dev.siliconcode.qatch.core.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

/**
 * Class to manage the data associated with a given Projects instance of a Quality Model. The goal
 * of this is to reduce the need for cloning.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class InstanceData {

  @Setter @Getter double tqiEval;
  @Getter private final Map<Property, Measure> propertyMeasures;
  @Getter private final Map<Characteristic, Double> characteristicEvals;
  @Getter private final Map<Property, Double> propertyEvals;
  @Getter private final Map<Measure, Double> measureNorms;
  @Getter private final Map<Measure, Double> measureValues;
  @Getter private final Map<Measure, Double> measureNormValues;

  /** Constructs a new instance of InstanceData */
  public InstanceData() {
    propertyMeasures = Maps.newHashMap();
    characteristicEvals = Maps.newHashMap();
    propertyEvals = Maps.newHashMap();
    measureNorms = Maps.newHashMap();
    measureValues = Maps.newHashMap();
    measureNormValues = Maps.newHashMap();
  }

  /**
   * Initializes the instance data for the given Quality Model
   *
   * @param model Model to which this will be an instance of, cannot be null.
   */
  public void init(@NonNull QualityModel model) {
    model.getCharacteristics().iterator().forEachRemaining(c -> characteristicEvals.put(c, 0.0));
    model
        .getProperties()
        .iterator()
        .forEachRemaining(
            p -> {
              propertyEvals.put(p, 0.0);
              propertyMeasures.put(p, p.getMeasure());
            });
  }

  /**
   * Adds an evaluation for the given characteristic
   *
   * @param characteristic Characteristic evaluated, cannot be null
   * @param eval The evaluation value
   */
  public void addCharacteristicEval(@NonNull Characteristic characteristic, double eval) {
    if (characteristic != null) characteristicEvals.put(characteristic, eval);
  }

  /**
   * Adds an evaluation for the given property
   *
   * @param property Property evaluated, cannot be null
   * @param eval Evaluation value
   */
  public void addPropertyEval(@NonNull Property property, double eval) {
    if (property != null) propertyEvals.put(property, eval);
  }

  /**
   * This method calculates the normalized value of this measure. It just divides the value field by
   * the normalizer field.
   *
   * @param measure The measure to be normalized, cannot be null
   */
  public void calculateNormValue(@NonNull Measure measure) {
    double normalizer = measureNorms.get(measure);
    double value = measureValues.get(measure);

    if (normalizer != 0) {
      measureNormValues.put(measure, value / normalizer);
    } else {
      measureNormValues.put(measure, Double.NaN);
    }
  }

  /**
   * Retrieves the normalized value for the given measure
   *
   * @param measure Measure, cannot be null
   * @return normalized value
   */
  public double getNormValue(@NonNull Measure measure) {
    return measureNormValues.get(measure);
  }
}
