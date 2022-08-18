package dev.siliconcode.qatch.analyzers.objc;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Language provider for the Objective-C programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ObjectiveCProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return ObjectiveCProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new ObjectiveCProvider();
  }

  /** Private default constructor */
  private ObjectiveCProvider() {}

  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  @Override
  public String getLanguage() {
    return "objc";
  }
}
