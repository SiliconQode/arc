package dev.siliconcode.qatch.analyzers.ts;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.js.*;

import java.util.Map;

/**
 * Language provider for the TypeScript programming language. Currently, it utilizes
 * ESLint for static issues analysis, and ck4js for metrics analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class TypeScriptProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return TypeScriptProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new TypeScriptProvider();
  }

  /** Private default constructor */
  private TypeScriptProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
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
    return "ts";
  }
}
