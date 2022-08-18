package dev.siliconcode.qatch.service.lang.files;

import dev.siliconcode.qatch.service.data.DbManager;
import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.lang.CodeProcessor;
import dev.siliconcode.qatch.service.lang.FileBuilder;
import dev.siliconcode.qatch.service.lang.LanguageProvider;
import dev.siliconcode.qatch.service.lang.ProjectBuilder;
import dev.siliconcode.qatch.service.lang.compilers.GradleProjectGenerator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Correctly reconstructs Java Files that are passed to the service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class JavaFileBuilder extends FileBuilder {

  /**
   * Constructs a new JavaFileBuilder contained by the provided LanguageProvider
   *
   * @param provider The LanguageProvider containing this JavaFileBuilder
   */
  public JavaFileBuilder(@NonNull LanguageProvider provider) {
    super(provider);
  }

  /** {@inheritDoc} */
  @Override
  public void createFile(
      @NonNull Project project, @NonNull String fileName, @NonNull String content)
      throws IllegalArgumentException {
    // if (fileName.isEmpty()) throw new IllegalArgumentException("file name cannot be null or
    // empty");

    DbManager.instance().open();
    String basePath = project.path();
    DbManager.instance().close();
    Path path = Paths.get(basePath, "src/main/java", fileName);

    try {
      Files.deleteIfExists(path);
      Path parent = path.getParent();
      Files.createDirectories(parent);
      Files.createFile(path);

      CodeProcessor processor = provider.getCodeProcessor();
      String fileContent = processor.processSingleFileProject(fileName, content);

      Files.write(path, List.of(fileContent.split("\n")));
    } catch (Exception ex) {
      log.error("Could not write file: " + path.toAbsolutePath());
    }
  }

  /** {@inheritDoc} */
  public void createFiles(
      @NonNull Project proj,
      @NonNull List<Map<String, String>> files,
      @NonNull String[] dependencies) {

    DbManager.instance().open();
    if (proj.language().equalsIgnoreCase("java")) {
      setupJavaBuild(proj, files, dependencies);
    }
    DbManager.instance().close();

    for (Map<String, String> pair : files) {
      String name = pair.get("name");
      String contents = pair.get("contents");

      createFile(proj, name, contents);
    }

    buildJavaProject(proj);
  }

  /**
   * Method to build the provided project using the selected Compiler/build system
   *
   * @param proj Project to be built, cannot be null
   */
  private void buildJavaProject(@NotNull Project proj) {
    ProjectBuilder comp = provider.getProjectBuilder();
    comp.selectAndExecuteBuilder(proj);
  }

  /**
   * Sets up the structure of the file system for the project
   *
   * @param proj Project to be built
   * @param files List of maps containing the name and contents of each file
   * @param dependencies List of dependencies (beyond the standard set of dependencies) needed for
   *     building the project
   */
  void setupJavaBuild(Project proj, List<Map<String, String>> files, String[] dependencies) {
    log.info("Creating Java Build Components");
    GradleProjectGenerator gen = new GradleProjectGenerator();
    gen.setupDirectoryStructure(proj.path(), proj, dependencies);
  }
}
