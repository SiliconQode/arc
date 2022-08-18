package dev.siliconcode.qatch.service.lang.builder;

import dev.siliconcode.qatch.service.lang.LanguageProvider;
import dev.siliconcode.qatch.service.lang.ProjectBuilder;
import lombok.NonNull;
import org.apache.commons.exec.CommandLine;

/**
 * Abstract base class for language specific project builders
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public abstract class AbstractProjectBuilder implements ProjectBuilder {

  /** The containing language provider */
  protected final LanguageProvider provider;
  /** The base path of the project */
  protected final String basePath;
  /** The command line for executing the build tool */
  protected CommandLine cmdLine;

  /**
   * Constructs a new AbstractProjectBuilder with the given containing language provider and base
   * path
   *
   * @param provider The containing language provider
   * @param basePath Base path of the project to be analyzed, cannot be null or empty
   */
  public AbstractProjectBuilder(@NonNull LanguageProvider provider, @NonNull String basePath) {
    if (basePath.isEmpty()) throw new IllegalArgumentException("Base path cannot be empty");
    this.basePath = basePath;
    this.provider = provider;
  }
}
