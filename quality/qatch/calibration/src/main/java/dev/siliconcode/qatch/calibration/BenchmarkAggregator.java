package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.PropertySet;

import java.util.List;

/**
 * This class is responsible for calculating the fields: normalizer, value and normValue of the
 * properties of a set of projects (i.e. BenchmarkProjects). It takes advantage of the data that are
 * stored in the objects: - IssueSet - MetricSet of each project, in order to calculate those
 * fields.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class BenchmarkAggregator {

  /**
   * This method is responsible for the aggregation of the properties of a set of projects. The
   * whole functionality of the aggregator is summarized in three steps:
   *
   * <ol>
   *   <li>Clone the PropertySet of the model to the PropertySet of each project
   *   <li>Call single project aggregators for each project of the project set
   *   <li>Normalize the values of the properties of the set of projects
   * </ol>
   *
   * @param projects The projects to aggregate
   * @param properties The properties to be aggregated
   * @param provider the language provider used in the analysis
   * @return The list of projects
   */
  public List<Project> aggregateProjects(List<Project> projects, PropertySet properties, LanguageProvider provider) {

    Aggregator issuesAggregator = provider.getIssuesAggregator();
    Aggregator metricsAggregator = provider.getMetricsAggregator();

    projects.forEach(project -> {
      issuesAggregator.aggregate(project);
      metricsAggregator.aggregate(project);
    });

    normalizeProperties(projects);

    return projects;
  }

  /**
   * This method is responsible for calculating the normalized value (normValue) of the properties
   * of each project found inside a set of projects.
   *
   * @param projects The projects for which properties should be normalized
   */
  public void normalizeProperties(List<Project> projects) {
    projects.forEach(Project::normalizeMeasures);
  }
}
