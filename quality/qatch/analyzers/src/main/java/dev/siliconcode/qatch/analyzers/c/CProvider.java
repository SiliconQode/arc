package dev.siliconcode.qatch.analyzers.c;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.cpp.*;
import lombok.NonNull;

import java.util.Map;

/**
 * Language Provider for the C programming language. It currently relies upon the
 * CppCheck and CqMetrics used by C++
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CProvider extends LanguageProvider {

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
   * loading and thread safety.
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new CProvider();
  }

  /** Private default constructor */
  private CProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(@NonNull Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new CppCheckAnalyzer(config.get("cppcheckPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CqmetricsAnalyzer(config.get("cqmetricsPath"), resultsPath);
    issuesAggregator = new CppCheckAggregator();
    metricsAggregator = new CqmetricsAggregator();
    issuesImporter = new CppCheckResultsImporter();
    metricsImporter = new CqmetricsResultsImporter();
  }

  /** {@inheritDoc}  */
  @Override
  public String getLanguage() {
    return "c";
  }
}
