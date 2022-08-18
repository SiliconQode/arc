package dev.siliconcode.qatch.service.lang;

import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Provides the logic used to reconstruct the files of a particular submission.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public abstract class FileBuilder {

  /** ServerContext instance */
  protected final ServerContext context = ServerContext.instance();
  /** Containing language provider */
  protected LanguageProvider provider;

  /**
   * Constructs a new FileBuilder contained by the provided LanguageProvider
   *
   * @param provider The LanguageProvider containing this JavaFileBuilder
   */
  public FileBuilder(LanguageProvider provider) {
    this.provider = provider;
  }

  /**
   * Reconstructs a given file with the given name and content
   *
   * @param project The project for which files will be reconstructed, cannot be null
   * @param fileName The name of the file to be reconstructed, cannot be null
   * @param content The content of the file to be reconstructed, cannot be null
   * @throws IllegalArgumentException if the file name empty
   */
  public abstract void createFile(
      @NonNull Project project, @NonNull String fileName, @NonNull String content)
      throws IllegalArgumentException;

  /**
   * Sets up and constructs the files associated with the given project
   *
   * @param proj Project for which the files are to be created, cannot be null
   * @param files List of maps defining the files, where each map contains two keys "name" and
   *     "content", which combined fully specify the file to be created, cannot be null
   * @param dependencies an array of strings representing the dependencies of the project, cannot be
   *     null
   */
  public abstract void createFiles(
      @NonNull Project proj,
      @NonNull List<Map<String, String>> files,
      @NonNull String[] dependencies);

  //  public static void main(String[] args) {
  //    FileBuilder builder = FileBuilder.instance();
  //
  //    DbManager.instance().loadCredentials();
  //    DbManager.instance().open();
  //    Project proj = Project.builder()
  //            .id("test")
  //            .name("test")
  //            .path("/home/idg/bin/Qatch/projects/test")
  //            .resultsPath("/home/idg/bin/Qatch/Results")
  //            .language("java")
  //            .build();
  //    DbManager.instance().close();
  //
  //    builder.createFiles(proj, List.of(
  //            Map.of("name", "test/App.java", "content", """
  //            package test;
  //
  //            public class App {}""")), new String[0]);
  //
  //  }
}
