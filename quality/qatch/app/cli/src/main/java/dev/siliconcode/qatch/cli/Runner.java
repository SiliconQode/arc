package dev.siliconcode.qatch.cli;

import dev.siliconcode.qatch.core.model.QualityModel;
import dev.siliconcode.qatch.core.util.QualityModelIO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The base class for executing an evaluation
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public abstract class Runner {

  /** The quality model to be analyzed */
  protected QualityModel qualityModel;
  /** The context used to store global configuration information */
  protected final QatchContext context;

  /**
   * Constructs a new Runner with the given context
   *
   * @param context global execution context
   */
  public Runner(QatchContext context) {
    this.context = context;
  }

  /** Executes the analysis process */
  public void run() {
    log.info("****  Project Evaluator ****");
    log.info("");

    long start = System.currentTimeMillis();
    loadQualityModel();
    createAndLoadProject();
    if (context.isStaticAnalysis()) analyzeWorkspace();
    importAnalysisResults();
    aggregateAnalysisResults();
    evaluateAgainstThresholds();
    evaluateCharacteristics();
    evaluateTqi();
    exportEvaluationResults();
    long end = System.currentTimeMillis();

    printTimeResults(start, end);
  }

  /**
   * Load the desired Quality Model
   */
  protected void loadQualityModel() {
    log.info("**** STEP 0: Quality Model Loader ****");
    log.info("*");
    log.info("* Loading the desired Quality Model...");
    log.info("* Please wait...");
    log.info("*");

    Path path;
    if (context.getQmPath() != null) path = Paths.get(context.getQmPath());
    else if (context.getModelsPath() != null) {
      path = Paths.get(context.getModelsPath(), context.getLanguage() + "QmModel.xml");
      log.info("Loading Quality Model: " + path);
    }
    else throw new RuntimeException("no model specified");

    // Load the desired quality model
    qualityModel = QualityModelIO.importModel(path.toAbsolutePath());

    log.info("* Quality Model successfully loaded..!");
  }

  /** Create the Project object that simulates the desired project */
  protected abstract void createAndLoadProject();

  /** Analyze the desired project against the selected properties */
  protected abstract void analyzeWorkspace();

  /** Import the results of the static analysis tools */
  protected abstract void importAnalysisResults();

  /** Aggregate the static analysis results of the desired project */
  protected abstract void aggregateAnalysisResults();

  /** Evaluate all the benchmark projects against their thresholds. */
  protected abstract void evaluateAgainstThresholds();

  /** Evaluate the project's characteristics */
  protected abstract void evaluateCharacteristics();

  /** Calculate the TQI of the project */
  protected abstract void evaluateTqi();

  /** Export the project's data and properties in a json file */
  protected abstract void exportEvaluationResults();

  /**
   * Prints the time required to complete the analysis
   *
   * @param start The start time for the analysis
   * @param end THe end time for the analysis
   */
  public void printTimeResults(long start, long end) {
    long delta = end - start;

    double totalSeconds = (double) delta / 1000;

    log.info("");
    log.info(String.format("Analysis took a total of %f s\n", totalSeconds));
  }

  /**
   * A method that checks the predefined directory structure, creates the directory tree if it
   * doesn't exist and clears its contents for the new analysis (optional).
   *
   * @param path The path to evaluate to create or clear
   */
  public void checkCreateClearDirectory(String path) {

    File dir = new File(path);

    // Check if the directory exists
    if (!dir.isDirectory() || !dir.exists()) {
      try {
        Files.createDirectories(dir.toPath());

        if (!context.isKeepResults()) {
          try {
            FileUtils.cleanDirectory(dir);
          } catch (IOException e) {
            log.error(e.getMessage());
          }
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
  }
}
