package dev.siliconcode.qatch.analyzers.php;

import dev.siliconcode.qatch.analyzers.LanguageProvider;

import java.util.Map;

/**
 * Language provider for the PHP programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PhpProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return PhpProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new PhpProvider();
  }

  /** Private default constructor */
  private PhpProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "php";
  }
}
