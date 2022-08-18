package dev.siliconcode.qatch.projectimport;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Responsible for correctly resurrecting the structure of a submission's code
 * This includes the construction of the files to be analyzed, and the overall directory structure
 * such that the code is both compilable and analyzable.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public abstract class ProjectGenerator {

  /**
   * Generates the directory and code for the provided list of submissions within the provided workspace.
   * Within the workspace a new directory for each submission will be created, and it will have the name
   * of the associated submissions unique id.
   *
   * @param workspace the parent directory in which all submission directories will be created, cannot be null
   * @param submissions the list of submissions to be analyzed, cannot be null
   */
  public abstract void generate(@NonNull String workspace, @NonNull List<Submission> submissions);

  /**
   * Generates the correct directory structure for the provided submission, within the
   * provided workspace directory.
   *
   * @param workspace The path to the parent workspace directory, cannot be null
   * @param submission The submission record, cannot be null
   */
  public abstract void createDirectoryStructure(@NonNull String workspace, @NonNull Submission submission);

  /**
   * Constructs the necessary files for the provided submission in the provided base directory
   *
   * @param base The path to the base directory in which submission files will be created, cannot be null
   * @param submission The Submission record, cannot be null
   */
  public abstract void createFiles(@NonNull String base, @NonNull Submission submission);

  /**
   * Retrieves the set of standard imports for the provided language.
   *
   * @param language the language, cannot be null
   * @return A string containing the standard imports which will appear at the top of any code file created.
   */
  public String getStandardImports(@NonNull String language) {
    String value = "";
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProjectGenerator.class.getResourceAsStream("/standard_imports/" + language + ".txt")))) {
      String line = "";
      List<String> lines = Lists.newArrayList();
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      value = String.join("\n", lines);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return value;
  }

  /**
   * Retrieves the set of standard dependencies for the provided language.
   *
   * @param language the language, cannot be null
   * @return A string containing the standard dependencies which will appear in build files.
   */
  public String getDefaultDependencies(String language) {
    String value = "";
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProjectGenerator.class.getResourceAsStream("/standard_deps/" + language + ".txt")))) {
      String line = "";
      List<String> lines = Lists.newArrayList();
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      value = String.join("\n", lines);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return value;
  }
}
