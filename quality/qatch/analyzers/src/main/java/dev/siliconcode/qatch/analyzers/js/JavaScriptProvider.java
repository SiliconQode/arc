package dev.siliconcode.qatch.analyzers.js;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Language provider for the JavaScript programming language. Currently it utilizes
 * ESLint for issues analysis and CK4JS for metrics analysis.
 *
 * @author IsaacGriffith
 * @version 2.0.0
 */
public class JavaScriptProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Private internal class designed to hold the singleton instance while providing
   * lazy loading and thread safety.
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new JavaScriptProvider();
  }

  /** Private default constructor */
  private JavaScriptProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(@NotNull Map<String, String> config) {
    super.initialize(config);

    issuesAnalyzer = new ESLintAnalyzer(config.get("eslintPath"), resultsPath, config.get("ruleSetPath"));
    metricsAnalyzer = new CK4JSAnalyzer(config.get("ck4jsPath"), resultsPath);
    issuesImporter = new ESLintResultsImporter();
    metricsImporter = new CK4JSResultsImporter();
    issuesAggregator = new ESLintAggregator();
    metricsAggregator = new CK4JSAggregator();
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "js";
  }
}
