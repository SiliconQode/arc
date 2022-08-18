package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Language provider for the python language (both version 2.7 and 3)
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PythonProvider extends AbstractLanguageProvider {

  /** Constructs a new PythonProvider */
  public PythonProvider() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public void initComponents(@NonNull Project project) {
    throw new NotImplementedException("Operation not yet implemented.");
  }
}
