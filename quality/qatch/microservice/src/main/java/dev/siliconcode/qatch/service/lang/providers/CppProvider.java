package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A language provider for the C++ language
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CppProvider extends AbstractLanguageProvider {

  /** {@inheritDoc} */
  @Override
  public void initComponents(@NonNull Project project) {
    throw new NotImplementedException("Operation not yet implemented.");
  }
}
