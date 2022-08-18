package dev.siliconcode.qatch.analyzers.pascal;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.json.JsonProvider;

import java.util.Map;

/**
 * Language provider for the Pascal programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PascalProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return PascalProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new PascalProvider();
  }

  /** Private default constructor */
  private PascalProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "pascal";
  }
}
