package dev.siliconcode.qatch.cli;

import dev.siliconcode.qatch.analyzers.*;
import dev.siliconcode.qatch.calibration.io.EvaluationResultsExporter;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Characteristic;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Tool to execute the analysis of a single project
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class SingleProjectRunner extends Runner {

  private Project project;

  /**
   * Constructs a new SingleProjectRunner with the given QatchContext
   *
   * @param context The QatchContext
   */
  public SingleProjectRunner(QatchContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @Override
  protected void createAndLoadProject() {
    log.info("");
    log.info("**** STEP 1: Project Loader ****");
    log.info("*");
    log.info("* Loading the desired project...");
    log.info("* Please wait...");
    log.info("*");

    File projectDir = new File(context.getWorkspacePath());

    project = new Project(qualityModel);

    project.setPath(context.getWorkspacePath());
    project.setName(projectDir.getName());

    log.info("* Project Name : " + project.getName());
    log.info("* Project Path : " + project.getPath());
    log.info("*");
    log.info("* Project successfully loaded..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void analyzeWorkspace() {
    checkCreateClearDirectory(context.getResultsPath());

    log.info("");
    log.info("**** STEP 2: Project Analyzer ****");
    log.info("*");
    log.info("* Analyzing the desired project");
    log.info("* Please wait...");
    log.info("*");

    IssuesAnalyzer issues = context.getCurrentProvider().getIssuesAnalyzer();
    MetricsAnalyzer metrics = context.getCurrentProvider().getMetricsAnalyzer();

    log.info("* Analyzing Issues");
    issues.analyze(
        context.getWorkspacePath(),
        Paths.get(context.getResultsPath(), project.getName()).toAbsolutePath().normalize().toString(),
        project.getProperties());

    log.info("* Analyzing Metrics");
    metrics.analyze(
        context.getWorkspacePath(),
        Paths.get(context.getResultsPath(), project.getName(), context.getCurrentProvider().getMetricsImporter().getFileName()).toAbsolutePath().normalize().toString(),
        project.getProperties());

    log.info("* The analysis is finished");
    log.info("* You can find the results at : " + context.getResultsPath());
    log.info("");
  }

  /** {@inheritDoc} */
  @Override
  protected void importAnalysisResults() {
    log.info("");
    log.info("**** STEP 3: Results Importer ****");
    log.info("*");
    log.info("* Importing the results of the analysis...");
    log.info("* Please wait...");
    log.info("*");

    IssuesImporter issueImporter = context.getCurrentProvider().getIssuesImporter();
    MetricsImporter metricImporter = context.getCurrentProvider().getMetricsImporter();

    File resultsDir = Paths.get(context.getResultsPath(), project.getName()).toAbsolutePath().normalize().toFile();
    if (resultsDir.exists() && resultsDir.isDirectory()) {
      File[] results = resultsDir.listFiles();

      if (results != null) {
        for (File resultFile : results) {
          if (!resultFile.getName().contains(metricImporter.getFileName())) {
            project.addIssueSet(issueImporter.parseIssues(resultFile.getAbsolutePath()));
          } else {
            project.setMetrics(metricImporter.parseMetrics(resultFile.getAbsolutePath()));
          }
        }
      }
    }

    log.info("*");
    log.info("* The results of the static analysis are successfully imported ");
  }

  /** {@inheritDoc} */
  @Override
  protected void aggregateAnalysisResults() {
    log.info("");
    log.info("**** STEP 4: Aggregation Process ****");

    log.info("*");
    log.info("* Aggregating the results of the project...");
    log.info("* I.e. Calculating the normalized values of its properties...");
    log.info("* Please wait...");
    log.info("*");

    Aggregator issuesAggregator = context.getCurrentProvider().getIssuesAggregator();
    Aggregator metricsAggregator = context.getCurrentProvider().getMetricsAggregator();

    issuesAggregator.aggregate(project);
    metricsAggregator.aggregate(project);

    project.normalizeMeasures();

    log.info("*");
    log.info("* Aggregation process finished..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateAgainstThresholds() {
    log.info("");
    log.info("**** STEP 5: Properties Evaluation ****");
    log.info("*");
    log.info("* Evaluating the project's properties against the calculated thresholds...");
    log.info("* This will take a while...");
    log.info("*");

    project.evaluateProjectProperties();

    log.info("*");
    log.info("* The project's properties successfully evaluated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateCharacteristics() {
    log.info("");
    log.info("**** STEP 6: Characteristics Evaluation ****");
    log.info("*");
    log.info(
        "* Evaluating the project's characteristics based on the eval values of its properties...");
    log.info("* This will take a while...");
    log.info("*");

    project.evaluateProjectCharacteristics();

    log.info("*");
    log.info("* The project's characteristics successfully evaluated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateTqi() {
    log.info("");
    log.info("**** STEP 7: TQI Calculation ****");
    log.info("*");
    log.info("* Calculating the TQI of the project ...");
    log.info("* This will take a while...");
    log.info("*");

    project.calculateTQI();

    log.info("*");
    log.info("* The TQI of the project successfully evaluated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void exportEvaluationResults() {
    log.info("");
    log.info("**** STEP 8: Exporting Evaluation Results ****");
    log.info("*");
    log.info("* Exporting the results of the project evaluation...");
    log.info("* This will take a while...");
    log.info("*");

    EvaluationResultsExporter exporter = new EvaluationResultsExporter();

    Map<String, String> results = Maps.newHashMap();
    results.put("submissionID", project.getName());
    results.put("TQI", Double.toString(project.getTqiEval()));
    for (Map.Entry<Characteristic, Double> entry : project.getCharacteristicEvals().entrySet()) {
      results.put(entry.getKey().getName(), Double.toString(entry.getValue()));
    }

    exporter.exportToJSON(
        results,
        Paths.get(context.getResPath(), project.getName() + "_evalResults.json")
            .toAbsolutePath()
            .toString());

    log.info("* Results successfully exported..!");
    log.info("* You can find the results at : " + new File(context.getResPath()).getAbsolutePath());
  }
}
