package dev.siliconcode.qatch.analyzers.ruby;

import dev.siliconcode.qatch.analyzers.LanguageProvider;

import java.util.Map;

/**
 * Language provider for the Ruby programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class RubyProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return RubyProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new RubyProvider();
  }

  /** Private default constructor */
  private RubyProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "ruby";
  }
}
