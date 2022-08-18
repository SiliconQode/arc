package dev.siliconcode.qatch.analyzers.kotlin;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.CKJMAggregator;
import dev.siliconcode.qatch.analyzers.java.CKJMAnalyzer;
import dev.siliconcode.qatch.analyzers.java.CKJMResultsImporter;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Programming language provider for the Kotlin programming language. Currently,
 * it utilizes the Detekt static analysis tool to analyze for issues and the CKJM
 * tool to analyze for metrics.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class KotlinProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return KotlinProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new KotlinProvider();
  }

  /** Private default constructor */
  private KotlinProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new DetektAnalyzer(config.get("detektPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CKJMAnalyzer(config.get("ckjmPath"), resultsPath);
    issuesImporter = new DetektResultsImporter();
    metricsImporter = new CKJMResultsImporter();
    issuesAggregator = new DetektAggregator();
    metricsAggregator = new CKJMAggregator();
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "kotlin";
  }
}
