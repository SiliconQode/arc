package dev.siliconcode.qatch.analyzers.csharp;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Language provider for the C# programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CSharpProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy
   * loading and thread safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new CSharpProvider();
  }

  /** Private default constructor */
  private CSharpProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(@NotNull Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "csharp";
  }
}
