package dev.siliconcode.qatch.analyzers.erlang;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.analyzers.java.JavaProvider;

import java.util.Map;

/**
 * Language provider for the Erlang programming language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ErlangProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return ErlangProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal class designed to hold the singleton instance while providing lazy loading and thread
   * safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new ErlangProvider();
  }

  /** Private default constructor */
  private ErlangProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "erlang";
  }
}
