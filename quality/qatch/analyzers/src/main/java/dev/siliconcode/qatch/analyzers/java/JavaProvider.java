package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Language provider for the Java programming language currently it utilize PMD
 * for issues and CKJM-ext for metrics
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class JavaProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new JavaProvider();
  }

  /** Private default constructor */
  private JavaProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(@NonNull Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new PMDAnalyzer(config.get("pmdPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CKJMAnalyzer(config.get("ckjmPath"), resultsPath);
    issuesImporter = new PMDResultsImporter();
    metricsImporter = new CKJMResultsImporter();
    issuesAggregator = new PMDAggregator();
    metricsAggregator = new CKJMAggregator();
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "java";
  }
}
