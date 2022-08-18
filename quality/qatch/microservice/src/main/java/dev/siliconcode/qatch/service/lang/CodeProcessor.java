package dev.siliconcode.qatch.service.lang;

import java.nio.file.Path;

/**
 * A processor which correctly formats the code provided as input to the service, in order to ensure
 * that the code will build
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface CodeProcessor {

  /**
   * Generates the correct content of a file with the given pathString and base content, when
   * considering the project to consist of a single file
   *
   * @param pathString The relative path string of the file.
   * @param content The base-level content before adding in standard imports and modifying to be
   *     compilable
   * @return A string representing the compilable version of the content
   */
  String processSingleFileProject(String pathString, String content);

  /**
   * A listing of the standard imports for the given language
   *
   * @return Language specific listing of the standard imports
   */
  String getStandardImports();

  /**
   * Returns the package (or in some languages namespace or module definition) for a file with the
   * given path.
   *
   * @param path Path of the file for which a package string must be created
   * @return The correct package string
   */
  String getPackageOrDefault(Path path);
}
