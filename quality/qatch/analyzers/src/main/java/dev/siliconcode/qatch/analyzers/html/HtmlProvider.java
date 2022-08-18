package dev.siliconcode.qatch.analyzers.html;

import dev.siliconcode.qatch.analyzers.LanguageProvider;

import java.util.Map;

/**
 * Language provider for the HTML markup language.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class HtmlProvider extends LanguageProvider {

  /**
   * Retrieves the singleton instance
   *
   * @return Singleton instance
   */
  public static LanguageProvider instance() {
    return HtmlProvider.InstanceHolder.INSTANCE;
  }

  /**
   * Internal clas designed to hold the singleton instance while providing lazy loading and thread
   * safety
   */
  private static class InstanceHolder {
    private static final LanguageProvider INSTANCE = new HtmlProvider();
  }

  /** Private default constructor */
  private HtmlProvider() {}

  /** {@inheritDoc} */
  @Override
  public void initialize(Map<String, String> config) {
    super.initialize(config);
  }

  /** {@inheritDoc} */
  @Override
  public String getLanguage() {
    return "html";
  }
}
