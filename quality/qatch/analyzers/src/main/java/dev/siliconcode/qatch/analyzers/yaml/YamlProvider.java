package dev.siliconcode.qatch.analyzers.yaml;

import dev.siliconcode.qatch.analyzers.LanguageProvider;

import java.util.Map;

/**
 * Language provider for the YAML document language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class YamlProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return YamlProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new YamlProvider();
  }

  /** Private default constructor */
  private YamlProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "yaml";
  }
}
