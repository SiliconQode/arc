package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.analyzers.IssuesAnalyzer;
import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.MetricsAnalyzer;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Creates a new Optimal (perhaps) Benchmark Analyzer
 *
 * <p>TODO This class requires serious refactoring
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class OptimalParallelBenchmarkAnalyzer {

  /** The path of the benchmark repository */
  @Setter private String benchRepoPath;

  /** The set of properties against which the project should be analyzed */
  @Setter @Getter
  private PropertySet properties;

  /** The path to the results directory */
  @Setter private String resultsPath;

  /** The language provider used in the analysis */
  private final LanguageProvider languageProvider;

  /** The default constructor of the class. */
  public OptimalParallelBenchmarkAnalyzer() {
    this(null, null);
  }

  /**
   * The basic constructor of the class.
   *
   * @param benchRepoPath Path to the benchmark projects
   * @param provider the language provider used in the analysis
   */
  public OptimalParallelBenchmarkAnalyzer(String benchRepoPath, LanguageProvider provider) {

    this(benchRepoPath, null, provider);
  }

  /**
   * The complete constructor of the class.
   *
   * @param benchRepoPath Path to the benchmark projects
   * @param properties the property set to evaluate
   * @param provider the language provider used in the analysis
   */
  public OptimalParallelBenchmarkAnalyzer(String benchRepoPath, PropertySet properties, LanguageProvider provider) {
    this(benchRepoPath, null, properties, provider);
  }

  /**
   * constructs a new OptimalParallelBenchmarkAnalyzer
   *
   * @param benchRepoPath Path to the benchmark projects
   * @param resultsPath Path to the results
   * @param properties PropertySet to evaluate
   * @param provider the language provider used in the analysis
   */
  public OptimalParallelBenchmarkAnalyzer(
      String benchRepoPath, String resultsPath, PropertySet properties, LanguageProvider provider) {
    this.benchRepoPath = benchRepoPath;
    this.properties = properties;
    this.resultsPath = resultsPath;
    this.languageProvider = provider;
  }

  /**
   * This method is responsible for analyzing the projects of the desired benchmark repository,
   * according to the user defined properties.
   *
   * <p>Its algorithm is pretty straightforward if you read the comments.
   */
  public void analyzeBenchmarkRepo() {
    IssuesAnalyzer issues = languageProvider.getIssuesAnalyzer();
    MetricsAnalyzer metrics = languageProvider.getMetricsAnalyzer();

    PropertySet metricsPropSet = new PropertySet();
    PropertySet issuesPropSet = new PropertySet();

    properties.stream().forEach(property -> {
      if (property.getMeasure().getTool().equals(issues.getToolName())) {
        issuesPropSet.addProperty(property);
      } else if (property.getMeasure().getTool().equals(metrics.getToolName())) {
        metricsPropSet.addProperty(property);
      } else {
        log.info("The property, " + property.getName() + " doesn't belong to any tool supported by the system!!");
      }
    });

    Path baseDir = Paths.get(benchRepoPath);
    List<Path> projects = Lists.newArrayList();
    try { projects = Files.list(baseDir).filter(Files::isDirectory).toList(); }
    catch (IOException ex) { log.error(ex.getMessage()); }

    projects.parallelStream().forEach(project -> {
      metrics.analyze(
              project.toAbsolutePath().normalize().toString(),
              Paths.get(resultsPath, project.getFileName().toString()).toAbsolutePath().normalize().toString(),
              metricsPropSet);
      issues.analyze(
              project.toAbsolutePath().normalize().toString(),
              Paths.get(resultsPath, project.getFileName().toString()).toAbsolutePath().normalize().toString(),
              issuesPropSet);
    });
  }
}
