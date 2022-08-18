package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.IssueSet;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Property;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Aggregates the results of a PMD analysis into a project
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class PMDAggregator implements Aggregator {

  /** The weights representing the relative importance of each PMD rule category */
  public static final int[] WEIGHT = {1, 1, 1, 1, 1};

  /**
   * This method is responsible for the aggregation of the issues of a single project.
   *
   * @param project The project to which the data will be aggregated into, cannot be null
   */
  @Override
  public void aggregate(@NonNull Project project) {

    // Create an array for storing the number of issues per severity level
    double[] num = new double[5];

    // Iterate through the different IsuueSets of the project (i.e. result sets)
    Iterator<IssueSet> iterator = project.issueSetIterator();
    while (iterator.hasNext()) {

      // Clear the num array
      Arrays.fill(num, 0);

      // Get the current IssueSet
      IssueSet issueSet = iterator.next();

      // Iterate through the issues of this IssueSet and count their number per severity
      for (int i = 0; i < issueSet.size(); i++) {
        if (issueSet.get(i).priority() >= 0 && issueSet.get(i).priority() < 5)
          num[issueSet.get(i).priority() - 1]++;
      }

      // Calculate the value of this issue set
      int value = 0;
      for (int i = 0; i < num.length; i++) {
        value += WEIGHT[i] * num[i];
      }

      // Find the property and set its value and its profile ...
      for (int i = 0; i < project.getProperties().size(); i++) {
        Property property = project.getProperties().get(i);
        if (issueSet.getPropertyName().equals(property.getName())) {
          project.setPropertyMeasureValue(property, value);
          //property.setProfile(num.clone());
          break;
        }
      }
    }
  }
}
