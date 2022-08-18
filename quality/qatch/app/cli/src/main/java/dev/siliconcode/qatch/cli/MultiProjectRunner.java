package dev.siliconcode.qatch.cli;

import dev.siliconcode.qatch.analyzers.*;
import dev.siliconcode.qatch.calibration.*;
import dev.siliconcode.qatch.calibration.io.BenchmarkResultImporter;
import dev.siliconcode.qatch.calibration.io.EvaluationResultsExporter;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Characteristic;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class contains the main script for evaluating all the projects of a workspace based on a
 * desired Quality Model.
 *
 * <p>It gives you the option to analyze the project or to use the results of the previous analysis.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class MultiProjectRunner extends Runner {

  // User defined fields
  private List<Project> projects;

  /**
   * Constructs a new MultiProjectRunner to evaluate multiple projects in a base workspece
   *
   * @param context The QatchContext for this runner
   */
  public MultiProjectRunner(QatchContext context) {
    super(context);
    projects = Lists.newArrayList();
  }

  /** {@inheritDoc} */
  @Override
  protected void exportEvaluationResults() {
    log.info("\n**** STEP 7: Exporting Evaluation Results ****");
    log.info("*");
    log.info("* Exporting the results of the project evaluation...");
    log.info("* This will take a while...");
    log.info("*");

    projects.forEach(project -> {
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
    });

    log.info("* Results successfully exported..!");
    log.info(
        "* You can find the results at        : "
            + new File(context.getResPath()).getAbsolutePath());
    log.info("* You can find the ranked results at : " + new File(context.getResultsPath()));
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateTqi() {
    log.info("");
    log.info("**** STEP 7: TQI Calculation ****");
    log.info("*");
    log.info("* Calculating the TQI of each project ...");
    log.info("* This will take a while...");
    log.info("*");

    projects.forEach(Project::calculateTQI);

    log.info("*");
    log.info("* The TQI of the projects successfully calculated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateCharacteristics() {
    log.info("");
    log.info("**** STEP 6: Characteristics Evaluation ****");
    log.info("*");
    log.info(
        "* Evaluating the projects' characteristics based on the eval values of their properties...");
    log.info("* This will take a while...");
    log.info("*");

    projects.forEach(Project::evaluateProjectCharacteristics);

    log.info("*");
    log.info("* The projects' characteristics are successfully evaluated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateAgainstThresholds() {
    log.info("");
    log.info("**** STEP 5: Properties Evaluation ****");
    log.info("*");
    log.info("* Evaluating the projects' properties against the calculated thresholds...");
    log.info("* This will take a while...");
    log.info("*");

    projects.forEach(Project::evaluateProjectProperties);

    log.info("*");
    log.info("* The projects' properties are successfully evaluated..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void aggregateAnalysisResults() {
    log.info("");
    log.info("**** STEP 4: Aggregation Process ****");
    log.info("*");
    log.info("* Aggregating the results of each project...");
    log.info("* I.e. Calculating the normalized values of their properties...");
    log.info("* Please wait...");
    log.info("*");

    Aggregator issues = context.getCurrentProvider().getIssuesAggregator();
    Aggregator metrics = context.getCurrentProvider().getMetricsAggregator();

    projects.forEach(proj -> {
      issues.aggregate(proj);
      metrics.aggregate(proj);

      proj.normalizeMeasures();
    });

    log.info("*");
    log.info("* Aggregation process finished..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void importAnalysisResults() {
    log.info("");
    log.info("**** STEP 3: Benchmark Importer ****");
    log.info("*");
    log.info("* Importing the results of the analysis...");
    log.info("* Please wait...");
    log.info("*");

    IssuesImporter issues = context.getCurrentProvider().getIssuesImporter();
    MetricsImporter metrics = context.getCurrentProvider().getMetricsImporter();

    projects.forEach(proj -> {
      Path resultsDir = Paths.get(context.getResultsPath(), proj.getName()).toAbsolutePath().normalize();
      if (Files.exists(resultsDir) && Files.isDirectory(resultsDir)) {
        try (var stream = Files.newDirectoryStream(resultsDir)) {
          stream.forEach(resultFile -> {
            if (!resultFile.getFileName().toString().contains(metrics.getFileName())) {
              proj.addIssueSet(issues.parseIssues(resultFile.toAbsolutePath().normalize().toString()));
            } else {
              proj.setMetrics(metrics.parseMetrics(resultFile.toAbsolutePath().normalize().toString()));
            }
          });
        } catch (IOException ex) {
          log.error(ex.getMessage());
        }
      }
    });

    log.info("*");
    log.info("* the results of the static analyses have been successfully imported");
  }

  /** {@inheritDoc} */
  @Override
  protected void createAndLoadProject() {
    log.info("");
    log.info("**** Step 1: Projects Loader ****");
    log.info("*");
    log.info("* Loading the desired projects...");
    log.info("* Please wait...");
    log.info("*");

    Path base = Paths.get(context.getWorkspacePath());
    List<Path> paths = Lists.newArrayList();
    try(var stream = Files.newDirectoryStream(base)) {
      stream.iterator().forEachRemaining(x -> {
        if (Files.isDirectory(x)) paths.add(x);
      });
    } catch(IOException ex) {
      log.error(ex.getMessage());
    }

    paths.forEach(p -> {
      Project project = new Project(qualityModel);
      project.setPath(p.toAbsolutePath().normalize().toString());
      project.setName(p.toFile().getName());
      projects.add(project);
    });

    log.info("* Number of projects: " + projects.size());
    log.info("*");
    log.info("* Projects successfully loaded...!");
  }

  /** {@inheritDoc} */
  @Override
  protected void analyzeWorkspace() {
    String analysisResPath = getCorrectDirectory();

    checkCreateClearDirectory(getCorrectDirectory());

    log.info("");
    log.info("**** STEP 2: Workspace Analyzer ****");
    log.info("*");
    log.info("* Analyzing the projects of your workspace");
    log.info("* Please wait...");
    log.info("*");

    IssuesAnalyzer issues = context.getCurrentProvider().getIssuesAnalyzer();
    MetricsAnalyzer metrics = context.getCurrentProvider().getMetricsAnalyzer();

    if (!context.isParallelAnalysis()) {
      projects.forEach(proj -> {
        issues.analyze(
                proj.getPath(),
                Paths.get(context.getResultsPath(), proj.getName()).toAbsolutePath().normalize().toString(),
                proj.getProperties());

        metrics.analyze(
                proj.getPath(),
                Paths.get(context.getResultsPath(), proj.getName(), context.getCurrentProvider().getMetricsImporter().getFileName()).toAbsolutePath().normalize().toString(),
                proj.getProperties());
      });
    } else {
      projects.parallelStream().forEach(proj -> {
        issues.analyze(
                proj.getPath(),
                Paths.get(context.getResultsPath(), proj.getName()).toAbsolutePath().normalize().toString(),
                proj.getProperties());

        metrics.analyze(
                proj.getPath(),
                Paths.get(context.getResultsPath(), proj.getName(), context.getCurrentProvider().getMetricsImporter().getFileName()).toAbsolutePath().normalize().toString(),
                proj.getProperties());
      });
    }

    log.info("*");
    log.info("* The analysis is finished..!");
    log.info("* You can find the results at : " + new File(analysisResPath).getAbsolutePath());
    log.info("");
  }

  /**
   * Selects the correct directory (either the benchmark results path or the workspace path)
   * depending on the runner configuration
   *
   * @return The path to the selected directory
   */
  private String getCorrectDirectory() {
    if (context.isUseBenchmarksResultDir()) {
      return context.getBenchmarkResPath();
    } else {
      return context.getWorkspacePath();
    }
  }
}
