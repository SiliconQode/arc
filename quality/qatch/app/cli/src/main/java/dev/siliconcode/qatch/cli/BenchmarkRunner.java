package dev.siliconcode.qatch.cli;

import dev.siliconcode.qatch.calibration.*;
import dev.siliconcode.qatch.calibration.io.*;
import dev.siliconcode.qatch.calibration.stats.ThresholdGenerator;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.CharacteristicSet;
import dev.siliconcode.qatch.core.model.PropertySet;
import dev.siliconcode.qatch.core.model.QualityModel;
import dev.siliconcode.qatch.core.model.Tqi;
import dev.siliconcode.qatch.core.util.QualityModelIO;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is made in order to execute the BenchmarkAnalyzer and the results importer!!!
 *
 * <p>Basically this class contains the central script of the Threshold Extractor.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class BenchmarkRunner extends Runner {

  private QualityModel qualityModel;
  private PropertySet properties;
  private CharacteristicSet characteristics;
  private Tqi tqi;

  private List<Project> projects = null;
  private final BenchmarkAnalysisExporter exporter = null;

  /**
   * Constructs a new BenchmarkRunner with the provided QatchContext
   *
   * @param context QatchContext
   */
  public BenchmarkRunner(@NonNull QatchContext context) {
    super(context);
  }

  /** Executes the analysis process */
  @Override
  public void run() {
    log.info("");
    log.info("");
    log.info("******************************  Model Designer *******************************");
    log.info("*");
    log.info("*");
    log.info("*");

    loadQualityModel();

    if (context.isCalibration()) {
      analyzeWorkspace();
      importAnalysisResults();
      aggregateAnalysisResults();
      Map<String, Triple<Double, Double, Double>> thresholds =
          executeThresholdStatisticalAnalysis();
      assignThresholds(thresholds);
    }

    // Weight elicitation process
    if (context.isWeightsElicitation()) {
      weightElicitation();
    }

    exportEvaluationResults();
  }

  /** Export the properties in an XML (or JSON) File */
  @Override
  protected void exportEvaluationResults() {
    log.info("");
    log.info("**** STEP 8: Exporting Results ****");
    log.info("*");
    log.info("* Exporting the results to XML, JSON files and JDOM Element object...");
    log.info("* This will take a while...");
    log.info("*");

    // Check and create the predefined results directory
    checkCreateClearDirectory(context.getBenchmarkResPath());
    Path path = Paths.get(context.getResPath(), context.getLanguage() + "QmModel.xml").toAbsolutePath().normalize();
    QualityModelIO.exportModel(
            qualityModel, path);
    path = Paths.get(context.getBenchmarkResPath(), context.getLanguage() + "QmModel.xml").toAbsolutePath().normalize();
    QualityModelIO.exportModel(
            qualityModel,
            path);

    log.info("* Results successfully exported..!");
    log.info("*");
    log.info(
        "* You can find the results at        : "
            + Paths.get(context.getResPath()).toAbsolutePath());
    log.info(
        "* You can find the ranked results at : "
            + Paths.get(context.getBenchmarkResPath()).toAbsolutePath());
  }

  /** Elicits the weights for the current quality model */
  private void weightElicitation() {
    log.info("");
    log.info(
        "**** Weight Elicitation ****");
    log.info("*");
    log.info("*");

    // Ask if the user wants simple AHP or fuzzy AHP
    String answer = selectMethod();

    // Create a console scanner
    Scanner console = new Scanner(System.in);

    boolean fuzzy = false;

    System.out.println("*");
    if ("simple".equalsIgnoreCase(answer)) {
      System.out.println("* AHP technique selected!");
      System.out.println("*");
      System.out.println(
          "* The comparison matrices can be found at:\n* "
              + new File(ComparisonMatricesCreator.COMP_MATRICES).getAbsolutePath());
      System.out.println("* Please fulfill the comparison matricies with numbers between 1 and 9.");
      System.out.println("*");
      System.out.println("* When you are ready hit \"Enter\"");

    } else {
      System.out.println("* Fuzzy AHP tecnique selected!");
      System.out.println("*");
      System.out.println(
          "* The comparison matrices can be found at:\n* "
              + new File(ComparisonMatricesCreator.COMP_MATRICES).getAbsolutePath());
      System.out.println("*");
      System.out.println(
          "* Please fulfill each cell of the comparison matrices with one of the following linguistic\n* variables:  Very Low, Low, Moderate, High, Very High");
      System.out.println("*");
      System.out.println(
          "* If you wish you can define how sure you are for your choice by providing one of the letters:\n* U, D, C next to your judgement, seperated by comma (U: Uncertain, D: Default, C: Certain)");
      System.out.println("* ");
      System.out.println(
          "* Please check your spelling! If you misspell a choice then the default values will be\n* automatically taken (i.e. Moderate and D)");
      System.out.println("* ");
      System.out.println("* When you are ready hit \"Enter\"");
      fuzzy = true;
    }

    // Create the appropriate comparison matrices
    ComparisonMatricesCreator.generateCompMatrices(characteristics, properties, fuzzy);

    // Wait until the user is ready
    console.nextLine();

    // Call R script for weight elicitation
    RInvoker invoker = new RInvoker();
    invoker.executeRScriptForWeightsElicitation();

    // Import the weights from the json file
    WeightsImporter weightImporter = new WeightsImporter();
    weightImporter.importWeights(tqi, characteristics);
  }

  /**
   * Replace property thresholds in the quality model
   *
   * @param thresholds Map of the thresholds to assign, cannot be null
   */
  private void assignThresholds(@NonNull Map<String, Triple<Double, Double, Double>> thresholds) {
    log.info("");
    log.info("**** STEP 6: Importing the thresholds ****");
    log.info("*");
    log.info("* Importing the thresholds from the JSON file into JVM...");
    log.info("* This will take a while...");
    log.info("*");

    qualityModel
        .getProperties()
        .iterator()
        .forEachRemaining(
            (prop) -> {
              String name = prop.getName();
              prop.clearThresholds();
              Triple<Double, Double, Double> triple = thresholds.get(name);
              prop.addAll(triple.getLeft(), triple.getMiddle(), triple.getRight());
            });

    log.info("* Thresholds successfully imported..!");
  }

  /**
   * Conduct threshold analysis
   *
   * @return Map of the threshold triples for each characteristic
   */
  private Map<String, Triple<Double, Double, Double>> executeThresholdStatisticalAnalysis() {
    log.info("");
    log.info("**** STEP 4: Threshold extraction ****");
    log.info("*");
    log.info("* Calling R for threshold extraction...");
    log.info("* This will take a while...");
    log.info("*");

    ThresholdGenerator gen = new ThresholdGenerator();
    Map<String, Triple<Double, Double, Double>> thresholds = gen.generateThresholds(projects);

    log.info("*");
    log.info("* R analysis finished..!");
    log.info("* Thresholds exported in a JSON file..!");

    return thresholds;
  }

  /** {@inheritDoc} */
  @Override
  protected void aggregateAnalysisResults() {
    log.info("");
    log.info("**** STEP 3: Aggregation Process ****");
    log.info("*");
    log.info("* Aggregating the results of each project...");
    log.info("* I.e. Calculating the normalized values of their properties...");
    log.info("* Please wait...");
    log.info("*");

    // Create an empty BenchmarkAggregator and aggregate the metrics of the project
    BenchmarkAggregator benchAggregator = new BenchmarkAggregator();
    benchAggregator.aggregateProjects(projects, properties, context.getCurrentProvider());

    log.info("*");
    log.info("* Aggregation process finished..!");
  }

  /** {@inheritDoc} */
  @Override
  protected void evaluateAgainstThresholds() {}

  /** {@inheritDoc} */
  @Override
  protected void evaluateCharacteristics() {}

  /** {@inheritDoc} */
  @Override
  protected void evaluateTqi() {}

  /** Import the results of the analysis and store them into different objects */
  @Override
  protected void importAnalysisResults() {
    log.info("");
    log.info("**** STEP 2 : Benchmark Importer ****");
    log.info("*");
    log.info("* Importing the results of the analysis...");
    log.info("* Please wait...");
    log.info("*");

    // Create an empty BenchmarkImporter
    BenchmarkResultImporter benchmarkImporter = new BenchmarkResultImporter();

    // Start importing the project results
    projects = Lists.newArrayList();
    benchmarkImporter.importResults(projects, context.getResultsPath().toString(), context.getCurrentProvider(), this.qualityModel);
    log.info("* Number of Projects: " + projects.size());

    // Print some informative messages to the console
    log.info("*");
    log.info("* The results are successfully imported..! ");
  }

  /*
   * Analyze the projects in the desired Benchmark Repository
   */
  @Override
  protected void analyzeWorkspace() {
    // Check if the results directory exists and if not create it. Clear it's contents as well.
    checkCreateClearDirectory(context.getBenchmarkResPath());

    log.info("");
    log.info("**** STEP 1 : Benchmark Analyzer ****");
    log.info("*");
    log.info("* Analyzing the projects of the desired benchmark repository");
    log.info("* Please wait...");
    log.info("*");

    // TODO: Remove the time calculation
    long startTime = System.currentTimeMillis();

    // TODO: Create a parent Benchmark Analyzer so that you can use it to move the common commands
    // outside the if statement
    if (!context.isParallelAnalysis()) {
      // Instantiate the serial benchmark analyzer
      BenchmarkAnalyzer benchmarkAnal =
          new BenchmarkAnalyzer(context.getBenchRepoPath(), context.getResultsPath(), properties, context.getCurrentProvider());

      // Start the analysis of the benchmark repository
      benchmarkAnal.analyzeBenchmarkRepo();
    } else {
      // Instantiate the parallel benchmark analyzer
      OptimalParallelBenchmarkAnalyzer benchmarkAnal =
          new OptimalParallelBenchmarkAnalyzer(
              context.getBenchRepoPath(), context.getResultsPath(), properties, context.getCurrentProvider());

      // Start the analysis of the workspace
      benchmarkAnal.analyzeBenchmarkRepo();
    }

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    // Print some messages to the user
    log.info("*");
    log.info("* The analysis is finished..!");
    log.info(
        "* You can find the results at : "
            + Paths.get(context.getBenchmarkResPath()).toAbsolutePath());
    log.info("* Total time is : " + totalTime + " ms");
    log.info("");
  }

  /*
   * Import the user defined properties
   */
  @Override
  protected void loadQualityModel() {
    qualityModel = QualityModelIO.importModel(Paths.get(context.getQmPath()));
    properties = qualityModel.getProperties();
    characteristics = qualityModel.getCharacteristics();
    tqi = qualityModel.getTqi();
  }

  /** {@inheritDoc} */
  @Override
  protected void createAndLoadProject() {}

  /**
   * This method is responsible for asking the user which Multi-Criteria decision making technique
   * he/she would like to use for the weight elicitation process.
   *
   * <p>Typically, it sets the appropriate R Script that will be executed and returns the user's
   * answer.
   */
  private String selectMethod() {

    // Create a console scanner
    Scanner console = new Scanner(System.in);

    String answer = "";
    boolean wrongAnswer = false;
    boolean asked = false;

    // While the correct answer is not provided by the user do...
    while ((!"simple".equalsIgnoreCase(answer)) && (!"fuzzy".equalsIgnoreCase(answer))) {
      // Ask the basic question only once
      if (!wrongAnswer && !asked) {
        log.info(
            "* Would you like to use simple AHP of fuzzy AHP for the weights elicitation? (simple/fuzzy): ");
        log.info("*");
        asked = true;
      }
      System.out.print("* ");

      // Retrieve the user's answer
      answer = console.nextLine();

      // Check if the answer is valid and set the path to the appropriate R script
      if ("simple".equalsIgnoreCase(answer)) {
        RInvoker.weightsScript = RInvoker.R_AHP_SCRIPT;
      } else if ("fuzzy".equalsIgnoreCase(answer)) {
        RInvoker.weightsScript = RInvoker.R_FAHP_SCRIPT;
      } else {
        // If the answer is not valid, inform the user and wait for a new answer...
        log.info("* ");
        log.info("* Wrong answer. Possible answers :  simple / fuzzy ");
      }
    }

    // Return the user's valid answer
    return answer;
  }
}
