package dev.siliconcode.qatch.analyzers.haskell;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Language provider for the Haskell programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class HaskellProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return HaskellProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy loading and thread
   * safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new HaskellProvider();
  }

  /** Private default constructor */
  private HaskellProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "haskell";
  }
}
