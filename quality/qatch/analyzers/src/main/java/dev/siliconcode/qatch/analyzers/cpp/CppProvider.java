package dev.siliconcode.qatch.analyzers.cpp;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Language provider for the C++ programming language. Current it utilizes CQMetrics
 * for metrics analysis and CppCheck for issue analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CppProvider extends LanguageProvider {

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
    private static final LanguageProvider INSTANCE = new CppProvider();
  }

  /** Private default constructor */
  private CppProvider() {}

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
    return "cpp";
  }
}
