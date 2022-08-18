package dev.siliconcode.qatch.service.data;

import lombok.Builder;
import lombok.NonNull;
import org.javalite.activejdbc.Model;

/**
 * Model object representing a project to be analyzed.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class Project extends Model {

  /** An empty constructor, necessary for using ActiveJDBC */
  public Project() {}

  /**
   * Constructs a new Project with the provided information
   *
   * @param id The submission ID, cannot be null
   * @param name The project name, cannot be null
   * @param path The project path, cannot be null
   * @param resultsPath The project results path, cannot be null
   * @param language The project implementation language, cannot be null
   */
  @Builder
  public Project(
      @NonNull String id,
      @NonNull String name,
      @NonNull String path,
      @NonNull String resultsPath,
      @NonNull String language) {
    set(
        "submissionId",
        id,
        "name",
        name,
        "path",
        path,
        "resultsPath",
        resultsPath,
        "language",
        language);
    save();
  }

  /**
   * Convenience method to retrieve the name of the project. Note: You should have an open database
   * connection.
   *
   * @return Project name
   */
  public String name() {
    return getString("name");
  }

  /**
   * Convenience method to retrieve the path to the current project's files. Note: You should have
   * an open database connection.
   *
   * @return This project's path to its files
   */
  public String path() {
    return getString("path");
  }

  /**
   * Convenience method to retrieve the results path for the project. Note: You should have an open
   * database connection.
   *
   * @return Project results path
   */
  public String resultsPath() {
    return getString("resultsPath");
  }

  /**
   * The language associated with this project. Note: you should have an open database cannection.
   *
   * <p>TODO: At some point it might be wise to allow for multiple languages
   *
   * @return Project implementation language
   */
  public String language() {
    return getString("language");
  }

  /**
   * The submissionId associated with this project
   *
   * @return Project submission id
   */
  public String submissionId() {
    return getString("submissionId");
  }
}
