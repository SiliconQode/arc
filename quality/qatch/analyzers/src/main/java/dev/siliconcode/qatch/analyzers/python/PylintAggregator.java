package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Property;
import org.apache.commons.lang3.Range;
import lombok.NonNull;

import java.util.Arrays;

/**
 * Aggregates the results of a pylint analysis into the project
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PylintAggregator implements Aggregator {

  /** The weights representing the relative importance of each rule category */
  public static final int[] WEIGHT = {1, 2, 3, 4, 5};

  /** {@inheritDoc} */
  @Override
  public void aggregate(@NonNull Project project) {
    Range<Integer> range = Range.between(0, 4);
    double[] num = new double[5];

    project
        .issuesSetStream()
        .forEach(
            issueSet -> {
              Arrays.fill(num, 0);

              issueSet.stream()
                  .forEach(
                      issue -> {
                        int priority = issue.priority();
                        if (range.contains(priority)) num[priority]++;
                      });

              int value = 0;
              for (int i = 0; i < num.length; i++) value += WEIGHT[i] * num[i];

              for (Property property : project.getProperties().getProperties()) {
                if (issueSet.getPropertyName().equals(property.getName())) {
                  project.setPropertyMeasureValue(property, value);
                  break;
                }
              }
            });
  }
}
