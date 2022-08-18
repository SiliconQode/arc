package dev.siliconcode.qatch.analyzers.scala;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.CKJMAggregator;
import dev.siliconcode.qatch.analyzers.java.CKJMAnalyzer;
import dev.siliconcode.qatch.analyzers.java.CKJMResultsImporter;

import java.util.Map;

/**
 * Language provider for the Scala programming language. Currently, it utilizes
 * the Linter tool for rule violations and the CKJM tool for metrics analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ScalaProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return ScalaProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new ScalaProvider();
  }

  /** Private default constructor */
  private ScalaProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new LinterAnalyzer(config.get("linterPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CKJMAnalyzer(config.get("ckjmPath"), resultsPath);
    issuesImporter = new LinterResultsImporter();
    metricsImporter = new CKJMResultsImporter();
    issuesAggregator = new LinterAggregator();
    metricsAggregator = new CKJMAggregator();
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "scala";
  }
}
