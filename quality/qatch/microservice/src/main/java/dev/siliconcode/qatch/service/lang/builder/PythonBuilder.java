package dev.siliconcode.qatch.service.lang.builder;

import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.lang.LanguageProvider;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A Python language specific project builder
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PythonBuilder extends AbstractProjectBuilder {

  /**
   * Constructs a new AbstractProjectBuilder with the given containing language provider and base
   * path
   *
   * @param provider The containing language provider
   * @param basePath Base path of the project to be analyzed, cannot be null or empty
   */
  public PythonBuilder(@NonNull LanguageProvider provider, @NonNull String basePath) {
    super(provider, basePath);
  }

  /** {@inheritDoc} */
  @Override
  public void selectAndExecuteBuilder(@NonNull Project proj) {
    throw new NotImplementedException("Operation not yet implemented.");
  }
}
