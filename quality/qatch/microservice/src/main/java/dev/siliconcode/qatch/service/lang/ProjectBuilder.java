package dev.siliconcode.qatch.service.lang;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;

/**
 * Interface defining the basic components of a system for the management of a project build prior
 * to analysis
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface ProjectBuilder {

  /**
   * Selects and executes the build system for the given project
   *
   * @param proj Project on which to execute a build
   */
  void selectAndExecuteBuilder(@NonNull Project proj);
}
