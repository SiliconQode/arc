package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Language provider for the JavaScript language
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class JsProvider extends AbstractLanguageProvider {

  /** Constructs a new JS Language Provider */
  public JsProvider() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public void initComponents(@NonNull Project project) {
    throw new NotImplementedException("Operation not yet implemented.");
  }
}
