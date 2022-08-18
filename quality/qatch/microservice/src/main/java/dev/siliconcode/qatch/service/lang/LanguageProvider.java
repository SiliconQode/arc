package dev.siliconcode.qatch.service.lang;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;

/**
 * Interface defining the contract for all Language providers
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface LanguageProvider {

  /**
   * Initializes all the components for the provided project
   *
   * @param project The project to reinitialize the provider components for, cannot be null
   */
  void initComponents(@NonNull Project project);

  /**
   * Retrieves this language's project builder
   *
   * @return ProjectBuilder for the language of this language provider
   */
  ProjectBuilder getProjectBuilder();

  /**
   * Retrieves this language's file builder
   *
   * @return FileBuilder for the language of this language provider
   */
  FileBuilder getFileBuilder();

  /**
   * Retrieves this language's code processor
   *
   * @return CodeProcessor for the language of this language provider
   */
  CodeProcessor getCodeProcessor();
}
