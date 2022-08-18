package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import lombok.NonNull;

import java.util.Map;

/**
 * Language provider for the Python programming language. Currently, it utilizes
 * the pylint static analysis tool for rule violations, and ckpm for metrics
 * analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PythonProvider extends LanguageProvider {

  /** Private default constructor */
  private PythonProvider() {
  }

  /**
   * Private internal class designed to hold the singleton instance while
   * providing lazy loading and thread safety
   */
  private static final class ProviderHolder {
    private static final LanguageProvider INSTANCE = new PythonProvider();
  }

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return ProviderHolder.INSTANCE;
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(@NonNull Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new PylintAnalyzer(config.get("pylintPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CKPMAnalyzer(config.get("ckpmPath"), resultsPath);
    issuesImporter = new PylintResultsImporter();
    metricsImporter = new CKPMResultsImporter();
    issuesAggregator = new PylintAggregator();
    metricsAggregator = new CKPMAggregator();
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "python";
  }
}
