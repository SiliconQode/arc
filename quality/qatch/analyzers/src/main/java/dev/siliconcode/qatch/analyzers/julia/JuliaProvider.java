package dev.siliconcode.qatch.analyzers.julia;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Language provider for the Julia programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class JuliaProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return JuliaProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new JuliaProvider();
  }

  /** Private default constructor */
  private JuliaProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "julia";
  }
}
