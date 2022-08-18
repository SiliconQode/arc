package dev.siliconcode.qatch.calibration.stats;

import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class generates the thresholds for each measure in the model (it is a replacement of an
 * original R file)
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ThresholdGenerator {

  /**
   * Retrieves the names of the properties within the Benchmark Projects set
   *
   * @param projects list of benchmark projects, cannot be null
   * @return Array of property names of the benchmark projects
   */
  public String[] getPropertyNames(@NonNull List<Project> projects) {
    Project proj = projects.get(0);
    List<String> names = Lists.newArrayList();
    proj.getProperties().iterator().forEachRemaining(x -> names.add(x.getName()));

    return names.toArray(new String[0]);
  }

  /**
   * Retrieves the measures for each of the benchmark projects
   *
   * @param projects The list of benchmark projects, cannot be null
   * @return 2D array of values of the measures of each project
   */
  public double[][] getProjectMeasures(@NonNull List<Project> projects) {
    double[][] matrix =
        new double[projects.get(0).getProperties().size()][projects.size()];
    for (int i = 0; i < projects.size(); i++) {
      Project project = projects.get(i);
      for (int j = 0; j < projects.get(i).getProperties().size(); j++) {
        Property property = project.getProperties().get(j);
        double normValue = project.normalizeMeasure(property);
        matrix[j][i] = normValue;
      }
    }
    return matrix;
  }

  /**
   * Generates the thresholds for the measures
   *
   * @param projects The list of benchmark projects, cannot be null
   * @return A Map (keyed by the property names) of the triples representing the threshold
   *     distributions of each measure
   */
  public Map<String, Triple<Double, Double, Double>> generateThresholds(
      @NonNull List<Project> projects) {
    double[][] measures = getProjectMeasures(projects);
    String[] propertyNames = getPropertyNames(projects);

    if (propertyNames.length != measures.length) throw new RuntimeException();

    Map<String, Triple<Double, Double, Double>> data = Maps.newHashMap();

    for (int i = 0; i < propertyNames.length; i++) {
      List<Double> property = Lists.newArrayList();
      for (double value : measures[i]) property.add(value);

      Collections.sort(property);

      double lowerQ = 0;
      double upperQ = 0;
      double med = 0;
      double cut = (double) property.size() / 4;
      if (property.size() % 4 == 0) {
        lowerQ = property.get((int) cut - 1);
        upperQ = property.get((int) (3 * cut - 1));
        med = property.get((int) (2 * cut));
      } else {
        lowerQ = (property.get((int) cut - 1) + property.get((int) cut)) / 2;
        upperQ = (property.get((int) (3 * cut) - 1) + property.get((int) (3 * cut))) / 2;
        med = (property.get((int) (2 * cut) - 1) + property.get((int) (2 *cut))) / 2;
      }
      double iqr = upperQ - lowerQ;

      double lowerThreshold = lowerQ - (1.5 * iqr);
      double t2 = med;
      double upperThreshold = upperQ + (1.5 * iqr);

      List<Double> part = property.stream().filter((x) -> x >= lowerThreshold).toList();
      double t1 = Collections.min(part);

      part = property.stream().filter((x) -> x <= upperThreshold).toList();
      double t3 = Collections.max(part);

      data.put(propertyNames[i], Triple.of(t1, t2, t3));
    }

    return data;
  }
}
