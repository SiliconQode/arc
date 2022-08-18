package dev.siliconcode.qatch.analyzers.fortran;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Language provider for the Fortran programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class FortranProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return FortranProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy loading and thread
   * safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new FortranProvider();
  }

  /** Private default constructor */
  private FortranProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "fortran";
  }
}
